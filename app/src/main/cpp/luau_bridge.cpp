#include <jni.h>

#include <atomic>
#include <chrono>
#include <cstdlib>
#include <cstring>
#include <string>
#include <vector>

#include "lua.h"
#include "lualib.h"
#include "luacode.h"
#include "Luau/Ast.h"
#include "Luau/Parser.h"
#include "Luau/ParseOptions.h"

namespace
{

constexpr int kProxyTag = 7;

enum Op
{
    OP_CALL = 0,
    OP_INDEX = 1,
    OP_NEWINDEX = 2,
    OP_NAMECALL = 3,
    OP_TOSTRING = 4,
    OP_EQ = 5,
    OP_LT = 6,
    OP_LE = 7,
    OP_ADD = 8,
    OP_SUB = 9,
    OP_MUL = 10,
    OP_DIV = 11,
    OP_MOD = 12,
    OP_POW = 13,
    OP_UNM = 14,
    OP_LEN = 15,
    OP_CONCAT = 16,
    OP_IDIV = 17,
    OP_ITER = 18,
    OP_CONTINUE = 19,
};

constexpr int kResultError = -1;
constexpr int kResultYield = -2;
constexpr int kResultErrorRaw = -3;

struct Proxy
{
    int handle;
};

struct StateData
{
    std::atomic<bool> stop{false};
    std::atomic<long long> deadlineNs{0};
    unsigned interruptCounter = 0;
    std::vector<int> freed;
    int proxyCacheRef = LUA_NOREF;
    int typeMetaRef = LUA_NOREF;
    int sharedRef = LUA_NOREF;
};

JavaVM* gVm = nullptr;
jclass gStateClass = nullptr;
jmethodID gDispatch = nullptr;
jmethodID gLog = nullptr;

long long nowNs()
{
    return std::chrono::duration_cast<std::chrono::nanoseconds>(std::chrono::steady_clock::now().time_since_epoch()).count();
}

JNIEnv* env()
{
    JNIEnv* e = nullptr;
    if (gVm->GetEnv(reinterpret_cast<void**>(&e), JNI_VERSION_1_6) != JNI_OK)
#ifdef __ANDROID__
        gVm->AttachCurrentThread(&e, nullptr);
#else
        gVm->AttachCurrentThread(reinterpret_cast<void**>(&e), nullptr);
#endif
    return e;
}

StateData* data(lua_State* L)
{
    return static_cast<StateData*>(lua_callbacks(L)->userdata);
}

lua_State* ptr(jlong p)
{
    return reinterpret_cast<lua_State*>(p);
}

void interruptHandler(lua_State* L, int gc)
{
    if (gc >= 0)
        return;
    StateData* sd = data(L);
    if (sd->stop.load(std::memory_order_relaxed))
        luaL_error(L, "Script stopped");
    if ((++sd->interruptCounter & 255) != 0)
        return;
    long long deadline = sd->deadlineNs.load(std::memory_order_relaxed);
    if (deadline != 0 && nowNs() > deadline)
    {
        sd->deadlineNs.store(0, std::memory_order_relaxed);
        luaL_error(L, "Script timeout: exhausted allowed execution time");
    }
}

void proxyDestructor(lua_State* L, void* ud)
{
    data(L)->freed.push_back(static_cast<Proxy*>(ud)->handle);
}

int finishJavaCall(lua_State* L, jint result)
{
    JNIEnv* e = env();
    if (e->ExceptionCheck())
    {
        e->ExceptionClear();
        luaL_error(L, "internal bridge error");
    }
    if (result >= 0)
        return result;
    if (result == kResultError)
    {
        if (lua_type(L, -1) == LUA_TSTRING)
        {
            luaL_where(L, 1);
            lua_insert(L, -2);
            lua_concat(L, 2);
        }
        lua_error(L);
    }
    if (result == kResultErrorRaw)
        lua_error(L);
    if (result == kResultYield)
    {
        if (!lua_isyieldable(L))
            luaL_error(L, "attempt to yield across metamethod/C-call boundary");
        lua_settop(L, 0);
        return lua_yield(L, 0);
    }
    luaL_error(L, "invalid bridge result");
    return 0;
}

int callJava(lua_State* L, int handle, int op)
{
    JNIEnv* e = env();
    jint r = e->CallStaticIntMethod(gStateClass, gDispatch, reinterpret_cast<jlong>(lua_mainthread(L)), reinterpret_cast<jlong>(L), handle, op);
    return finishJavaCall(L, r);
}

int javaContinuation(lua_State* L, int status)
{
    return callJava(L, -1, OP_CONTINUE);
}

int javaFunction(lua_State* L)
{
    Proxy* p = static_cast<Proxy*>(lua_touserdatatagged(L, lua_upvalueindex(1), kProxyTag));
    return callJava(L, p ? p->handle : -1, OP_CALL);
}

template<int OPV>
int metamethod(lua_State* L)
{
    return callJava(L, -1, OPV);
}

int metaNamecall(lua_State* L)
{
    const char* name = lua_namecallatom(L, nullptr);
    if (!name)
        luaL_error(L, "invalid method call");
    lua_pushstring(L, name);
    return callJava(L, -1, OP_NAMECALL);
}

int luauPrint(lua_State* L, int kind)
{
    int n = lua_gettop(L);
    std::string out;
    for (int i = 1; i <= n; i++)
    {
        size_t len = 0;
        const char* s = luaL_tolstring(L, i, &len);
        if (i > 1)
            out += ' ';
        out.append(s, len);
        lua_pop(L, 1);
    }
    JNIEnv* e = env();
    jbyteArray bytes = e->NewByteArray(static_cast<jsize>(out.size()));
    e->SetByteArrayRegion(bytes, 0, static_cast<jsize>(out.size()), reinterpret_cast<const jbyte*>(out.data()));
    e->CallStaticVoidMethod(gStateClass, gLog, reinterpret_cast<jlong>(lua_mainthread(L)), kind, bytes);
    e->DeleteLocalRef(bytes);
    if (e->ExceptionCheck())
        e->ExceptionClear();
    return 0;
}

int printFn(lua_State* L)
{
    return luauPrint(L, 0);
}

int warnFn(lua_State* L)
{
    return luauPrint(L, 1);
}

char* compileSource(const char* src, size_t len, size_t* outSize)
{
    static const char* const mutableGlobals[] = {"game", "Game", "workspace", "Workspace", "script", "plugin", "shared", "_G", nullptr};
    lua_CompileOptions options = {};
    options.optimizationLevel = 1;
    options.debugLevel = 1;
    options.mutableGlobals = mutableGlobals;
    return luau_compile(src, len, &options, outSize);
}

int loadstringFn(lua_State* L)
{
    size_t len = 0;
    const char* src = luaL_checklstring(L, 1, &len);
    const char* chunk = luaL_optstring(L, 2, "=loadstring");
    size_t bcSize = 0;
    char* bc = compileSource(src, len, &bcSize);
    lua_Debug ar = {};
    int envIndex = 0;
    if (lua_getinfo(L, 1, "f", &ar))
    {
        lua_getfenv(L, -1);
        lua_remove(L, -2);
        envIndex = lua_gettop(L);
    }
    int status = luau_load(L, chunk, bc, bcSize, envIndex);
    free(bc);
    if (status != 0)
    {
        lua_pushnil(L);
        lua_insert(L, -2);
        return 2;
    }
    return 1;
}

void pushTypeMetatable(lua_State* L, const char* typeName)
{
    StateData* sd = data(L);
    lua_getref(L, sd->typeMetaRef);
    lua_getfield(L, -1, typeName);
    if (!lua_isnil(L, -1))
    {
        lua_remove(L, -2);
        return;
    }
    lua_pop(L, 1);
    lua_createtable(L, 0, 24);
    lua_pushstring(L, typeName);
    lua_setfield(L, -2, "__type");
    lua_pushstring(L, "The metatable is locked");
    lua_setfield(L, -2, "__metatable");
    lua_pushcfunction(L, metamethod<OP_INDEX>, "__index");
    lua_setfield(L, -2, "__index");
    lua_pushcfunction(L, metamethod<OP_NEWINDEX>, "__newindex");
    lua_setfield(L, -2, "__newindex");
    lua_pushcclosurek(L, metamethod<OP_CALL>, "__call", 0, javaContinuation);
    lua_setfield(L, -2, "__call");
    lua_pushcclosurek(L, metaNamecall, "__namecall", 0, javaContinuation);
    lua_setfield(L, -2, "__namecall");
    lua_pushcfunction(L, metamethod<OP_TOSTRING>, "__tostring");
    lua_setfield(L, -2, "__tostring");
    lua_pushcfunction(L, metamethod<OP_EQ>, "__eq");
    lua_setfield(L, -2, "__eq");
    lua_pushcfunction(L, metamethod<OP_LT>, "__lt");
    lua_setfield(L, -2, "__lt");
    lua_pushcfunction(L, metamethod<OP_LE>, "__le");
    lua_setfield(L, -2, "__le");
    lua_pushcfunction(L, metamethod<OP_ADD>, "__add");
    lua_setfield(L, -2, "__add");
    lua_pushcfunction(L, metamethod<OP_SUB>, "__sub");
    lua_setfield(L, -2, "__sub");
    lua_pushcfunction(L, metamethod<OP_MUL>, "__mul");
    lua_setfield(L, -2, "__mul");
    lua_pushcfunction(L, metamethod<OP_DIV>, "__div");
    lua_setfield(L, -2, "__div");
    lua_pushcfunction(L, metamethod<OP_IDIV>, "__idiv");
    lua_setfield(L, -2, "__idiv");
    lua_pushcfunction(L, metamethod<OP_MOD>, "__mod");
    lua_setfield(L, -2, "__mod");
    lua_pushcfunction(L, metamethod<OP_POW>, "__pow");
    lua_setfield(L, -2, "__pow");
    lua_pushcfunction(L, metamethod<OP_UNM>, "__unm");
    lua_setfield(L, -2, "__unm");
    lua_pushcfunction(L, metamethod<OP_LEN>, "__len");
    lua_setfield(L, -2, "__len");
    lua_pushcfunction(L, metamethod<OP_CONCAT>, "__concat");
    lua_setfield(L, -2, "__concat");
    lua_pushcfunction(L, metamethod<OP_ITER>, "__iter");
    lua_setfield(L, -2, "__iter");
    lua_setreadonly(L, -1, 1);
    lua_pushvalue(L, -1);
    lua_setfield(L, -3, typeName);
    lua_remove(L, -2);
}

jbyteArray toBytes(JNIEnv* e, const char* s, size_t len)
{
    jbyteArray arr = e->NewByteArray(static_cast<jsize>(len));
    if (arr && len)
        e->SetByteArrayRegion(arr, 0, static_cast<jsize>(len), reinterpret_cast<const jbyte*>(s));
    return arr;
}

std::string fromBytes(JNIEnv* e, jbyteArray arr)
{
    if (!arr)
        return std::string();
    jsize n = e->GetArrayLength(arr);
    std::string s(static_cast<size_t>(n), '\0');
    if (n)
        e->GetByteArrayRegion(arr, 0, n, reinterpret_cast<jbyte*>(&s[0]));
    return s;
}

}

