package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.Http;
import com.goydevv.studrblx.net.WebPermissions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import com.goydevv.studrblx.lua.luau.LuauState;
import com.goydevv.studrblx.lua.luau.LuauYield;
import com.goydevv.studrblx.lua.luau.ScriptThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: RobloxServices.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RobloxServices {
    public static final RobloxServices INSTANCE = new RobloxServices();

    private RobloxServices() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:190:0x050b  */
    /* JADX WARN: Code duplicated, block: B:639:0x1184 A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Code duplicated, block: B:873:0x1873  */
    /* JADX WARN: Code restructure failed: missing block: B:1001:0x1ce6, code lost:
    
        if (r37.equals("InputHoldEnd") != false) goto L1002;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1013:0x1d64, code lost:
    
        if (r37.equals("IsMouseButtonPressed") != false) goto L1014;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1014:0x1d66, code lost:
    
        r0 = org.luaj.vm2.LuaValue.FALSE;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "FALSE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:1022:0x1dca, code lost:
    
        if (r37.equals("BindActionAtPriority") != false) goto L1023;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1023:0x1dcc, code lost:
    
        r4 = r38.arg(4).toboolean();
     */
    /* JADX WARN: Code restructure failed: missing block: B:1024:0x1dd6, code lost:
    
        if (r9 == 0) goto L1026;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1025:0x1dd8, code lost:
    
        r5 = r12.checkjstring();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, "checkjstring(...)");
        r6 = r38.arg(3).checkfunction();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, "checkfunction(...)");
        r9.bindAction(r5, r6, r4);
        r5 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1026:0x1df3, code lost:
    
        r0 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:1028:0x1e0e, code lost:
    
        if (r37.equals("TweenSize") == false) goto L1029;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1099:0x1fc3, code lost:
    
        if (r37.equals("SetPartCollisionGroup") != false) goto L1100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1168:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1183:?, code lost:
    
        return dataStore(r35, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:1189:?, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1190:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(new com.goydevv.studrblx.model.Vector3(0.4f, 0.8f, 0.3f).getUnit());
     */
    /* JADX WARN: Code restructure failed: missing block: B:1191:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1193:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1202:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1203:?, code lost:
    
        return r6.wrap(new com.goydevv.studrblx.model.Ray(r5, r4.times(com.goydevv.studrblx.lua.LuaTypesKt.optNum(r13, 1.0f))));
     */
    /* JADX WARN: Code restructure failed: missing block: B:1209:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1212:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1214:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1218:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1219:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1240:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1243:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0404, code lost:
    
        if (r37.equals("CreateToolbar") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x04a6, code lost:
    
        if (r37.equals("CreateDockWidgetPluginGui") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x04b5, code lost:
    
        if (r37.equals("FilterStringAsync") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0521, code lost:
    
        if (r37.equals("SetRBXEvent") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x05d0, code lost:
    
        if (r37.equals("RequestAsync") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x05df, code lost:
    
        if (r37.equals("ScreenPointToRay") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x05ee, code lost:
    
        if (r37.equals("ReportStats") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0626, code lost:
    
        if (r37.equals("GetObjects") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x06a3, code lost:
    
        if (r37.equals("GetKeysPressed") != false) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x06bb, code lost:
    
        if (r37.equals("CreateButton") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x07b4, code lost:
    
        if (r37.equals("SelectRibbonTool") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x089b, code lost:
    
        if (r37.equals("ReportCounter") != false) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x089d, code lost:
    
        r14 = "NONE";
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x08ac, code lost:
    
        if (r37.equals("GetMoonDirection") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x08d3, code lost:
    
        if (r37.equals("TeleportToPlaceInstance") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x08e2, code lost:
    
        if (r37.equals("IsFriendsWith") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x08f1, code lost:
    
        if (r37.equals("GetSessionId") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0945, code lost:
    
        if (r37.equals("GetSetting") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0954, code lost:
    
        if (r37.equals("CreatePluginAction") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x09e1, code lost:
    
        if (r37.equals("IsKeyDown") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x09f0, code lost:
    
        if (r37.equals("CreatePluginMenu") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x09ff, code lost:
    
        if (r37.equals("HasAppearanceLoaded") != false) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0aa7, code lost:
    
        if (r37.equals("GetClientId") != false) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0ab7, code lost:
    
        if (r37.equals("GetOrderedDataStore") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0b33, code lost:
    
        if (r37.equals("SetPlaceId") == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x0b3b, code lost:
    
        r0 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "NONE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0b55, code lost:
    
        if (r37.equals("SetPosition") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0d80, code lost:
    
        if (r37.equals("Emit") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0f83, code lost:
    
        if (r37.equals("Deactivate") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0f94, code lost:
    
        if (r37.equals("ReportInfluxSeries") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0fd0, code lost:
    
        if (r37.equals("GetGlobalDataStore") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x1036, code lost:
    
        if (r37.equals("IsServer") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x1047, code lost:
    
        if (r37.equals("GetDataStore") != false) goto L575;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x104d, code lost:
    
        if (r12.isnil() == false) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x104f, code lost:
    
        r0 = "global";
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x1052, code lost:
    
        r0 = r12.tojstring();
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x1056, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x1070, code lost:
    
        if (r37.equals("FireLogEvent") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x1081, code lost:
    
        if (r37.equals("PostAsync") != false) goto L585;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x11a0, code lost:
    
        if (r37.equals("FilterStringForBroadcast") != false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x11a2, code lost:
    
        r0 = r12.tojstring();
        r4 = new org.luaj.vm2.LuaTable();
        r5 = kotlin.collections.CollectionsKt.listOf((java.lang.Object[]) new java.lang.String[]{"GetNonChatStringForBroadcastAsync", "GetNonChatStringForUserAsync", "GetChatForUserAsync"}).iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x11cc, code lost:
    
        if (r5.hasNext() == false) goto L1119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x11ce, code lost:
    
        r6 = (java.lang.String) r5.next();
        r4.set(r6, com.goydevv.studrblx.lua.LuaTypesKt.fnv(new com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda25(r0)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x11e9, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r37, "FilterStringForBroadcast") == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x11eb, code lost:
    
        r5 = org.luaj.vm2.LuaValue.valueOf(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x11f2, code lost:
    
        r5 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x11f5, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x120c, code lost:
    
        if (r37.equals("SendEventDeferred") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x121d, code lost:
    
        if (r37.equals("ResetWaypoints") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x122e, code lost:
    
        if (r37.equals("GetSunDirection") != false) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x125d, code lost:
    
        if (r37.equals("TweenPosition") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x1270, code lost:
    
        if (r37.equals("IsClient") != false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x1272, code lost:
    
        r0 = org.luaj.vm2.LuaValue.TRUE;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "TRUE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x135f, code lost:
    
        if (r37.equals("GetCanUndo") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x1372, code lost:
    
        if (r37.equals("GetCanRedo") != false) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x1374, code lost:
    
        r0 = org.luaj.vm2.LuaValue.TRUE;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "TRUE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x138e, code lost:
    
        if (r37.equals("WorldToViewportPoint") != false) goto L704;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x14de, code lost:
    
        if (r37.equals("UserOwnsGamePassAsync") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x14ef, code lost:
    
        if (r37.equals("SetDescription") != false) goto L746;
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x1523, code lost:
    
        if (r37.equals("PromptImportFile") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1585, code lost:
    
        if (r37.equals("SetEnabled") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1597, code lost:
    
        if (r37.equals("IsInGroup") != false) goto L770;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1599, code lost:
    
        r0 = org.luaj.vm2.LuaValue.valueOf(kotlin.jvm.internal.Intrinsics.areEqual(r37, "HasAppearanceLoaded"));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x15ba, code lost:
    
        if (r37.equals("GetStudioUserId") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:775:0x15cb, code lost:
    
        if (r37.equals("ViewportPointToRay") != false) goto L776;
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x15cd, code lost:
    
        r0 = r2.guiUnit();
        r4 = r2.getView();
        kotlin.jvm.internal.Intrinsics.checkNotNull(r12);
        r5 = com.goydevv.studrblx.lua.LuaTypesKt.num(r12) * r0;
        r6 = r38.arg(3);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, "arg(...)");
        r4 = r4.screenRay(r5, com.goydevv.studrblx.lua.LuaTypesKt.num(r6) * r0);
        r5 = r4.component1();
        r4 = r4.component2();
        r6 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r13 = r38.arg(4);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r13, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x162a, code lost:
    
        if (r37.equals("CollisionGroupSetCollidable") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x1664, code lost:
    
        if (r37.equals("TeleportAsync") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:787:0x1675, code lost:
    
        if (r37.equals("InputHoldBegin") != false) goto L788;
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x1687, code lost:
    
        if (r37.equals("UserHasBadgeAsync") != false) goto L791;
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:0x16a3, code lost:
    
        if (r37.equals("addItem") != false) goto L794;
     */
    /* JADX WARN: Code restructure failed: missing block: B:807:0x1716, code lost:
    
        if (r37.equals("TrackEvent") != false) goto L808;
     */
    /* JADX WARN: Code restructure failed: missing block: B:813:0x1761, code lost:
    
        if (r37.equals("IsActivated") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x1772, code lost:
    
        if (r37.equals("GetSelectedRibbonTool") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:819:0x1783, code lost:
    
        if (r37.equals("Teleport") != false) goto L820;
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x1785, code lost:
    
        com.goydevv.studrblx.core.OutputLog.INSTANCE.warn("Teleporting is not supported in Studio");
        r0 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:876:0x188d, code lost:
    
        if (r37.equals("IsRunning") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:879:0x189f, code lost:
    
        if (r37.equals("IsRunMode") != false) goto L880;
     */
    /* JADX WARN: Code restructure failed: missing block: B:880:0x18a1, code lost:
    
        r0 = org.luaj.vm2.LuaValue.valueOf(r35.getIsPlay());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:882:0x18bf, code lost:
    
        if (r37.equals("FinishRecording") != false) goto L883;
     */
    /* JADX WARN: Code restructure failed: missing block: B:885:0x18d9, code lost:
    
        if (r37.equals("CreateCollisionGroup") != false) goto L886;
     */
    /* JADX WARN: Code restructure failed: missing block: B:886:0x18db, code lost:
    
        r5 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:888:0x18ee, code lost:
    
        if (r37.equals("GetUserId") != false) goto L889;
     */
    /* JADX WARN: Code restructure failed: missing block: B:889:0x18f0, code lost:
    
        r0 = org.luaj.vm2.LuaValue.valueOf(0);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:891:0x190c, code lost:
    
        if (r37.equals("PromptProductPurchase") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:894:0x191d, code lost:
    
        if (r37.equals("PromptGamePassPurchase") != false) goto L895;
     */
    /* JADX WARN: Code restructure failed: missing block: B:903:0x1991, code lost:
    
        if (r37.equals("Activate") != false) goto L904;
     */
    /* JADX WARN: Code restructure failed: missing block: B:905:0x199d, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r36.getClassName(), "Plugin") == false) goto L907;
     */
    /* JADX WARN: Code restructure failed: missing block: B:906:0x199f, code lost:
    
        r0 = org.luaj.vm2.LuaValue.NONE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:908:0x19a8, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r37, "Activate") == false) goto L910;
     */
    /* JADX WARN: Code restructure failed: missing block: B:909:0x19aa, code lost:
    
        r0 = "Activated";
     */
    /* JADX WARN: Code restructure failed: missing block: B:910:0x19ad, code lost:
    
        r0 = "Deactivated";
     */
    /* JADX WARN: Code restructure failed: missing block: B:911:0x19af, code lost:
    
        r35.fire(r36, r0, new org.luaj.vm2.LuaValue[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:912:0x19b6, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:914:0x19cd, code lost:
    
        if (r37.equals("GetPlaySessionId") != false) goto L915;
     */
    /* JADX WARN: Code restructure failed: missing block: B:915:0x19cf, code lost:
    
        r0 = org.luaj.vm2.LuaValue.valueOf("00000000-0000-0000-0000-000000000000");
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:920:0x1a0a, code lost:
    
        if (r37.equals("SetSetting") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:942:0x1aa8, code lost:
    
        if (r37.equals("ReleaseFocus") != false) goto L943;
     */
    /* JADX WARN: Code restructure failed: missing block: B:978:0x1c23, code lost:
    
        if (r37.equals("BindAction") == false) goto L991;
     */
    /* JADX WARN: Code restructure failed: missing block: B:990:0x1c89, code lost:
    
        if (r37.equals("OpenScript") == false) goto L991;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v111 */
    /* JADX WARN: Type inference failed for: r14v113 */
    /*  JADX ERROR: UnsupportedOperationException in pass: SwitchBreakVisitor
        java.lang.UnsupportedOperationException
        	at java.base/java.util.Collections$UnmodifiableCollection.add(Collections.java:1092)
        	at jadx.core.dex.visitors.regions.SwitchBreakVisitor$BaseSwitchRegionVisitor.leaveRegion(SwitchBreakVisitor.java:210)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:91)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:31)
        	at jadx.core.dex.visitors.regions.SwitchBreakVisitor$IterativeSwitchRegionVisitor.leaveRegion(SwitchBreakVisitor.java:177)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:91)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:27)
        	at jadx.core.dex.visitors.regions.SwitchBreakVisitor.runSwitchTraverse(SwitchBreakVisitor.java:52)
        	at jadx.core.dex.visitors.regions.SwitchBreakVisitor.visit(SwitchBreakVisitor.java:45)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs call(com.goydevv.studrblx.lua.InstanceApi api, com.goydevv.studrblx.lua.LuaRuntime rt, com.goydevv.studrblx.model.Instance svc, java.lang.String name, org.luaj.vm2.Varargs a) throws java.io.UnsupportedEncodingException {
        String scn = svc.getClassName();
        switch (scn) {
            case "TweenService":
                if ("Create".equals(name)) {
                    Object instObj = a.arg(2).isuserdata() ? a.arg(2).touserdata() : null;
                    Object infoObj = a.arg(3).isuserdata() ? a.arg(3).touserdata() : null;
                    LuaTable goalTbl = a.arg(4).istable() ? a.arg(4).checktable() : null;
                    if (instObj instanceof Instance && infoObj instanceof TweenInfoV && goalTbl != null) {
                        java.util.LinkedHashMap<String, Object> goals = new java.util.LinkedHashMap<>();
                        LuaValue k = LuaValue.NIL;
                        while (true) {
                            Varargs n = goalTbl.next(k);
                            k = n.arg1();
                            if (k.isnil()) break;
                            String ks = k.tojstring();
                            if (ks != null) {
                                goals.put(ks, rt.fromLua(n.arg(2), null, (Instance) instObj, ks));
                            }
                        }
                        return tweenValue(rt, new Tween((Instance) instObj, (TweenInfoV) infoObj, goals));
                    }
                }
                if ("GetValue".equals(name)) {
                    double alpha = Math.max(0.0d, Math.min(1.0d, a.arg(2).optdouble(0.0d)));
                    Object stObj = a.arg(3).isuserdata() ? a.arg(3).touserdata() : null;
                    int style = (stObj instanceof EnumItemV) ? ((EnumItemV) stObj).getValue() : a.arg(3).optint(0);
                    Object drObj = a.arg(4).isuserdata() ? a.arg(4).touserdata() : null;
                    int dir = (drObj instanceof EnumItemV) ? ((EnumItemV) drObj).getValue() : a.arg(4).optint(1);
                    return LuaValue.valueOf(evalEasing(alpha, style, dir));
                }
                if ("SmoothDamp".equals(name)) {
                    double smoothTime = Math.max(1.0E-4d, a.arg(5).optdouble(0.3d));
                    double maxSpeed = a.arg(6).isnumber() ? a.arg(6).checkdouble() : Double.POSITIVE_INFINITY;
                    double dt = a.arg(7).optdouble(0.016666667d);
                    double omega = 2.0d / smoothTime;
                    double x = omega * dt;
                    double exp = 1.0d / (1.0d + x + (0.48d * x * x) + (0.235d * x * x * x));
                    LuaValue curV = a.arg(2);
                    LuaValue tgtV = a.arg(3);
                    LuaValue velV = a.arg(4);
                    if (curV.isnumber()) {
                        double cur = curV.checkdouble();
                        double tgt = tgtV.checkdouble();
                        double vel = velV.optdouble(0.0d);
                        double change = cur - tgt;
                        double maxChg = maxSpeed * smoothTime;
                        change = Math.max(-maxChg, Math.min(maxChg, change));
                        double adjTgt = cur - change;
                        double temp = (vel + (omega * change)) * dt;
                        double newVel = (vel - (omega * temp)) * exp;
                        double outVal = adjTgt + ((change + temp) * exp);
                        return LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(outVal), LuaValue.valueOf(newVel)});
                    }
                    Object co = curV.isuserdata() ? curV.touserdata() : null;
                    Object to = tgtV.isuserdata() ? tgtV.touserdata() : null;
                    Object vo = velV.isuserdata() ? velV.touserdata() : null;
                    if (co instanceof Vector3 && to instanceof Vector3) {
                        Vector3 c3 = (Vector3) co;
                        Vector3 t3 = (Vector3) to;
                        Vector3 v3 = vo instanceof Vector3 ? (Vector3) vo : Vector3.INSTANCE.getZERO();
                        Vector3 chg = c3.minus(t3);
                        float mag = chg.getMagnitude();
                        float maxChg = (float) Math.min(maxSpeed * smoothTime, 1.0E9d);
                        if (mag > maxChg && mag > 1.0E-5f) chg = chg.div(mag).times(maxChg);
                        Vector3 adj = c3.minus(chg);
                        Vector3 tmp = v3.plus(chg.times((float) omega)).times((float) dt);
                        Vector3 nVel = v3.minus(tmp.times((float) omega)).times((float) exp);
                        Vector3 nPos = adj.plus(chg.plus(tmp).times((float) exp));
                        return LuaValue.varargsOf(new LuaValue[]{LuaTypes.INSTANCE.wrap(nPos), LuaTypes.INSTANCE.wrap(nVel)});
                    }
                    return LuaValue.varargsOf(new LuaValue[]{curV, velV});
                }
                return LuaValue.NIL;
            case "ContextActionService": {
                if ("BindAction".equals(name) || "BindActionAtPriority".equals(name)) {
                    final String actName = a.arg(2).checkjstring();
                    final LuaValue fn = a.arg(3);
                    boolean makeBtn = a.arg(4).toboolean();
                    int prio = "BindActionAtPriority".equals(name) ? a.arg(5).optint(2000) : 2000;
                    LuaTable info = new LuaTable();
                    info.set("priorityLevel", prio);
                    info.set("stackOrder", CAS_ACTIONS.size() + 1);
                    info.set("createTouchButton", LuaValue.valueOf(makeBtn));
                    CAS_ACTIONS.put(actName, new CasEntry(actName, fn, prio, info, null));
                    if (makeBtn && rt.getPlay() != null && rt.getPlay().getPlayer() != null && CAS_BTN_COUNT < 7) {
                        Instance pg = Instance.findFirstChild$default(rt.getPlay().getPlayer(), "PlayerGui", false, 2, null);
                        if (pg != null) {
                            Instance cag = Instance.findFirstChild$default(pg, "ContextActionGui", false, 2, null);
                            if (cag == null) {
                                cag = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("ScreenGui");
                                cag.setName("ContextActionGui");
                                Instance.setParent$default(cag, pg, 0, 2, null);
                            }
                            Instance frame = Instance.findFirstChild$default(cag, "ContextButtonFrame", false, 2, null);
                            if (frame == null) {
                                frame = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("Frame");
                                frame.setName("ContextButtonFrame");
                                frame.set("BackgroundTransparency", Float.valueOf(1.0f), false);
                                frame.set("Size", new com.goydevv.studrblx.model.UDim2(0.4f, 0, 0.4f, 0), false);
                                frame.set("Position", new com.goydevv.studrblx.model.UDim2(0.58f, 0, 0.52f, 0), false);
                                Instance.setParent$default(frame, cag, 0, 2, null);
                            }
                            Instance btn = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("TextButton");
                            btn.setName(actName);
                            btn.set("Text", actName, false);
                            btn.set("Size", new com.goydevv.studrblx.model.UDim2(0.0f, 64, 0.0f, 64), false);
                            btn.set("Position", new com.goydevv.studrblx.model.UDim2(0.0f, (CAS_BTN_COUNT % 3) * 72, 0.0f, (CAS_BTN_COUNT / 3) * 72), false);
                            btn.set("BackgroundColor3", com.goydevv.studrblx.model.Color3.INSTANCE.fromRGB(30, 34, 42), false);
                            btn.set("BackgroundTransparency", Float.valueOf(0.25f), false);
                            btn.set("TextColor3", com.goydevv.studrblx.model.Color3.INSTANCE.fromRGB(255, 255, 255), false);
                            Instance.setParent$default(btn, frame, 0, 2, null);
                            CAS_BTN_COUNT++;
                            CAS_ACTIONS.get(actName).button = btn;
                            if (fn.isfunction()) {
                                rt.connect(btn, "MouseButton1Down", LuaTypesKt.fnv(new Function1() {
                                    @Override
                                    public final Object invoke(Object obj) {
                                        fn.invoke(LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(actName), LuaTypes.INSTANCE.enumItem("UserInputState", "Begin"), new LuaTable()}));
                                        return LuaValue.NONE;
                                    }
                                }), false);
                                rt.connect(btn, "MouseButton1Up", LuaTypesKt.fnv(new Function1() {
                                    @Override
                                    public final Object invoke(Object obj) {
                                        fn.invoke(LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(actName), LuaTypes.INSTANCE.enumItem("UserInputState", "End"), new LuaTable()}));
                                        return LuaValue.NONE;
                                    }
                                }), false);
                            }
                        }
                    }
                    return LuaValue.NONE;
                }
                if ("UnbindAction".equals(name)) {
                    CasEntry ce = CAS_ACTIONS.remove(a.arg(2).optjstring(""));
                    if (ce != null && ce.button != null) {
                        ce.button.destroy();
                        if (CAS_BTN_COUNT > 0) CAS_BTN_COUNT--;
                    }
                    return LuaValue.NONE;
                }
                if ("UnbindAllActions".equals(name)) {
                    for (CasEntry ce : CAS_ACTIONS.values()) {
                        if (ce.button != null) ce.button.destroy();
                    }
                    CAS_ACTIONS.clear();
                    CAS_BTN_COUNT = 0;
                    return LuaValue.NONE;
                }
                if ("GetButton".equals(name)) {
                    CasEntry ce = CAS_ACTIONS.get(a.arg(2).optjstring(""));
                    return (ce != null && ce.button != null) ? rt.wrap(ce.button) : LuaValue.NIL;
                }
                if ("SetTitle".equals(name)) {
                    CasEntry ce = CAS_ACTIONS.get(a.arg(2).optjstring(""));
                    if (ce != null && ce.button != null) ce.button.set("Text", a.arg(3).optjstring(""), false);
                    return LuaValue.NONE;
                }
                if ("SetPosition".equals(name)) {
                    CasEntry ce = CAS_ACTIONS.get(a.arg(2).optjstring(""));
                    Object uo = a.arg(3).isuserdata() ? a.arg(3).touserdata() : null;
                    if (ce != null && ce.button != null && uo instanceof com.goydevv.studrblx.model.UDim2) {
                        ce.button.set("Position", uo, false);
                    }
                    return LuaValue.NONE;
                }
                if ("SetImage".equals(name) || "SetDescription".equals(name)) {
                    return LuaValue.NONE;
                }
                if ("GetBoundActionInfo".equals(name)) {
                    CasEntry ce = CAS_ACTIONS.get(a.arg(2).optjstring(""));
                    return ce != null ? ce.info : new LuaTable();
                }
                if ("GetAllBoundActionInfo".equals(name)) {
                    LuaTable all = new LuaTable();
                    for (CasEntry ce : CAS_ACTIONS.values()) {
                        all.set(ce.name, ce.info);
                    }
                    return all;
                }
                return LuaValue.NIL;
            }
            case "StarterGui": {
                if ("SetCoreGuiEnabled".equals(name)) {
                    CORE_GUI_ENABLED.put(Integer.valueOf(a.arg(2).isuserdata() && a.arg(2).touserdata() instanceof EnumItemV ? ((EnumItemV) a.arg(2).touserdata()).getValue() : a.arg(2).optint(0)), Boolean.valueOf(a.arg(3).toboolean()));
                    return LuaValue.NONE;
                }
                if ("GetCoreGuiEnabled".equals(name)) {
                    int key = a.arg(2).isuserdata() && a.arg(2).touserdata() instanceof EnumItemV ? ((EnumItemV) a.arg(2).touserdata()).getValue() : a.arg(2).optint(0);
                    Boolean b = CORE_GUI_ENABLED.get(Integer.valueOf(key));
                    return LuaValue.valueOf(b == null || b.booleanValue());
                }
                if ("SetCore".equals(name)) {
                    return LuaValue.NONE;
                }
                if ("GetCore".equals(name)) {
                    return LuaValue.TRUE;
                }
                return LuaValue.NIL;
            }
            case "GuiService": {
                if ("GetGuiInset".equals(name)) {
                    return LuaValue.varargsOf(new LuaValue[]{LuaTypes.INSTANCE.wrap(new com.goydevv.studrblx.model.Vector2(0.0f, 36.0f)), LuaTypes.INSTANCE.wrap(new com.goydevv.studrblx.model.Vector2(0.0f, 0.0f))});
                }
                if ("IsTenFootInterface".equals(name)) {
                    return LuaValue.FALSE;
                }
                return LuaValue.NIL;
            }
            case "Teams": {
                if ("GetTeams".equals(name)) {
                    LuaTable out = new LuaTable();
                    int idx = 1;
                    for (Instance ch : svc.getChildren()) {
                        if ("Team".equals(ch.getClassName())) out.set(idx++, rt.wrap(ch));
                    }
                    return out;
                }
                return LuaValue.NIL;
            }
            case "PhysicsService": {
                if ("RegisterCollisionGroup".equals(name) || "CreateCollisionGroup".equals(name)) {
                    COLLISION_GROUPS.add(a.arg(2).optjstring("Default"));
                    return LuaValue.NONE;
                }
                if ("IsCollisionGroupRegistered".equals(name)) {
                    String gn = a.arg(2).optjstring("");
                    return LuaValue.valueOf("Default".equals(gn) || COLLISION_GROUPS.contains(gn));
                }
                if ("GetRegisteredCollisionGroups".equals(name)) {
                    LuaTable out = new LuaTable();
                    int idx = 1;
                    LuaTable def = new LuaTable(); def.set("name", "Default"); def.set("mask", -1);
                    out.set(idx++, def);
                    for (String g : COLLISION_GROUPS) {
                        if ("Default".equals(g)) continue;
                        LuaTable e = new LuaTable(); e.set("name", g); e.set("mask", -1);
                        out.set(idx++, e);
                    }
                    return out;
                }
                if ("CollisionGroupsAreCollidable".equals(name)) {
                    return LuaValue.TRUE;
                }
                return LuaValue.NONE;
            }
            case "BadgeService": {
                if ("UserHasBadgeAsync".equals(name)) return LuaValue.FALSE;
                if ("AwardBadge".equals(name)) return LuaValue.TRUE;
                if ("GetBadgeInfoAsync".equals(name)) {
                    LuaTable bi = new LuaTable();
                    bi.set("Name", "Badge"); bi.set("Description", ""); bi.set("IconImageId", 0); bi.set("IsEnabled", LuaValue.TRUE);
                    return bi;
                }
                return LuaValue.NIL;
            }
            case "TeleportService": {
                return LuaValue.NONE;
            }
            case "Debris":
                if ("AddItem".equals(name)) {
                    Object o = a.arg(2).isuserdata() ? a.arg(2).touserdata() : null;
                    double sec = a.optdouble(3, 10.0d);
                    if (o instanceof Instance) {
                        final Instance item = (Instance) o;
                        rt.after(sec, new Function0<Unit>() {
                            @Override public Unit invoke() { return call$lambda$3(item); }
                        });
                    }
                }
                return LuaValue.NIL;
            case "Players":
                if ("GetPlayers".equals(name)) {
                    LuaTable tbl = new LuaTable();
                    com.goydevv.studrblx.engine.PlaySession pl = rt.getPlay();
                    if (pl != null && pl.getPlayer() != null) tbl.set(1, rt.wrap(pl.getPlayer()));
                    return tbl;
                }
                if ("GetPlayerFromCharacter".equals(name)) {
                    Object o = a.arg(2).isuserdata() ? a.arg(2).touserdata() : null;
                    com.goydevv.studrblx.engine.PlaySession pl = rt.getPlay();
                    if (pl != null && o instanceof Instance && o == pl.getCharacter() && pl.getPlayer() != null) {
                        return rt.wrap(pl.getPlayer());
                    }
                    return LuaValue.NIL;
                }
                return LuaValue.NIL;
            case "RunService":
                if ("IsClient".equals(name) || "IsServer".equals(name) || "IsStudio".equals(name)) return LuaValue.TRUE;
                if ("IsRunning".equals(name) || "IsRunMode".equals(name)) return LuaValue.valueOf(rt.getPlay() != null);
                return LuaValue.NIL;
            case "HttpService":
                return http(rt, svc, name, a);
            case "DataStoreService":
                if ("GetDataStore".equals(name) || "GetOrderedDataStore".equals(name)) {
                    return dataStore(rt, a.checkjstring(2));
                }
                return LuaValue.NIL;
            case "PathfindingService":
                if ("CreatePath".equals(name)) {
                    return pathObject(rt);
                }
                return LuaValue.NIL;
            case "MarketplaceService":
                if ("GetProductInfo".equals(name)) {
                    return productInfo(rt, a.checklong(2), a.optjstring(3, "Asset"));
                }
                return LuaValue.NIL;
            case "Lighting":
                if ("GetMinutesAfterMidnight".equals(name)) {
                    Object ct = svc.get("ClockTime");
                    float ctf = ct instanceof Number ? ((Number) ct).floatValue() : 14.0f;
                    return LuaValue.valueOf(ctf * 60.0f);
                }
                if ("SetMinutesAfterMidnight".equals(name)) {
                    float mins = (float) a.checkdouble(2);
                    svc.set("ClockTime", Float.valueOf((mins / 60.0f) % 24.0f), true);
                    return LuaValue.NIL;
                }
                return LuaValue.NIL;
            case "SoundService":
                if ("PlayLocalSound".equals(name)) {
                    Object o = a.arg(2).isuserdata() ? a.arg(2).touserdata() : null;
                    if (o instanceof Instance) {
                        Instance snd = (Instance) o;
                        Object sq = snd.getAttributes().get("__playSeq");
                        int nsq = (sq instanceof Integer) ? ((Integer) sq).intValue() + 1 : 1;
                        snd.getAttributes().put("__playSeq", Integer.valueOf(nsq));
                        snd.set("Playing", Boolean.TRUE, true);
                    }
                }
                return LuaValue.NIL;
            case "UserInputService":
                if ("IsKeyDown".equals(name)) return LuaValue.FALSE;
                if ("GetMouseLocation".equals(name)) return LuaTypes.INSTANCE.wrap(new com.goydevv.studrblx.model.Vector2(0.0f, 0.0f));
                return LuaValue.NIL;
            default:
                return LuaValue.NIL;
        }
    }

    static final Unit call$lambda$3(Instance $item) {
        if (!$item.getDestroyed()) {
            $item.destroy();
        }
        return Unit.INSTANCE;
    }

    static final Varargs call$lambda$18(String $text, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaString luaStringValueOf = LuaValue.valueOf($text);
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    static final Varargs call$lambda$24$lambda$23(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs call$lambda$27$lambda$25(Varargs a2x) {
        Intrinsics.checkNotNullParameter(a2x, "a2x");
        LuaValue luaValueArg = a2x.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        return luaValueArg;
    }

    static final Varargs call$lambda$27$lambda$26(Varargs a2x) {
        Intrinsics.checkNotNullParameter(a2x, "a2x");
        LuaValue luaValueArg = a2x.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        return luaValueArg;
    }

    private static final LuaTable productInfo$placeholder(long $id) {
        LuaTable luaTable = new LuaTable();
        luaTable.set("Name", "Product");
        luaTable.set("Description", "");
        luaTable.set("PriceInRobux", 0);
        luaTable.set("AssetId", $id);
        luaTable.set("IsForSale", LuaValue.FALSE);
        return luaTable;
    }

    private final LuaValue productInfo(final LuaRuntime rt, final long id, String infoType) {
        final ScriptThread co = rt.currentCoroutine();
        if (co == null) {
            return productInfo$placeholder(id);
        }
        if (!Intrinsics.areEqual(infoType, "Asset") || id <= 0) {
            return productInfo$placeholder(id);
        }
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                RobloxServices.productInfo$lambda$32(id, rt, co);
            }
        }).start();
        throw new LuauYield(new LuauState.Continuation() {
            @Override
            public Varargs resume(Varargs r) {
                return r.arg1();
            }
        });
    }

    static final void productInfo$lambda$32(final long $id, final LuaRuntime $rt, final ScriptThread $co) {
        String str;
        try {
            str = new String(Http.get$default(Http.INSTANCE, "https://economy.roblox.com/v2/assets/" + $id + "/details", null, 0, 6, null), Charsets.UTF_8);
        } catch (Exception e) {
            str = null;
        }
        final String json = str;
        $rt.getMainQueue().add(new Function0() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return RobloxServices.productInfo$lambda$32$lambda$31(json, $rt, $co, $id);
            }
        });
    }

    static final Unit productInfo$lambda$32$lambda$31(String $json, LuaRuntime $rt, ScriptThread $co, long $id) {
        LuaValue v = null;
        if ($json != null) {
            try {
                v = Json.INSTANCE.decode($rt, $json);
            } catch (Exception e) {
            }
        }
        $rt.resume($co, (v == null || !v.istable()) ? productInfo$placeholder($id) : v);
        return Unit.INSTANCE;
    }

    private final LuaValue tweenValue(final LuaRuntime rt, final Tween tw) {
        LuaTable t = new LuaTable();
        t.set("Play", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.tweenValue$lambda$35(rt, tw, (Varargs) obj);
            }
        }));
        t.set("Pause", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.tweenValue$lambda$36(tw, (Varargs) obj);
            }
        }));
        t.set("Cancel", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.tweenValue$lambda$37(tw, rt, (Varargs) obj);
            }
        }));
        t.set("Destroy", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.tweenValue$lambda$38(tw, rt, (Varargs) obj);
            }
        }));
        t.set("Instance", rt.wrap(tw.getInst()));
        t.set("TweenInfo", LuaTypes.INSTANCE.wrap(tw.getInfo()));
        LuaTable mt = new LuaTable();
        mt.set("__type", "Tween");
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return RobloxServices.tweenValue$lambda$39(rt, tw, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        t.setmetatable(mt);
        return t;
    }

    static final Varargs tweenValue$lambda$35(LuaRuntime $rt, Tween $tw, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $rt.playTween($tw);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs tweenValue$lambda$36(Tween $tw, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if ($tw.getState() == 2) {
            $tw.setState(3);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs tweenValue$lambda$37(Tween $tw, LuaRuntime $rt, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $tw.setState(5);
        $rt.getTweens().remove($tw);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs tweenValue$lambda$38(Tween $tw, LuaRuntime $rt, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $tw.setState(5);
        $rt.getTweens().remove($tw);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final LuaValue tweenValue$lambda$39(LuaRuntime $rt, Tween $tw, LuaValue luaValue, LuaValue k) {
        Intrinsics.checkNotNullParameter(luaValue, "<unused var>");
        Intrinsics.checkNotNullParameter(k, "k");
        String str = k.tojstring();
        if (Intrinsics.areEqual(str, "Completed")) {
            return $rt.getApi().signal($tw, "Completed");
        }
        if (Intrinsics.areEqual(str, "PlaybackState")) {
            List names = CollectionsKt.listOf(new String[]{"Begin", "Delayed", "Playing", "Paused", "Completed", "Cancelled"});
            return LuaTypes.INSTANCE.wrap(new EnumItemV("PlaybackState", (String) names.get($tw.getState()), $tw.getState()));
        }
        LuaValue NIL = LuaValue.NIL;
        Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        return NIL;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [T, java.util.List] */
    private final LuaTable pathObject(final LuaRuntime rt) {
        final LuaTable p = new LuaTable();
        final Ref.ObjectRef pts = new Ref.ObjectRef();
        pts.element = CollectionsKt.emptyList();
        p.set("Status", LuaTypes.INSTANCE.wrap(new EnumItemV("PathStatus", "Success", 0)));
        p.set("ComputeAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.pathObject$lambda$40(pts, (Varargs) obj);
            }
        }));
        p.set("GetWaypoints", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.pathObject$lambda$41(pts, (Varargs) obj);
            }
        }));
        p.set("Destroy", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.pathObject$lambda$42((Varargs) obj);
            }
        }));
        LuaTable mt = new LuaTable();
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return RobloxServices.pathObject$lambda$43(rt, p, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        p.setmetatable(mt);
        return p;
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [T, java.util.List] */
    static final Varargs pathObject$lambda$40(Ref.ObjectRef $pts, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Vector3[] vector3Arr = new Vector3[2];
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 vector3 = (Vector3) obj;
        if (vector3 == null) {
            throw new LuaError("Vector3 expected, got " + LuaTypes.INSTANCE.typeOf(luaValueArg));
        }
        vector3Arr[0] = vector3;
        LuaValue luaValueArg2 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 vector4 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector4 != null) {
            vector3Arr[1] = vector4;
            $pts.element = CollectionsKt.listOf(vector3Arr);
            LuaValue NONE = LuaValue.NONE;
            Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
            return NONE;
        }
        throw new LuaError("Vector3 expected, got " + LuaTypes.INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs pathObject$lambda$41(Ref.ObjectRef $pts, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaTable t = new LuaTable();
        for (Vector3 v : (List<Vector3>) $pts.element) {
            LuaTable w = new LuaTable();
            w.set("Position", LuaTypes.INSTANCE.wrap(v));
            w.set("Action", LuaTypes.INSTANCE.wrap(new EnumItemV("PathWaypointAction", "Walk", 0)));
            t.insert(0, w);
        }
        return t;
    }

    static final Varargs pathObject$lambda$42(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final LuaValue pathObject$lambda$43(LuaRuntime $rt, LuaTable $p, LuaValue luaValue, LuaValue k) {
        Intrinsics.checkNotNullParameter(luaValue, "<unused var>");
        Intrinsics.checkNotNullParameter(k, "k");
        if (!Intrinsics.areEqual(k.tojstring(), "Blocked") && !Intrinsics.areEqual(k.tojstring(), "Unblocked")) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        InstanceApi api = $rt.getApi();
        String str = k.tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        return api.signal($p, str);
    }

    private final LuaTable dataStore(final LuaRuntime rt, String name) {
        HashMap<String, String> map;
        Map dataStores = rt.getStudio().getDataStores();
        HashMap<String, String> map2 = (HashMap<String, String>) dataStores.get(name);
        if (map2 == null) {
            map = new HashMap<>();
            dataStores.put(name, map);
        } else {
            map = map2;
        }
        final HashMap<String, String> map3 = map;
        LuaTable t = new LuaTable();
        t.set("GetAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$46(map3, rt, (Varargs) obj);
            }
        }));
        t.set("SetAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$47(map3, (Varargs) obj);
            }
        }));
        t.set("RemoveAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$48(map3, rt, (Varargs) obj);
            }
        }));
        t.set("IncrementAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$49(map3, rt, (Varargs) obj);
            }
        }));
        t.set("UpdateAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$50(map3, rt, (Varargs) obj);
            }
        }));
        t.set("GetSortedAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$54(map3, rt, (Varargs) obj);
            }
        }));
        return t;
    }

    private static final LuaValue dataStore$get(HashMap<String, String> map, LuaRuntime $rt, String k) {
        LuaValue luaValueDecode;
        String str = map.get(k);
        if (str != null && (luaValueDecode = Json.INSTANCE.decode($rt, str)) != null) {
            return luaValueDecode;
        }
        LuaValue NIL = LuaValue.NIL;
        Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        return NIL;
    }

    static final Varargs dataStore$lambda$46(HashMap $store, LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        String str = a.arg(2).tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        return dataStore$get($store, $rt, str);
    }

    static final Varargs dataStore$lambda$47(HashMap $store, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        String str = a.arg(2).tojstring();
        Json json = Json.INSTANCE;
        LuaValue luaValueArg = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        $store.put(str, json.encode(luaValueArg));
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs dataStore$lambda$48(HashMap $store, LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        String k = a.arg(2).tojstring();
        Intrinsics.checkNotNull(k);
        LuaValue old = dataStore$get($store, $rt, k);
        $store.remove(k);
        return old;
    }

    static final Varargs dataStore$lambda$49(HashMap $store, LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        String k = a.arg(2).tojstring();
        Intrinsics.checkNotNull(k);
        double nv = dataStore$get($store, $rt, k).optdouble(0.0d) + a.arg(3).optdouble(1.0d);
        Json json = Json.INSTANCE;
        LuaNumber luaNumberValueOf = LuaValue.valueOf(nv);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        $store.put(k, json.encode(luaNumberValueOf));
        LuaNumber luaNumberValueOf2 = LuaValue.valueOf(nv);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
        return luaNumberValueOf2;
    }

    static final Varargs dataStore$lambda$50(HashMap $store, LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        String k = a.arg(2).tojstring();
        LuaValue luaValueArg = a.arg(3);
        Intrinsics.checkNotNull(k);
        LuaValue r = luaValueArg.call(dataStore$get($store, $rt, k));
        if (!r.isnil()) {
            Json json = Json.INSTANCE;
            Intrinsics.checkNotNull(r);
            $store.put(k, json.encode(r));
        }
        Intrinsics.checkNotNull(r);
        return r;
    }

    static final Varargs dataStore$lambda$54(HashMap $store, LuaRuntime $rt, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaTable page = new LuaTable();
        final LuaTable items = new LuaTable();
        Iterable<Map.Entry> iterableEntrySet = $store.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        for (Map.Entry entry : iterableEntrySet) {
            Intrinsics.checkNotNull(entry);
            Object key = entry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "component1(...)");
            Object value = entry.getValue();
            Intrinsics.checkNotNullExpressionValue(value, "component2(...)");
            LuaTable luaTable = new LuaTable();
            luaTable.set("key", (String) key);
            luaTable.set("value", Json.INSTANCE.decode($rt, (String) value));
            items.insert(0, luaTable);
        }
        page.set("GetCurrentPage", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$54$lambda$52(items, (Varargs) obj);
            }
        }));
        page.set("AdvanceToNextPageAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxServices.dataStore$lambda$54$lambda$53((Varargs) obj);
            }
        }));
        page.set("IsFinished", LuaValue.TRUE);
        return page;
    }

    static final Varargs dataStore$lambda$54$lambda$52(LuaTable $items, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return $items;
    }

    static final Varargs dataStore$lambda$54$lambda$53(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    private final Varargs http(final LuaRuntime rt, Instance svc, final String name, Varargs a) {
        String body;
        final String method;
        final String url;
        final String url2;
        if (!Intrinsics.areEqual(svc.get("HttpEnabled"), (Object) true)) {
            throw new LuaError("Http requests are not enabled. Enable via game settings (set HttpService.HttpEnabled = true)");
        }
        final ScriptThread co = rt.currentCoroutine();
        if (co == null) {
            throw new LuaError("HTTP requests must be made from a yieldable thread");
        }
        LuaValue req = a.arg(2);
        final HashMap headers = new HashMap();
        if (Intrinsics.areEqual(name, "RequestAsync")) {
            String url3 = req.get("Url").checkjstring();
            Intrinsics.checkNotNullExpressionValue(url3, "checkjstring(...)");
            String method2 = req.get("Method").optjstring("GET");
            Intrinsics.checkNotNullExpressionValue(method2, "optjstring(...)");
            body = req.get("Body").isnil() ? null : req.get("Body").tojstring();
            LuaValue h = req.get("Headers");
            if (h.istable()) {
                LuaValue k = LuaValue.NIL;
                while (true) {
                    Varargs n = h.next(k);
                    k = n.arg1();
                    if (k.isnil()) {
                        break;
                    }
                    headers.put(k.tojstring(), n.arg(2).tojstring());
                }
            }
            method = method2;
            url = url3;
            url2 = body;
        } else {
            String url4 = req.checkjstring();
            Intrinsics.checkNotNullExpressionValue(url4, "checkjstring(...)");
            String method3 = Intrinsics.areEqual(name, "PostAsync") ? "POST" : "GET";
            body = Intrinsics.areEqual(name, "PostAsync") ? a.arg(3).tojstring() : null;
            if (Intrinsics.areEqual(name, "PostAsync")) {
                headers.put("Content-Type", "application/json");
            }
            method = method3;
            url = url4;
            url2 = body;
        }
        final String who = rt.getPlay() != null ? "A script in this game" : "A plugin";
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RobloxServices.http$lambda$58(url, method, url2, rt, who, headers, name, co);
            }
        }).start();
        throw new LuauYield(new LuauState.Continuation() {
            @Override
            public Varargs resume(Varargs r) {
                if (!r.arg1().toboolean()) {
                    throw new LuaError(r.arg(2).tojstring());
                }
                return r.arg(2);
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:101:0x020a  */
    /* JADX WARN: Code duplicated, block: B:87:0x01bd  */
    /* JADX WARN: Multi-variable type inference failed */
    static final void http$lambda$58(String str, String str2, String str3, final LuaRuntime luaRuntime, String str4, HashMap map, final String str5, final ScriptThread luaThread) {
        String message;
        String string;
        String lowerCase;
        HttpURLConnection httpURLConnection;
        String str6;
        String t;
        Throwable th;
        String str7 = "toLowerCase(...)";
        final Ref.IntRef intRef = new Ref.IntRef();
        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
        String str8 = "";
        objectRef.element = "";
        final Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        String string2 = str;
        int i = 0;
        String str9 = str2;
        String str10 = str3;
        while (true) {
            try {
                URL url = new URL(string2);
                String protocol = url.getProtocol();
                Intrinsics.checkNotNullExpressionValue(protocol, "getProtocol(...)");
                String lowerCase2 = protocol.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase2, str7);
                if (!Intrinsics.areEqual(lowerCase2, "https") && !Intrinsics.areEqual(lowerCase2, "http")) {
                    throw new IOException("Only http and https URLs are supported");
                }
                String host = url.getHost();
                if (host != null) {
                    lowerCase = host.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(lowerCase, str7);
                } else {
                    lowerCase = null;
                }
                if (lowerCase == null) {
                    lowerCase = str8;
                }
                if (lowerCase.length() == 0) {
                    throw new IOException("Invalid URL");
                }
                if (WebPermissions.INSTANCE.isRobloxHost(lowerCase)) {
                    throw new IOException("Http requests to Roblox domains are not allowed");
                }
                try {
                    if (!WebPermissions.INSTANCE.allow(luaRuntime.getStudio().getApp(), lowerCase, str4)) {
                        throw new IOException("Access to " + lowerCase + " was not allowed (Studio Settings → Privacy)");
                    }
                    URLConnection uRLConnectionOpenConnection = url.openConnection();
                    Intrinsics.checkNotNull(uRLConnectionOpenConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
                    httpURLConnection = (HttpURLConnection) uRLConnectionOpenConnection;
                    httpURLConnection.setInstanceFollowRedirects(false);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod(str9);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setReadTimeout(20000);
                    for (Object _eObj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) _eObj;
                        String str11 = (String) entry.getKey();
                        String str12 = (String) entry.getValue();
                        String str13 = str7;
                        String str14 = str8;
                        if (!StringsKt.equals(str11, "Cookie", true)) {
                            httpURLConnection.setRequestProperty(str11, str12);
                        }
                        str8 = str14;
                        str7 = str13;
                    }
                    String str15 = str7;
                    str6 = str8;
                    if (str10 != null) {
                        httpURLConnection.setDoOutput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        try {
                            OutputStream outputStream2 = outputStream;
                            byte[] bytes = str10.getBytes(Charsets.UTF_8);
                            try {
                                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                                outputStream2.write(bytes);
                                Unit unit = Unit.INSTANCE;
                                CloseableKt.closeFinally(outputStream, null);
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    throw th;
                                } catch (Throwable th3) {
                                    CloseableKt.closeFinally(outputStream, th);
                                    throw th3;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                    intRef.element = httpURLConnection.getResponseCode();
                    String headerField = httpURLConnection.getHeaderField("Location");
                    if (!(300 <= intRef.element && intRef.element < 400) || headerField == null) {
                        break;
                    }
                    int i2 = i + 1;
                    if (i >= 5) {
                        break;
                    }
                    httpURLConnection.disconnect();
                    string2 = new URL(url, headerField).toString();
                    Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                    if (intRef.element == 303 || ((intRef.element == 301 || intRef.element == 302) && Intrinsics.areEqual(str9, "POST"))) {
                        str10 = null;
                        str9 = "GET";
                    }
                    i = i2;
                    str8 = str6;
                    str7 = str15;
                } catch (Exception e) {
                    message = e.getMessage();
                    string = message;
                    if (message == null) {
                        string = e.toString();
                    }
                    objectRef2.element = string;
                }
            } catch (Exception e2) {
                message = e2.getMessage();
                string = message;
                if (message == null) {
                    string = e2.toString();
                }
                objectRef2.element = string;
            }
            luaRuntime.getMainQueue().add(new Function0() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return RobloxServices.http$lambda$58$lambda$57(objectRef2, str5, intRef, objectRef, luaRuntime, luaThread);
                }
            });
        }
        InputStream errorStream; try { errorStream = intRef.element >= 400 ? httpURLConnection.getErrorStream() : httpURLConnection.getInputStream(); } catch (java.io.IOException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
        if (errorStream != null) {
            Reader inputStreamReader = new InputStreamReader(errorStream, Charsets.UTF_8);
            String text = TextStreamsKt.readText(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192));
            if (text != null) {
                t = text;
            } else {
                t = str6;
            }
        } else {
            t = str6;
        }
        objectRef.element = t;
        httpURLConnection.disconnect();
        luaRuntime.getMainQueue().add(new Function0() { // from class: com.goydevv.studrblx.lua.RobloxServices$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return RobloxServices.http$lambda$58$lambda$57(objectRef2, str5, intRef, objectRef, luaRuntime, luaThread);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit http$lambda$58$lambda$57(Ref.ObjectRef $err, String $name, Ref.IntRef $code, Ref.ObjectRef $text, LuaRuntime $rt, ScriptThread $co) {
        Varargs result;
        if ($err.element != null) {
            result = LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf("HttpError: " + $err.element));
            Intrinsics.checkNotNullExpressionValue(result, "varargsOf(...)");
        } else {
            if (Intrinsics.areEqual($name, "RequestAsync")) {
                LuaTable r = new LuaTable();
                r.set("Success", LuaValue.valueOf(200 <= $code.element && $code.element < 300));
                r.set("StatusCode", $code.element);
                r.set("StatusMessage", "");
                r.set("Body", (String) $text.element);
                r.set("Headers", new LuaTable());
                result = LuaValue.varargsOf(LuaValue.TRUE, r);
                Intrinsics.checkNotNull(result);
            } else {
                if (200 <= $code.element && $code.element < 300) {
                    result = LuaValue.varargsOf(LuaValue.TRUE, LuaValue.valueOf((String) $text.element));
                    Intrinsics.checkNotNullExpressionValue(result, "varargsOf(...)");
                } else {
                    result = LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf("HTTP " + $code.element));
                    Intrinsics.checkNotNullExpressionValue(result, "varargsOf(...)");
                }
            }
        }
        $rt.resume($co, result);
        return Unit.INSTANCE;
    }

    private static final double easeIn(double t, int style) {
        switch (style) {
            case 0: return t; // Linear
            case 1: return 1.0d - Math.cos((t * Math.PI) * 0.5d); // Sine
            case 2: { double s = 1.70158d; return t * t * (((s + 1.0d) * t) - s); } // Back
            case 3: return t * t; // Quad
            case 4: return t * t * t * t; // Quart
            case 5: return t * t * t * t * t; // Quint
            case 6: return 1.0d - easeOutBounce(1.0d - t); // Bounce
            case 7: {
                if (t <= 0.0d || t >= 1.0d) return t;
                return -Math.pow(2.0d, (10.0d * t) - 10.0d) * Math.sin((((t * 10.0d) - 10.75d) * (2.0d * Math.PI)) / 3.0d);
            } // Elastic
            case 8: return t <= 0.0d ? 0.0d : Math.pow(2.0d, (10.0d * t) - 10.0d); // Exponential
            case 9: return 1.0d - Math.sqrt(Math.max(0.0d, 1.0d - (t * t))); // Circular
            case 10: return t * t * t; // Cubic
            default: return t;
        }
    }

    private static final double easeOutBounce(double t) {
        if (t < 1.0d / 2.75d) return 7.5625d * t * t;
        if (t < 2.0d / 2.75d) { t -= 1.5d / 2.75d; return (7.5625d * t * t) + 0.75d; }
        if (t < 2.5d / 2.75d) { t -= 2.25d / 2.75d; return (7.5625d * t * t) + 0.9375d; }
        t -= 2.625d / 2.75d;
        return (7.5625d * t * t) + 0.984375d;
    }

    private static final double evalEasing(double t, int style, int dir) {
        if (dir == 0) return easeIn(t, style); // In
        if (dir == 1) return 1.0d - easeIn(1.0d - t, style); // Out
        return t < 0.5d ? (0.5d * easeIn(t * 2.0d, style)) : (0.5d + (0.5d * (1.0d - easeIn(2.0d - (t * 2.0d), style)))); // InOut
    }

    private static final class CasEntry {
        final String name;
        final LuaValue fn;
        final int priority;
        final LuaTable info;
        Instance button;
        CasEntry(String name, LuaValue fn, int priority, LuaTable info, Instance button) {
            this.name = name; this.fn = fn; this.priority = priority; this.info = info; this.button = button;
        }
    }
    private static final java.util.LinkedHashMap<String, CasEntry> CAS_ACTIONS = new java.util.LinkedHashMap<>();
    private static int CAS_BTN_COUNT = 0;
    private static final java.util.HashMap<Integer, Boolean> CORE_GUI_ENABLED = new java.util.HashMap<>();
    private static final java.util.LinkedHashSet<String> COLLISION_GROUPS = new java.util.LinkedHashSet<>();
}
