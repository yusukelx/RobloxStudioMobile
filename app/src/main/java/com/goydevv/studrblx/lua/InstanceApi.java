package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.engine.Geometry;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.engine.RayHit;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.OptionalCFrame;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.PropNames;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

/* JADX INFO: compiled from: InstanceApi.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class InstanceApi {
    private final Set<String> BASE_PROPS;
    private final Set<String> CONSTRAINT_READOUTS;
    private final Map<String, List<String>> MEMBER_OWNERS;
    private final Set<String> PART_PROPS;
    private final List<Pair<String, Integer>> PRIORITIES;
    private final Map<String, Set<String>> SPECIAL_PROPS;
    private final Map<String, Set<String>> classMethods;
    private final Set<String> commonMethods;
    private final LuaTable connMeta;
    private final Set<String> events;
    private final HashMap<String, LuaValue> methodCache;
    private static final java.util.Set<String> DEFAULT_SERVICES = new java.util.HashSet<>(java.util.Arrays.asList("Workspace", "Players", "Lighting", "MaterialService", "ReplicatedFirst", "ReplicatedStorage", "ServerScriptService", "ServerStorage", "StarterGui", "StarterPack", "StarterPlayer", "Teams", "SoundService", "TextChatService", "Chat", "LocalizationService", "TestService"));
    private static final java.util.Set<String> STRICT_CONTAINERS = new java.util.HashSet<>(java.util.Arrays.asList("ReplicatedStorage", "ReplicatedFirst", "ServerStorage", "ServerScriptService", "StarterPack", "Folder", "Configuration"));
    private final LuaRuntime rt;
    private final Set<String> sequenceClasses;
    private final LuaTable signalMeta;
    private final Set<String> universalEvents;
    private final HashSet<String> warnedMissing;

    public InstanceApi(LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(rt, "rt");
        this.rt = rt;
        this.methodCache = new HashMap<>();
        this.warnedMissing = new HashSet<>();
        this.signalMeta = new LuaTable();
        this.connMeta = new LuaTable();
        char c = 6;
        char c2 = 21;
        this.commonMethods = SetsKt.setOf(new String[]{"FindFirstChild", "FindFirstChildOfClass", "FindFirstChildWhichIsA", "FindFirstAncestor", "FindFirstAncestorOfClass", "FindFirstAncestorWhichIsA", "FindFirstDescendant", "WaitForChild", "GetChildren", "GetDescendants", "IsA", "IsDescendantOf", "IsAncestorOf", "Destroy", "Remove", "Clone", "ClearAllChildren", "GetFullName", "GetAttribute", "SetAttribute", "GetAttributes", "GetAttributeChangedSignal", "GetPropertyChangedSignal", "GetService", "FindService", "GetTags", "HasTag", "AddTag", "RemoveTag", "GetActor", "GetDebugId", "IsPropertyModified", "ResetPropertyToDefault"});
        this.classMethods = MapsKt.mapOf(TuplesKt.to("BasePart", SetsKt.setOf(new String[]{"GetMass", "ApplyImpulse", "ApplyAngularImpulse", "GetConnectedParts", "GetTouchingParts", "GetRootPart", "SetNetworkOwner", "GetNetworkOwner", "CanSetNetworkOwnership", "BreakJoints", "MakeJoints", "Resize", "GetVelocityAtPosition", "GetPivot", "PivotTo", "IsGrounded", "GetJoints"})), TuplesKt.to("Model", SetsKt.setOf(new String[]{"GetPivot", "PivotTo", "MoveTo", "SetPrimaryPartCFrame", "GetPrimaryPartCFrame", "GetBoundingBox", "GetExtentsSize", "BreakJoints", "MakeJoints", "TranslateBy", "GetModelCFrame", "GetModelSize", "ScaleTo", "GetScale"})), TuplesKt.to("Humanoid", SetsKt.setOf(new String[]{"TakeDamage", "MoveTo", "ChangeState", "GetState", "LoadAnimation", "EquipTool", "UnequipTools", "GetPlayingAnimationTracks", "SetStateEnabled", "GetStateEnabled", "Move", "ApplyDescription", "GetAppliedDescription", "AddAccessory", "GetAccessories"})), TuplesKt.to("Animator", SetsKt.setOf(new String[]{"LoadAnimation", "GetPlayingAnimationTracks"})), TuplesKt.to("AnimationController", SetsKt.setOf(new String[]{"LoadAnimation", "GetPlayingAnimationTracks"})), TuplesKt.to("Workspace", SetsKt.setOf(new String[]{"Raycast", "FindPartOnRay", "FindPartOnRayWithIgnoreList", "FindPartOnRayWithWhitelist", "GetPartBoundsInBox", "GetPartBoundsInRadius", "GetPartsInPart", "GetServerTimeNow", "GetRealPhysicsFPS", "Spherecast", "Blockcast", "FindPartsInRegion3", "FindPartsInRegion3WithIgnoreList", "FindPartsInRegion3WithWhiteList", "IsRegion3Empty", "IsRegion3EmptyWithIgnoreList", "JoinToOutsiders", "UnjoinFromOutsiders", "BulkMoveTo", "ArePartsTouchingOthers", "GetNumAwakeParts", "PGSIsEnabled", "SetInsertPoint", "ZoomToExtents", "CalculateJumpPower", "CalculateJumpHeight", "GetPhysicsThrottling", "Shapecast"})), TuplesKt.to("Players", SetsKt.setOf(new String[]{"GetPlayers", "GetPlayerFromCharacter", "GetPlayerByUserId", "GetUserIdFromNameAsync", "GetNameFromUserIdAsync", "GetUserThumbnailAsync", "GetHumanoidDescriptionFromUserId"})), TuplesKt.to("Player", SetsKt.setOf(new String[]{"Kick", "LoadCharacter", "GetMouse", "IsInGroup", "GetRankInGroup", "GetRoleInGroup", "DistanceFromCharacter", "IsFriendsWith", "HasAppearanceLoaded", "GetJoinData", "ClearCharacterAppearance", "GetNetworkPing"})), TuplesKt.to("RunService", SetsKt.setOf(new String[]{"IsStudio", "IsRunning", "IsServer", "IsClient", "IsEdit", "IsRunMode", "BindToRenderStep", "UnbindFromRenderStep"})), TuplesKt.to("TweenService", SetsKt.setOf(new String[]{"Create", "GetValue", "SmoothDamp"})), TuplesKt.to("Debris", SetsKt.setOf(new String[]{"AddItem", "addItem"})), TuplesKt.to("HttpService", SetsKt.setOf(new String[]{"JSONEncode", "JSONDecode", "GenerateGUID", "UrlEncode", "GetAsync", "PostAsync", "RequestAsync"})), TuplesKt.to("CollectionService", SetsKt.setOf(new String[]{"GetTagged", "AddTag", "RemoveTag", "HasTag", "GetTags", "GetInstanceAddedSignal", "GetInstanceRemovedSignal", "GetAllTags"})), TuplesKt.to("Selection", SetsKt.setOf(new String[]{"Get", "Set", "Add", "Remove"})), TuplesKt.to("ChangeHistoryService", SetsKt.setOf(new String[]{"SetWaypoint", "TryBeginRecording", "FinishRecording", "Undo", "Redo", "ResetWaypoints", "SetEnabled", "GetCanUndo", "GetCanRedo"})), TuplesKt.to("DataStoreService", SetsKt.setOf(new String[]{"GetDataStore", "GetGlobalDataStore", "GetOrderedDataStore"})), TuplesKt.to("MarketplaceService", SetsKt.setOf(new String[]{"PromptPurchase", "PromptGamePassPurchase", "PromptProductPurchase", "UserOwnsGamePassAsync", "GetProductInfo", "PlayerOwnsAsset"})), TuplesKt.to("UserInputService", SetsKt.setOf(new String[]{"GetMouseLocation", "IsKeyDown", "GetFocusedTextBox", "IsMouseButtonPressed", "GetKeysPressed", "GetLastInputType", "GetConnectedGamepads"})), TuplesKt.to("ContextActionService", SetsKt.setOf(new String[]{"BindAction", "UnbindAction", "BindActionAtPriority", "SetTitle", "SetImage", "SetPosition", "SetDescription", "GetButton", "UnbindAllActions", "GetBoundActionInfo", "GetAllBoundActionInfo"})), TuplesKt.to("StarterGui", SetsKt.setOf(new String[]{"SetCore", "GetCore", "SetCoreGuiEnabled", "GetCoreGuiEnabled"})), TuplesKt.to("BindableEvent", SetsKt.setOf("Fire")), TuplesKt.to("BindableFunction", SetsKt.setOf("Invoke")), TuplesKt.to("RemoteEvent", SetsKt.setOf(new String[]{"FireServer", "FireClient", "FireAllClients"})), TuplesKt.to("UnreliableRemoteEvent", SetsKt.setOf(new String[]{"FireServer", "FireClient", "FireAllClients"})), TuplesKt.to("RemoteFunction", SetsKt.setOf(new String[]{"InvokeServer", "InvokeClient"})), TuplesKt.to("Sound", SetsKt.setOf(new String[]{"Play", "Stop", "Pause", "Resume"})), TuplesKt.to("GuiObject", SetsKt.setOf(new String[]{"TweenPosition", "TweenSize", "TweenSizeAndPosition"})), TuplesKt.to("TextBox", SetsKt.setOf(new String[]{"CaptureFocus", "ReleaseFocus", "IsFocused"})), TuplesKt.to("TextService", SetsKt.setOf(new String[]{"GetTextSize", "FilterStringAsync"})), TuplesKt.to("Plugin", SetsKt.setOf(new String[]{"CreateToolbar", "GetSetting", "SetSetting", "CreateDockWidgetPluginGui", "CreateDockWidgetPluginGuiAsync", "CreateQWidgetPluginGui", "OpenScript", "GetMouse", "Activate", "Deactivate", "IsActivated", "IsActivatedWithExclusiveMouse", "GetStudioUserId", "CreatePluginAction", "CreatePluginMenu", "SelectRibbonTool", "GetSelectedRibbonTool", "Union", "Negate", "Separate", "Intersect", "StartDrag", "OpenWikiPage", "SaveSelectedToRoblox", "GetJoinMode", "PromptForExistingAssetId", "ImportFbxRig", "GetItem", "SetItem", "GetGridSize", "IsLoaded", "PromptSaveSelection"})), TuplesKt.to("PluginGui", SetsKt.setOf(new String[]{"BindToClose", "GetRelativeMousePosition"})), TuplesKt.to("PluginToolbar", SetsKt.setOf("CreateButton")), TuplesKt.to("PluginToolbarButton", SetsKt.setOf("SetActive")), TuplesKt.to("ParticleEmitter", SetsKt.setOf(new String[]{"Emit", "Clear"})), TuplesKt.to("Camera", SetsKt.setOf(new String[]{"WorldToScreenPoint", "WorldToViewportPoint", "ScreenPointToRay", "ViewportPointToRay", "GetPartsObscuringTarget"})), TuplesKt.to("Lighting", SetsKt.setOf(new String[]{"GetMinutesAfterMidnight", "SetMinutesAfterMidnight", "GetSunDirection", "GetMoonDirection"})), TuplesKt.to("SoundService", SetsKt.setOf("PlayLocalSound")), TuplesKt.to("PathfindingService", SetsKt.setOf(new String[]{"CreatePath", "FindPathAsync"})), TuplesKt.to("BadgeService", SetsKt.setOf(new String[]{"AwardBadge", "UserHasBadgeAsync", "GetBadgeInfoAsync"})), TuplesKt.to("TeleportService", SetsKt.setOf(new String[]{"Teleport", "TeleportAsync", "TeleportToPlaceInstance"})), TuplesKt.to("InsertService", SetsKt.setOf("LoadAsset")), TuplesKt.to("Teams", SetsKt.setOf("GetTeams")), TuplesKt.to("Team", SetsKt.setOf("GetPlayers")), TuplesKt.to("PhysicsService", SetsKt.setOf(new String[]{"RegisterCollisionGroup", "CollisionGroupSetCollidable", "SetPartCollisionGroup", "CreateCollisionGroup", "CollisionGroupsAreCollidable", "GetRegisteredCollisionGroups"})), TuplesKt.to("Tool", SetsKt.setOf(new String[]{"Activate", "Deactivate"})), TuplesKt.to("ProximityPrompt", SetsKt.setOf(new String[]{"InputHoldBegin", "InputHoldEnd"})), TuplesKt.to("PlayerGui", SetsKt.setOf(new String[]{"SetTopbarTransparency", "GetGuiObjectsAtPosition"})), TuplesKt.to("MessagingService", SetsKt.setOf(new String[]{"PublishAsync", "SubscribeAsync"})), TuplesKt.to("Chat", SetsKt.setOf(new String[]{"Chat", "FilterStringForBroadcast"})), TuplesKt.to("TextChannel", SetsKt.setOf(new String[]{"SendAsync", "DisplaySystemMessage"})), TuplesKt.to("GuiService", SetsKt.setOf(new String[]{"GetGuiInset", "IsTenFootInterface"})), TuplesKt.to("LocalizationService", SetsKt.setOf(new String[]{"GetTranslatorForPlayerAsync", "GetCountryRegionForPlayerAsync"})), TuplesKt.to("PolicyService", SetsKt.setOf("GetPolicyInfoForPlayerAsync")), TuplesKt.to("StudioService", SetsKt.setOf(new String[]{"GetUserId", "PromptImportFile"})), TuplesKt.to("RbxAnalyticsService", SetsKt.setOf(new String[]{"GetSessionId", "GetClientId", "GetPlaySessionId", "ReportStats", "SendEventDeferred", "SendEventImmediately", "ReportCounter", "ReportInfluxSeries", "TrackEvent", "FireLogEvent", "SetRBXEvent"})), TuplesKt.to("DataModel", SetsKt.setOf(new String[]{"BindToClose", "IsLoaded", "GetJobsInfo", "SetPlaceId", "GetObjects"})));
        this.events = SetsKt.setOf(new String[]{"Touched", "TouchEnded", "ChildAdded", "ChildRemoved", "DescendantAdded", "DescendantRemoving", "Changed", "AncestryChanged", "Destroying", "AttributeChanged", "Heartbeat", "RenderStepped", "Stepped", "PreSimulation", "PostSimulation", "PreRender", "PlayerAdded", "PlayerRemoving", "CharacterAdded", "CharacterRemoving", "CharacterAppearanceLoaded", "Died", "HealthChanged", "Running", "Jumping", "StateChanged", "MoveToFinished", "FreeFalling", "Seated", "MouseButton1Click", "MouseButton1Down", "MouseButton1Up", "MouseButton2Click", "Activated", "MouseEnter", "MouseLeave", "FocusLost", "Focused", "Event", "OnServerEvent", "OnClientEvent", "MouseClick", "RightMouseClick", "MouseHoverEnter", "MouseHoverLeave", "Triggered", "TriggerEnded", "PromptShown", "PromptHidden", "PromptTriggered", "Completed", "Click", "SelectionChanged", "InputBegan", "InputEnded", "InputChanged", "TouchTapInWorld", "TouchTap", "TouchStarted", "TouchEnded", "TouchMoved", "Equipped", "Unequipped", "Deactivated", "Played", "Ended", "Paused", "Resumed", "Loaded", "Chatted", "Idled", "Button1Down", "Button1Up", "Move", "Blocked", "PromptPurchaseFinished", "PromptGamePassPurchaseFinished", "ServerScriptServiceChanged", "WindowFocused", "WindowFocusReleased", "Unloading", "Close", "BindToClose", "Deactivation", "ChildAddedToCharacter", "ReturnPressedFromOnScreenKeyboard", "TextChanged", "MessageReceived", "OnIncomingMessage", "LastInputTypeChanged", "JumpRequest", "MouseDrag", "Unloading", "Deactivation", "Loaded", "PromptProductPurchaseFinished", "PromptPurchaseFinished", "PromptGamePassPurchaseFinished", "PromptBundlePurchaseFinished", "Loaded", "GraphicsQualityChangeRequest", "OnTeleport", "ChildRemoving", "ClockTimeChanged", "PlaybackStateChanged", "DidLoop", "Stopped", "KeyframeReached", "OnUndo", "OnRedo", "OnRecordingStarted", "OnRecordingFinished", "WaypointSet", "Activated", "PromptDismissed", "DragBegin", "DragContinue", "DragEnd", "MouseButton2Down", "MouseButton2Up", "MouseWheelForward", "MouseWheelBackward", "WheelBackward", "WheelForward", "Button2Down", "Button2Up", "KeyDown", "KeyUp", "SelectionGained", "SelectionLost"});
        this.sequenceClasses = SetsKt.setOf(new String[]{"UIGradient", "ParticleEmitter", "Beam", "Trail"});
        this.universalEvents = SetsKt.setOf(new String[]{"ChildAdded", "ChildRemoved", "DescendantAdded", "DescendantRemoving", "Changed", "AncestryChanged", "Destroying", "AttributeChanged"});
        LuaTable m = this.rt.getInstanceMeta();
        m.set("__type", "Instance");
        m.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return InstanceApi._init_$lambda$0(InstanceApi.this, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        m.set(LuaValue.NEWINDEX, new ThreeArgFunction() { // from class: com.goydevv.studrblx.lua.InstanceApi.2
            @Override // org.luaj.vm2.lib.ThreeArgFunction, org.luaj.vm2.lib.LibFunction, org.luaj.vm2.LuaValue
            public LuaValue call(LuaValue self, LuaValue key, LuaValue v) {
                Intrinsics.checkNotNullParameter(self, "self");
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(v, "v");
                InstanceApi instanceApi = InstanceApi.this;
                Object obj = self.touserdata();
                Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.model.Instance");
                String str = key.tojstring();
                Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
                instanceApi.newIndex((Instance) obj, str, v);
                LuaValue NONE = LuaValue.NONE;
                Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
                return NONE;
            }
        });
        m.set(LuaValue.TOSTRING, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi._init_$lambda$1((LuaValue) obj);
            }
        }));
        this.signalMeta.set("__type", "RBXScriptSignal");
        this.signalMeta.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return InstanceApi._init_$lambda$5(InstanceApi.this, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        this.signalMeta.set(LuaValue.TOSTRING, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi._init_$lambda$6((LuaValue) obj);
            }
        }));
        this.connMeta.set("__type", "RBXScriptConnection");
        this.connMeta.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return InstanceApi._init_$lambda$8(InstanceApi.this, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        this.CONSTRAINT_READOUTS = SetsKt.setOf(new String[]{"CurrentPosition", "CurrentAngle", "CurrentDistance", "CurrentLength"});
        this.BASE_PROPS = SetsKt.setOf(new String[]{"Name", "ClassName", "Parent", "Archivable"});
        this.PART_PROPS = SetsKt.setOf(new String[]{"Position", "Orientation", "Rotation", "Size", "Color", "BrickColor", "CFrame", "AssemblyLinearVelocity", "Velocity", "AssemblyAngularVelocity", "RotVelocity", "Mass", "AssemblyMass", "PivotOffset", "AssemblyRootPart", "Anchored", "CanCollide", "Transparency", "Material", "Shape", "CanTouch", "CanQuery", "Locked", "Massless", "Reflectance", "CastShadow"});
        this.SPECIAL_PROPS = MapsKt.mapOf(TuplesKt.to("DataModel", SetsKt.setOf(new String[]{"PlaceId", "GameId", "CreatorId", "CreatorType", "PlaceVersion", "JobId", "PrivateServerId", "PrivateServerOwnerId"})), TuplesKt.to("ImageLabel", SetsKt.setOf("IsLoaded")), TuplesKt.to("ImageButton", SetsKt.setOf("IsLoaded")), TuplesKt.to("StudioService", SetsKt.setOf(new String[]{"GridSize", "RotateIncrement", "UseLocalSpace", "DraggerSolveConstraints", "ShowConstraintDetails", "ActiveScript", "StudioLocaleId", "Theme"})), TuplesKt.to("DraggerService", SetsKt.setOf(new String[]{"HoverThickness", "AlignDraggedObjects", "ShowHover", "AnimateHover", "PivotSnapToGeometry", "ShowPivotIndicator", "JointsEnabled", "CollisionsEnabled", "AngleSnapEnabled", "AngleSnapIncrement", "LinearSnapEnabled", "LinearSnapIncrement", "HoverAnimateFrequency", "GeometrySnapColor"})), TuplesKt.to("Players", SetsKt.setOf(new String[]{"LocalPlayer", "MaxPlayers"})), TuplesKt.to("Humanoid", SetsKt.setOf(new String[]{"Health", "RootPart", "MoveDirection", "FloorMaterial", "Jump", "SeatPart", "Sit", "PlatformStand", "AutoRotate", "UseJumpPower", "JumpHeight", "RigType"})), TuplesKt.to("Workspace", SetsKt.setOf(new String[]{"DistributedGameTime", "CurrentCamera", "Gravity", "StreamingEnabled"})), TuplesKt.to("Camera", SetsKt.setOf(new String[]{"ViewportSize", "CFrame", "Focus", "CameraSubject", "CameraType", "FieldOfView"})), TuplesKt.to("Lighting", SetsKt.setOf(new String[]{"TimeOfDay", "ClockTime", "Brightness", "Ambient", "OutdoorAmbient", "FogEnd", "FogStart", "FogColor", "GlobalShadows"})), TuplesKt.to("UserInputService", SetsKt.setOf(new String[]{"TouchEnabled", "KeyboardEnabled", "MouseEnabled", "GamepadEnabled", "VREnabled", "MouseBehavior", "MouseIconEnabled", "AccelerometerEnabled", "GyroscopeEnabled", "ModalEnabled"})), TuplesKt.to("HttpService", SetsKt.setOf("HttpEnabled")), TuplesKt.to("RunService", SetsKt.setOf("ClientGitHash")), TuplesKt.to("Player", SetsKt.setOf(new String[]{"PlayerGui", "Backpack", "Character", "UserId", "DisplayName", "Team", "TeamColor", "Neutral", "AccountAge", "MembershipType", "RespawnLocation", "CameraMode", "CameraMaxZoomDistance", "CameraMinZoomDistance", "DevTouchMovementMode"})), TuplesKt.to("TextBox", SetsKt.setOf(new String[]{"TextBounds", "ContentText", "TextFits"})), TuplesKt.to("TextLabel", SetsKt.setOf(new String[]{"TextBounds", "ContentText", "TextFits"})), TuplesKt.to("TextButton", SetsKt.setOf(new String[]{"TextBounds", "ContentText", "TextFits"})), TuplesKt.to("StarterGui", SetsKt.setOf(new String[]{"ShowDevelopmentGui", "ResetPlayerGuiOnSpawn"})), TuplesKt.to("Sound", SetsKt.setOf(new String[]{"TimePosition", "TimeLength", "IsPlaying", "IsLoaded", "PlaybackSpeed", "PlaybackLoudness"})));
        HashMap map = new HashMap();
        List listListOf = CollectionsKt.listOf(new String[]{"TextLabel", "TextButton", "TextBox", "Message", "Hint"});
        Iterator it = CollectionsKt.listOf(new String[]{"Text", "TextScaled", "TextSize", "TextBounds", "TextColor3", "TextWrapped", "TextXAlignment", "TextYAlignment", "Font", "FontFace", "TextTransparency", "TextStrokeTransparency", "TextStrokeColor3", "RichText", "LineHeight", "MaxVisibleGraphemes", "TextFits", "ContentText", "TextTruncate", "TextDirection", "TextWrap"}).iterator();
        while (it.hasNext()) {
            map.put((String) it.next(), listListOf);
        }
        char c3 = c2;
        Iterator it2 = CollectionsKt.listOf(new String[]{"PlaceholderText", "PlaceholderColor3", "ClearTextOnFocus", "MultiLine", "CursorPosition", "SelectionStart", "TextEditable", "ShowNativeInput"}).iterator();
        while (it2.hasNext()) {
            map.put((String) it2.next(), CollectionsKt.listOf("TextBox"));
        }
        char c4 = c;
        String[] strArr = new String[7];
        strArr[0] = "ScaleType";
        strArr[1] = "SliceCenter";
        strArr[2] = "SliceScale";
        strArr[3] = "ImageRectOffset";
        strArr[4] = "ImageRectSize";
        strArr[5] = "TileSize";
        strArr[c4] = "ResampleMode";
        Iterator it3 = CollectionsKt.listOf(strArr).iterator();
        while (it3.hasNext()) {
            map.put((String) it3.next(), CollectionsKt.listOf(new String[]{"ImageLabel", "ImageButton"}));
        }
        map.put("Image", CollectionsKt.listOf(new String[]{"ImageLabel", "ImageButton", "ImageHandleAdornment"}));
        Iterator it4 = CollectionsKt.listOf(new String[]{"ImageColor3", "ImageTransparency"}).iterator();
        while (it4.hasNext()) {
            map.put((String) it4.next(), CollectionsKt.listOf(new String[]{"ImageLabel", "ImageButton", "ViewportFrame"}));
        }
        String[] strArr2 = new String[12];
        strArr2[0] = "BackgroundColor3";
        strArr2[1] = "BackgroundTransparency";
        strArr2[2] = "BorderColor3";
        strArr2[3] = "BorderSizePixel";
        strArr2[4] = "BorderMode";
        strArr2[5] = "AnchorPoint";
        strArr2[c4] = "AutomaticSize";
        strArr2[7] = "ClipsDescendants";
        strArr2[8] = "LayoutOrder";
        strArr2[9] = "SizeConstraint";
        strArr2[10] = "Selectable";
        strArr2[11] = "Interactable";
        Iterator it5 = CollectionsKt.listOf(strArr2).iterator();
        while (it5.hasNext()) {
            map.put((String) it5.next(), CollectionsKt.listOf("GuiObject"));
        }
        Iterator it6 = CollectionsKt.listOf(new String[]{"AutoButtonColor", "Modal", "Selected"}).iterator();
        while (it6.hasNext()) {
            map.put((String) it6.next(), CollectionsKt.listOf("GuiButton"));
        }
        String[] strArr3 = new String[24];
        strArr3[0] = "Anchored";
        strArr3[1] = "CanCollide";
        strArr3[2] = "CanTouch";
        strArr3[3] = "CanQuery";
        strArr3[4] = "CastShadow";
        strArr3[5] = "Massless";
        strArr3[c4] = "Reflectance";
        strArr3[7] = "Material";
        strArr3[8] = "MaterialVariant";
        strArr3[9] = "AssemblyLinearVelocity";
        strArr3[10] = "AssemblyAngularVelocity";
        strArr3[11] = "AssemblyMass";
        strArr3[12] = "AssemblyRootPart";
        strArr3[13] = "CollisionGroup";
        strArr3[14] = "CustomPhysicalProperties";
        strArr3[15] = "TopSurface";
        strArr3[16] = "BottomSurface";
        strArr3[17] = "FrontSurface";
        strArr3[18] = "BackSurface";
        strArr3[19] = "LeftSurface";
        strArr3[20] = "RightSurface";
        strArr3[c3] = "BrickColor";
        strArr3[22] = "Locked";
        strArr3[23] = "RootPriority";
        Iterator it7 = CollectionsKt.listOf(strArr3).iterator();
        while (it7.hasNext()) {
            map.put((String) it7.next(), CollectionsKt.listOf("BasePart"));
        }
        map.put("Value", CollectionsKt.listOf(new String[]{"ValueBase", "IntConstrainedValue", "DoubleConstrainedValue"}));
        map.put("PrimaryPart", CollectionsKt.listOf("Model"));
        map.put("WorldPivot", CollectionsKt.listOf("PVInstance"));
        map.put("Source", CollectionsKt.listOf("LuaSourceContainer"));
        map.put("Disabled", CollectionsKt.listOf("BaseScript"));
        Iterator it8 = CollectionsKt.listOf(new String[]{"SoundId", "IsPaused", "RollOffMode", "SoundGroup"}).iterator();
        while (it8.hasNext()) {
            map.put((String) it8.next(), CollectionsKt.listOf("Sound"));
        }
        map.put("Volume", CollectionsKt.listOf(new String[]{"Sound", "SoundGroup", "AudioPlayer", "AudioFader", "VideoFrame"}));
        Iterator it9 = CollectionsKt.listOf(new String[]{"TimePosition", "TimeLength", "IsPlaying"}).iterator();
        while (it9.hasNext()) {
            map.put((String) it9.next(), CollectionsKt.listOf(new String[]{"Sound", "AudioPlayer", "VideoFrame"}));
        }
        map.put("Looped", CollectionsKt.listOf(new String[]{"Sound", "VideoFrame"}));
        map.put("PlaybackSpeed", CollectionsKt.listOf(new String[]{"Sound", "AudioPlayer"}));
        String[] strArr4 = new String[8];
        strArr4[0] = "WalkSpeed";
        strArr4[1] = "JumpPower";
        strArr4[2] = "JumpHeight";
        strArr4[3] = "Health";
        strArr4[4] = "MaxHealth";
        strArr4[5] = "HipHeight";
        strArr4[c4] = "RigType";
        strArr4[7] = "UseJumpPower";
        Iterator it10 = CollectionsKt.listOf(strArr4).iterator();
        while (it10.hasNext()) {
            map.put((String) it10.next(), CollectionsKt.listOf("Humanoid"));
        }
        map.put("DisplayName", CollectionsKt.listOf(new String[]{"Humanoid", "Player"}));
        this.MEMBER_OWNERS = map;
        Pair[] pairArr = new Pair[7];
        pairArr[0] = TuplesKt.to("Idle", 0);
        pairArr[1] = TuplesKt.to("Movement", 1);
        pairArr[2] = TuplesKt.to("Action", 2);
        pairArr[3] = TuplesKt.to("Action2", 3);
        pairArr[4] = TuplesKt.to("Action3", 4);
        pairArr[5] = TuplesKt.to("Action4", 5);
        pairArr[c4] = TuplesKt.to("Core", 1000);
        this.PRIORITIES = CollectionsKt.listOf(pairArr);
    }

    public final LuaRuntime getRt() {
        return this.rt;
    }

    public final LuaTable getSignalMeta() {
        return this.signalMeta;
    }

    public final Set<String> getEvents() {
        return this.events;
    }

    static final LuaValue _init_$lambda$0(InstanceApi this$0, LuaValue self, LuaValue key) {
        Intrinsics.checkNotNullParameter(self, "self");
        Intrinsics.checkNotNullParameter(key, "key");
        Object obj = self.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.model.Instance");
        String str = key.tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        return this$0.index((Instance) obj, str);
    }

    static final LuaValue _init_$lambda$1(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Object obj = it.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.model.Instance");
        LuaString luaStringValueOf = LuaValue.valueOf(((Instance) obj).getName());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0032, code lost:
    
        if (r1.equals("wait") != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003b, code lost:
    
        if (r1.equals("Wait") != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0063, code lost:
    
        if (r1.equals("ConnectParallel") != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x006c, code lost:
    
        if (r1.equals("Connect") != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0079, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypesKt.fnv(new com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda11(r4, r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypesKt.fnv(new com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda13(r4, r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0029, code lost:
    
        if (r1.equals("connect") != false) goto L24;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$5(final com.goydevv.studrblx.lua.InstanceApi r4, org.luaj.vm2.LuaValue r5, org.luaj.vm2.LuaValue r6) {
        Intrinsics.checkNotNullParameter(r5, "self");
        Intrinsics.checkNotNullParameter(r6, "key");
        Object obj = r5.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.lua.SignalRef");
        final SignalRef s = (SignalRef) obj;
        String k = r6.tojstring();
        if (k != null) {
            switch (k) {
                case "Connect":
                case "connect":
                case "ConnectParallel":
                    return LuaTypesKt.fnv(new Function1() {
                        @Override
                        public final Object invoke(Object a) {
                            return InstanceApi.lambda$5$lambda$2(r4, s, (Varargs) a);
                        }
                    });
                case "Once":
                case "once":
                    return LuaTypesKt.fnv(new Function1() {
                        @Override
                        public final Object invoke(Object a) {
                            return InstanceApi.lambda$5$lambda$3(r4, s, (Varargs) a);
                        }
                    });
                case "Wait":
                case "wait":
                    return LuaTypesKt.fnv(new Function1() {
                        @Override
                        public final Object invoke(Object a) {
                            return InstanceApi.lambda$5$lambda$4(r4, s, (Varargs) a);
                        }
                    });
            }
        }
        throw new LuaError(r6.tojstring() + " is not a valid member of RBXScriptSignal");
    }

    static final Varargs lambda$5$lambda$2(InstanceApi this$0, SignalRef $s, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaRuntime luaRuntime = this$0.rt;
        Object owner = $s.getOwner();
        String name = $s.getName();
        LuaFunction luaFunctionCheckfunction = a.arg(2).checkfunction();
        Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
        return this$0.connectionValue(luaRuntime.connect(owner, name, luaFunctionCheckfunction, false));
    }

    static final Varargs lambda$5$lambda$3(InstanceApi this$0, SignalRef $s, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaRuntime luaRuntime = this$0.rt;
        Object owner = $s.getOwner();
        String name = $s.getName();
        LuaFunction luaFunctionCheckfunction = a.arg(2).checkfunction();
        Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
        return this$0.connectionValue(luaRuntime.connect(owner, name, luaFunctionCheckfunction, true));
    }

    static final Varargs lambda$5$lambda$4(InstanceApi this$0, SignalRef $s, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        return this$0.rt.waitSignal($s.getOwner(), $s.getName());
    }

    static final LuaValue _init_$lambda$6(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Object obj = it.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.lua.SignalRef");
        LuaString luaStringValueOf = LuaValue.valueOf("Signal " + ((SignalRef) obj).getName());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0041, code lost:
    
        if (r1.equals("disconnect") == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004a, code lost:
    
        if (r1.equals("Disconnect") != false) goto L15;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$8(final com.goydevv.studrblx.lua.InstanceApi r3, org.luaj.vm2.LuaValue r4, org.luaj.vm2.LuaValue r5) {
        Intrinsics.checkNotNullParameter(r4, "self");
        Intrinsics.checkNotNullParameter(r5, "key");
        Object obj = r4.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.lua.Connection");
        final Connection c = (Connection) obj;
        String k = r5.tojstring();
        if ("Connected".equals(k) || "connected".equals(k)) {
            LuaValue val = LuaValue.valueOf(c.getConnected());
            Intrinsics.checkNotNullExpressionValue(val, "valueOf(...)");
            return val;
        }
        if ("Disconnect".equals(k) || "disconnect".equals(k)) {
            return LuaTypesKt.fnv(new Function1() {
                @Override
                public final Object invoke(Object a) {
                    return InstanceApi.lambda$8$lambda$7(r3, c, (Varargs) a);
                }
            });
        }
        return LuaValue.NIL;
    }

    static final Varargs lambda$8$lambda$7(InstanceApi this$0, Connection $c, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        this$0.rt.disconnect($c);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    public final LuaValue signal(Object owner, String name) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        return new LuaUserdata(new SignalRef(owner, name), this.signalMeta);
    }

    private final LuaValue connectionValue(Connection c) {
        return new LuaUserdata(c, this.connMeta);
    }

    private final boolean methodAllowed(Instance inst, String name) {
        if (this.commonMethods.contains(name) || ApiExtras.allows(inst, name)) {
            return true;
        }
        for (Map.Entry<String, Set<String>> entry : this.classMethods.entrySet()) {
            String cls = entry.getKey();
            if (entry.getValue().contains(name) && inst.isA(cls)) {
                return true;
            }
        }
        return false;
    }

    private final boolean isPart(Instance i) {
        return i.isA("BasePart");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:502:0x07aa  */
    /* JADX WARN: Code duplicated, block: B:505:0x07b2  */
    /* JADX WARN: Code duplicated, block: B:507:0x07ba  */
    /* JADX WARN: Code duplicated, block: B:509:0x07c1  */
    /* JADX WARN: Code duplicated, block: B:511:0x07c9  */
    /* JADX WARN: Code duplicated, block: B:513:0x07ce  */
    /* JADX WARN: Code duplicated, block: B:516:0x07d8  */
    /* JADX WARN: Code duplicated, block: B:518:0x07e2  */
    /* JADX WARN: Code duplicated, block: B:519:0x07e5  */
    /* JADX WARN: Code duplicated, block: B:521:0x07e8  */
    /* JADX WARN: Code duplicated, block: B:522:0x07ed  */
    /* JADX WARN: Code duplicated, block: B:525:0x07fb  */
    /* JADX WARN: Code duplicated, block: B:527:0x0800  */
    /* JADX WARN: Code duplicated, block: B:532:0x0815  */
    /* JADX WARN: Code duplicated, block: B:534:0x081b  */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x026e, code lost:
    
        if (r12.equals("CurrentPosition") == false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0277, code lost:
    
        if (r12.equals("CurrentAngle") == false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0280, code lost:
    
        if (r12.equals("CurrentDistance") != false) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0289, code lost:
    
        if (r12.equals("CurrentLength") != false) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x028b, code lost:
    
        r0 = r11.get(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x028f, code lost:
    
        if (r0 != null) goto L575;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0295, code lost:
    
        return java.lang.Float.valueOf(0.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x03ec, code lost:
    
        if (r12.equals("AccelerometerEnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x03f5, code lost:
    
        if (r12.equals("MouseEnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x03fe, code lost:
    
        if (r12.equals("VREnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0407, code lost:
    
        if (r12.equals("MouseIconEnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x0410, code lost:
    
        if (r12.equals("KeyboardEnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0419, code lost:
    
        if (r12.equals("GyroscopeEnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0422, code lost:
    
        if (r12.equals("MouseBehavior") != false) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0424, code lost:
    
        r0 = r11.get(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x0428, code lost:
    
        if (r0 != null) goto L581;
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x042f, code lost:
    
        return new com.goydevv.studrblx.model.EnumValue(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x0436, code lost:
    
        if (r12.equals("GamepadEnabled") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x043e, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0072, code lost:
    
        if (r12.equals("RotVelocity") == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007c, code lost:
    
        if (r12.equals("AssemblyMass") == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0086, code lost:
    
        if (r12.equals("AssemblyLinearVelocity") == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0628, code lost:
    
        if (r12.equals("ShowConstraintDetails") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x063a, code lost:
    
        if (r12.equals("DraggerSolveConstraints") != false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x0640, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0702, code lost:
    
        if (r12.equals("AnimateHover") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x070c, code lost:
    
        if (r12.equals("PivotSnapToGeometry") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x074a, code lost:
    
        if (r12.equals("ShowHover") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0753, code lost:
    
        if (r12.equals("AlignDraggedObjects") == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x078a, code lost:
    
        if (r12.equals("ShowPivotIndicator") != false) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0790, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ba, code lost:
    
        if (r12.equals("AssemblyAngularVelocity") != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00c2, code lost:
    
        return com.goydevv.studrblx.model.Vector3.INSTANCE.getZERO();
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00fe, code lost:
    
        if (r12.equals("Mass") != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0100, code lost:
    
        r0 = com.goydevv.studrblx.model.PartData.INSTANCE.size(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x011c, code lost:
    
        return java.lang.Float.valueOf(((r0.getX() * r0.getY()) * r0.getZ()) * 0.7f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0161, code lost:
    
        if (r12.equals("Velocity") != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0163, code lost:
    
        r0 = r10.rt.getPlay();
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0169, code lost:
    
        if (r0 == null) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x016b, code lost:
    
        r0 = r0.velocityOf(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x016f, code lost:
    
        if (r0 == null) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0178, code lost:
    
        return com.goydevv.studrblx.model.Vector3.INSTANCE.getZERO();
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object getK(com.goydevv.studrblx.model.Instance inst, java.lang.String k) {
        if (inst == null || k == null) return null;
        Object special = SpecialProps.get(this.rt, inst, k);
        if (special != SpecialProps.NONE) return special;
        Object got = getStored(inst, k);
        if (got != null) return got;
        String rk = PropNames.INSTANCE.resolve(inst, k);
        if (rk != null && !rk.equals(k)) {
            got = inst.get(rk);
            if (got != null) return got;
        }
        return RobloxDefaults.get(inst.getClassName(), k);
    }

    private Object getStored(Instance inst, String k) {
        switch (k) {
            case "Name": return inst.getName();
            case "ClassName": return inst.getClassName();
            case "Parent": return inst.getParent();
            case "CFrame":
                if (inst.isA("BasePart")) return PartData.INSTANCE.cframe(inst);
                return inst.get("CFrame");
            case "Position":
                if (inst.isA("BasePart")) return PartData.INSTANCE.cframe(inst).getPosition();
                return inst.get("Position");
            case "Size":
                if (inst.isA("BasePart")) return PartData.INSTANCE.size(inst);
                return inst.get("Size");
            case "Color": case "Color3":
                if (inst.isA("BasePart")) return PartData.INSTANCE.color(inst);
                return inst.get(k);
            case "BrickColor":
                if (inst.isA("BasePart")) return new com.goydevv.studrblx.model.BrickColorV(com.goydevv.studrblx.model.BrickColors.INSTANCE.closest(PartData.INSTANCE.color(inst)));
                return inst.get("BrickColor");
            case "Transparency":
                if (inst.isA("BasePart")) return Float.valueOf(PartData.INSTANCE.transparency(inst));
                return inst.get("Transparency");
            case "Material":
                if (inst.isA("BasePart")) return new EnumValue(PartData.INSTANCE.material(inst));
                return inst.get("Material");
            case "Anchored":
                Object anc = inst.get("Anchored");
                return anc instanceof Boolean ? anc : Boolean.FALSE;
            case "CanCollide":
                Object cc = inst.get("CanCollide");
                return cc instanceof Boolean ? cc : Boolean.TRUE;
            case "Visible":
                Object vis = inst.get("Visible");
                return vis instanceof Boolean ? vis : Boolean.TRUE;
            case "Enabled":
                Object en = inst.get("Enabled");
                return en instanceof Boolean ? en : Boolean.TRUE;
            case "Health":
                Object hp = inst.get("Health");
                if (hp == null) hp = inst.get("Health_XML");
                return hp instanceof Number ? hp : Float.valueOf(100.0f);
            case "MaxHealth":
                Object mhp = inst.get("MaxHealth");
                return mhp instanceof Number ? mhp : Float.valueOf(100.0f);
            case "WalkSpeed":
                Object ws = inst.get("WalkSpeed");
                return ws instanceof Number ? ws : Float.valueOf(16.0f);
            case "JumpPower":
                Object jp = inst.get("JumpPower");
                return jp instanceof Number ? jp : Float.valueOf(50.0f);
            case "JumpHeight":
                Object jh = inst.get("JumpHeight");
                return jh instanceof Number ? jh : Float.valueOf(7.2f);
            case "LocalPlayer":
                if ("Players".equals(inst.getClassName())) {
                    PlaySession pl = this.rt.getPlay();
                    return pl != null ? pl.getPlayer() : null;
                }
                return inst.get(k);
            case "Character":
                if ("Player".equals(inst.getClassName())) {
                    PlaySession pl = this.rt.getPlay();
                    if (pl != null && pl.getPlayer() == inst) return pl.getCharacter();
                }
                return inst.get(k);
            case "CurrentCamera":
                if ("Workspace".equals(inst.getClassName())) {
                    Object cam = inst.get("CurrentCamera");
                    if (cam != null) return cam;
                    return Instance.findFirstChild$default(inst, "Camera", false, 2, null);
                }
                return inst.get(k);
            default:
                Object v = inst.get(k);
                if (v == null && "Size".equals(k)) v = inst.get("size");
                return v;
        }
    }

    public final boolean hasK(Instance inst, String name) {
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(name, "name");
        if (this.BASE_PROPS.contains(name)) {
            return true;
        }
        if (isPart(inst) && this.PART_PROPS.contains(name)) {
            return true;
        }
        if (inst.isA("Model") && Intrinsics.areEqual(name, "WorldPivot")) {
            return true;
        }
        boolean z = false;
        if (inst.isA("GuiBase2d") && StringsKt.startsWith(name, "Absolute", false)) {
            return true;
        }
        if (inst.isA("UIGridStyleLayout") && (Intrinsics.areEqual(name, "AbsoluteContentSize") || (Intrinsics.areEqual(name, "AbsoluteCellSize") && Intrinsics.areEqual(inst.getClassName(), "UIGridLayout")))) {
            return true;
        }
        if (inst.isA("BaseScript") && Intrinsics.areEqual(name, "Enabled")) {
            return true;
        }
        if (inst.isA("Constraint") && this.CONSTRAINT_READOUTS.contains(name)) {
            return true;
        }
        if (SpecialProps.has(inst, name)) {
            return true;
        }
        if (RobloxDefaults.has(inst.getClassName(), name)) {
            return true;
        }
        Set<String> set = this.SPECIAL_PROPS.get(inst.getClassName());
        if (set != null && set.contains(name)) {
            z = true;
        }
        if (z) {
            return true;
        }
        return inst.getProps().containsKey(PropNames.INSTANCE.resolve(inst, name));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:213:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0116, code lost:
    
        if (r20.equals("AssemblyLinearVelocity") == false) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x022d, code lost:
    
        if (r20.equals("Velocity") != false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x022f, code lost:
    
        r0 = r18.rt.getPlay();
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0235, code lost:
    
        if (r0 == null) goto L213;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0237, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r21, "null cannot be cast to non-null type com.goydevv.studrblx.model.Vector3");
        r0.setVelocity(r19, (com.goydevv.studrblx.model.Vector3) r21);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0240, code lost:
    
        return;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setK(com.goydevv.studrblx.model.Instance inst, java.lang.String k, java.lang.Object v) {
        if (inst == null || k == null) return;
        switch (k) {
            case "Name":
                if (v != null) inst.setName(v.toString());
                return;
            case "Parent":
                Instance np = v instanceof Instance ? (Instance) v : null;
                Instance.setParent$default(inst, np, 0, 2, null);
                return;
            case "Position":
                if (inst.isA("BasePart") && v instanceof Vector3) {
                    CFrame cur = PartData.INSTANCE.cframe(inst);
                    inst.set("CFrame", cur.withPosition((Vector3) v), true);
                    return;
                }
                inst.set(k, v, true);
                return;
            case "CFrame":
                if (inst.isA("BasePart") && v instanceof CFrame) {
                    inst.set("CFrame", v, true);
                    return;
                }
                inst.set(k, v, true);
                return;
            case "Size":
                if (inst.isA("BasePart") && v instanceof Vector3) {
                    PartData.INSTANCE.setSize(inst, (Vector3) v);
                    return;
                }
                inst.set(k, v, true);
                return;
            case "Color": case "Color3":
                if (inst.isA("BasePart") && v instanceof Color3) {
                    PartData.INSTANCE.setColor(inst, (Color3) v);
                    return;
                }
                inst.set(k, v, true);
                return;
            case "BrickColor":
                if (inst.isA("BasePart") && v instanceof com.goydevv.studrblx.model.BrickColorV) {
                    PartData.INSTANCE.setColor(inst, ((com.goydevv.studrblx.model.BrickColorV) v).getColor());
                    return;
                }
                inst.set("BrickColor", v, true);
                return;
            case "Health":
                inst.set(inst.getProps().containsKey("Health") ? "Health" : "Health_XML", v, true);
                return;
            case "Orientation":
            case "Rotation":
                if (inst.isA("BasePart") && v instanceof Vector3) {
                    Vector3 o = (Vector3) v;
                    float r = (float) (Math.PI / 180.0d);
                    CFrame cur = PartData.INSTANCE.cframe(inst);
                    CFrame rot = "Orientation".equals(k) ? CFrame.INSTANCE.fromOrientation(o.getX() * r, o.getY() * r, o.getZ() * r) : CFrame.INSTANCE.angles(o.getX() * r, o.getY() * r, o.getZ() * r);
                    inst.set("CFrame", rot.withPosition(cur.getPosition()), true);
                    return;
                }
                inst.set(k, v, true);
                return;
            case "AssemblyLinearVelocity":
            case "Velocity":
                if (inst.isA("BasePart") && v instanceof Vector3 && this.rt.getPlay() != null) {
                    this.rt.getPlay().setVelocity(inst, (Vector3) v);
                }
                inst.set(k, v, true);
                return;
            case "Playing":
                if ("Sound".equals(inst.getClassName()) && Boolean.TRUE.equals(v)) {
                    Object sq = inst.getAttributes().get("__playSeq");
                    int nsq = (sq instanceof Integer) ? ((Integer) sq).intValue() + 1 : 1;
                    inst.getAttributes().put("__playSeq", Integer.valueOf(nsq));
                }
                inst.set("Playing", v, true);
                return;
            default:
                inst.set(k, v, true);
                return;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:229:0x0322  */
    /* JADX WARN: Code duplicated, block: B:252:0x0373  */
    /* JADX WARN: Code duplicated, block: B:253:0x037a  */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x002d, code lost:
    
        return 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x006f, code lost:
    
        if (r19.equals("RotVelocity") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0078, code lost:
    
        if (r19.equals("AssemblyLinearVelocity") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007f, code lost:
    
        if (r19.equals("Position") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0088, code lost:
    
        if (r19.equals("Orientation") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0091, code lost:
    
        if (r19.equals("AssemblyAngularVelocity") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a3, code lost:
    
        if (r19.equals("Rotation") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00aa, code lost:
    
        if (r19.equals("Size") == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00c2, code lost:
    
        if (r19.equals("Velocity") != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ca, code lost:
    
        return 14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0022, code lost:
    
        if (r19.equals("Archivable") == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x013b, code lost:
    
        if (r19.equals("Size") == false) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0142, code lost:
    
        if (r19.equals("Transparency") == false) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x014b, code lost:
    
        if (r19.equals("Squash") != false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0153, code lost:
    
        return 21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x002b, code lost:
    
        if (r19.equals("Enabled") != false) goto L10;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final java.lang.Integer typeHint(com.goydevv.studrblx.model.Instance r18, java.lang.String r19) {
        if (r19 == null) {
            return null;
        }
        String cn = r18 != null ? r18.getClassName() : "";
        switch (r19) {
            case "Name":
            case "Text":
            case "PlaceholderText":
            case "ToolTip":
            case "TimeOfDay":
            case "CollisionGroup":
            case "MaterialVariant":
            case "ActionText":
            case "ObjectText":
            case "DisplayName":
            case "Title":
                return 1;
            case "SoundId":
            case "Image":
            case "HoverImage":
            case "PressedImage":
            case "Texture":
            case "TextureId":
            case "TextureID":
            case "MeshId":
            case "AnimationId":
            case "ShirtTemplate":
            case "PantsTemplate":
            case "Graphic":
            case "SkyboxBk":
            case "SkyboxDn":
            case "SkyboxFt":
            case "SkyboxLf":
            case "SkyboxRt":
            case "SkyboxUp":
            case "ColorMap":
            case "NormalMap":
            case "MetalnessMap":
            case "RoughnessMap":
                return 34;
            case "Anchored":
            case "CanCollide":
            case "CanTouch":
            case "CanQuery":
            case "Locked":
            case "Massless":
            case "CastShadow":
            case "Archivable":
            case "Enabled":
            case "Visible":
            case "Active":
            case "ClipsDescendants":
            case "Selectable":
            case "Interactable":
            case "Modal":
            case "Selected":
            case "AutoButtonColor":
            case "TextScaled":
            case "TextWrapped":
            case "RichText":
            case "ClearTextOnFocus":
            case "MultiLine":
            case "TextEditable":
            case "ResetOnSpawn":
            case "IgnoreGuiInset":
            case "AlwaysOnTop":
            case "ScrollingEnabled":
            case "GlobalShadows":
            case "Looped":
            case "Playing":
            case "PlayOnRemove":
            case "AutoRotate":
            case "Jump":
            case "Sit":
            case "PlatformStand":
            case "UseJumpPower":
            case "BreakJointsOnDeath":
            case "Disabled":
            case "Neutral":
            case "AllowTeamChangeOnTouch":
            case "RequiresHandle":
            case "CanBeDropped":
            case "ManualActivationOnly":
            case "Shadows":
            case "DoubleSided":
            case "RequiresLineOfSight":
            case "HttpEnabled":
            case "AutoAssignable":
                return 2;
            case "ZIndex":
            case "LayoutOrder":
            case "BorderSizePixel":
            case "DisplayOrder":
            case "ScrollBarThickness":
            case "MaxVisibleGraphemes":
            case "Duration":
                return 3;
            case "Transparency":
            case "Reflectance":
            case "BackgroundTransparency":
            case "TextTransparency":
            case "TextStrokeTransparency":
            case "ImageTransparency":
            case "TextSize":
            case "Rotation":
            case "LineHeight":
            case "Thickness":
            case "Scale":
            case "Health":
            case "MaxHealth":
            case "WalkSpeed":
            case "JumpPower":
            case "JumpHeight":
            case "HipHeight":
            case "Volume":
            case "PlaybackSpeed":
            case "TimePosition":
            case "RollOffMaxDistance":
            case "RollOffMinDistance":
            case "Brightness":
            case "Range":
            case "Angle":
            case "ClockTime":
            case "FogStart":
            case "FogEnd":
            case "ExposureCompensation":
            case "GeographicLatitude":
            case "ShadowSoftness":
            case "EnvironmentDiffuseScale":
            case "EnvironmentSpecularScale":
            case "Density":
            case "Offset":
            case "Haze":
            case "Glare":
            case "FieldOfView":
            case "Gravity":
            case "FallenPartsDestroyHeight":
            case "Length":
            case "FreeLength":
            case "Stiffness":
            case "Damping":
            case "MaxActivationDistance":
            case "HoldDuration":
            case "FillTransparency":
            case "OutlineTransparency":
            case "Rate":
            case "Drag":
            case "LightEmission":
            case "StudsPerTileU":
            case "StudsPerTileV":
            case "OffsetStudsU":
            case "OffsetStudsV":
            case "MaxSpeed":
            case "Torque":
            case "Throttle":
            case "Steer":
            case "TurnSpeed":
                return 4;
            case "Size":
            case "Position":
                if (r18 != null && (r18.isA("GuiObject") || Intrinsics.areEqual(cn, "BillboardGui"))) {
                    return 7;
                }
                return 14;
            case "CanvasSize":
                if (Intrinsics.areEqual(cn, "ScrollingFrame")) {
                    return 7;
                }
                return null;
            case "BrickColor":
            case "TeamColor":
                return 11;
            case "Color":
            case "Color3":
            case "BackgroundColor3":
            case "BorderColor3":
            case "TextColor3":
            case "TextStrokeColor3":
            case "PlaceholderColor3":
            case "ImageColor3":
            case "ScrollBarImageColor3":
            case "Ambient":
            case "OutdoorAmbient":
            case "FogColor":
            case "ColorShift_Top":
            case "ColorShift_Bottom":
            case "TintColor":
            case "Decay":
            case "FillColor":
            case "OutlineColor":
                return 12;
            case "Orientation":
            case "Velocity":
            case "AssemblyLinearVelocity":
            case "AssemblyAngularVelocity":
            case "MoveDirection":
            case "WalkToPoint":
            case "StudsOffset":
            case "StudsOffsetWorldSpace":
            case "VertexColor":
            case "Acceleration":
                return 14;
            case "CFrame":
            case "C0":
            case "C1":
            case "Transform":
            case "Focus":
            case "Grip":
            case "WorldCFrame":
                return 16;
            case "WorldPivot":
                return 30;
            case "Material":
            case "Shape":
            case "Face":
            case "Font":
            case "TextXAlignment":
            case "TextYAlignment":
            case "TextTruncate":
            case "ScaleType":
            case "FillDirection":
            case "HorizontalAlignment":
            case "VerticalAlignment":
            case "SortOrder":
            case "ZIndexBehavior":
            case "AutomaticSize":
            case "SizeConstraint":
            case "CameraType":
            case "RigType":
            case "Technology":
            case "TopSurface":
            case "BottomSurface":
            case "FrontSurface":
            case "BackSurface":
            case "LeftSurface":
            case "RightSurface":
            case "MeshType":
            case "DepthMode":
            case "Style":
            case "KeyboardKeyCode":
            case "GamepadKeyCode":
                return 18;
            case "Parent":
            case "PrimaryPart":
            case "Adornee":
            case "Part0":
            case "Part1":
            case "Attachment0":
            case "Attachment1":
            case "CameraSubject":
            case "Occupant":
            case "RespawnLocation":
            case "Team":
            case "SoundGroup":
                return 19;
            case "UserId":
                return 27;
            default:
                if (Enums.INSTANCE.typeForProp(cn, r19) != null) {
                    return 18;
                }
                return null;
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public final LuaValue index(Instance inst, String key0) {
        LuaValue luaValue;
        Instance instanceFindFirstChild$default;
        VarArgFunction varArgFunctionFnv;
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(key0, "key0");
        String str = key0;
        boolean z = true;
        if ((str.length() > 0) && Character.isLowerCase(str.charAt(0))) {
            char upperCase = Character.toUpperCase(str.charAt(0));
            String strSubstring = str.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            String cap = upperCase + strSubstring;
            if (methodAllowed(inst, cap)) {
                str = cap;
            }
        }
        switch (str.hashCode()) {
            case -1911556918:
                if (str.equals("Parent")) {
                    return this.rt.wrap(inst.getParent());
                }
                break;
            case 2420395:
                if (str.equals("Name")) {
                    LuaString luaStringValueOf = LuaValue.valueOf(inst.getName());
                    Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                    return luaStringValueOf;
                }
                break;
            case 1994079235:
                if (str.equals("ClassName")) {
                    LuaString luaStringValueOf2 = LuaValue.valueOf(inst.getClassName());
                    Intrinsics.checkNotNullExpressionValue(luaStringValueOf2, "valueOf(...)");
                    return luaStringValueOf2;
                }
                break;
        }
        if (methodAllowed(inst, str)) {
            Map map = this.methodCache;
            String str2 = str;
            LuaValue luaValue2 = (LuaValue) map.get(str2);
            if (luaValue2 == null) {
                final String str3 = str;
                varArgFunctionFnv = LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda20
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return InstanceApi.index$lambda$12$lambda$11(InstanceApi.this, str3, (Varargs) obj);
                    }
                });
                map.put(str2, varArgFunctionFnv);
            } else {
                varArgFunctionFnv = (VarArgFunction) luaValue2;
            }
            return varArgFunctionFnv;
        }
        if ((inst.isA("BaseScript") || inst.isA("ModuleScript")) && Intrinsics.areEqual(str, "Source")) {
            Object obj = inst.get("Source");
            String str4 = obj instanceof String ? (String) obj : null;
            LuaString luaStringValueOf3 = LuaValue.valueOf(str4 != null ? str4 : "");
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf3, "valueOf(...)");
            return luaStringValueOf3;
        }
        if (inst.isA("TextChatService") && (Intrinsics.areEqual(str, "OnIncomingMessage") || Intrinsics.areEqual(str, "OnBubbleAdded") || Intrinsics.areEqual(str, "OnChatWindowAdded"))) {
            HashMap<String, LuaValue> callbacks = this.rt.getCallbacks().get(inst);
            LuaValue callback = callbacks == null ? null : callbacks.get(str);
            if (callback != null) return callback;
        }
        if (hasK(inst, str)) {
            Object v = getK(inst, str);
            if (v == null) {
                Instance namedChild = Instance.findFirstChild$default(inst, str, false, 2, null);
                if (namedChild != null) return this.rt.wrap(namedChild);
                if ("DataModel".equals(inst.getClassName()) && ("Workspace".equals(str) || "workspace".equals(str))) {
                    Instance ws = Instance.findFirstChild$default(inst, "Workspace", false, 2, null);
                    if (ws != null) return this.rt.wrap(ws);
                }
            }
            return this.rt.toLua(v, inst, PropNames.INSTANCE.resolve(inst, str));
        }
        HashMap<String, LuaValue> map2 = this.rt.getCallbacks().get(inst);
        if (map2 != null && (luaValue = map2.get(str)) != null) {
            return luaValue;
        }
        if (this.events.contains(str)) {
            return (this.universalEvents.contains(str) || (instanceFindFirstChild$default = Instance.findFirstChild$default(inst, str, false, 2, null)) == null) ? signal(inst, str) : this.rt.wrap(instanceFindFirstChild$default);
        }
        Instance instanceFindFirstChild$default2 = Instance.findFirstChild$default(inst, str, false, 2, null);
        if (instanceFindFirstChild$default2 != null) {
            return this.rt.wrap(instanceFindFirstChild$default2);
        }
        if (inst instanceof com.goydevv.studrblx.model.DataModel && DEFAULT_SERVICES.contains(str)) {
            Instance svc = ((com.goydevv.studrblx.model.DataModel) inst).getService(str);
            if (svc != null) return this.rt.wrap(svc);
        }
        String strContentAlias = contentAlias(inst, str);
        if (strContentAlias != null) {
            if (hasK(inst, strContentAlias)) {
                return this.rt.toLua(getK(inst, strContentAlias), inst, strContentAlias);
            }
            LuaString luaStringValueOf4 = LuaValue.valueOf("");
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf4, "valueOf(...)");
            return luaStringValueOf4;
        }
        Iterable iterable = (List) this.MEMBER_OWNERS.get(str);
        if (iterable != null) {
            Iterable iterable2 = iterable;
            if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                Iterator it = iterable2.iterator();
                while (it.hasNext()) {
                    if (inst.isA((String) it.next())) {
                        z = false;
                    }
                }
            }
            if (z) {
                throw new LuaError(((Object) str) + " is not a valid member of " + inst.getClassName() + " \"" + inst.fullName() + "\"");
            }
        }
        if (STRICT_CONTAINERS.contains(inst.getClassName()) && !hasK(inst, str)) {
            throw new LuaError(((Object) str) + " is not a valid member of " + inst.getClassName() + " \"" + inst.fullName() + "\"");
        }
        if (this.warnedMissing.add(inst.getClassName() + "." + ((Object) str))) {
            OutputLog.INSTANCE.warn(((Object) str) + " is not a known member of " + inst.getClassName() + " (treated as nil)");
        }
        LuaValue NIL = LuaValue.NIL;
        Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        return NIL;
    }

    static final Varargs index$lambda$12$lambda$11(InstanceApi this$0, String $k, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return this$0.callMethod($k, a);
    }

    public final void newIndex(Instance inst, String key, LuaValue v) {
        HashMap<String, LuaValue> map;
        String want;
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(v, "v");
        String strContentAlias = contentAlias(inst, key);
        String str = "";
        if (strContentAlias != null && (v.isstring() || v.isnil())) {
            LuaValue luaStringValueOf = v.isnil() ? LuaValue.valueOf("") : v;
            Intrinsics.checkNotNull(luaStringValueOf);
            newIndex(inst, strContentAlias, luaStringValueOf);
            return;
        }
        boolean textChatCallback = inst.isA("TextChatService") && (Intrinsics.areEqual(key, "OnIncomingMessage") || Intrinsics.areEqual(key, "OnBubbleAdded") || Intrinsics.areEqual(key, "OnChatWindowAdded"));
        if (Intrinsics.areEqual(key, "OnServerInvoke") || Intrinsics.areEqual(key, "OnClientInvoke") || Intrinsics.areEqual(key, "OnInvoke") || textChatCallback || (v.isfunction() && !inst.getProps().containsKey(key))) {
            Map callbacks = this.rt.getCallbacks();
            HashMap<String, LuaValue> map2 = (HashMap<String, LuaValue>) callbacks.get(inst);
            if (map2 == null) {
                map = new HashMap<>();
                callbacks.put(inst, map);
            } else {
                map = map2;
            }
            if (textChatCallback) {
                if (v.isnil()) {
                    map.remove(key);
                } else if (v.isfunction()) {
                    map.put(key, v);
                } else {
                    throw new LuaError(key + " must be a function or nil");
                }
            } else {
                map.put(key, v);
            }
            return;
        }
        if (Intrinsics.areEqual(key, "Source") && inst.isA("LuaSourceContainer")) {
            if (this.rt.getIsPlay()) {
                throw new LuaError("The current thread cannot write 'Source' (lacking capability PluginOrOpenCloud)");
            }
            Instance.set$default(inst, "Source", v.tojstring(), false, 4, null);
            return;
        }
        Integer type = typeHint(inst, key);
        try {
            Object kv = this.rt.fromLua(v, type, inst, PropNames.INSTANCE.resolve(inst, key));
            try {
                setK(inst, key, kv);
            } catch (ClassCastException e) {
                throw new LuaError("Unable to assign property " + key + ". " + LuaTypes.INSTANCE.typeOf(v) + " is not the correct type");
            }
        } catch (LuaError e2) {
            if ((type != null && type.intValue() == 4) || ((type != null && type.intValue() == 5) || ((type != null && type.intValue() == 3) || (type != null && type.intValue() == 27)))) {
                want = "number";
            } else if (type != null && type.intValue() == 2) {
                want = "boolean";
            } else if (type == null || type.intValue() != 1) {
                want = (type == null || type.intValue() != 18) ? null : "EnumItem";
            } else {
                want = "string";
            }
            if (want == null) {
                String message = e2.getMessage();
                if (message != null) {
                    str = message;
                }
            } else {
                str = want + " expected, got " + LuaTypes.INSTANCE.typeOf(v);
            }
            throw new LuaError("Unable to assign property " + key + ". " + str);
        }
    }

    public final List<Instance> partsIn(Instance i) {
        Intrinsics.checkNotNullParameter(i, "i");
        ArrayList out = new ArrayList();
        if (i.isA("BasePart")) {
            out.add(i);
        }
        for (Instance d : Instance.descendants$default(i, null, 1, null)) {
            if (d.isA("BasePart")) {
                out.add(d);
            }
        }
        return out;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final String contentAlias(Instance inst, String key) {
        if (inst.getProps().containsKey(key)) {
            return null;
        }
        switch (key.hashCode()) {
            case -1926308098:
                if (!key.equals("TextureContent")) {
                    return null;
                }
                if (Intrinsics.areEqual(inst.getClassName(), "MeshPart")) {
                    return "TextureID";
                }
                if (inst.isA("Decal")) {
                    return "Texture";
                }
                return null;
            case -1813296724:
                if (key.equals("MeshContent")) {
                    return "MeshId";
                }
                return null;
            case 1147523044:
                if (key.equals("NormalMapContent")) {
                    return "NormalMap";
                }
                return null;
            case 1714113214:
                if (key.equals("ImageContent")) {
                    return "Image";
                }
                return null;
            case 1832176416:
                if (key.equals("ColorMapContent")) {
                    return "ColorMap";
                }
                return null;
            default:
                return null;
        }
    }

    public final CFrame modelPivot(Instance m) {
        CFrame cframe;
        Intrinsics.checkNotNullParameter(m, "m");
        Object obj = m.get("PrimaryPart");
        Instance instance = obj instanceof Instance ? (Instance) obj : null;
        if (instance != null) {
            return PartData.INSTANCE.cframe(instance);
        }
        Object obj2 = m.get("WorldPivotData");
        OptionalCFrame optionalCFrame = obj2 instanceof OptionalCFrame ? (OptionalCFrame) obj2 : null;
        if (optionalCFrame != null && (cframe = optionalCFrame.getCframe()) != null) {
            return cframe;
        }
        Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(partsIn(m));
        return pairBoundsOf == null ? CFrame.INSTANCE.getIDENTITY() : CFrame.INSTANCE.at(pairBoundsOf.getFirst().plus(pairBoundsOf.getSecond()).div(2.0f));
    }

    public final void pivotTo(Instance target, CFrame cf) {
        Intrinsics.checkNotNullParameter(target, "target");
        Intrinsics.checkNotNullParameter(cf, "cf");
        if (target.isA("BasePart")) {
            PartData.INSTANCE.setCFrame(target, cf);
            return;
        }
        CFrame old = modelPivot(target);
        CFrame delta = cf.times(old.inverse());
        for (Instance p : partsIn(target)) {
            PartData.INSTANCE.setCFrame(p, delta.times(PartData.INSTANCE.cframe(p)));
        }
        if (!target.getProps().containsKey("WorldPivotData") || target.get("PrimaryPart") != null) {
            return;
        }
        Instance.set$default(target, "WorldPivotData", new OptionalCFrame(cf), false, 4, null);
    }

    private final List<String> tags(Instance i) {
        String raw;
        Object t = i.get("Tags");
        if (t instanceof BinaryString) {
            raw = new String(((BinaryString) t).getBytes(), Charsets.UTF_8);
        } else {
            raw = t instanceof String ? (String) t : "";
        }
        Iterable iterableSplit$default = StringsKt.split((CharSequence) raw, new char[]{0}, false, 0);
        Collection arrayList = new ArrayList();
        for (Object obj : iterableSplit$default) {
            if (((String) obj).length() > 0) {
                arrayList.add(obj);
            }
        }
        return CollectionsKt.toMutableList(arrayList);
    }

    private final void setTags(Instance i, List<String> t) {
        byte[] bytes = CollectionsKt.joinToString(t, "\u0000", "", "", -1, "...", null).getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        Instance.set$default(i, "Tags", new BinaryString(bytes), false, 4, null);
        i.getPropTypes().put("Tags", 1);
    }

    private final LuaTable listToLua(List<? extends Instance> l) {
        LuaTable t = new LuaTable();
        for (Instance x : l) {
            t.insert(0, this.rt.wrap(x));
        }
        return t;
    }

    private final List<Instance> instList(LuaValue v) {
        if (!v.istable()) {
            return CollectionsKt.emptyList();
        }
        ArrayList out = new ArrayList();
        int k = 1;
        int length = v.length();
        if (1 <= length) {
            while (true) {
                LuaValue luaValue = v.get(k);
                Intrinsics.checkNotNullExpressionValue(luaValue, "get(...)");
                Object obj = luaValue.isuserdata() ? luaValue.touserdata() : null;
                Instance instance = (Instance) (obj instanceof Instance ? obj : null);
                if (instance != null) {
                    out.add(instance);
                }
                if (k == length) {
                    break;
                }
                k++;
            }
        }
        return out;
    }

    private final List<Instance> workspaceParts() {
        Instance ws = this.rt.getDm().findService("Workspace");
        if (ws == null) {
            return CollectionsKt.emptyList();
        }
        return this.rt.getDm() == this.rt.getStudio().getModel() ? this.rt.getStudio().renderParts() : partsIn(ws);
    }

    private final Function1<Instance, Boolean> filterFrom(LuaValue params) {
        if (!params.istable()) {
            return new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(InstanceApi.filterFrom$lambda$25((Instance) obj));
                }
            };
        }
        LuaValue luaValue = params.get("FilterDescendantsInstances");
        Intrinsics.checkNotNullExpressionValue(luaValue, "get(...)");
        final List<Instance> listInstList = instList(luaValue);
        LuaValue luaValue2 = params.get("FilterType");
        Intrinsics.checkNotNullExpressionValue(luaValue2, "get(...)");
        Object obj = luaValue2.isuserdata() ? luaValue2.touserdata() : null;
        EnumItemV enumItemV = (EnumItemV) (obj instanceof EnumItemV ? obj : null);
        final boolean include = (enumItemV != null && enumItemV.getValue() == 1);
        final boolean respectCollide = params.get("RespectCanCollide").toboolean();
        return new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return Boolean.valueOf(InstanceApi.filterFrom$lambda$27(listInstList, include, respectCollide, (Instance) obj2));
            }
        };
    }

    static final boolean filterFrom$lambda$25(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return !Intrinsics.areEqual(it.get("CanQuery"), (Object) false);
    }

    static final boolean filterFrom$lambda$27(List $list, boolean $include, boolean $respectCollide, Instance p) {
        boolean inList;
        boolean z;
        Intrinsics.checkNotNullParameter(p, "p");
        List list = $list;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    Instance instance = (Instance) it.next();
                    if (p == instance || p.isDescendantOf(instance)) {
                        inList = true;
                        break;
                    }
                } else {
                    inList = false;
                    break;
                }
            }
        } else {
            inList = false;
        }
        if ($include) {
            z = inList;
        } else {
            z = !inList;
        }
        return (!z || Intrinsics.areEqual(p.get("CanQuery"), (Object) false) || ($respectCollide && Intrinsics.areEqual(p.get("CanCollide"), (Object) false))) ? false : true;
    }

    private final LuaValue raycastResult(Vector3 o, Vector3 d, Function1<? super Instance, Boolean> filter) {
        float len = d.getMagnitude();
        if (len < 1.0E-6f) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        RayHit hit = Geometry.INSTANCE.raycast(workspaceParts(), o, d.div(len), len, filter);
        if (hit == null) {
            LuaValue NIL2 = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL2, "NIL");
            return NIL2;
        }
        LuaTable t = new LuaTable();
        t.set("Instance", this.rt.wrap(hit.getPart()));
        t.set("Position", LuaTypes.INSTANCE.wrap(hit.getPosition()));
        t.set("Normal", LuaTypes.INSTANCE.wrap(hit.getNormal()));
        t.set("Distance", LuaValue.valueOf(hit.getT()));
        int mat = PartData.INSTANCE.material(hit.getPart());
        LuaTypes luaTypes = LuaTypes.INSTANCE;
        String strItemName = Enums.INSTANCE.itemName("Material", mat);
        if (strItemName == null) {
            strItemName = "Plastic";
        }
        t.set("Material", luaTypes.wrap(new EnumItemV("Material", strItemName, mat)));
        return t;
    }

    private final boolean isEditCamera(Instance cam) {
        Instance ws = this.rt.getStudio().getEditModel().getWorkspace();
        return cam.getParent() == ws && (ws.get("CurrentCamera") == cam || ws.get("CurrentCamera") == null);
    }

    private final EnumItemV priorityItem(int v) {
        Object next;
        String str;
        Iterator it = this.PRIORITIES.iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!(((Number) ((Pair) next).getSecond()).intValue() == v));
        Pair pair = (Pair) next;
        if (pair == null || (str = (String) pair.getFirst()) == null) {
            str = "Core";
        }
        return new EnumItemV("AnimationPriority", str, v);
    }

    private final LuaTable animationTrack(Instance owner, final Instance anim) {
        final LuaTable t = new LuaTable();
        final PlaySession play = this.rt.getPlay();
        final PlaySession.ScriptTrack st = play != null ? play.loadTrack(owner, anim, t) : null;
        final Ref.BooleanRef playing = new Ref.BooleanRef();
        final Ref.FloatRef speed = new Ref.FloatRef();
        speed.element = 1.0f;
        final Ref.ObjectRef looped = new Ref.ObjectRef();
        final Ref.ObjectRef priority = new Ref.ObjectRef();
        t.set("Play", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$30(speed, st, play, playing, (Varargs) obj);
            }
        }));
        t.set("Stop", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$31(st, play, playing, InstanceApi.this, t, (Varargs) obj);
            }
        }));
        t.set("AdjustSpeed", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$33(speed, st, play, (Varargs) obj);
            }
        }));
        t.set("AdjustWeight", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$35(st, play, (Varargs) obj);
            }
        }));
        t.set("GetMarkerReachedSignal", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$36(InstanceApi.this, t, (Varargs) obj);
            }
        }));
        t.set("GetTimeOfKeyframe", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$37((Varargs) obj);
            }
        }));
        t.set("Destroy", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return InstanceApi.animationTrack$lambda$39(st, play, (Varargs) obj);
            }
        }));
        t.set("Animation", this.rt.wrap(anim));
        LuaTable mt = new LuaTable();
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return InstanceApi.animationTrack$lambda$40(st, playing, speed, looped, InstanceApi.this, priority, anim, t, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        mt.set(LuaValue.NEWINDEX, new ThreeArgFunction() { // from class: com.goydevv.studrblx.lua.InstanceApi.animationTrack.9
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Code duplicated, block: B:41:0x00cd  */
            /* JADX WARN: Type inference failed for: r1v11, types: [T, java.lang.Integer] */
            /* JADX WARN: Type inference failed for: r1v4, types: [T, java.lang.Boolean] */
            @Override // org.luaj.vm2.lib.ThreeArgFunction, org.luaj.vm2.lib.LibFunction, org.luaj.vm2.LuaValue
            public LuaValue call(LuaValue self, LuaValue k, LuaValue v) {
                PlaySession playSession;
                Intrinsics.checkNotNullParameter(self, "self");
                Intrinsics.checkNotNullParameter(k, "k");
                Intrinsics.checkNotNullParameter(v, "v");
                String str = k.tojstring();
                if (str != null) {
                    switch (str) {
                        case "Looped":
                            looped.element = Boolean.valueOf(v.toboolean());
                            PlaySession.ScriptTrack scriptTrack = st;
                            if (scriptTrack != null) {
                                PlaySession playSession2 = play;
                                Ref.ObjectRef<Boolean> objectRef = looped;
                                if (playSession2 != null) {
                                    PlaySession.adjustTrack$default(playSession2, scriptTrack, null, null, null, objectRef.element, null, null, 110, null);
                                }
                            }
                            break;
                        case "Priority":
                            Ref.ObjectRef<Integer> objectRef2 = priority;
                            Object obj = v.isuserdata() ? v.touserdata() : null;
                            EnumItemV enumItemV = (EnumItemV) (obj instanceof EnumItemV ? obj : null);
                            objectRef2.element = Integer.valueOf(enumItemV != null ? enumItemV.getValue() : v.optint(1000));
                            PlaySession.ScriptTrack scriptTrack2 = st;
                            if (scriptTrack2 != null) {
                                PlaySession playSession3 = play;
                                Ref.ObjectRef<Integer> objectRef3 = priority;
                                if (playSession3 != null) {
                                    PlaySession.adjustTrack$default(playSession3, scriptTrack2, null, null, null, null, objectRef3.element, null, 94, null);
                                }
                            }
                            break;
                        case "TimePosition":
                            PlaySession.ScriptTrack scriptTrack3 = st;
                            if (scriptTrack3 != null && (playSession = play) != null) {
                                PlaySession.adjustTrack$default(playSession, scriptTrack3, null, null, null, null, null, Float.valueOf((float) v.optdouble(0.0d)), 62, null);
                            }
                            break;
                        default:
                            ((LuaTable) self).rawset(k, v);
                            break;
                    }
                } else {
                    ((LuaTable) self).rawset(k, v);
                }
                LuaValue NONE = LuaValue.NONE;
                Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
                return NONE;
            }
        });
        t.setmetatable(mt);
        return t;
    }

    static final Varargs animationTrack$lambda$30(Ref.FloatRef $speed, PlaySession.ScriptTrack $st, PlaySession $play, Ref.BooleanRef $playing, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        float fade = (float) a.arg(2).optdouble(0.1d);
        float w = (float) a.arg(3).optdouble(1.0d);
        float sp = (float) a.arg(4).optdouble($speed.element);
        $speed.element = sp;
        if ($st == null) {
            $playing.element = true;
        } else if ($play != null) {
            $play.playTrack($st, fade, w, sp);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs animationTrack$lambda$31(PlaySession.ScriptTrack $st, PlaySession $play, Ref.BooleanRef $playing, InstanceApi this$0, LuaTable $t, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        float fade = (float) a.arg(2).optdouble(0.1d);
        if ($st != null) {
            if ($play != null) {
                $play.stopTrack($st, fade);
            }
        } else if ($playing.element) {
            $playing.element = false;
            this$0.rt.fire($t, "Stopped", new LuaValue[0]);
            this$0.rt.fire($t, "Ended", new LuaValue[0]);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs animationTrack$lambda$33(Ref.FloatRef $speed, PlaySession.ScriptTrack $st, PlaySession $play, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        $speed.element = (float) a.arg(2).optdouble(1.0d);
        if ($st != null && $play != null) {
            PlaySession.adjustTrack$default($play, $st, Float.valueOf($speed.element), null, null, null, null, null, 124, null);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs animationTrack$lambda$35(PlaySession.ScriptTrack $st, PlaySession $play, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        if ($st != null && $play != null) {
            PlaySession.adjustTrack$default($play, $st, null, Float.valueOf((float) a.arg(2).optdouble(1.0d)), Float.valueOf((float) a.arg(3).optdouble(0.1d)), null, null, null, 114, null);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs animationTrack$lambda$36(InstanceApi this$0, LuaTable $t, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return this$0.signal($t, "Marker:" + a.arg(2).tojstring());
    }

    static final Varargs animationTrack$lambda$37(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final Varargs animationTrack$lambda$39(PlaySession.ScriptTrack $st, PlaySession $play, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        if ($st != null && $play != null) {
            $play.stopTrack($st, 0.0f);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0090, code lost:
    
        if (r12.equals("Ended") == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00d8, code lost:
    
        if (r12.equals("Stopped") == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00e2, code lost:
    
        if (r12.equals("KeyframeReached") == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ec, code lost:
    
        if (r12.equals("DidLoop") == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x011e, code lost:
    
        if (r12.equals("Played") != false) goto L70;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue animationTrack$lambda$40(com.goydevv.studrblx.engine.PlaySession.ScriptTrack st, kotlin.jvm.internal.Ref.BooleanRef playing, kotlin.jvm.internal.Ref.FloatRef speed, kotlin.jvm.internal.Ref.ObjectRef looped, com.goydevv.studrblx.lua.InstanceApi this$0, kotlin.jvm.internal.Ref.ObjectRef priority, com.goydevv.studrblx.model.Instance anim, org.luaj.vm2.LuaTable table, org.luaj.vm2.LuaValue self, org.luaj.vm2.LuaValue key) {
        String k = key.tojstring();
        if (k == null) return LuaValue.NIL;
        switch (k) {
            case "IsPlaying": return LuaValue.valueOf(st != null ? st.isPlaying() : playing.element);
            case "Length": return LuaValue.valueOf(st != null ? st.getLength() : 0.0f);
            case "TimePosition": return LuaValue.valueOf(st != null ? st.getTime() : 0.0f);
            case "Speed": return LuaValue.valueOf(speed.element);
            case "Looped":
                Boolean lp = (Boolean) looped.element;
                if (lp != null) return LuaValue.valueOf(lp.booleanValue());
                com.goydevv.studrblx.engine.AnimClip cl = st != null ? st.getClip() : null;
                return LuaValue.valueOf(cl != null && cl.getLooped());
            case "Priority":
                Integer pr = (Integer) priority.element;
                return LuaTypes.INSTANCE.wrap(this$0.priorityItem(pr != null ? pr.intValue() : 1000));
            case "WeightCurrent": case "WeightTarget":
                return LuaValue.valueOf(st != null && st.isPlaying() ? 1.0f : 0.0f);
            case "Animation":
                return this$0.rt.wrap(anim);
            case "Stopped": case "Ended": case "DidLoop": case "Played":
                return new LuaUserdata(new SignalRef(table, k), this$0.signalMeta);
            default:
                return LuaValue.NIL;
        }
    }

    private final Instance self(Varargs a, String name) {
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Instance instance = (Instance) (obj instanceof Instance ? obj : null);
        if (instance != null) {
            return instance;
        }
        throw new LuaError("Expected ':' not '.' calling member function " + name);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x031b, code lost:
    
        r2 = r19.rt;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
        r7 = r6.findService(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0324, code lost:
    
        if (r7 != null) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0326, code lost:
    
        r7 = r6.getService(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0336, code lost:
    
        if (com.goydevv.studrblx.model.ClassDb.INSTANCE.getServiceNames().contains(r0) != false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0338, code lost:
    
        r7.setArchivable(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x033c, code lost:
    
        r2 = r2.wrap(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x04d7, code lost:
    
        if (r20.equals("Destroy") == false) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x0581, code lost:
    
        if (r20.equals("Remove") != false) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0587, code lost:
    
        if (r3.getIsService() != false) goto L206;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x058b, code lost:
    
        if ((r3 instanceof com.goydevv.studrblx.model.DataModel) != false) goto L206;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0591, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r20, "Destroy") == false) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x0593, code lost:
    
        r3.destroy();
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0597, code lost:
    
        com.goydevv.studrblx.model.Instance.setParent$default(r3, null, 0, 2, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x059c, code lost:
    
        r0 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x05c2, code lost:
    
        throw new org.luaj.vm2.LuaError("Cannot destroy " + r3.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:?, code lost:
    
        return listToLua(kotlin.collections.CollectionsKt.toList(r3.getChildren()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:?, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0169, code lost:
    
        if (r20.equals("children") == false) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01ce, code lost:
    
        if (r20.equals("GetChildren") != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0265, code lost:
    
        if (r20.equals("GetService") == false) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x029b, code lost:
    
        if (r20.equals("FindService") != false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x029d, code lost:
    
        r0 = r6.checkjstring();
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x02ab, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r3.getClassName(), "UserSettings") == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x02ad, code lost:
    
        r8 = r3.getChildren().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x02bc, code lost:
    
        if (r8.hasNext() == false) goto L234;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x02be, code lost:
    
        r9 = r8.next();
        r10 = (com.goydevv.studrblx.model.Instance) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x02ce, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r10.getClassName(), r0) != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x02d8, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r10.getName(), r0) != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x02da, code lost:
    
        r10 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x02dc, code lost:
    
        r10 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x02dd, code lost:
    
        if (r10 == false) goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x02df, code lost:
    
        r6 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x02e2, code lost:
    
        r6 = (com.goydevv.studrblx.model.Instance) r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x02e4, code lost:
    
        if (r6 == null) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02e6, code lost:
    
        r2 = r19.rt.wrap(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02ed, code lost:
    
        if (r2 == null) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02f0, code lost:
    
        r2 = org.luaj.vm2.LuaValue.NIL;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, "NIL");
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x02f7, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x02fa, code lost:
    
        if ((r3 instanceof com.goydevv.studrblx.model.DataModel) == false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x02fc, code lost:
    
        r6 = (com.goydevv.studrblx.model.DataModel) r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02ff, code lost:
    
        if (r6 != null) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0301, code lost:
    
        r6 = r19.rt.getDm();
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x030b, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r20, "FindService") == false) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x030d, code lost:
    
        r2 = r19.rt;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
        r2 = r2.wrap(r6.findService(r0));
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs callMethod(java.lang.String name, org.luaj.vm2.Varargs args) {
        Instance inst = self(args, name);
        String cn = inst.getClassName();
        Varargs extra = ApiExtras.call(this, this.rt, inst, name, args);
        if (extra != null) {
            return extra;
        }
        switch (name) {
            case "FindFirstChild": {
                String n = args.checkjstring(2);
                boolean rec = args.optboolean(3, false);
                Instance f = inst.findFirstChild(n, rec);
                return f != null ? this.rt.wrap(f) : LuaValue.NIL;
            }
            case "FindFirstChildOfClass": {
                Instance f = inst.findFirstChildOfClass(args.checkjstring(2));
                return f != null ? this.rt.wrap(f) : LuaValue.NIL;
            }
            case "FindFirstChildWhichIsA": {
                Instance f = inst.findFirstChildWhichIsA(args.checkjstring(2), args.optboolean(3, false));
                return f != null ? this.rt.wrap(f) : LuaValue.NIL;
            }
            case "WaitForChild": {
                String n = args.checkjstring(2);
                return this.rt.waitForChild(inst, n, args.isnoneornil(3) ? null : Double.valueOf(args.checkdouble(3)));
            }
            case "GetChildren": {
                LuaTable tbl = new LuaTable();
                ArrayList<Instance> chs = inst.getChildren();
                for (int i = 0; i < chs.size(); i++) {
                    tbl.set(i + 1, this.rt.wrap(chs.get(i)));
                }
                return tbl;
            }
            case "GetDescendants": {
                LuaTable tbl = new LuaTable();
                List<Instance> descs = Instance.descendants$default(inst, null, 1, null);
                for (int i = 0; i < descs.size(); i++) {
                    tbl.set(i + 1, this.rt.wrap(descs.get(i)));
                }
                return tbl;
            }
            case "Clone": {
                return this.rt.wrap(inst.cloneTree());
            }
            case "Destroy": case "Remove": {
                inst.destroy();
                return LuaValue.NIL;
            }
            case "ClearAllChildren": {
                ArrayList<Instance> copy = new ArrayList<>(inst.getChildren());
                for (Instance ch : copy) ch.destroy();
                return LuaValue.NIL;
            }
            case "IsA": {
                return LuaValue.valueOf(inst.isA(args.checkjstring(2)));
            }
            case "IsDescendantOf": {
                Object o = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                return LuaValue.valueOf((o instanceof Instance) && inst.isDescendantOf((Instance) o));
            }
            case "IsAncestorOf": {
                Object o = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                return LuaValue.valueOf((o instanceof Instance) && ((Instance) o).isDescendantOf(inst));
            }
            case "GetFullName": {
                return LuaValue.valueOf(inst.fullName());
            }
            case "GetAttribute": {
                Object av = inst.getAttributes().get(args.checkjstring(2));
                return av != null ? this.rt.toLua(av, inst, args.checkjstring(2)) : LuaValue.NIL;
            }
            case "SetAttribute": {
                String ak = args.checkjstring(2);
                LuaValue av = args.arg(3);
                if (av.isnil()) inst.getAttributes().remove(ak);
                else inst.getAttributes().put(ak, this.rt.fromLua(av, null, inst, ak));
                return LuaValue.NIL;
            }
            case "GetAttributes": {
                LuaTable tbl = new LuaTable();
                for (Map.Entry<String, Object> e : inst.getAttributes().entrySet()) {
                    tbl.set(e.getKey(), this.rt.toLua(e.getValue(), inst, e.getKey()));
                }
                return tbl;
            }
            case "GetPropertyChangedSignal": {
                return new LuaUserdata(new SignalRef(inst, "Changed:" + args.checkjstring(2)), this.signalMeta);
            }
            case "GetService": case "FindService": {
                String sn = args.checkjstring(2);
                com.goydevv.studrblx.model.DataModel dm = this.rt.getStudio().getModel();
                Instance svc = "GetService".equals(name) ? dm.getService(sn) : dm.findService(sn);
                return svc != null ? this.rt.wrap(svc) : LuaValue.NIL;
            }
            case "Play": {
                if ("Sound".equals(cn)) {
                    Object sq = inst.getAttributes().get("__playSeq");
                    int nsq = (sq instanceof Integer) ? ((Integer) sq).intValue() + 1 : 1;
                    inst.getAttributes().put("__playSeq", Integer.valueOf(nsq));
                    inst.set("Playing", Boolean.TRUE, true);
                }
                return LuaValue.NIL;
            }
            case "Stop": case "Pause": {
                if ("Sound".equals(cn)) {
                    inst.set("Playing", Boolean.FALSE, true);
                }
                return LuaValue.NIL;
            }
            case "Resume": {
                if ("Sound".equals(cn)) {
                    inst.set("Playing", Boolean.TRUE, true);
                }
                return LuaValue.NIL;
            }
            case "FireServer": case "FireClient": case "FireAllClients": {
                if ("RemoteEvent".equals(cn) || "UnreliableRemoteEvent".equals(cn)) {
                    int start = "FireClient".equals(name) ? 3 : 2;
                    int cnt = Math.max(0, args.narg() - start + 1);
                    LuaValue[] pass = new LuaValue[cnt];
                    for (int i = 0; i < cnt; i++) pass[i] = args.arg(start + i);
                    if ("FireServer".equals(name)) {
                        PlaySession pl = this.rt.getPlay();
                        LuaValue[] withPlr = new LuaValue[cnt + 1];
                        withPlr[0] = (pl != null && pl.getPlayer() != null) ? this.rt.wrap(pl.getPlayer()) : LuaValue.NIL;
                        System.arraycopy(pass, 0, withPlr, 1, cnt);
                        this.rt.fire(inst, "OnServerEvent", withPlr);
                    } else {
                        this.rt.fire(inst, "OnClientEvent", pass);
                    }
                } else if ("BindableEvent".equals(cn)) {
                    int cnt = Math.max(0, args.narg() - 1);
                    LuaValue[] pass = new LuaValue[cnt];
                    for (int i = 0; i < cnt; i++) pass[i] = args.arg(2 + i);
                    this.rt.fire(inst, "Event", pass);
                }
                return LuaValue.NIL;
            }
            case "InvokeServer": case "InvokeClient": case "Invoke": {
                HashMap<String, LuaValue> cbs = this.rt.getCallbacks().get(inst);
                String cbKey = "InvokeServer".equals(name) ? "OnServerInvoke" : ("InvokeClient".equals(name) ? "OnClientInvoke" : "OnInvoke");
                LuaValue fn = cbs != null ? cbs.get(cbKey) : null;
                if (fn != null && fn.isfunction()) {
                    int start = "InvokeClient".equals(name) ? 3 : 2;
                    int cnt = Math.max(0, args.narg() - start + 1);
                    if ("InvokeServer".equals(name)) {
                        PlaySession pl = this.rt.getPlay();
                        LuaValue[] withPlr = new LuaValue[cnt + 1];
                        withPlr[0] = (pl != null && pl.getPlayer() != null) ? this.rt.wrap(pl.getPlayer()) : LuaValue.NIL;
                        for (int i = 0; i < cnt; i++) withPlr[i + 1] = args.arg(start + i);
                        return fn.invoke(LuaValue.varargsOf(withPlr));
                    }
                    LuaValue[] pass = new LuaValue[cnt];
                    for (int i = 0; i < cnt; i++) pass[i] = args.arg(start + i);
                    return fn.invoke(LuaValue.varargsOf(pass));
                }
                return LuaValue.NIL;
            }
            case "Fire": {
                if ("BindableEvent".equals(cn)) {
                    int cnt = Math.max(0, args.narg() - 1);
                    LuaValue[] pass = new LuaValue[cnt];
                    for (int i = 0; i < cnt; i++) pass[i] = args.arg(2 + i);
                    this.rt.fire(inst, "Event", pass);
                }
                return LuaValue.NIL;
            }
            case "LoadAnimation": {
                Object aObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                if (aObj instanceof Instance) {
                    return animationTrack(inst, (Instance) aObj);
                }
                return LuaValue.NIL;
            }
            default:
                break;
        }
        if ("Workspace".equals(cn)) return workspaceCall(name, args);
        if ("CollectionService".equals(cn)) return collectionCall(name, args);
        if ("Humanoid".equals(cn) || "Animator".equals(cn) || "AnimationController".equals(cn)) return humanoidCall(inst, name, args);
        if (inst.isA("Model")) return modelCall(inst, name, args);
        if (inst.isA("BasePart")) return partCall(inst, name, args);
        try {
            return RobloxServices.INSTANCE.call(this, this.rt, inst, name, args);
        } catch (LuaError e) {
            throw e;
        } catch (Exception e) {
            throw new LuaError(name + ": " + e);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x008b, code lost:
    
        if (r14.equals("AddTag") == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0095, code lost:
    
        if (r14.equals("GetTags") == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00d1, code lost:
    
        if (r14.equals("RemoveTag") == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x015d, code lost:
    
        if (r14.equals("HasTag") == false) goto L55;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs collectionCall(java.lang.String name, org.luaj.vm2.Varargs args) {
        switch (name) {
            case "AddTag": {
                Object o = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                String tag = args.checkjstring(3);
                if (o instanceof Instance) {
                    Instance target = (Instance) o;
                    if (!Boolean.TRUE.equals(target.getAttributes().get("__tag:" + tag))) {
                        target.getAttributes().put("__tag:" + tag, Boolean.TRUE);
                        Instance cs = this.rt.getStudio().getModel().findService("CollectionService");
                        if (cs != null) {
                            this.rt.fire(cs, "TagAdded:" + tag, this.rt.wrap(target));
                        }
                    }
                }
                return LuaValue.NIL;
            }
            case "RemoveTag": {
                Object o = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                String tag = args.checkjstring(3);
                if (o instanceof Instance) {
                    Instance target = (Instance) o;
                    if (target.getAttributes().remove("__tag:" + tag) != null) {
                        Instance cs = this.rt.getStudio().getModel().findService("CollectionService");
                        if (cs != null) {
                            this.rt.fire(cs, "TagRemoved:" + tag, this.rt.wrap(target));
                        }
                    }
                }
                return LuaValue.NIL;
            }
            case "GetInstanceAddedSignal": {
                String tag = args.checkjstring(2);
                Instance cs = this.rt.getStudio().getModel().getService("CollectionService");
                return signal(cs, "TagAdded:" + tag);
            }
            case "GetInstanceRemovedSignal": {
                String tag = args.checkjstring(2);
                Instance cs = this.rt.getStudio().getModel().getService("CollectionService");
                return signal(cs, "TagRemoved:" + tag);
            }
            case "GetAllTags": {
                java.util.LinkedHashSet<String> all = new java.util.LinkedHashSet<>();
                for (Instance d : Instance.descendants$default(this.rt.getStudio().getModel(), null, 1, null)) {
                    for (String k : d.getAttributes().keySet()) {
                        if (k.startsWith("__tag:")) all.add(k.substring(6));
                    }
                }
                LuaTable tbl = new LuaTable();
                int idx = 1;
                for (String t : all) tbl.set(idx++, LuaValue.valueOf(t));
                return tbl;
            }
            case "HasTag": {
                Object o = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                String tag = args.checkjstring(3);
                return LuaValue.valueOf((o instanceof Instance) && Boolean.TRUE.equals(((Instance) o).getAttributes().get("__tag:" + tag)));
            }
            case "GetTagged": {
                String tag = args.checkjstring(2);
                LuaTable tbl = new LuaTable();
                int idx = 1;
                for (Instance d : Instance.descendants$default(this.rt.getStudio().getModel(), null, 1, null)) {
                    if (Boolean.TRUE.equals(d.getAttributes().get("__tag:" + tag))) {
                        tbl.set(idx++, this.rt.wrap(d));
                    }
                }
                return tbl;
            }
            case "GetTags": {
                Object o = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                LuaTable tbl = new LuaTable();
                if (o instanceof Instance) {
                    int idx = 1;
                    for (String k : ((Instance) o).getAttributes().keySet()) {
                        if (k.startsWith("__tag:")) tbl.set(idx++, LuaValue.valueOf(k.substring(6)));
                    }
                }
                return tbl;
            }
            default:
                return LuaValue.NIL;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:127:0x0385  */
    /* JADX WARN: Code duplicated, block: B:186:0x051e  */
    /* JADX WARN: Code duplicated, block: B:432:0x0521 A[SYNTHETIC] */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x006a, code lost:
    
        if (r30.equals("FindPartOnRayWithIgnoreList") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x03f4, code lost:
    
        if (r30.equals("FindPartsInRegion3") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x03fe, code lost:
    
        if (r30.equals("IsRegion3Empty") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x0408, code lost:
    
        if (r30.equals("FindPartsInRegion3WithIgnoreList") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0412, code lost:
    
        if (r30.equals("Spherecast") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x042f, code lost:
    
        if (r30.equals("FindPartOnRay") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0439, code lost:
    
        if (r30.equals("JoinToOutsiders") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x0557, code lost:
    
        if (r30.equals("Blockcast") != false) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x055d, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r30, "Blockcast") == false) goto L207;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x055f, code lost:
    
        r3 = r31.arg(2);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x056d, code lost:
    
        if (r3.isuserdata() == false) goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x056f, code lost:
    
        r7 = r3.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0574, code lost:
    
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0577, code lost:
    
        if ((r7 instanceof com.goydevv.studrblx.model.CFrame) != false) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0579, code lost:
    
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x057a, code lost:
    
        r7 = (com.goydevv.studrblx.model.CFrame) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x057c, code lost:
    
        if (r7 == null) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x057e, code lost:
    
        r3 = r7.getPosition();
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x05a3, code lost:
    
        throw new org.luaj.vm2.LuaError("CFrame expected, got " + com.goydevv.studrblx.lua.LuaTypes.INSTANCE.typeOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x05a4, code lost:
    
        r3 = r31.arg(2);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x05b2, code lost:
    
        if (r3.isuserdata() == false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x05b4, code lost:
    
        r7 = r3.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x05b9, code lost:
    
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x05bc, code lost:
    
        if ((r7 instanceof com.goydevv.studrblx.model.Vector3) != false) goto L214;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x05be, code lost:
    
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x05bf, code lost:
    
        r7 = (com.goydevv.studrblx.model.Vector3) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x05c1, code lost:
    
        if (r7 == null) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x05c3, code lost:
    
        r3 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x05c4, code lost:
    
        r4 = r31.arg(4);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x05d3, code lost:
    
        if (r4.isuserdata() == false) goto L220;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x05d5, code lost:
    
        r8 = r4.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x05da, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x05dd, code lost:
    
        if ((r8 instanceof com.goydevv.studrblx.model.Vector3) == false) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x05df, code lost:
    
        r6 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x05e0, code lost:
    
        r6 = (com.goydevv.studrblx.model.Vector3) r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x05e2, code lost:
    
        if (r6 == null) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x05e4, code lost:
    
        r4 = r31.arg(5);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0618, code lost:
    
        throw new org.luaj.vm2.LuaError("Vector3 expected, got " + com.goydevv.studrblx.lua.LuaTypes.INSTANCE.typeOf(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x0639, code lost:
    
        throw new org.luaj.vm2.LuaError("Vector3 expected, got " + com.goydevv.studrblx.lua.LuaTypes.INSTANCE.typeOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x07fd, code lost:
    
        if (r30.equals("ZoomToExtents") != false) goto L293;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x07ff, code lost:
    
        r3 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "NONE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x080c, code lost:
    
        if (r30.equals("FindPartOnRayWithWhitelist") != false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x080e, code lost:
    
        r3 = r31.arg(2);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x081c, code lost:
    
        if (r3.isuserdata() == false) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x081e, code lost:
    
        r10 = r3.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0823, code lost:
    
        r10 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0826, code lost:
    
        if ((r10 instanceof com.goydevv.studrblx.model.Ray) != false) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x0828, code lost:
    
        r10 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0829, code lost:
    
        r10 = (com.goydevv.studrblx.model.Ray) r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x082b, code lost:
    
        if (r10 == null) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0837, code lost:
    
        if (r31.arg(3).istable() == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0839, code lost:
    
        r3 = r31.arg(3);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "arg(...)");
        r3 = instList(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x084d, code lost:
    
        if (r31.arg(3).isuserdata() == false) goto L318;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x084f, code lost:
    
        r3 = r31.arg(3);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x085b, code lost:
    
        if (r3.isuserdata() == false) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x085d, code lost:
    
        r8 = r3.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0862, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0865, code lost:
    
        if ((r8 instanceof com.goydevv.studrblx.model.Instance) != false) goto L317;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0867, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0868, code lost:
    
        r3 = kotlin.collections.CollectionsKt.listOfNotNull((com.goydevv.studrblx.model.Instance) r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x086f, code lost:
    
        r3 = kotlin.collections.CollectionsKt.emptyList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0873, code lost:
    
        r7 = kotlin.jvm.internal.Intrinsics.areEqual(r30, "FindPartOnRayWithWhitelist");
        r8 = r10.getDirection().getMagnitude();
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0885, code lost:
    
        if (r8 >= 1.0E-6f) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x088a, code lost:
    
        r6 = com.goydevv.studrblx.engine.Geometry.INSTANCE.raycast(workspaceParts(), r10.getOrigin(), r10.getDirection().div(r8), r8, new com.goydevv.studrblx.lua.InstanceApi$$ExternalSyntheticLambda14(r3, r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x08a9, code lost:
    
        if (r6 != null) goto L325;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x08ab, code lost:
    
        r3 = org.luaj.vm2.LuaValue.varargsOf(org.luaj.vm2.LuaValue.NIL, com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r10.getOrigin().plus(r10.getDirection())));
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x08c7, code lost:
    
        r8 = new org.luaj.vm2.LuaValue[4];
        r8[0] = r29.rt.wrap(r6.getPart());
        r8[1] = com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r6.getPosition());
        r8[2] = com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r6.getNormal());
        r5 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r11 = com.goydevv.studrblx.model.Enums.INSTANCE.itemName("Material", com.goydevv.studrblx.model.PartData.INSTANCE.material(r6.getPart()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0903, code lost:
    
        if (r11 != null) goto L328;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0905, code lost:
    
        r11 = "Plastic";
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0907, code lost:
    
        r8[3] = r5.wrap(new com.goydevv.studrblx.lua.EnumItemV("Material", r11, com.goydevv.studrblx.model.PartData.INSTANCE.material(r6.getPart())));
        r3 = org.luaj.vm2.LuaValue.varargsOf(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x091f, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0945, code lost:
    
        throw new org.luaj.vm2.LuaError("Ray expected, got " + com.goydevv.studrblx.lua.LuaTypes.INSTANCE.typeOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x094a, code lost:
    
        if (r30.equals("FindPartsInRegion3WithWhiteList") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x094e, code lost:
    
        r3 = r31.arg(2);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x095c, code lost:
    
        if (r3.isuserdata() == false) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x095e, code lost:
    
        r9 = r3.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0963, code lost:
    
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0966, code lost:
    
        if ((r9 instanceof com.goydevv.studrblx.lua.Region3V) != false) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0968, code lost:
    
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0969, code lost:
    
        r9 = (com.goydevv.studrblx.lua.Region3V) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x096b, code lost:
    
        if (r9 == null) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x096e, code lost:
    
        r3 = r31.arg(3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0977, code lost:
    
        if (r3.istable() == false) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0979, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r3);
        r4 = instList(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0985, code lost:
    
        if (r3.isuserdata() == false) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0987, code lost:
    
        kotlin.jvm.internal.Intrinsics.checkNotNull(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0990, code lost:
    
        if (r3.isuserdata() == false) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0992, code lost:
    
        r8 = r3.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0997, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x099a, code lost:
    
        if ((r8 instanceof com.goydevv.studrblx.model.Instance) != false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x099c, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x099d, code lost:
    
        r4 = kotlin.collections.CollectionsKt.listOfNotNull((com.goydevv.studrblx.model.Instance) r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x09a4, code lost:
    
        r4 = kotlin.collections.CollectionsKt.emptyList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x09a8, code lost:
    
        r7 = kotlin.jvm.internal.Intrinsics.areEqual(r30, "FindPartsInRegion3WithWhiteList");
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x09b5, code lost:
    
        if (kotlin.text.StringsKt.startsWith(r30, "Is", false) == false) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x09b7, code lost:
    
        r10 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x09bb, code lost:
    
        r10 = kotlin.ranges.RangesKt.coerceAtLeast(r31.arg(4).optint(20), 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x09cb, code lost:
    
        r12 = new java.util.ArrayList();
        r13 = workspaceParts().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x09dc, code lost:
    
        if (r13.hasNext() == false) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x09de, code lost:
    
        r14 = r13.next();
        r15 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x09eb, code lost:
    
        if ((r15 instanceof java.util.Collection) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x09f4, code lost:
    
        if (r15.isEmpty() == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x09f6, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x09f8, code lost:
    
        r11 = r15.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x0a00, code lost:
    
        if (r11.hasNext() == false) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0a02, code lost:
    
        r5 = (com.goydevv.studrblx.model.Instance) r11.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x0a0c, code lost:
    
        if (r14 == r5) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x0a12, code lost:
    
        if (r14.isDescendantOf(r5) != false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0a14, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0a16, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0a17, code lost:
    
        if (r5 == false) goto L381;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x0a19, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0a1d, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0a1f, code lost:
    
        if (r7 == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0a21, code lost:
    
        if (r5 != false) goto L387;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0a23, code lost:
    
        r11 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0a25, code lost:
    
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x0a27, code lost:
    
        r11 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0a28, code lost:
    
        if (r11 != false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x0a2a, code lost:
    
        r11 = com.goydevv.studrblx.engine.Geometry.INSTANCE.partAabb(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0a54, code lost:
    
        if (com.goydevv.studrblx.engine.Geometry.aabbOverlap$default(com.goydevv.studrblx.engine.Geometry.INSTANCE, r11.component1(), r11.component2(), r9.getMin(), r9.getMax(), 0.0f, 16, null) == false) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0a56, code lost:
    
        r12.add(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0a5d, code lost:
    
        if (r12.size() < r10) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x0a6e, code lost:
    
        if (kotlin.text.StringsKt.startsWith(r30, "Is", false) == false) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0a70, code lost:
    
        r5 = org.luaj.vm2.LuaValue.valueOf(r12.isEmpty());
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0a79, code lost:
    
        r5 = listToLua(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0a80, code lost:
        kotlin.jvm.internal.Intrinsics.checkNotNull(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0aaa, code lost:
    
        throw new org.luaj.vm2.LuaError("Region3 expected, got " + com.goydevv.studrblx.lua.LuaTypes.INSTANCE.typeOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:?, code lost:
    
        return raycastResult(r3, r6, filterFrom(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:?, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:?, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:?, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01d4, code lost:
    
        if (r30.equals("IsRegion3EmptyWithIgnoreList") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01de, code lost:
    
        if (r30.equals("SetInsertPoint") == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02c1, code lost:
    
        if (r30.equals("UnjoinFromOutsiders") == false) goto L404;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v12 */
    /* JADX WARN: Type inference failed for: r13v13 */
    /* JADX WARN: Type inference failed for: r13v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v150 */
    /* JADX WARN: Type inference failed for: r3v151 */
    /* JADX WARN: Type inference failed for: r3v152 */
    /* JADX WARN: Type inference failed for: r3v154 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v22 */
    /* JADX WARN: Type inference failed for: r7v16 */
    /* JADX WARN: Type inference failed for: r7v17 */
    /* JADX WARN: Type inference failed for: r7v20 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs workspaceCall(java.lang.String name, org.luaj.vm2.Varargs args) {
        if ("Raycast".equals(name)) {
            Object oObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
            Object dObj = args.arg(3).isuserdata() ? args.arg(3).touserdata() : null;
            if (!(oObj instanceof Vector3) || !(dObj instanceof Vector3)) return LuaValue.NIL;
            Vector3 origin = (Vector3) oObj;
            Vector3 dir = (Vector3) dObj;
            float maxDist = dir.getMagnitude();
            if (maxDist <= 1.0E-5f) return LuaValue.NIL;
            Vector3 unit = dir.div(maxDist);
            Object rpObj = args.arg(4).isuserdata() ? args.arg(4).touserdata() : null;
            final RaycastParamsV rp = rpObj instanceof RaycastParamsV ? (RaycastParamsV) rpObj : null;
            final Function1<Instance, Boolean> tf = filterFrom(args.arg(4));
            com.goydevv.studrblx.engine.RayHit hit = com.goydevv.studrblx.engine.Geometry.INSTANCE.raycast(
                this.rt.getStudio().renderParts(), origin, unit, maxDist,
                new Function1<Instance, Boolean>() {
                    @Override
                    public Boolean invoke(Instance p) {
                        if (rp == null) return tf.invoke(p);
                        boolean inList = false;
                        for (Instance fi : rp.getFilter()) {
                            if (p == fi || p.isDescendantOf(fi)) { inList = true; break; }
                        }
                        return Boolean.valueOf((rp.getFilterType() == 1) ? inList : !inList);
                    }
                }
            );
            if (hit == null) return LuaValue.NIL;
            return LuaTypes.INSTANCE.wrap(new RaycastResultV(new RayHitInfo(hit.getPart(), hit.getPosition(), hit.getNormal(), hit.getT(), PartData.INSTANCE.material(hit.getPart())), this.rt));
        }
        if ("GetPartBoundsInRadius".equals(name)) {
            Object pObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
            if (!(pObj instanceof Vector3)) return new LuaTable();
            Vector3 center = (Vector3) pObj;
            float radius = (float) args.arg(3).optdouble(1.0d);
            LuaTable out = new LuaTable();
            int idx = 1;
            for (Instance p : this.rt.getStudio().renderParts()) {
                CFrame cf = PartData.INSTANCE.cframe(p);
                Vector3 sz = PartData.INSTANCE.size(p);
                float pr = sz.getMagnitude() * 0.5f;
                if (cf.getPosition().minus(center).getMagnitude() <= radius + pr) {
                    out.set(idx++, this.rt.wrap(p));
                }
            }
            return out;
        }
        if ("GetPartBoundsInBox".equals(name)) {
            Object cObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
            Object sObj = args.arg(3).isuserdata() ? args.arg(3).touserdata() : null;
            if (!(cObj instanceof CFrame) || !(sObj instanceof Vector3)) return new LuaTable();
            Vector3 center = ((CFrame) cObj).getPosition();
            Vector3 half = ((Vector3) sObj).div(2.0f);
            LuaTable out = new LuaTable();
            int idx = 1;
            for (Instance p : this.rt.getStudio().renderParts()) {
                Vector3 pc = PartData.INSTANCE.cframe(p).getPosition();
                Vector3 ph = PartData.INSTANCE.size(p).div(2.0f);
                if (Math.abs(pc.getX() - center.getX()) <= half.getX() + ph.getX()
                    && Math.abs(pc.getY() - center.getY()) <= half.getY() + ph.getY()
                    && Math.abs(pc.getZ() - center.getZ()) <= half.getZ() + ph.getZ()) {
                    out.set(idx++, this.rt.wrap(p));
                }
            }
            return out;
        }
        if ("GetPartsInPart".equals(name)) {
            Object pObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
            if (!(pObj instanceof Instance) || !((Instance) pObj).isA("BasePart")) return new LuaTable();
            Instance target = (Instance) pObj;
            Vector3 center = PartData.INSTANCE.cframe(target).getPosition();
            Vector3 half = PartData.INSTANCE.size(target).div(2.0f);
            LuaTable out = new LuaTable();
            int idx = 1;
            for (Instance p : this.rt.getStudio().renderParts()) {
                if (p == target) continue;
                Vector3 pc = PartData.INSTANCE.cframe(p).getPosition();
                Vector3 ph = PartData.INSTANCE.size(p).div(2.0f);
                if (Math.abs(pc.getX() - center.getX()) <= half.getX() + ph.getX()
                    && Math.abs(pc.getY() - center.getY()) <= half.getY() + ph.getY()
                    && Math.abs(pc.getZ() - center.getZ()) <= half.getZ() + ph.getZ()) {
                    out.set(idx++, this.rt.wrap(p));
                }
            }
            return out;
        }
        if ("Spherecast".equals(name) || "Blockcast".equals(name)) {
            Vector3 origin;
            Vector3 dir;
            Object rpObj;
            if ("Spherecast".equals(name)) {
                Object oObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                Object dObj = args.arg(4).isuserdata() ? args.arg(4).touserdata() : null;
                if (!(oObj instanceof Vector3) || !(dObj instanceof Vector3)) return LuaValue.NIL;
                origin = (Vector3) oObj;
                dir = (Vector3) dObj;
                rpObj = args.arg(5).isuserdata() ? args.arg(5).touserdata() : null;
            } else {
                Object cObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                Object dObj = args.arg(4).isuserdata() ? args.arg(4).touserdata() : null;
                if (!(cObj instanceof CFrame) || !(dObj instanceof Vector3)) return LuaValue.NIL;
                origin = ((CFrame) cObj).getPosition();
                dir = (Vector3) dObj;
                rpObj = args.arg(5).isuserdata() ? args.arg(5).touserdata() : null;
            }
            float maxDist = dir.getMagnitude();
            if (maxDist <= 1.0E-5f) return LuaValue.NIL;
            final RaycastParamsV rp = rpObj instanceof RaycastParamsV ? (RaycastParamsV) rpObj : null;
            final Function1<Instance, Boolean> tf = filterFrom(args.arg(4));
            com.goydevv.studrblx.engine.RayHit hit = com.goydevv.studrblx.engine.Geometry.INSTANCE.raycast(
                this.rt.getStudio().renderParts(), origin, dir.div(maxDist), maxDist,
                new Function1<Instance, Boolean>() {
                    @Override
                    public Boolean invoke(Instance p) {
                        if (rp == null) return tf.invoke(p);
                        boolean inList = false;
                        for (Instance fi : rp.getFilter()) {
                            if (p == fi || p.isDescendantOf(fi)) { inList = true; break; }
                        }
                        return Boolean.valueOf((rp.getFilterType() == 1) ? inList : !inList);
                    }
                }
            );
            if (hit == null) return LuaValue.NIL;
            return LuaTypes.INSTANCE.wrap(new RaycastResultV(new RayHitInfo(hit.getPart(), hit.getPosition(), hit.getNormal(), hit.getT(), PartData.INSTANCE.material(hit.getPart())), this.rt));
        }
        if ("BulkMoveTo".equals(name)) {
            if (args.arg(2).istable() && args.arg(3).istable()) {
                LuaTable pts = args.arg(2).checktable();
                LuaTable cfs = args.arg(3).checktable();
                int len = Math.min(pts.length(), cfs.length());
                for (int i = 1; i <= len; i++) {
                    LuaValue pv = pts.get(i);
                    LuaValue cv = cfs.get(i);
                    Object po = pv.isuserdata() ? pv.touserdata() : null;
                    Object co = cv.isuserdata() ? cv.touserdata() : null;
                    if (po instanceof Instance && co instanceof CFrame) {
                        ((Instance) po).set("CFrame", co, false);
                    }
                }
            }
            return LuaValue.NONE;
        }
        if ("GetServerTimeNow".equals(name)) {
            return LuaValue.valueOf(((double) System.currentTimeMillis()) / 1000.0d);
        }
        if ("GetRealPhysicsFPS".equals(name)) {
            return LuaValue.valueOf(60.0d);
        }
        return LuaValue.NIL;
    }

    static final boolean workspaceCall$lambda$52(List $list, boolean $white, Instance p) {
        boolean inList;
        Intrinsics.checkNotNullParameter(p, "p");
        List list = $list;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    Instance instance = (Instance) it.next();
                    if (p == instance || p.isDescendantOf(instance)) {
                        inList = true;
                        break;
                    }
                } else {
                    inList = false;
                    break;
                }
            }
        } else {
            inList = false;
        }
        if ($white) {
            return inList;
        }
        return !inList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:106:?, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getSecond().minus(r0.getFirst()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:?, code lost:
    
        return r0.wrap(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0053, code lost:
    
        if (r14.equals("GetPivot") == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005d, code lost:
    
        if (r14.equals("SetPrimaryPartCFrame") == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00ce, code lost:
    
        if (r14.equals("PivotTo") != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00d0, code lost:
    
        r0 = r15.arg(2);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "arg(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00de, code lost:
    
        if (r0.isuserdata() == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e0, code lost:
    
        r4 = r0.touserdata();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00e5, code lost:
    
        r4 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00e8, code lost:
    
        if ((r4 instanceof com.goydevv.studrblx.model.CFrame) == false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ea, code lost:
    
        r5 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00eb, code lost:
    
        r5 = (com.goydevv.studrblx.model.CFrame) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00ed, code lost:
    
        if (r5 == null) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00ef, code lost:
    
        pivotTo(r13, r5);
        r0 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x011b, code lost:
    
        throw new org.luaj.vm2.LuaError("CFrame expected, got " + com.goydevv.studrblx.lua.LuaTypes.INSTANCE.typeOf(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0212, code lost:
    
        if (r14.equals("GetModelCFrame") == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x021c, code lost:
    
        if (r14.equals("GetModelSize") == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0226, code lost:
    
        if (r14.equals("GetExtentsSize") != false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0228, code lost:
    
        r0 = com.goydevv.studrblx.engine.Geometry.INSTANCE.boundsOf(partsIn(r13));
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0232, code lost:
    
        if (r0 != null) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0234, code lost:
    
        r0 = kotlin.TuplesKt.to(com.goydevv.studrblx.model.Vector3.INSTANCE.getZERO(), com.goydevv.studrblx.model.Vector3.INSTANCE.getZERO());
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x02c1, code lost:
    
        if (r14.equals("GetPrimaryPartCFrame") == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02c4, code lost:
    
        r0 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02cc, code lost:
    
        if (r13.isA("BasePart") == false) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02ce, code lost:
    
        r1 = com.goydevv.studrblx.model.PartData.INSTANCE.cframe(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x02d5, code lost:
    
        r1 = modelPivot(r13);
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs modelCall(com.goydevv.studrblx.model.Instance m, java.lang.String name, org.luaj.vm2.Varargs args) {
        switch (name) {
            case "GetPivot": case "GetPrimaryPartCFrame": {
                Object pp = m.get("PrimaryPart");
                if (pp instanceof Instance && ((Instance) pp).isA("BasePart")) {
                    return LuaTypes.INSTANCE.wrap(PartData.INSTANCE.cframe((Instance) pp));
                }
                for (Instance d : Instance.descendants$default(m, null, 1, null)) {
                    if (d.isA("BasePart")) return LuaTypes.INSTANCE.wrap(PartData.INSTANCE.cframe(d));
                }
                return LuaTypes.INSTANCE.wrap(CFrame.INSTANCE.getIDENTITY());
            }
            case "PivotTo": case "SetPrimaryPartCFrame": case "MoveTo": {
                Object argObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                CFrame target = null;
                if (argObj instanceof CFrame) target = (CFrame) argObj;
                else if (argObj instanceof Vector3) target = CFrame.INSTANCE.at((Vector3) argObj);
                if (target == null) return LuaValue.NIL;
                CFrame cur = CFrame.INSTANCE.getIDENTITY();
                Object pp = m.get("PrimaryPart");
                if (pp instanceof Instance && ((Instance) pp).isA("BasePart")) {
                    cur = PartData.INSTANCE.cframe((Instance) pp);
                } else {
                    for (Instance d : Instance.descendants$default(m, null, 1, null)) {
                        if (d.isA("BasePart")) { cur = PartData.INSTANCE.cframe(d); break; }
                    }
                }
                CFrame delta = target.times(cur.inverse());
                for (Instance d : Instance.descendants$default(m, null, 1, null)) {
                    if (d.isA("BasePart")) {
                        d.set("CFrame", delta.times(PartData.INSTANCE.cframe(d)), true);
                    }
                }
                return LuaValue.NIL;
            }
            default:
                return LuaValue.NIL;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004b, code lost:
    
        if (r9.equals("GetPivot") == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0136, code lost:
    
        if (r9.equals("PivotTo") != false) goto L45;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs partCall(com.goydevv.studrblx.model.Instance p, java.lang.String name, org.luaj.vm2.Varargs args) {
        switch (name) {
            case "GetMass": {
                Vector3 sz = PartData.INSTANCE.size(p);
                return LuaValue.valueOf(sz.getX() * sz.getY() * sz.getZ() * 0.7f);
            }
            case "GetPivot": {
                return LuaTypes.INSTANCE.wrap(PartData.INSTANCE.cframe(p));
            }
            case "PivotTo": {
                Object cfObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                if (cfObj instanceof CFrame) p.set("CFrame", cfObj, true);
                return LuaValue.NIL;
            }
            case "GetTouchingParts": case "GetConnectedParts": {
                return new LuaTable();
            }
            default:
                return LuaValue.NIL;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0299, code lost:
    
        if (r22.equals("ApplyDescription") == false) goto L160;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x030c, code lost:
    
        if (r22.equals("AddAccessory") == false) goto L160;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs humanoidCall(com.goydevv.studrblx.model.Instance h, java.lang.String name, org.luaj.vm2.Varargs args) {
        switch (name) {
            case "LoadAnimation": {
                Object aObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                if (aObj instanceof Instance) {
                    return animationTrack(h, (Instance) aObj);
                }
                return LuaValue.NIL;
            }
            case "TakeDamage": {
                float dmg = (float) args.optdouble(2, 0.0d);
                if (dmg > 0.0f) {
                    Instance chr = h.getParent();
                    if (chr != null && chr.findFirstChildOfClass("ForceField") != null) {
                        return LuaValue.NIL;
                    }
                }
                Object cur = h.get("Health");
                if (cur == null) cur = h.get("Health_XML");
                float hp = cur instanceof Number ? ((Number) cur).floatValue() : 100.0f;
                Object maxObj = h.get("MaxHealth");
                float maxHp = maxObj instanceof Number ? ((Number) maxObj).floatValue() : 100.0f;
                float nextHp = Math.max(0.0f, Math.min(maxHp, hp - dmg));
                h.set(h.getProps().containsKey("Health") ? "Health" : "Health_XML", Float.valueOf(nextHp), true);
                if (nextHp != hp) {
                    this.rt.fire(h, "HealthChanged", LuaValue.valueOf((double) nextHp));
                }
                if (hp > 0.0f && nextHp <= 0.0f) {
                    this.rt.fire(h, "Died");
                }
                return LuaValue.NIL;
            }
            case "MoveTo": {
                Object vObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                PlaySession pl = this.rt.getPlay();
                if (pl != null && vObj instanceof Vector3) {
                    pl.moveTo(h, (Vector3) vObj);
                }
                return LuaValue.NIL;
            }
            case "GetState": {
                Object stObj = h.getAttributes().get("__state");
                String stName = stObj instanceof String ? (String) stObj : "Running";
                return LuaTypes.INSTANCE.enumItem("HumanoidStateType", stName);
            }
            case "ChangeState": {
                LuaValue stArg = args.arg(2);
                String stName = stArg.isuserdata() && stArg.touserdata() instanceof EnumItemV ? ((EnumItemV) stArg.touserdata()).getName() : stArg.optjstring("Running");
                Object prevObj = h.getAttributes().get("__state");
                String prevName = prevObj instanceof String ? (String) prevObj : "Running";
                h.getAttributes().put("__state", stName);
                this.rt.fire(h, "StateChanged", LuaTypes.INSTANCE.enumItem("HumanoidStateType", prevName), LuaTypes.INSTANCE.enumItem("HumanoidStateType", stName));
                return LuaValue.NIL;
            }
            case "EquipTool": {
                Object tObj = args.arg(2).isuserdata() ? args.arg(2).touserdata() : null;
                Instance chr = h.getParent();
                if (tObj instanceof Instance && chr != null) {
                    Instance tool = (Instance) tObj;
                    Instance.setParent$default(tool, chr, 0, 2, null);
                    this.rt.fire(tool, "Equipped");
                }
                return LuaValue.NIL;
            }
            case "UnequipTools": {
                Instance chr = h.getParent();
                PlaySession pl = this.rt.getPlay();
                Instance bp = (pl != null && pl.getPlayer() != null) ? Instance.findFirstChild$default(pl.getPlayer(), "Backpack", false, 2, null) : null;
                if (chr != null && bp != null) {
                    for (Instance ch : new ArrayList<>(chr.getChildren())) {
                        if ("Tool".equals(ch.getClassName())) {
                            Instance.setParent$default(ch, bp, 0, 2, null);
                            this.rt.fire(ch, "Unequipped");
                        }
                    }
                }
                return LuaValue.NIL;
            }
            case "GetPlayingAnimationTracks": {
                return new LuaTable();
            }
            default:
                return LuaValue.NIL;
        }
    }
}