#define JFN(ret, name) extern "C" JNIEXPORT ret JNICALL Java_com_goydevv_studrblx_lua_luau_LuauNative_##name

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*)
{
    gVm = vm;
    JNIEnv* e = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&e), JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;
    jclass cls = e->FindClass("com/goydevv/studrblx/lua/luau/LuauState");
    if (!cls)
        return JNI_ERR;
    gStateClass = static_cast<jclass>(e->NewGlobalRef(cls));
    gDispatch = e->GetStaticMethodID(gStateClass, "dispatch", "(JJII)I");
    gLog = e->GetStaticMethodID(gStateClass, "log", "(JI[B)V");
    if (!gDispatch || !gLog)
        return JNI_ERR;
    return JNI_VERSION_1_6;
}

JFN(jlong, newState)(JNIEnv*, jclass)
{
    lua_State* L = luaL_newstate();
    if (!L)
        return 0;
    StateData* sd = new StateData();
    lua_callbacks(L)->userdata = sd;
    lua_callbacks(L)->interrupt = interruptHandler;
    lua_setuserdatadtor(L, kProxyTag, proxyDestructor);
    luaL_openlibs(L);

    lua_createtable(L, 0, 0);
    lua_createtable(L, 0, 1);
    lua_pushstring(L, "v");
    lua_setfield(L, -2, "__mode");
    lua_setmetatable(L, -2);
    sd->proxyCacheRef = lua_ref(L, -1);
    lua_pop(L, 1);

    lua_createtable(L, 0, 32);
    sd->typeMetaRef = lua_ref(L, -1);
    lua_pop(L, 1);

    lua_pushcfunction(L, printFn, "print");
    lua_setglobal(L, "print");
    lua_pushcfunction(L, warnFn, "warn");
    lua_setglobal(L, "warn");
    lua_pushcfunction(L, loadstringFn, "loadstring");
    lua_setglobal(L, "loadstring");
    lua_getglobal(L, "pcall");
    lua_setglobal(L, "ypcall");
    lua_createtable(L, 0, 0);
    lua_setglobal(L, "shared");
    lua_createtable(L, 0, 0);
    lua_setglobal(L, "_G");
    return reinterpret_cast<jlong>(L);
}

