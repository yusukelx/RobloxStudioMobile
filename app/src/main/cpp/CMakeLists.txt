cmake_minimum_required(VERSION 3.18)
project(studrblx_luau LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(LUAU_BUILD_CLI OFF CACHE BOOL "" FORCE)
set(LUAU_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(LUAU_BUILD_WEB OFF CACHE BOOL "" FORCE)
set(LUAU_WERROR OFF CACHE BOOL "" FORCE)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

add_subdirectory(luau EXCLUDE_FROM_ALL)

add_library(luaubridge SHARED luau_bridge.cpp)
target_link_libraries(luaubridge PRIVATE Luau.VM Luau.Compiler Luau.Ast)
if(ANDROID)
    target_link_libraries(luaubridge PRIVATE log)
else()
    find_package(JNI REQUIRED)
    target_include_directories(luaubridge PRIVATE ${JNI_INCLUDE_DIRS})
endif()
target_compile_options(luaubridge PRIVATE -O2 -fexceptions -frtti)
target_link_options(luaubridge PRIVATE -Wl,--gc-sections -Wl,-z,max-page-size=16384)