JFN(void, closeState)(JNIEnv*, jclass, jlong p)
{
    lua_State* L = ptr(p);
    if (!L)
        return;
    StateData* sd = data(L);
    sd->stop.store(true);
    lua_close(L);
    delete sd;
}

JFN(void, sandbox)(JNIEnv*, jclass, jlong p)
{
    lua_State* L = ptr(p);
    lua_pushvalue(L, LUA_GLOBALSINDEX);
    lua_pushnil(L);
    while (lua_next(L, -2) != 0)
    {
        if (lua_istable(L, -1) && !lua_getreadonly(L, -1))
        {
            lua_pushvalue(L, -2);
            bool shared = lua_isstring(L, -1) && (strcmp(lua_tostring(L, -1), "_G") == 0 || strcmp(lua_tostring(L, -1), "shared") == 0);
            lua_pop(L, 1);
            if (!shared)
                lua_setreadonly(L, -1, true);
        }
        lua_pop(L, 1);
    }
    lua_setsafeenv(L, -1, true);
    lua_pop(L, 1);
}

JFN(void, setStopped)(JNIEnv*, jclass, jlong p, jboolean stopped)
{
    data(ptr(p))->stop.store(stopped);
}

JFN(void, setDeadline)(JNIEnv*, jclass, jlong p, jlong millis)
{
    data(ptr(p))->deadlineNs.store(millis > 0 ? nowNs() + millis * 1000000LL : 0);
}

JFN(jintArray, drainFreed)(JNIEnv* e, jclass, jlong p)
{
    StateData* sd = data(ptr(p));
    if (sd->freed.empty())
        return nullptr;
    jintArray arr = e->NewIntArray(static_cast<jsize>(sd->freed.size()));
    e->SetIntArrayRegion(arr, 0, static_cast<jsize>(sd->freed.size()), sd->freed.data());
    sd->freed.clear();
    return arr;
}

JFN(jbyteArray, compile)(JNIEnv* e, jclass, jbyteArray source)
{
    std::string src = fromBytes(e, source);
    size_t size = 0;
    char* bc = compileSource(src.data(), src.size(), &size);
    jbyteArray out = toBytes(e, bc, size);
    free(bc);
    return out;
}

JFN(jbyteArray, parseErrors)(JNIEnv* e, jclass, jbyteArray source)
{
    std::string src = fromBytes(e, source);
    Luau::Allocator allocator;
    Luau::AstNameTable names(allocator);
    Luau::ParseOptions options;
    Luau::ParseResult result = Luau::Parser::parse(src.data(), src.size(), names, allocator, options);
    std::string out;
    for (const Luau::ParseError& err : result.errors)
    {
        const Luau::Location& loc = err.getLocation();
        out += std::to_string(loc.begin.line + 1) + ":" + std::to_string(loc.begin.column + 1) + ":" + std::to_string(loc.end.line + 1) + ":" +
               std::to_string(loc.end.column + 1) + ":" + err.getMessage() + "\n";
    }
    return toBytes(e, out.data(), out.size());
}

JFN(jint, load)(JNIEnv* e, jclass, jlong p, jbyteArray chunkName, jbyteArray bytecode, jint envRef)
{
    lua_State* L = ptr(p);
    std::string name = fromBytes(e, chunkName);
    std::string bc = fromBytes(e, bytecode);
    int envIndex = 0;
    if (envRef > 0)
    {
        lua_getref(L, envRef);
        envIndex = lua_gettop(L);
    }
    int status = luau_load(L, name.c_str(), bc.data(), bc.size(), envIndex);
    if (envIndex)
        lua_remove(L, envIndex);
    return status;
}

JFN(jint, newEnv)(JNIEnv*, jclass, jlong p)
{
    lua_State* L = ptr(p);
    lua_createtable(L, 0, 4);
    lua_createtable(L, 0, 1);
    lua_pushvalue(L, LUA_GLOBALSINDEX);
    lua_setfield(L, -2, "__index");
    lua_setreadonly(L, -1, 1);
    lua_setmetatable(L, -2);
    lua_setsafeenv(L, -1, 1);
    int ref = lua_ref(L, -1);
    lua_pop(L, 1);
    return ref;
}

JFN(jlong, newThread)(JNIEnv*, jclass, jlong p)
{
    lua_State* L = ptr(p);
    lua_State* co = lua_newthread(L);
    luaL_sandboxthread(co);
    return reinterpret_cast<jlong>(co);
}

JFN(jlong, toThread)(JNIEnv*, jclass, jlong p, jint idx)
{
    return reinterpret_cast<jlong>(lua_tothread(ptr(p), idx));
}

JFN(void, pushThread)(JNIEnv*, jclass, jlong p)
{
    lua_pushthread(ptr(p));
}

JFN(jint, resume)(JNIEnv*, jclass, jlong co, jlong from, jint nargs)
{
    return lua_resume(ptr(co), from ? ptr(from) : nullptr, nargs);
}

JFN(jint, costatus)(JNIEnv*, jclass, jlong p, jlong co)
{
    return lua_costatus(ptr(p), ptr(co));
}

JFN(jint, status)(JNIEnv*, jclass, jlong co)
{
    return lua_status(ptr(co));
}

JFN(jboolean, isYieldable)(JNIEnv*, jclass, jlong p)
{
    return lua_isyieldable(ptr(p)) != 0;
}

JFN(jbyteArray, traceback)(JNIEnv* e, jclass, jlong p)
{
    const char* t = lua_debugtrace(ptr(p));
    return t ? toBytes(e, t, strlen(t)) : nullptr;
}

JFN(void, resetThread)(JNIEnv*, jclass, jlong co)
{
    lua_resetthread(ptr(co));
}

JFN(jboolean, pushCallerEnv)(JNIEnv*, jclass, jlong p)
{
    lua_State* L = ptr(p);
    for (int level = 1; level < 64; level++)
    {
        lua_Debug ar = {};
        if (!lua_getinfo(L, level, "f", &ar))
            return JNI_FALSE;
        if (lua_isfunction(L, -1) && !lua_iscfunction(L, -1))
        {
            lua_getfenv(L, -1);
            lua_remove(L, -2);
            return JNI_TRUE;
        }
        lua_pop(L, 1);
    }
    return JNI_FALSE;
}

JFN(jint, getTop)(JNIEnv*, jclass, jlong p)
{
    return lua_gettop(ptr(p));
}

JFN(void, setTop)(JNIEnv*, jclass, jlong p, jint idx)
{
    lua_settop(ptr(p), idx);
}

JFN(jboolean, checkStack)(JNIEnv*, jclass, jlong p, jint n)
{
    return lua_checkstack(ptr(p), n) != 0;
}

JFN(void, xmove)(JNIEnv*, jclass, jlong from, jlong to, jint n)
{
    lua_xmove(ptr(from), ptr(to), n);
}

JFN(void, pushValue)(JNIEnv*, jclass, jlong p, jint idx)
{
    lua_pushvalue(ptr(p), idx);
}

JFN(void, remove)(JNIEnv*, jclass, jlong p, jint idx)
{
    lua_remove(ptr(p), idx);
}

JFN(jint, type)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_type(ptr(p), idx);
}

JFN(jboolean, toBoolean)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_toboolean(ptr(p), idx) != 0;
}

JFN(jdouble, toNumber)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_tonumber(ptr(p), idx);
}

JFN(jbyteArray, toBytes)(JNIEnv* e, jclass, jlong p, jint idx)
{
    lua_State* L = ptr(p);
    if (lua_type(L, idx) != LUA_TSTRING)
        return nullptr;
    size_t len = 0;
    const char* s = lua_tolstring(L, idx, &len);
    return toBytes(e, s, len);
}

JFN(jfloatArray, toVector)(JNIEnv* e, jclass, jlong p, jint idx)
{
    const float* v = lua_tovector(ptr(p), idx);
    if (!v)
        return nullptr;
    jfloatArray arr = e->NewFloatArray(3);
    e->SetFloatArrayRegion(arr, 0, 3, v);
    return arr;
}

JFN(jint, toProxy)(JNIEnv*, jclass, jlong p, jint idx)
{
    lua_State* L = ptr(p);
    int t = lua_type(L, idx);
    if (t == LUA_TUSERDATA)
    {
        Proxy* px = static_cast<Proxy*>(lua_touserdatatagged(L, idx, kProxyTag));
        return px ? px->handle : -1;
    }
    if (t == LUA_TFUNCTION && lua_tocfunction(L, idx) == javaFunction)
    {
        lua_getupvalue(L, idx, 1);
        Proxy* px = static_cast<Proxy*>(lua_touserdatatagged(L, -1, kProxyTag));
        int h = px ? px->handle : -1;
        lua_pop(L, 1);
        return h;
    }
    return -1;
}

JFN(jlong, toPointer)(JNIEnv*, jclass, jlong p, jint idx)
{
    return reinterpret_cast<jlong>(lua_topointer(ptr(p), idx));
}

JFN(void, pushNil)(JNIEnv*, jclass, jlong p)
{
    lua_pushnil(ptr(p));
}

JFN(void, pushBoolean)(JNIEnv*, jclass, jlong p, jboolean b)
{
    lua_pushboolean(ptr(p), b);
}

JFN(void, pushNumber)(JNIEnv*, jclass, jlong p, jdouble d)
{
    lua_pushnumber(ptr(p), d);
}

JFN(void, pushBytes)(JNIEnv* e, jclass, jlong p, jbyteArray bytes)
{
    lua_State* L = ptr(p);
    jsize n = bytes ? e->GetArrayLength(bytes) : 0;
    if (n <= 256)
    {
        jbyte buf[256];
        if (n)
            e->GetByteArrayRegion(bytes, 0, n, buf);
        lua_pushlstring(L, reinterpret_cast<const char*>(buf), static_cast<size_t>(n));
        return;
    }
    jbyte* raw = e->GetByteArrayElements(bytes, nullptr);
    lua_pushlstring(L, reinterpret_cast<const char*>(raw), static_cast<size_t>(n));
    e->ReleaseByteArrayElements(bytes, raw, JNI_ABORT);
}

JFN(void, pushVector)(JNIEnv*, jclass, jlong p, jfloat x, jfloat y, jfloat z)
{
    lua_pushvector(ptr(p), x, y, z);
}

JFN(jboolean, pushProxy)(JNIEnv* e, jclass, jlong p, jint handle, jboolean isFunction, jbyteArray typeName)
{
    lua_State* L = ptr(p);
    StateData* sd = data(L);
    lua_getref(L, sd->proxyCacheRef);
    lua_rawgeti(L, -1, handle);
    if (!lua_isnil(L, -1))
    {
        lua_remove(L, -2);
        return JNI_FALSE;
    }
    lua_pop(L, 1);
    if (isFunction)
    {
        Proxy* px = static_cast<Proxy*>(lua_newuserdatatagged(L, sizeof(Proxy), kProxyTag));
        px->handle = handle;
        std::string name = fromBytes(e, typeName);
        lua_pushcclosurek(L, javaFunction, name.empty() ? nullptr : name.c_str(), 1, javaContinuation);
    }
    else
    {
        Proxy* px = static_cast<Proxy*>(lua_newuserdatatagged(L, sizeof(Proxy), kProxyTag));
        px->handle = handle;
        std::string name = fromBytes(e, typeName);
        pushTypeMetatable(L, name.empty() ? "userdata" : name.c_str());
        lua_setmetatable(L, -2);
    }
    lua_pushvalue(L, -1);
    lua_rawseti(L, -3, handle);
    lua_remove(L, -2);
    return JNI_TRUE;
}

JFN(void, pushRef)(JNIEnv*, jclass, jlong p, jint ref)
{
    lua_getref(ptr(p), ref);
}

JFN(jint, ref)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_ref(ptr(p), idx);
}

JFN(void, unref)(JNIEnv*, jclass, jlong p, jint ref)
{
    lua_unref(ptr(p), ref);
}

JFN(void, pushGlobals)(JNIEnv*, jclass, jlong p)
{
    lua_pushvalue(ptr(p), LUA_GLOBALSINDEX);
}

JFN(void, newTable)(JNIEnv*, jclass, jlong p, jint narr, jint nrec)
{
    lua_createtable(ptr(p), narr, nrec);
}

JFN(void, rawSet)(JNIEnv*, jclass, jlong p, jint idx)
{
    lua_rawset(ptr(p), idx);
}

JFN(void, rawGet)(JNIEnv*, jclass, jlong p, jint idx)
{
    lua_rawget(ptr(p), idx);
}

JFN(jint, rawLen)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_objlen(ptr(p), idx);
}

JFN(jboolean, next)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_next(ptr(p), idx) != 0;
}

JFN(jboolean, isReadonly)(JNIEnv*, jclass, jlong p, jint idx)
{
    return lua_getreadonly(ptr(p), idx) != 0;
}

JFN(jint, gcCountKb)(JNIEnv*, jclass, jlong p)
{
    return lua_gc(ptr(p), LUA_GCCOUNT, 0);
}

JFN(void, gcStep)(JNIEnv*, jclass, jlong p, jint kb)
{
    lua_gc(ptr(p), LUA_GCSTEP, kb);
}
