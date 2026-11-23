package com.goydevv.studrblx.ui;

import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.model.ClassDb;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PropNames;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Autocomplete.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Autocomplete {
    public static final Autocomplete INSTANCE = new Autocomplete();
    private static final List<String> keywords = CollectionsKt.listOf(new String[]{"and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if", "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while", "continue", "export", "type"});
    private static final List<String> globals = CollectionsKt.listOf(new String[]{"game", "workspace", "script", "plugin", "shared", "_G", "print", "warn", "error", "assert", "pcall", "xpcall", "typeof", "type", "tostring", "tonumber", "pairs", "ipairs", "next", "select", "unpack", "require", "setmetatable", "getmetatable", "rawget", "rawset", "rawequal", "rawlen", "wait", "spawn", "delay", "tick", "time", "elapsedTime", "collectgarbage", "gcinfo", "newproxy", "version", "settings", "UserSettings"});
    private static final Map<String, List<String>> libs = MapsKt.mapOf(TuplesKt.to("math", CollectionsKt.listOf(new String[]{"abs", "acos", "asin", "atan", "atan2", "ceil", "clamp", "cos", "cosh", "deg", "exp", "floor", "fmod", "frexp", "huge", "ldexp", "log", "log10", "max", "min", "modf", "noise", "pi", "pow", "rad", "random", "randomseed", "round", "sign", "sin", "sinh", "sqrt", "tan", "tanh", "lerp", "map"})), TuplesKt.to("string", CollectionsKt.listOf(new String[]{"byte", "char", "find", "format", "gmatch", "gsub", "len", "lower", "match", "rep", "reverse", "split", "sub", "upper", "pack", "packsize", "unpack"})), TuplesKt.to("table", CollectionsKt.listOf(new String[]{"clear", "clone", "concat", "create", "find", "freeze", "insert", "isfrozen", "maxn", "move", "pack", "remove", "sort", "unpack"})), TuplesKt.to("task", CollectionsKt.listOf(new String[]{"wait", "spawn", "defer", "delay", "cancel", "synchronize", "desynchronize"})), TuplesKt.to("coroutine", CollectionsKt.listOf(new String[]{"create", "resume", "running", "status", "wrap", "yield", "isyieldable", "close"})), TuplesKt.to("os", CollectionsKt.listOf(new String[]{"time", "clock", "date", "difftime"})), TuplesKt.to("bit32", CollectionsKt.listOf(new String[]{"band", "bor", "bxor", "bnot", "lshift", "rshift", "arshift", "btest", "extract", "replace", "lrotate", "rrotate", "countlz", "countrz"})), TuplesKt.to("utf8", CollectionsKt.listOf(new String[]{"char", "charpattern", "codepoint", "codes", "len", "offset"})), TuplesKt.to("debug", CollectionsKt.listOf(new String[]{"traceback", "info", "profilebegin", "profileend"})), TuplesKt.to("buffer", CollectionsKt.listOf(new String[]{"create", "fromstring", "tostring", "len", "readi8", "readu8", "readi32", "readu32", "readf32", "readf64", "writei8", "writeu8", "writei32", "writeu32", "writef32", "writef64", "readstring", "writestring", "copy", "fill"})));
    private static final Map<String, List<String>> dataStatics = MapsKt.mapOf(TuplesKt.to("Vector3", CollectionsKt.listOf(new String[]{"new", "zero", "one", "xAxis", "yAxis", "zAxis", "FromNormalId", "FromAxis"})), TuplesKt.to("Vector2", CollectionsKt.listOf(new String[]{"new", "zero", "one", "xAxis", "yAxis"})), TuplesKt.to("CFrame", CollectionsKt.listOf(new String[]{"new", "Angles", "fromEulerAnglesXYZ", "fromEulerAnglesYXZ", "fromOrientation", "fromAxisAngle", "lookAt", "fromMatrix", "identity", "lookAlong"})), TuplesKt.to("Color3", CollectionsKt.listOf(new String[]{"new", "fromRGB", "fromHSV", "fromHex"})), TuplesKt.to("UDim2", CollectionsKt.listOf(new String[]{"new", "fromScale", "fromOffset"})), TuplesKt.to("UDim", CollectionsKt.listOf("new")), TuplesKt.to("BrickColor", CollectionsKt.listOf(new String[]{"new", "random", "palette", "White", "Black", "Red", "Blue", "Green", "Yellow", "Gray", "DarkGray"})), TuplesKt.to("Instance", CollectionsKt.listOf(new String[]{"new", "fromExisting"})), TuplesKt.to("TweenInfo", CollectionsKt.listOf("new")), TuplesKt.to("Ray", CollectionsKt.listOf("new")), TuplesKt.to("NumberRange", CollectionsKt.listOf("new")), TuplesKt.to("NumberSequence", CollectionsKt.listOf("new")), TuplesKt.to("ColorSequence", CollectionsKt.listOf("new")), TuplesKt.to("NumberSequenceKeypoint", CollectionsKt.listOf("new")), TuplesKt.to("ColorSequenceKeypoint", CollectionsKt.listOf("new")), TuplesKt.to("Random", CollectionsKt.listOf("new")), TuplesKt.to("Rect", CollectionsKt.listOf("new")), TuplesKt.to("Region3", CollectionsKt.listOf("new")), TuplesKt.to("RaycastParams", CollectionsKt.listOf("new")), TuplesKt.to("OverlapParams", CollectionsKt.listOf("new")), TuplesKt.to("DateTime", CollectionsKt.listOf(new String[]{"now", "fromUnixTimestamp", "fromUnixTimestampMillis", "fromIsoDate", "fromLocalTime", "fromUniversalTime"})), TuplesKt.to("Font", CollectionsKt.listOf(new String[]{"new", "fromEnum", "fromName", "fromId"})), TuplesKt.to("PhysicalProperties", CollectionsKt.listOf("new")), TuplesKt.to("Axes", CollectionsKt.listOf("new")), TuplesKt.to("Faces", CollectionsKt.listOf("new")));
    private static final Map<String, Pair<List<String>, List<String>>> dataMembers = MapsKt.mapOf(TuplesKt.to("Vector3", TuplesKt.to(CollectionsKt.listOf(new String[]{"X", "Y", "Z", "Magnitude", "Unit"}), CollectionsKt.listOf(new String[]{"Dot", "Cross", "Lerp", "FuzzyEq", "Abs", "Ceil", "Floor", "Sign", "Angle", "Max", "Min"}))), TuplesKt.to("Vector2", TuplesKt.to(CollectionsKt.listOf(new String[]{"X", "Y", "Magnitude", "Unit"}), CollectionsKt.listOf(new String[]{"Dot", "Cross", "Lerp", "FuzzyEq", "Abs", "Ceil", "Floor", "Sign", "Angle", "Max", "Min"}))), TuplesKt.to("CFrame", TuplesKt.to(CollectionsKt.listOf(new String[]{"Position", "LookVector", "RightVector", "UpVector", "XVector", "YVector", "ZVector", "X", "Y", "Z", "Rotation"}), CollectionsKt.listOf(new String[]{"Inverse", "Lerp", "Orthonormalize", "ToWorldSpace", "ToObjectSpace", "PointToWorldSpace", "PointToObjectSpace", "VectorToWorldSpace", "VectorToObjectSpace", "GetComponents", "ToEulerAnglesXYZ", "ToEulerAnglesYXZ", "ToOrientation", "ToAxisAngle", "FuzzyEq"}))), TuplesKt.to("Color3", TuplesKt.to(CollectionsKt.listOf(new String[]{"R", "G", "B"}), CollectionsKt.listOf(new String[]{"Lerp", "ToHSV", "ToHex"}))), TuplesKt.to("UDim2", TuplesKt.to(CollectionsKt.listOf(new String[]{"X", "Y", "Width", "Height"}), CollectionsKt.listOf("Lerp"))), TuplesKt.to("UDim", TuplesKt.to(CollectionsKt.listOf(new String[]{"Scale", "Offset"}), CollectionsKt.emptyList())), TuplesKt.to("BrickColor", TuplesKt.to(CollectionsKt.listOf(new String[]{"Name", "Number", "Color", "r", "g", "b"}), CollectionsKt.emptyList())), TuplesKt.to("Random", TuplesKt.to(CollectionsKt.emptyList(), CollectionsKt.listOf(new String[]{"NextNumber", "NextInteger", "NextUnitVector", "Shuffle", "Clone"}))), TuplesKt.to("RaycastParams", TuplesKt.to(CollectionsKt.listOf(new String[]{"FilterDescendantsInstances", "FilterType", "IgnoreWater", "CollisionGroup", "RespectCanCollide", "BruteForceAllSlow"}), CollectionsKt.listOf("AddToFilter"))), TuplesKt.to("OverlapParams", TuplesKt.to(CollectionsKt.listOf(new String[]{"FilterDescendantsInstances", "FilterType", "MaxParts", "CollisionGroup", "RespectCanCollide"}), CollectionsKt.listOf("AddToFilter"))), TuplesKt.to("Ray", TuplesKt.to(CollectionsKt.listOf(new String[]{"Origin", "Direction", "Unit"}), CollectionsKt.listOf(new String[]{"ClosestPoint", "Distance"}))), TuplesKt.to("TweenInfo", TuplesKt.to(CollectionsKt.listOf(new String[]{"Time", "EasingStyle", "EasingDirection", "RepeatCount", "Reverses", "DelayTime"}), CollectionsKt.emptyList())), TuplesKt.to("RaycastResult", TuplesKt.to(CollectionsKt.listOf(new String[]{"Instance", "Position", "Normal", "Material", "Distance"}), CollectionsKt.emptyList())), TuplesKt.to("Tween", TuplesKt.to(CollectionsKt.listOf(new String[]{"PlaybackState", "TweenInfo", "Instance"}), CollectionsKt.listOf(new String[]{"Play", "Pause", "Cancel"}))), TuplesKt.to("RBXScriptSignal", TuplesKt.to(CollectionsKt.emptyList(), CollectionsKt.listOf(new String[]{"Connect", "Once", "Wait", "ConnectParallel"}))), TuplesKt.to("RBXScriptConnection", TuplesKt.to(CollectionsKt.listOf("Connected"), CollectionsKt.listOf("Disconnect"))), TuplesKt.to("AnimationTrack", TuplesKt.to(CollectionsKt.listOf(new String[]{"IsPlaying", "Length", "Looped", "Priority", "Speed", "TimePosition", "WeightCurrent", "Animation"}), CollectionsKt.listOf(new String[]{"Play", "Stop", "AdjustSpeed", "AdjustWeight", "GetMarkerReachedSignal", "GetTimeOfKeyframe"}))), TuplesKt.to("DataStore", TuplesKt.to(CollectionsKt.emptyList(), CollectionsKt.listOf(new String[]{"GetAsync", "SetAsync", "UpdateAsync", "RemoveAsync", "IncrementAsync", "GetSortedAsync", "ListKeysAsync"}))), TuplesKt.to("Path", TuplesKt.to(CollectionsKt.listOf("Status"), CollectionsKt.listOf(new String[]{"ComputeAsync", "GetWaypoints", "CheckOcclusionAsync"}))), TuplesKt.to("Mouse", TuplesKt.to(CollectionsKt.listOf(new String[]{"Hit", "Target", "X", "Y", "Origin", "UnitRay", "Icon", "TargetFilter", "ViewSizeX", "ViewSizeY"}), CollectionsKt.emptyList())), TuplesKt.to("InputObject", TuplesKt.to(CollectionsKt.listOf(new String[]{"UserInputType", "UserInputState", "KeyCode", "Position", "Delta"}), CollectionsKt.listOf("IsModifierKeyDown"))));
    private static final Map<String, Members> classMembers = MapsKt.mapOf(TuplesKt.to("Instance", INSTANCE.m("Name Parent ClassName Archivable", "FindFirstChild WaitForChild GetChildren GetDescendants Destroy Clone IsA FindFirstChildOfClass FindFirstChildWhichIsA FindFirstAncestor FindFirstAncestorOfClass FindFirstAncestorWhichIsA FindFirstDescendant GetAttribute SetAttribute GetAttributes GetAttributeChangedSignal GetPropertyChangedSignal ClearAllChildren GetFullName IsDescendantOf IsAncestorOf AddTag RemoveTag HasTag GetTags", "Changed ChildAdded ChildRemoved DescendantAdded DescendantRemoving AncestryChanged Destroying AttributeChanged")), TuplesKt.to("PVInstance", m$default(INSTANCE, "", "GetPivot PivotTo", null, 4, null)), TuplesKt.to("BasePart", INSTANCE.m("Position CFrame Size Orientation Rotation Anchored CanCollide CanTouch CanQuery Transparency Color BrickColor Material MaterialVariant Reflectance Locked Massless CastShadow CollisionGroup AssemblyLinearVelocity AssemblyAngularVelocity AssemblyMass AssemblyRootPart TopSurface BottomSurface FrontSurface BackSurface LeftSurface RightSurface", "GetMass ApplyImpulse ApplyAngularImpulse GetTouchingParts GetConnectedParts GetJoints BreakJoints SetNetworkOwner GetNetworkOwner GetRootPart GetVelocityAtPosition CanSetNetworkOwnership", "Touched TouchEnded")), TuplesKt.to("Part", m$default(INSTANCE, "Shape", null, null, 6, null)), TuplesKt.to("MeshPart", m$default(INSTANCE, "MeshId TextureID DoubleSided", null, null, 6, null)), TuplesKt.to("SpawnLocation", m$default(INSTANCE, "Enabled Neutral TeamColor Duration AllowTeamChangeOnTouch", null, null, 6, null)), TuplesKt.to("Seat", m$default(INSTANCE, "Disabled Occupant", "Sit", null, 4, null)), TuplesKt.to("VehicleSeat", m$default(INSTANCE, "Disabled Occupant MaxSpeed Torque Throttle Steer TurnSpeed", "Sit", null, 4, null)), TuplesKt.to("Model", m$default(INSTANCE, "PrimaryPart WorldPivot", "MoveTo GetBoundingBox GetExtentsSize ScaleTo GetScale TranslateBy SetPrimaryPartCFrame GetPrimaryPartCFrame", null, 4, null)), TuplesKt.to("Workspace", m$default(INSTANCE, "Gravity CurrentCamera FallenPartsDestroyHeight DistributedGameTime Terrain StreamingEnabled", "Raycast Blockcast Spherecast Shapecast GetPartBoundsInBox GetPartBoundsInRadius GetPartsInPart GetServerTimeNow GetRealPhysicsFPS FindPartOnRay", null, 4, null)), TuplesKt.to("Humanoid", INSTANCE.m("Health MaxHealth WalkSpeed JumpPower JumpHeight UseJumpPower HipHeight AutoRotate Sit Jump MoveDirection RootPart SeatPart DisplayName RigType FloorMaterial PlatformStand WalkToPoint NameDisplayDistance HealthDisplayType BreakJointsOnDeath", "TakeDamage MoveTo Move ChangeState GetState LoadAnimation EquipTool UnequipTools GetPlayingAnimationTracks SetStateEnabled GetStateEnabled ApplyDescription GetAppliedDescription AddAccessory GetAccessories", "Died HealthChanged Running Jumping StateChanged MoveToFinished FreeFalling Seated Climbing Touched")), TuplesKt.to("Players", INSTANCE.m("LocalPlayer MaxPlayers RespawnTime CharacterAutoLoads", "GetPlayers GetPlayerFromCharacter GetPlayerByUserId GetUserIdFromNameAsync GetNameFromUserIdAsync GetUserThumbnailAsync GetHumanoidDescriptionFromUserId", "PlayerAdded PlayerRemoving")), TuplesKt.to("Player", INSTANCE.m("Character UserId DisplayName Team TeamColor Neutral AccountAge MembershipType CameraMaxZoomDistance CameraMinZoomDistance CameraMode RespawnLocation PlayerGui Backpack PlayerScripts", "Kick LoadCharacter GetMouse IsInGroup GetRankInGroup GetRoleInGroup DistanceFromCharacter GetNetworkPing", "CharacterAdded CharacterRemoving CharacterAppearanceLoaded Chatted Idled")), TuplesKt.to("GuiObject", INSTANCE.m("Position Size AnchorPoint BackgroundColor3 BackgroundTransparency BorderColor3 BorderSizePixel Visible ZIndex LayoutOrder Rotation AbsolutePosition AbsoluteSize ClipsDescendants Active AutomaticSize SizeConstraint Selectable Interactable", "TweenPosition TweenSize TweenSizeAndPosition", "MouseEnter MouseLeave InputBegan InputEnded InputChanged")), TuplesKt.to("GuiButton", INSTANCE.m("AutoButtonColor Modal Selected", "", "MouseButton1Click MouseButton1Down MouseButton1Up MouseButton2Click Activated")), TuplesKt.to("TextLabel", m$default(INSTANCE, "Text TextColor3 TextSize TextScaled TextWrapped Font FontFace RichText TextXAlignment TextYAlignment TextTransparency TextStrokeColor3 TextStrokeTransparency TextTruncate LineHeight MaxVisibleGraphemes TextBounds", null, null, 6, null)), TuplesKt.to("TextButton", m$default(INSTANCE, "Text TextColor3 TextSize TextScaled TextWrapped Font FontFace RichText TextXAlignment TextYAlignment TextTransparency TextStrokeColor3 TextStrokeTransparency", null, null, 6, null)), TuplesKt.to("TextBox", INSTANCE.m("Text PlaceholderText PlaceholderColor3 ClearTextOnFocus MultiLine TextEditable TextColor3 TextSize TextScaled Font FontFace", "CaptureFocus ReleaseFocus IsFocused", "FocusLost Focused ReturnPressedFromOnScreenKeyboard")), TuplesKt.to("ImageLabel", m$default(INSTANCE, "Image ImageColor3 ImageTransparency ScaleType SliceCenter TileSize ImageRectOffset ImageRectSize ResampleMode", null, null, 6, null)), TuplesKt.to("ImageButton", m$default(INSTANCE, "Image ImageColor3 ImageTransparency HoverImage PressedImage ScaleType SliceCenter", null, null, 6, null)), TuplesKt.to("ScrollingFrame", m$default(INSTANCE, "CanvasSize CanvasPosition AutomaticCanvasSize ScrollBarThickness ScrollingEnabled ScrollingDirection ScrollBarImageColor3 ElasticBehavior", null, null, 6, null)), TuplesKt.to("ScreenGui", m$default(INSTANCE, "Enabled ResetOnSpawn IgnoreGuiInset DisplayOrder ZIndexBehavior ScreenInsets", null, null, 6, null)), TuplesKt.to("BillboardGui", m$default(INSTANCE, "Adornee Size StudsOffset StudsOffsetWorldSpace MaxDistance AlwaysOnTop Enabled LightInfluence", null, null, 6, null)), TuplesKt.to("SurfaceGui", m$default(INSTANCE, "Adornee Face CanvasSize PixelsPerStud SizingMode AlwaysOnTop LightInfluence Enabled", null, null, 6, null)), TuplesKt.to("UICorner", m$default(INSTANCE, "CornerRadius", null, null, 6, null)), TuplesKt.to("UIStroke", m$default(INSTANCE, "Color Thickness Transparency ApplyStrokeMode LineJoinMode Enabled", null, null, 6, null)), TuplesKt.to("UIListLayout", m$default(INSTANCE, "FillDirection HorizontalAlignment VerticalAlignment Padding SortOrder Wraps", null, null, 6, null)), TuplesKt.to("UIGridLayout", m$default(INSTANCE, "CellSize CellPadding FillDirection SortOrder FillDirectionMaxCells", null, null, 6, null)), TuplesKt.to("UIPadding", m$default(INSTANCE, "PaddingTop PaddingBottom PaddingLeft PaddingRight", null, null, 6, null)), TuplesKt.to("UIGradient", m$default(INSTANCE, "Color Transparency Rotation Offset Enabled", null, null, 6, null)), TuplesKt.to("UIScale", m$default(INSTANCE, "Scale", null, null, 6, null)), TuplesKt.to("RunService", INSTANCE.m("", "IsStudio IsServer IsClient IsRunning IsEdit IsRunMode BindToRenderStep UnbindFromRenderStep", "Heartbeat RenderStepped Stepped PreSimulation PostSimulation PreRender PreAnimation")), TuplesKt.to("TweenService", m$default(INSTANCE, "", "Create GetValue", null, 4, null)), TuplesKt.to("UserInputService", INSTANCE.m("TouchEnabled KeyboardEnabled MouseEnabled GamepadEnabled MouseBehavior MouseIconEnabled ModalEnabled", "IsKeyDown IsMouseButtonPressed GetMouseLocation GetFocusedTextBox GetKeysPressed GetLastInputType GetConnectedGamepads", "InputBegan InputEnded InputChanged TouchTap TouchStarted TouchEnded TouchMoved JumpRequest LastInputTypeChanged TouchTapInWorld")), TuplesKt.to("ContextActionService", m$default(INSTANCE, "", "BindAction BindActionAtPriority UnbindAction UnbindAllActions SetTitle SetImage SetPosition SetDescription GetButton", null, 4, null)), TuplesKt.to("DataStoreService", m$default(INSTANCE, "", "GetDataStore GetOrderedDataStore GetGlobalDataStore", null, 4, null)), TuplesKt.to("HttpService", m$default(INSTANCE, "HttpEnabled", "JSONEncode JSONDecode GenerateGUID GetAsync PostAsync RequestAsync UrlEncode", null, 4, null)), TuplesKt.to("MarketplaceService", INSTANCE.m("ProcessReceipt", "PromptPurchase PromptGamePassPurchase PromptProductPurchase UserOwnsGamePassAsync PlayerOwnsAsset GetProductInfo", "PromptGamePassPurchaseFinished PromptPurchaseFinished PromptProductPurchaseFinished")), TuplesKt.to("CollectionService", m$default(INSTANCE, "", "GetTagged AddTag RemoveTag HasTag GetTags GetAllTags GetInstanceAddedSignal GetInstanceRemovedSignal", null, 4, null)), TuplesKt.to("Debris", m$default(INSTANCE, "", "AddItem", null, 4, null)), TuplesKt.to("PathfindingService", m$default(INSTANCE, "", "CreatePath FindPathAsync", null, 4, null)), TuplesKt.to("StarterGui", m$default(INSTANCE, "ShowDevelopmentGui", "SetCore GetCore SetCoreGuiEnabled GetCoreGuiEnabled", null, 4, null)), TuplesKt.to("Lighting", m$default(INSTANCE, "ClockTime TimeOfDay Brightness Ambient OutdoorAmbient FogEnd FogStart FogColor GlobalShadows ExposureCompensation GeographicLatitude Technology", "GetMinutesAfterMidnight SetMinutesAfterMidnight GetSunDirection GetMoonDirection", null, 4, null)), TuplesKt.to("Camera", m$default(INSTANCE, "CFrame Focus FieldOfView CameraType CameraSubject ViewportSize NearPlaneZ", "WorldToScreenPoint WorldToViewportPoint ScreenPointToRay ViewportPointToRay GetPartsObscuringTarget", null, 4, null)), TuplesKt.to("Tool", INSTANCE.m("Handle RequiresHandle CanBeDropped ToolTip Grip Enabled ManualActivationOnly TextureId", "Activate Deactivate", "Activated Deactivated Equipped Unequipped")), TuplesKt.to("ValueBase", INSTANCE.m("Value", "", "Changed")), TuplesKt.to("Sound", INSTANCE.m("SoundId Volume Looped Playing PlaybackSpeed TimePosition TimeLength IsPlaying RollOffMaxDistance SoundGroup", "Play Stop Pause Resume", "Ended Played Paused Resumed Loaded DidLoop")), TuplesKt.to("ClickDetector", INSTANCE.m("MaxActivationDistance CursorIcon", "", "MouseClick RightMouseClick MouseHoverEnter MouseHoverLeave")), TuplesKt.to("ProximityPrompt", INSTANCE.m("ActionText ObjectText HoldDuration MaxActivationDistance KeyboardKeyCode GamepadKeyCode Enabled RequiresLineOfSight Style", "InputHoldBegin InputHoldEnd", "Triggered TriggerEnded PromptShown PromptHidden PromptButtonHoldBegan PromptButtonHoldEnded")), TuplesKt.to("RemoteEvent", INSTANCE.m("", "FireServer FireClient FireAllClients", "OnServerEvent OnClientEvent")), TuplesKt.to("UnreliableRemoteEvent", INSTANCE.m("", "FireServer FireClient FireAllClients", "OnServerEvent OnClientEvent")), TuplesKt.to("RemoteFunction", m$default(INSTANCE, "OnServerInvoke OnClientInvoke", "InvokeServer InvokeClient", null, 4, null)), TuplesKt.to("BindableEvent", INSTANCE.m("", "Fire", "Event")), TuplesKt.to("BindableFunction", m$default(INSTANCE, "OnInvoke", "Invoke", null, 4, null)), TuplesKt.to("Animator", m$default(INSTANCE, "", "LoadAnimation GetPlayingAnimationTracks", null, 4, null)), TuplesKt.to("Animation", m$default(INSTANCE, "AnimationId", null, null, 6, null)), TuplesKt.to("Teams", m$default(INSTANCE, "", "GetTeams", null, 4, null)), TuplesKt.to("Team", INSTANCE.m("TeamColor AutoAssignable", "GetPlayers", "PlayerAdded PlayerRemoved")), TuplesKt.to("TextChatService", INSTANCE.m("ChatVersion", "", "MessageReceived OnIncomingMessage SendingMessage")), TuplesKt.to("TextChannel", INSTANCE.m("", "SendAsync DisplaySystemMessage", "MessageReceived")), TuplesKt.to("Light", m$default(INSTANCE, "Brightness Color Enabled Range Shadows", null, null, 6, null)), TuplesKt.to("SpotLight", m$default(INSTANCE, "Angle Face", null, null, 6, null)), TuplesKt.to("ParticleEmitter", m$default(INSTANCE, "Rate Enabled Lifetime Speed Color Size Transparency Texture SpreadAngle Acceleration Drag LightEmission", "Emit Clear", null, 4, null)), TuplesKt.to("Attachment", m$default(INSTANCE, "CFrame Position Orientation WorldCFrame WorldPosition Axis SecondaryAxis Visible", null, null, 6, null)), TuplesKt.to("Weld", m$default(INSTANCE, "Part0 Part1 C0 C1 Enabled", null, null, 6, null)), TuplesKt.to("Motor6D", m$default(INSTANCE, "Part0 Part1 C0 C1 Transform CurrentAngle DesiredAngle MaxVelocity", null, null, 6, null)), TuplesKt.to("WeldConstraint", m$default(INSTANCE, "Part0 Part1 Enabled", null, null, 6, null)), TuplesKt.to("Decal", m$default(INSTANCE, "Texture Color3 Transparency Face ZIndex", null, null, 6, null)), TuplesKt.to("Texture", m$default(INSTANCE, "StudsPerTileU StudsPerTileV OffsetStudsU OffsetStudsV", null, null, 6, null)), TuplesKt.to("SpecialMesh", m$default(INSTANCE, "MeshType MeshId TextureId Scale Offset VertexColor", null, null, 6, null)), TuplesKt.to("Highlight", m$default(INSTANCE, "Adornee FillColor FillTransparency OutlineColor OutlineTransparency DepthMode Enabled", null, null, 6, null)), TuplesKt.to("BadgeService", m$default(INSTANCE, "", "AwardBadge UserHasBadgeAsync GetBadgeInfoAsync", null, 4, null)), TuplesKt.to("TeleportService", m$default(INSTANCE, "", "Teleport TeleportAsync TeleportToPlaceInstance", null, 4, null)), TuplesKt.to("SoundService", m$default(INSTANCE, "AmbientReverb RespectFilteringEnabled", "PlayLocalSound", null, 4, null)), TuplesKt.to("PlayerGui", m$default(INSTANCE, "ScreenOrientation SelectionImageObject", "SetTopbarTransparency GetGuiObjectsAtPosition", null, 4, null)), TuplesKt.to("Backpack", m$default(INSTANCE, "", null, null, 6, null)), TuplesKt.to("StarterPlayer", m$default(INSTANCE, "CameraMaxZoomDistance CameraMinZoomDistance CameraMode EnableMouseLockOption CharacterWalkSpeed CharacterJumpPower CharacterUseJumpPower DevTouchMovementMode", null, null, 6, null)), TuplesKt.to("GuiService", m$default(INSTANCE, "MenuIsOpen", "GetGuiInset IsTenFootInterface", null, 4, null)));
    private static final Map<String, String> propTypes = MapsKt.mapOf(TuplesKt.to("LocalPlayer", "Player"), TuplesKt.to("Character", "Model"), TuplesKt.to("PlayerGui", "PlayerGui"), TuplesKt.to("Backpack", "Backpack"), TuplesKt.to("PlayerScripts", "PlayerScripts"), TuplesKt.to("CurrentCamera", "Camera"), TuplesKt.to("Terrain", "Terrain"), TuplesKt.to("PrimaryPart", "BasePart"), TuplesKt.to("RootPart", "BasePart"), TuplesKt.to("Humanoid", "Humanoid"), TuplesKt.to("HumanoidRootPart", "BasePart"), TuplesKt.to("Head", "BasePart"), TuplesKt.to("Torso", "BasePart"), TuplesKt.to("Handle", "BasePart"), TuplesKt.to("Adornee", "Instance"), TuplesKt.to("Team", "Team"), TuplesKt.to("Position", "#Vector3"), TuplesKt.to("Size", "#Vector3"), TuplesKt.to("Orientation", "#Vector3"), TuplesKt.to("CFrame", "#CFrame"), TuplesKt.to("Color", "#Color3"), TuplesKt.to("Velocity", "#Vector3"), TuplesKt.to("AssemblyLinearVelocity", "#Vector3"), TuplesKt.to("LookVector", "#Vector3"), TuplesKt.to("RightVector", "#Vector3"), TuplesKt.to("UpVector", "#Vector3"), TuplesKt.to("Unit", "#Vector3"), TuplesKt.to("BackgroundColor3", "#Color3"), TuplesKt.to("TextColor3", "#Color3"), TuplesKt.to("AbsolutePosition", "#Vector2"), TuplesKt.to("AbsoluteSize", "#Vector2"), TuplesKt.to("MoveDirection", "#Vector3"), TuplesKt.to("Focus", "#CFrame"), TuplesKt.to("WorldPivot", "#CFrame"), TuplesKt.to("C0", "#CFrame"), TuplesKt.to("C1", "#CFrame"), TuplesKt.to("BrickColor", "#BrickColor"), TuplesKt.to("Hit", "#CFrame"), TuplesKt.to("Target", "BasePart"), TuplesKt.to("Occupant", "Humanoid"), TuplesKt.to("SeatPart", "BasePart"), TuplesKt.to("Parent", "Instance"));
    private static final Map<String, String> methodReturns = MapsKt.mapOf(TuplesKt.to("GetMouse", "#Mouse"), TuplesKt.to("LoadAnimation", "#AnimationTrack"), TuplesKt.to("Create", "#Tween"), TuplesKt.to("GetDataStore", "#DataStore"), TuplesKt.to("GetOrderedDataStore", "#DataStore"), TuplesKt.to("CreatePath", "#Path"), TuplesKt.to("Raycast", "#RaycastResult"), TuplesKt.to("GetPivot", "#CFrame"), TuplesKt.to("Clone", "@self"), TuplesKt.to("GetPlayerFromCharacter", "Player"), TuplesKt.to("FindFirstAncestorOfClass", "@arg"), TuplesKt.to("FindFirstChildOfClass", "@arg"), TuplesKt.to("FindFirstChildWhichIsA", "@arg"), TuplesKt.to("FindFirstAncestorWhichIsA", "@arg"), TuplesKt.to("GetPropertyChangedSignal", "#RBXScriptSignal"), TuplesKt.to("GetAttributeChangedSignal", "#RBXScriptSignal"), TuplesKt.to("GetInstanceAddedSignal", "#RBXScriptSignal"), TuplesKt.to("Connect", "#RBXScriptConnection"), TuplesKt.to("Once", "#RBXScriptConnection"));
    private static final Map<String, String> nameHints = MapsKt.mapOf(TuplesKt.to("player", "Player"), TuplesKt.to("plr", "Player"), TuplesKt.to("localplayer", "Player"), TuplesKt.to("character", "Model"), TuplesKt.to("char", "Model"), TuplesKt.to("humanoid", "Humanoid"), TuplesKt.to("hum", "Humanoid"), TuplesKt.to("hit", "BasePart"), TuplesKt.to("part", "BasePart"), TuplesKt.to("otherpart", "BasePart"), TuplesKt.to("root", "BasePart"), TuplesKt.to("hrp", "BasePart"), TuplesKt.to("rootpart", "BasePart"), TuplesKt.to("tool", "Tool"), TuplesKt.to("camera", "Camera"), TuplesKt.to("cam", "Camera"), TuplesKt.to("mouse", "#Mouse"), TuplesKt.to("input", "#InputObject"), TuplesKt.to("gui", "ScreenGui"), TuplesKt.to("button", "TextButton"), TuplesKt.to("frame", "Frame"), TuplesKt.to("label", "TextLabel"), TuplesKt.to("textbox", "TextBox"), TuplesKt.to("sound", "Sound"), TuplesKt.to("remote", "RemoteEvent"), TuplesKt.to("event", "RemoteEvent"), TuplesKt.to("animator", "Animator"), TuplesKt.to("track", "#AnimationTrack"), TuplesKt.to("tween", "#Tween"), TuplesKt.to("model", "Model"), TuplesKt.to("folder", "Folder"), TuplesKt.to("leaderstats", "Folder"));
    private static final List<String> extraEnums = CollectionsKt.listOf(new String[]{"KeyCode", "UserInputType", "Material", "EasingStyle", "EasingDirection", "HumanoidStateType", "PartType", "SurfaceType", "Font", "NormalId", "CameraType", "RaycastFilterType", "CoreGuiType", "SortOrder", "FillDirection", "HorizontalAlignment", "VerticalAlignment", "TextXAlignment", "TextYAlignment", "AutomaticSize", "ScaleType", "ZIndexBehavior", "PlaybackState", "AnimationPriority", "ContextActionResult"});

    /* JADX INFO: compiled from: Autocomplete.kt */
    public enum Kind {
        KEYWORD("kw", -496260),
        GLOBAL("g", -8071433),
        LIBRARY("lib", -8071433),
        FUNCTION("fn", -132195),
        METHOD("m", -132195),
        PROPERTY("p", -6497026),
        EVENT("ev", -14848),
        CLASS("c", -11613776),
        ENUM("e", -4862296),
        SERVICE("s", -11613776),
        VARIABLE("v", -3355444),
        CHILD("ch", -2302806);

        private final String badge;
        private final int color;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Kind> getEntries() {
            return $ENTRIES;
        }

        Kind(String badge, int color) {
            this.badge = badge;
            this.color = color;
        }

        public final String getBadge() {
            return this.badge;
        }

        public final int getColor() {
            return this.color;
        }
    }

    private Autocomplete() {
    }

    /* JADX INFO: compiled from: Autocomplete.kt */
    public static final class Item {
        private final String detail;
        private final String insert;
        private final Kind kind;
        private final String label;

        public Item(String label, Kind kind, String detail, String insert) {
            Intrinsics.checkNotNullParameter(label, "label");
            Intrinsics.checkNotNullParameter(kind, "kind");
            Intrinsics.checkNotNullParameter(detail, "detail");
            Intrinsics.checkNotNullParameter(insert, "insert");
            this.label = label;
            this.kind = kind;
            this.detail = detail;
            this.insert = insert;
        }

        public /* synthetic */ Item(String str, Kind kind, String str2, String str3, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, kind, (i & 4) != 0 ? "" : str2, (i & 8) != 0 ? str : str3);
        }

        public final String getDetail() {
            return this.detail;
        }

        public final String getInsert() {
            return this.insert;
        }

        public final Kind getKind() {
            return this.kind;
        }

        public final String getLabel() {
            return this.label;
        }
    }

    /* JADX INFO: compiled from: Autocomplete.kt */
    public static final class Result {
        private final int end;
        private final List<Item> items;
        private final String prefix;
        private final int start;

        public Result(int start, int end, String prefix, List<Item> items) {
            Intrinsics.checkNotNullParameter(prefix, "prefix");
            Intrinsics.checkNotNullParameter(items, "items");
            this.start = start;
            this.end = end;
            this.prefix = prefix;
            this.items = items;
        }

        public final int getEnd() {
            return this.end;
        }

        public final List<Item> getItems() {
            return this.items;
        }

        public final String getPrefix() {
            return this.prefix;
        }

        public final int getStart() {
            return this.start;
        }
    }

    /* JADX INFO: compiled from: Autocomplete.kt */
    private static final class Members {
        private final List<String> events;
        private final List<String> methods;
        private final List<String> props;

        public Members(List<String> props, List<String> methods, List<String> events) {
            Intrinsics.checkNotNullParameter(props, "props");
            Intrinsics.checkNotNullParameter(methods, "methods");
            Intrinsics.checkNotNullParameter(events, "events");
            this.props = props;
            this.methods = methods;
            this.events = events;
        }

        public final List<String> getEvents() {
            return this.events;
        }

        public final List<String> getMethods() {
            return this.methods;
        }

        public final List<String> getProps() {
            return this.props;
        }
    }

    static /* synthetic */ Members m$default(Autocomplete autocomplete, String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "";
        }
        if ((i & 4) != 0) {
            str3 = "";
        }
        return autocomplete.m(str, str2, str3);
    }

    private final Members m(String props, String methods, String events) {
        Iterable iterableSplit$default = StringsKt.split((CharSequence) props, new char[]{' '}, false, 0);
        Collection arrayList = new ArrayList();
        for (Object obj : iterableSplit$default) {
            if (!StringsKt.isBlank((String) obj)) {
                arrayList.add(obj);
            }
        }
        List arrayList2 = (List) arrayList;
        Iterable iterableSplit$default2 = StringsKt.split((CharSequence) methods, new char[]{' '}, false, 0);
        Collection arrayList3 = new ArrayList();
        for (Object obj2 : iterableSplit$default2) {
            if (!StringsKt.isBlank((String) obj2)) {
                arrayList3.add(obj2);
            }
        }
        List arrayList4 = (List) arrayList3;
        Iterable iterableSplit$default3 = StringsKt.split((CharSequence) events, new char[]{' '}, false, 0);
        Collection arrayList5 = new ArrayList();
        for (Object obj3 : iterableSplit$default3) {
            if (!StringsKt.isBlank((String) obj3)) {
                arrayList5.add(obj3);
            }
        }
        return new Members(arrayList2, arrayList4, (List) arrayList5);
    }

    /* JADX INFO: compiled from: Autocomplete.kt */
    private static abstract class T {
        public /* synthetic */ T(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class Inst extends T {
            private final Instance i;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Inst(Instance i) {
                super(null);
                Intrinsics.checkNotNullParameter(i, "i");
                this.i = i;
            }

            public final Instance getI() {
                return this.i;
            }
        }

        private T() {
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class Cls extends T {
            private final String name;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Cls(String name) {
                super(null);
                Intrinsics.checkNotNullParameter(name, "name");
                this.name = name;
            }

            public final String getName() {
                return this.name;
            }
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class Lib extends T {
            private final String name;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Lib(String name) {
                super(null);
                Intrinsics.checkNotNullParameter(name, "name");
                this.name = name;
            }

            public final String getName() {
                return this.name;
            }
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class DataStatic extends T {
            private final String name;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public DataStatic(String name) {
                super(null);
                Intrinsics.checkNotNullParameter(name, "name");
                this.name = name;
            }

            public final String getName() {
                return this.name;
            }
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class Data extends T {
            private final String name;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Data(String name) {
                super(null);
                Intrinsics.checkNotNullParameter(name, "name");
                this.name = name;
            }

            public final String getName() {
                return this.name;
            }
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class EnumRoot extends T {
            public static final EnumRoot INSTANCE = new EnumRoot();

            private EnumRoot() {
                super(null);
            }
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class EnumType extends T {
            private final String name;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public EnumType(String name) {
                super(null);
                Intrinsics.checkNotNullParameter(name, "name");
                this.name = name;
            }

            public final String getName() {
                return this.name;
            }
        }

        /* JADX INFO: compiled from: Autocomplete.kt */
        public static final class Signal extends T {
            public static final Signal INSTANCE = new Signal();

            private Signal() {
                super(null);
            }
        }
    }

    private final String clsOf(T t) {
        if (t instanceof T.Inst) {
            return ((T.Inst) t).getI().getClassName();
        }
        if (t instanceof T.Cls) {
            return ((T.Cls) t).getName();
        }
        return null;
    }

    private final List<String> classChain(String cls) {
        ArrayList<String> out = new ArrayList<>();
        String c = cls;
        int guard = 0;
        while (c != null && guard++ < 32 && !out.contains(c)) {
            out.add(c);
            if (c.equals("Instance")) break;
            c = ClassDb.INSTANCE.superclass(c);
        }
        if (!out.contains("Instance")) {
            out.add("Instance");
        }
        return out;
    }

    private final Triple<Set<String>, Set<String>, Set<String>> membersFor(String cls) {
        LinkedHashSet p = new LinkedHashSet();
        LinkedHashSet me = new LinkedHashSet();
        LinkedHashSet ev = new LinkedHashSet();
        for (String c : classChain(cls)) {
            Members members = classMembers.get(c);
            if (members != null) {
                p.addAll(members.getProps());
                me.addAll(members.getMethods());
                ev.addAll(members.getEvents());
            }
        }
        return new Triple<>(p, me, ev);
    }

    private final T typeFromHint(String s, Studio studio) {
        if (s == null) {
            return null;
        }
        if (!StringsKt.startsWith(s, "#", false)) {
            return new T.Cls(s);
        }
        String strSubstring = s.substring(1);
        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return new T.Data(strSubstring);
    }

    private final boolean isId(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private final Pair<Integer, Integer> lexState(String text, int pos) {
        int lvl;
        int n = Math.min(pos, text.length());
        int i = 0;
        while (i < n) {
            char c = text.charAt(i);
            if (c == '-' && i + 1 < n && text.charAt(i + 1) == '-') {
                int lvl2 = longLevel(text, i + 2);
                if (lvl2 >= 0) {
                    String close = "]" + StringsKt.repeat("=", lvl2) + "]";
                    int e = StringsKt.indexOf((CharSequence) text, close, i + 4 + lvl2, false);
                    if (e < 0 || close.length() + e > n) {
                        return TuplesKt.to(2, Integer.valueOf(i));
                    }
                    i = e + close.length();
                } else {
                    int e2 = StringsKt.indexOf((CharSequence) text, '\n', i, false);
                    if (e2 < 0 || e2 >= n) {
                        return TuplesKt.to(2, Integer.valueOf(i));
                    }
                    i = e2 + 1;
                }
            } else {
                switch (c) {
                    case '\"':
                    case '\'':
                    case '`':
                        int j = i + 1;
                        while (j < n && text.charAt(j) != c && text.charAt(j) != '\n') {
                            if (text.charAt(j) == '\\') {
                                j++;
                            }
                            j++;
                        }
                        if (j >= n) {
                            return TuplesKt.to(1, Integer.valueOf(i));
                        }
                        i = j + 1;
                        break;
                    default:
                        if (c == '[' && (lvl = longLevel(text, i)) >= 0) {
                            String close2 = "]" + StringsKt.repeat("=", lvl) + "]";
                            int e3 = StringsKt.indexOf((CharSequence) text, close2, i + 2 + lvl, false);
                            if (e3 < 0 || close2.length() + e3 > n) {
                                return TuplesKt.to(2, Integer.valueOf(i));
                            }
                            i = e3 + close2.length();
                        } else {
                            i++;
                        }
                        break;
                }
            }
        }
        return TuplesKt.to(0, -1);
    }

    private final int longLevel(String text, int at) {
        if (at >= text.length() || text.charAt(at) != '[') {
            return -1;
        }
        int j = at + 1;
        int l = 0;
        while (j < text.length() && text.charAt(j) == '=') {
            l++;
            j++;
        }
        if (j >= text.length() || text.charAt(j) != '[') {
            return -1;
        }
        return l;
    }

    /* JADX INFO: compiled from: Autocomplete.kt */
    private static final class Step {
        private final char acc;
        private final String arg;
        private final boolean call;
        private final String name;

        public Step(String name, char acc, String arg, boolean call) {
            Intrinsics.checkNotNullParameter(name, "name");
            this.name = name;
            this.acc = acc;
            this.arg = arg;
            this.call = call;
        }

        public final char getAcc() {
            return this.acc;
        }

        public final String getArg() {
            return this.arg;
        }

        public final boolean getCall() {
            return this.call;
        }

        public final String getName() {
            return this.name;
        }
    }

    private final List<Step> chainBefore(String text, int end) {
        List<Step> list = null;
        List<String> groupValues;
        String s;
        ArrayList steps = new ArrayList();
        int i = end - 1;
        int guard = 0;
        while (true) {
            if (i >= 0) {
                guard++;
                if (guard < 24) {
                    Object arg = null;
                    boolean call = false;
                    char c = ')';
                    if (text.charAt(i) == ')') {
                        int depth = 0;
                        int j = i;
                        while (j >= 0) {
                            if (text.charAt(j) != c) {
                                if (text.charAt(j) == '(' && (depth = depth - 1) == 0) {
                                    break;
                                }
                            } else {
                                depth++;
                            }
                            j--;
                            c = ')';
                        }
                        if (j < 0) {
                            return null;
                        }
                        String strSubstring = text.substring(j + 1, i);
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                        String inside = StringsKt.trim((CharSequence) strSubstring).toString();
                        MatchResult matchResultFind$default = (new Regex("^[\"']([^\"']*)[\"']")).find(inside, 0);
                        if (matchResultFind$default != null) {
                            arg = matchResultFind$default.getGroupValues().get(1);
                        }
                        call = true;
                        i = j - 1;
                    } else if (text.charAt(i) == ']') {
                        int j2 = i;
                        while (j2 >= 0 && text.charAt(j2) != '[') {
                            j2--;
                        }
                        if (j2 < 0) {
                            return null;
                        }
                        String strSubstring2 = text.substring(j2 + 1, i);
                        Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
                        String inside2 = StringsKt.trim((CharSequence) strSubstring2).toString();
                        MatchResult matchResultFind$default2 = (new Regex("^[\"']([^\"']*)[\"']$")).find(inside2, 0);
                        if (matchResultFind$default2 == null || (groupValues = matchResultFind$default2.getGroupValues()) == null || (s = groupValues.get(1)) == null) {
                            return null;
                        }
                        steps.add(0, new Step(s, '.', null, false));
                        i = j2 - 1;
                        if ((i < 0 || !isId(text.charAt(i))) && i < 0) {
                            return null;
                        }
                    }
                    while (i >= 0 && text.charAt(i) == ' ') {
                        i--;
                    }
                    if (i >= 0) {
                        if (isId(text.charAt(i))) {
                            int e = i + 1;
                            while (i >= 0 && isId(text.charAt(i))) {
                                i--;
                            }
                            String name = text.substring(i + 1, e);
                            Intrinsics.checkNotNullExpressionValue(name, "substring(...)");
                            if (Character.isDigit(StringsKt.first(name))) {
                                return null;
                            }
                            char acc = (i < 0 || !(text.charAt(i) == '.' || text.charAt(i) == ':')) ? ' ' : text.charAt(i);
                            steps.add(0, new Step(name, acc, (String) arg, call));
                            if (acc == ' ') {
                                return steps;
                            }
                            i--;
                        } else {
                            list = null;
                        }
                    } else {
                        list = null;
                    }
                    return steps.isEmpty() ? list : steps;
                }
            }
            if (steps.isEmpty()) {
                return null;
            }
            return steps;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:26:0x0076  */
    /* JADX WARN: Code duplicated, block: B:27:0x007e  */
    /* JADX WARN: Code duplicated, block: B:29:0x0086  */
    /* JADX WARN: Code duplicated, block: B:30:0x008e  */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005e, code lost:
    
        if (r4.equals("Workspace") == false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:?, code lost:
    
        return new com.goydevv.studrblx.ui.Autocomplete.T.Inst(r0.getWorkspace());
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0012, code lost:
    
        if (r4.equals("workspace") == false) goto L24;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final com.goydevv.studrblx.ui.Autocomplete.T resolveRoot(java.lang.String r4, com.goydevv.studrblx.core.Studio r5, com.goydevv.studrblx.model.Instance r6, java.lang.String r7, int r8, int r9) {
        DataModel dm = r5.getModel();
        switch (r4) {
            case "game":
            case "Game":
                return new T.Inst(dm);
            case "workspace":
            case "Workspace":
                return new T.Inst(dm.getWorkspace());
            case "script":
                return new T.Inst(r6);
            case "Enum":
                return T.EnumRoot.INSTANCE;
            case "plugin":
                return new T.Cls("Plugin");
            default:
                if (libs.containsKey(r4)) {
                    return new T.Lib(r4);
                }
                if (dataStatics.containsKey(r4)) {
                    return new T.DataStatic(r4);
                }
                return resolveLocal(r4, r5, r6, r7, r8, r9);
        }
    }

    private final T resolveLocal(String name, Studio studio, Instance script, String text, int before, int depth) {
        if (depth > 3) {
            return null;
        }
        Regex rx = new Regex("(?:^|[\\s;(])local\\s+" + Regex.Companion.escape(name) + "\\s*(?::\\s*[\\w.<>]+\\s*)?=\\s*([^\\n;]+)");
        String strSubstring = text.substring(0, Math.min(before, text.length()));
        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        MatchResult m = (MatchResult) SequencesKt.lastOrNull((rx).findAll(strSubstring, 0));
        if (m != null) {
            String expr = StringsKt.removeSuffix(StringsKt.trim((CharSequence) m.getGroupValues().get(1)).toString(), (CharSequence) ",");
            T t = resolveExpr(expr, studio, script, depth + 1);
            if (t != null) {
                return t;
            }
        }
        Map<String, String> map = nameHints;
        String lowerCase = name.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        String str = map.get(lowerCase);
        if (str != null) {
            return INSTANCE.typeFromHint(str, studio);
        }
        if (!ClassDb.INSTANCE.isServiceName(name)) {
            return null;
        }
        Instance instanceFindService = studio.getModel().findService(name);
        return instanceFindService != null ? new T.Inst(instanceFindService) : new T.Cls(name);
    }

    private final T resolveExpr(String expr, Studio studio, Instance script, int depth) {
        String e = StringsKt.trim((CharSequence) expr).toString();
        MatchResult matchResultFind$default = (new Regex("^(\\w+)\\.new\\s*\\(")).find(e, 0);
        if (matchResultFind$default != null) {
            String str = matchResultFind$default.getGroupValues().get(1);
            if (Intrinsics.areEqual(str, "Instance")) {
                MatchResult matchResultFind$default2 = (new Regex("Instance\\.new\\s*\\(\\s*[\"'](\\w+)[\"']")).find(e, 0);
                if (matchResultFind$default2 != null) {
                    return new T.Cls(matchResultFind$default2.getGroupValues().get(1));
                }
            } else if (dataStatics.containsKey(str)) {
                return new T.Data(str);
            }
        }
        MatchResult matchResultFind$default3 = (new Regex("^(\\w+)\\.(fromRGB|fromHSV|fromHex|Angles|lookAt|fromOrientation|fromScale|fromOffset)\\b")).find(e, 0);
        if (matchResultFind$default3 != null) {
            return new T.Data(matchResultFind$default3.getGroupValues().get(1));
        }
        List<Step> listChainBefore = chainBefore(e + " ", e.length());
        if (listChainBefore == null) {
            return null;
        }
        return resolveSteps(listChainBefore, studio, script, e, e.length(), depth);
    }

    private final T resolveSteps(List<Step> steps, Studio studio, Instance script, String text, int before, int depth) {
        T t;
        if (steps.isEmpty() || (t = resolveRoot(steps.get(0).getName(), studio, script, text, before, depth)) == null) {
            return null;
        }
        for (Object _sObj : CollectionsKt.drop(steps, 1)) {
            Step s = (Step) _sObj;
            T tMember = member(t, s, studio);
            if (tMember == null) {
                return null;
            }
            t = tMember;
        }
        return t;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:101:0x01aa  */
    /* JADX WARN: Code duplicated, block: B:103:0x01ba  */
    /* JADX WARN: Code duplicated, block: B:104:0x01c3  */
    /* JADX WARN: Code duplicated, block: B:126:0x0224 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:127:0x0225  */
    /* JADX WARN: Code duplicated, block: B:129:0x022e  */
    /* JADX WARN: Code duplicated, block: B:130:0x0230  */
    /* JADX WARN: Code duplicated, block: B:132:0x0238  */
    /* JADX WARN: Code duplicated, block: B:134:0x023e  */
    /* JADX WARN: Code duplicated, block: B:136:0x0248  */
    /* JADX WARN: Code duplicated, block: B:99:0x01a4  */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x01d2, code lost:
    
        if (r1.equals("WaitForChild") == false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x01d5, code lost:
    
        r1 = r14.getArg();
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x01d9, code lost:
    
        if (r1 != null) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01e2, code lost:
    
        return new com.goydevv.studrblx.ui.Autocomplete.T.Cls("Instance");
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x01e3, code lost:
    
        if (r3 == null) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01e5, code lost:
    
        r2 = r3.findFirstChild(r1, kotlin.jvm.internal.Intrinsics.areEqual(r14.getName(), "FindFirstDescendant"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01f1, code lost:
    
        if (r2 == null) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01fb, code lost:
    
        return new com.goydevv.studrblx.ui.Autocomplete.T.Inst(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x01fc, code lost:
    
        r2 = com.goydevv.studrblx.ui.Autocomplete.propTypes.get(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0204, code lost:
    
        if (r2 == null) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x020d, code lost:
    
        return com.goydevv.studrblx.ui.Autocomplete.INSTANCE.typeFromHint(r2, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0215, code lost:
    
        return new com.goydevv.studrblx.ui.Autocomplete.T.Cls("Instance");
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0189, code lost:
    
        if (r1.equals("FindFirstChild") == false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0190, code lost:
    
        if (r1.equals("FindFirstDescendant") == false) goto L124;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final com.goydevv.studrblx.ui.Autocomplete.T member(com.goydevv.studrblx.ui.Autocomplete.T r13, com.goydevv.studrblx.ui.Autocomplete.Step r14, com.goydevv.studrblx.core.Studio r15) {
        if (r13 == null || r14 == null) {
            return null;
        }
        String name = r14.getName();
        String arg = r14.getArg();
        if (r13 instanceof T.EnumRoot) {
            return new T.EnumType(name);
        }
        if (r13 instanceof T.DataStatic) {
            String dn = ((T.DataStatic) r13).getName();
            if (Intrinsics.areEqual(dn, "Instance") && Intrinsics.areEqual(name, "new") && arg != null && arg.length() > 0) {
                return new T.Cls(arg);
            }
            return new T.Data(dn);
        }
        if (r13 instanceof T.Inst) {
            Instance inst = ((T.Inst) r13).getI();
            if (Intrinsics.areEqual(name, "Parent")) {
                Instance p = inst.getParent();
                return p != null ? new T.Inst(p) : new T.Cls("Instance");
            }
            if (r14.getCall()) {
                if ((Intrinsics.areEqual(name, "GetService") || Intrinsics.areEqual(name, "FindService")) && arg != null) {
                    Instance svc = r15.getModel().findService(arg);
                    return svc != null ? new T.Inst(svc) : new T.Cls(arg);
                }
                if ((Intrinsics.areEqual(name, "FindFirstChild") || Intrinsics.areEqual(name, "WaitForChild") || Intrinsics.areEqual(name, "FindFirstDescendant")) && arg != null) {
                    Instance ch = Instance.findFirstChild$default(inst, arg, false, 2, null);
                    return ch != null ? new T.Inst(ch) : new T.Cls("Instance");
                }
            } else {
                Instance ch = Instance.findFirstChild$default(inst, name, false, 2, null);
                if (ch != null) {
                    return new T.Inst(ch);
                }
            }
        }
        if (r14.getCall()) {
            String ret = methodReturns.get(name);
            if (ret != null) {
                if (Intrinsics.areEqual(ret, "@self")) {
                    return r13;
                }
                if (Intrinsics.areEqual(ret, "@arg") && arg != null && arg.length() > 0) {
                    return new T.Cls(arg);
                }
                if (StringsKt.startsWith(ret, "#", false)) {
                    return new T.Data(ret.substring(1));
                }
                return new T.Cls(ret);
            }
            if (Intrinsics.areEqual(name, "GetService") && arg != null && arg.length() > 0) {
                Instance svc = r15.getModel().findService(arg);
                return svc != null ? new T.Inst(svc) : new T.Cls(arg);
            }
        }
        String pt = propTypes.get(name);
        if (pt != null) {
            if (StringsKt.startsWith(pt, "#", false)) {
                return new T.Data(pt.substring(1));
            }
            return new T.Cls(pt);
        }
        return null;
    }

    private final Set<String> localNames(String text, int upTo) {
        char c;
        int i;
        char c2;
        LinkedHashSet out = new LinkedHashSet();
        char c3 = 0;
        String src = text.substring(0, Math.min(upTo, text.length()));
        Intrinsics.checkNotNullExpressionValue(src, "substring(...)");
        int i2 = 2;
        Iterator it = (new Regex("\\blocal\\s+(?:function\\s+)?([A-Za-z_][\\w]*(?:\\s*,\\s*[A-Za-z_][\\w]*)*)")).findAll(src, 0).iterator();
        while (true) {
            c = ',';
            i = 1;
            if (!it.hasNext()) {
                break;
            }
            String str = ((MatchResult) it.next()).getGroupValues().get(1);
            char[] cArr = new char[1];
            cArr[c3] = ',';
            Iterator it2 = StringsKt.split((CharSequence) str, cArr, false, 0).iterator();
            while (it2.hasNext()) {
                out.add(StringsKt.trim((CharSequence) it2.next()).toString());
            }
            c3 = 0;
        }
        Iterator it3 = (new Regex("\\bfunction\\s+([A-Za-z_][\\w.:]*)")).findAll(src, 0).iterator();
        while (true) {
            c2 = ':';
            if (!it3.hasNext()) {
                break;
            }
            out.add(com.goydevv.studrblx.util.KtCompat.substringBefore(com.goydevv.studrblx.util.KtCompat.substringBefore(((MatchResult) it3.next()).getGroupValues().get(1), '.'), ':'));
        }
        char c4 = c;
        Iterator it4 = (new Regex("\\bfunction\\s*[\\w.:]*\\s*\\(([^)]*)\\)")).findAll(src, 0).iterator();
        while (it4.hasNext()) {
            String str2 = ((MatchResult) it4.next()).getGroupValues().get(i);
            char[] cArr2 = new char[i];
            cArr2[0] = c4;
            Iterator it5 = StringsKt.split((CharSequence) str2, cArr2, false, 0).iterator();
            while (it5.hasNext()) {
                String string = StringsKt.trim((CharSequence) (((i2) & 2) != 0 ? com.goydevv.studrblx.util.KtCompat.substringBefore(StringsKt.trim((CharSequence) it5.next()).toString(), c2) : StringsKt.substringBefore(StringsKt.trim((CharSequence) it5.next()).toString(), c2, (String) null))).toString();
                if (!((string.length() > 0) && !Intrinsics.areEqual(string, "..."))) {
                    string = null;
                }
                if (string != null) {
                    out.add(string);
                }
                i2 = 2;
                c2 = ':';
            }
            i2 = 2;
            c2 = ':';
            i = 1;
        }
        Iterator it6 = (new Regex("\\bfor\\s+([\\w\\s,]+?)\\s+(?:in|=)")).findAll(src, 0).iterator();
        while (it6.hasNext()) {
            Iterator it7 = StringsKt.split((CharSequence) ((MatchResult) it6.next()).getGroupValues().get(1), new char[]{c4}, false, 0).iterator();
            while (it7.hasNext()) {
                out.add(StringsKt.trim((CharSequence) it7.next()).toString());
            }
        }
        return out;
    }

    private final List<Item> rank(List<Item> items, final String prefix) {
        if (!(prefix.length() == 0)) {
            final String p = prefix.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(p, "toLowerCase(...)");
            return SequencesKt.toList(SequencesKt.take(SequencesKt.sortedWith(SequencesKt.distinctBy(SequencesKt.filter(CollectionsKt.asSequence(items), new Function1() { // from class: com.goydevv.studrblx.ui.Autocomplete$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(Autocomplete.rank$lambda$32(p, prefix, (Autocomplete.Item) obj));
                }
            }), new Function1() { // from class: com.goydevv.studrblx.ui.Autocomplete$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Autocomplete.rank$lambda$33((Autocomplete.Item) obj);
                }
            }), ComparisonsKt.compareBy(new Function1() { // from class: com.goydevv.studrblx.ui.Autocomplete$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Autocomplete.rank$lambda$34(prefix, p, (Autocomplete.Item) obj);
                }
            }, new Function1() { // from class: com.goydevv.studrblx.ui.Autocomplete$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Autocomplete.rank$lambda$35((Autocomplete.Item) obj);
                }
            }, new Function1() { // from class: com.goydevv.studrblx.ui.Autocomplete$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Autocomplete.rank$lambda$36((Autocomplete.Item) obj);
                }
            })), 40));
        }
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (Object obj : items) {
            if (hashSet.add(((Item) obj).getLabel())) {
                arrayList.add(obj);
            }
        }
        return CollectionsKt.take(arrayList, 60);
    }

    static final boolean rank$lambda$32(String $p, String $prefix, Item it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String lowerCase = it.getLabel().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return StringsKt.contains((CharSequence) lowerCase, (CharSequence) $p, false) && !Intrinsics.areEqual(it.getLabel(), $prefix);
    }

    static final String rank$lambda$33(Item it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getLabel();
    }

    static final Comparable rank$lambda$34(String $prefix, String $p, Item it) {
        int i;
        Intrinsics.checkNotNullParameter(it, "it");
        if (StringsKt.startsWith(it.getLabel(), $prefix, false)) {
            i = 0;
        } else {
            String lowerCase = it.getLabel().toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            i = StringsKt.startsWith(lowerCase, $p, false) ? 1 : 2;
        }
        return i;
    }

    static final Comparable rank$lambda$35(Item it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Integer.valueOf(it.getLabel().length());
    }

    static final Comparable rank$lambda$36(Item it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getLabel();
    }

    public static /* synthetic */ Result complete$default(Autocomplete autocomplete, Studio studio, Instance instance, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 16) != 0) {
            z = false;
        }
        return autocomplete.complete(studio, instance, str, i, z);
    }

    public final Result complete(Studio studio, Instance script, String text, int cursor, boolean explicit) {
        T t;
        List<Item> listMembersOf;
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(script, "script");
        Intrinsics.checkNotNullParameter(text, "text");
        Result result = null;
        if (cursor >= 0 && cursor <= text.length()) {
            Pair<Integer, Integer> pairLexState = lexState(text, cursor);
            int state = pairLexState.component1().intValue();
            int at = pairLexState.component2().intValue();
            if (state == 2) {
                return null;
            }
            if (state == 1) {
                return completeString(studio, script, text, cursor, at);
            }
            int start = cursor;
            while (start > 0 && isId(text.charAt(start - 1))) {
                start--;
            }
            String prefix = text.substring(start, cursor);
            Intrinsics.checkNotNullExpressionValue(prefix, "substring(...)");
            if ((prefix.length() > 0) && Character.isDigit(prefix.charAt(0))) {
                return null;
            }
            int accIdx = start - 1;
            char acc = accIdx >= 0 ? text.charAt(accIdx) : ' ';
            if (acc == '.' && accIdx > 0 && text.charAt(accIdx - 1) == '.') {
                return null;
            }
            switch (acc) {
                case '.':
                case 58 /* 58 */:
                    List<Step> listChainBefore = chainBefore(text, accIdx);
                    if (listChainBefore == null) {
                        return null;
                    }
                    synchronized (studio.getLock()) {
                        t = INSTANCE.resolveSteps(listChainBefore, studio, script, text, accIdx, 0);
                    }
                    if (t == null) {
                        return null;
                    }
                    synchronized (studio.getLock()) {
                        listMembersOf = INSTANCE.membersOf(t, acc);
                    }
                    List<Item> listRank = rank(listMembersOf, prefix);
                    if (listRank.isEmpty()) {
                        return null;
                    }
                    return new Result(start, cursor, prefix, listRank);
                default:
                    if ((prefix.length() == 0) && !explicit) {
                        return null;
                    }
                    String before = text.substring(Math.max(0, start - 16), start);
                    Intrinsics.checkNotNullExpressionValue(before, "substring(...)");
                    if (new Regex("(\\blocal|\\bfunction|\\bfor)\\s+$").containsMatchIn(before) && !explicit) {
                        return null;
                    }
                    ArrayList items = new ArrayList();
                    Iterable iterableLocalNames = localNames(text, start);
                    Collection arrayList = new ArrayList();
                    for (Object obj : iterableLocalNames) {
                        Result result2 = result;
                        String before2 = before;
                        if (!Intrinsics.areEqual((String) obj, prefix)) {
                            arrayList.add(obj);
                        }
                        result = result2;
                        before = before2;
                    }
                    Result result3 = result;
                    Iterator it = ((List) arrayList).iterator();
                    while (it.hasNext()) {
                        items.add(new Item((String) it.next(), Kind.VARIABLE, "local", null, 8, null));
                    }
                    Iterator it2 = keywords.iterator();
                    while (it2.hasNext()) {
                        items.add(new Item((String) it2.next(), Kind.KEYWORD, null, null, 12, null));
                    }
                    Iterator it3 = globals.iterator();
                    while (it3.hasNext()) {
                        items.add(new Item((String) it3.next(), Kind.GLOBAL, null, null, 12, null));
                    }
                    Iterator it4 = libs.keySet().iterator();
                    while (it4.hasNext()) {
                        items.add(new Item((String) it4.next(), Kind.LIBRARY, "library", null, 8, null));
                    }
                    Iterator it5 = dataStatics.keySet().iterator();
                    while (it5.hasNext()) {
                        items.add(new Item((String) it5.next(), Kind.CLASS, "datatype", null, 8, null));
                    }
                    items.add(new Item("Enum", Kind.ENUM, null, null, 12, null));
                    List<Item> listRank2 = rank(items, prefix);
                    return listRank2.isEmpty() ? result3 : new Result(start, cursor, prefix, listRank2);
            }
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:135:0x0448  */
    /* JADX WARN: Code duplicated, block: B:187:0x044b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:21:0x005e  */
    /* JADX WARN: Code duplicated, block: B:22:0x0061  */
    private final List<Item> membersOf(T t, char acc) {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        Iterable<String> iterableKeySet;
        Kind kind;
        ArrayList out = new ArrayList();
        if (t instanceof T.Lib) {
            Iterable<String> iterable = (List) libs.get(((T.Lib) t).getName());
            if (iterable != null) {
                for (String str : iterable) {
                    switch (str) {
                        case "pi":
                        case "huge":
                        case "charpattern":
                            kind = Kind.PROPERTY;
                            break;
                        default:
                            kind = Kind.FUNCTION;
                            break;
                    }
                    out.add(new Item(str, kind, ((T.Lib) t).getName() + "." + str, null, 8, null));
                }
                i = 0;
            } else {
                i = 0;
            }
        } else if (t instanceof T.DataStatic) {
            Iterable<String> iterable2 = (List) dataStatics.get(((T.DataStatic) t).getName());
            if (iterable2 != null) {
                for (String str2 : iterable2) {
                    out.add(new Item(str2, (!Character.isUpperCase(StringsKt.first(str2)) || Intrinsics.areEqual(str2, "Angles")) ? Kind.FUNCTION : Kind.PROPERTY, ((T.DataStatic) t).getName() + "." + str2, null, 8, null));
                }
                i = 0;
            } else {
                i = 0;
            }
        } else if (t instanceof T.Data) {
            Pair<List<String>, List<String>> pair = dataMembers.get(((T.Data) t).getName());
            if (pair != null) {
                Iterable iterable3 = (List) pair.component1();
                Iterable iterable4 = (List) pair.component2();
                if (acc == ':') {
                    Iterator it = iterable4.iterator();
                    while (it.hasNext()) {
                        out.add(new Item((String) it.next(), Kind.METHOD, ((T.Data) t).getName(), null, 8, null));
                    }
                } else {
                    Iterator it2 = iterable3.iterator();
                    while (it2.hasNext()) {
                        out.add(new Item((String) it2.next(), Kind.PROPERTY, ((T.Data) t).getName(), null, 8, null));
                    }
                }
            }
            i = 0;
        } else if (t instanceof T.EnumRoot) {
            out.add(new Item("GetEnums", Kind.FUNCTION, null, null, 12, null));
            Iterator it3 = CollectionsKt.sorted(SetsKt.plus((Set) Enums.INSTANCE.getTypes().keySet(), (Iterable) extraEnums)).iterator();
            while (it3.hasNext()) {
                out.add(new Item((String) it3.next(), Kind.ENUM, "Enum", null, 8, null));
            }
            i = 0;
        } else if (t instanceof T.EnumType) {
            if (acc == ':') {
                out.add(new Item("GetEnumItems", Kind.METHOD, null, null, 12, null));
                i = 0;
            } else {
                LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get(((T.EnumType) t).getName());
                if (linkedHashMap == null || (iterableKeySet = linkedHashMap.keySet()) == null) {
                    i = 0;
                } else {
                    for (String str3 : iterableKeySet) {
                        Intrinsics.checkNotNull(str3);
                        out.add(new Item(str3, Kind.ENUM, "Enum." + ((T.EnumType) t).getName(), null, 8, null));
                    }
                    i = 0;
                }
            }
        } else if (!(t instanceof T.Signal)) {
            if (!(t instanceof T.Inst) && !(t instanceof T.Cls)) {
                throw new NoWhenBranchMatchedException();
            }
            String cls = clsOf(t);
            Intrinsics.checkNotNull(cls);
            Triple<Set<String>, Set<String>, Set<String>> tripleMembersFor = membersFor(cls);
            Iterable iterable5 = (Set) tripleMembersFor.component1();
            Iterable iterable6 = (Set) tripleMembersFor.component2();
            Iterable iterable7 = (Set) tripleMembersFor.component3();
            if (acc == ':') {
                Iterator it4 = iterable6.iterator();
                while (it4.hasNext()) {
                    out.add(new Item((String) it4.next(), Kind.METHOD, cls, null, 8, null));
                }
                i = 0;
            } else {
                if (t instanceof T.Inst) {
                    for (Instance instance : ((T.Inst) t).getI().getChildren()) {
                        out.add(new Item(instance.getName(), Kind.CHILD, instance.getClassName(), INSTANCE.isSimpleName(instance.getName()) ? instance.getName() : ""));
                    }
                    if (((T.Inst) t).getI() instanceof DataModel) {
                        Iterator it5 = ClassDb.INSTANCE.getServiceNames().iterator();
                        while (it5.hasNext()) {
                            out.add(new Item((String) it5.next(), Kind.SERVICE, "service", null, 8, null));
                        }
                    }
                    Iterable iterableKeySet2 = ((T.Inst) t).getI().getProps().keySet();
                    Intrinsics.checkNotNullExpressionValue(iterableKeySet2, "<get-keys>(...)");
                    Iterable<String> iterable8 = iterableKeySet2;
                    Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable8, 10));
                    for (String str4 : iterable8) {
                        PropNames propNames = PropNames.INSTANCE;
                        Intrinsics.checkNotNull(str4);
                        arrayList.add(propNames.display(str4));
                    }
                    int i5 = 0;
                    Collection arrayList2 = new ArrayList();
                    for (Object obj : (List) arrayList) {
                        String str5 = (String) obj;
                        if (INSTANCE.isSimpleName(str5)) {
                            i2 = i5;
                            i3 = 0;
                            if (!StringsKt.startsWith(str5, "xml", false)) {
                                i4 = 1;
                            }
                            if (i4 != 0) {
                                arrayList2.add(obj);
                            }
                            i5 = i2;
                        } else {
                            i2 = i5;
                            i3 = 0;
                        }
                        i4 = i3;
                        if (i4 != 0) {
                            arrayList2.add(obj);
                        }
                        i5 = i2;
                    }
                    i = 0;
                    Iterator it6 = ((List) arrayList2).iterator();
                    while (it6.hasNext()) {
                        out.add(new Item((String) it6.next(), Kind.PROPERTY, cls, null, 8, null));
                    }
                } else {
                    i = 0;
                }
                Iterator it7 = iterable5.iterator();
                while (it7.hasNext()) {
                    out.add(new Item((String) it7.next(), Kind.PROPERTY, cls, null, 8, null));
                }
                Iterator it8 = iterable7.iterator();
                while (it8.hasNext()) {
                    out.add(new Item((String) it8.next(), Kind.EVENT, cls, null, 8, null));
                }
            }
        } else if (acc == ':') {
            Iterator it9 = CollectionsKt.listOf(new String[]{"Connect", "Once", "Wait", "ConnectParallel"}).iterator();
            while (it9.hasNext()) {
                out.add(new Item((String) it9.next(), Kind.METHOD, "RBXScriptSignal", null, 8, null));
            }
            i = 0;
        } else {
            i = 0;
        }
        Collection arrayList3 = new ArrayList();
        for (Object obj2 : out) {
            if ((((Item) obj2).getInsert().length() > 0 ? 1 : i) != 0) {
                arrayList3.add(obj2);
            }
        }
        return (List) arrayList3;
    }

    private final boolean isSimpleName(String s) {
        boolean z;
        if ((s.length() > 0) && (Character.isLetter(s.charAt(0)) || s.charAt(0) == '_')) {
            String str = s;
            int i = 0;
            while (true) {
                if (i < str.length()) {
                    if (!INSTANCE.isId(str.charAt(i))) {
                        z = false;
                        break;
                    }
                    i++;
                } else {
                    z = true;
                    break;
                }
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    private final List<String> getServiceList() {
        return CollectionsKt.sorted(CollectionsKt.distinct(CollectionsKt.plus((Collection) ClassDb.INSTANCE.getServiceNames(), (Iterable) CollectionsKt.listOf(new String[]{"RunService", "TweenService", "Debris", "HttpService", "UserInputService", "ContextActionService", "CollectionService", "MarketplaceService", "DataStoreService", "PathfindingService", "TextService", "GuiService", "BadgeService", "PhysicsService", "InsertService", "TeleportService", "ProximityPromptService", "MessagingService", "MemoryStoreService", "AssetService", "SocialService", "PolicyService", "HapticService", "VRService", "GroupService", "Selection", "ChangeHistoryService", "StudioService"}))));
    }

    /* JADX WARN: Code duplicated, block: B:120:0x023f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:122:0x0242  */
    /* JADX WARN: Code duplicated, block: B:125:0x0293 A[LOOP:8: B:123:0x028d->B:125:0x0293, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:138:0x02d5  */
    /* JADX WARN: Code duplicated, block: B:143:0x02f1  */
    /* JADX WARN: Code duplicated, block: B:146:0x02f7 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:148:0x02ff A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:149:0x0318 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:151:0x0320 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:152:0x0335 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:156:0x0351 A[Catch: all -> 0x0452, LOOP:3: B:154:0x034b->B:156:0x0351, LOOP_END, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:158:0x037f A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:160:0x0383 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:162:0x0392 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:165:0x03b9 A[Catch: all -> 0x0452, LOOP:4: B:163:0x03b3->B:165:0x03b9, LOOP_END, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:167:0x03e2 A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x03ea A[Catch: all -> 0x0452, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:172:0x040f A[Catch: all -> 0x0452, LOOP:5: B:170:0x0409->B:172:0x040f, LOOP_END, TryCatch #1 {, blocks: (B:144:0x02f3, B:146:0x02f7, B:148:0x02ff, B:153:0x0342, B:154:0x034b, B:156:0x0351, B:174:0x0437, B:149:0x0318, B:151:0x0320, B:152:0x0335, B:158:0x037f, B:160:0x0383, B:162:0x0392, B:163:0x03b3, B:165:0x03b9, B:167:0x03e2, B:169:0x03ea, B:170:0x0409, B:172:0x040f), top: B:193:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:178:0x0448 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:179:0x044a  */
    /* JADX WARN: Code restructure failed: missing block: B:216:?, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x008f, code lost:
    
        if (r0.equals("FindFirstChild") == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0098, code lost:
    
        if (r0.equals("SetAttribute") == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a1, code lost:
    
        if (r0.equals("FindFirstDescendant") != false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00ae, code lost:
    
        if (r0.equals("GetService") == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00b7, code lost:
    
        if (r0.equals("FindService") != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b9, code lost:
    
        r5 = getServiceList().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00c8, code lost:
    
        if (r5.hasNext() == false) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00ca, code lost:
    
        r3.add(new com.goydevv.studrblx.ui.Autocomplete.Item((java.lang.String) r5.next(), com.goydevv.studrblx.ui.Autocomplete.Kind.SERVICE, androidx.core.app."service", null, 8, null));
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00e9, code lost:
    
        r12 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00f3, code lost:
    
        if (r0.equals("GetAttribute") == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00f9, code lost:
    
        if (r2 == ':') goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00fb, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00fc, code lost:
    
        r0 = chainBefore(r30, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0100, code lost:
    
        if (r0 != null) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0102, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0103, code lost:
    
        r11 = r28.getLock();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0108, code lost:
    
        monitor-enter(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x010d, code lost:
    
        r12 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0116, code lost:
    
        r2 = com.goydevv.studrblx.ui.Autocomplete.INSTANCE.resolveSteps(r0, r28, r29, r30, r7, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x011a, code lost:
    
        monitor-exit(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x011d, code lost:
    
        if ((r2 instanceof com.goydevv.studrblx.ui.Autocomplete.T.Inst) == false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x011f, code lost:
    
        r4 = r28.getLock();
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0123, code lost:
    
        monitor-enter(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0125, code lost:
    
        r5 = ((com.goydevv.studrblx.ui.Autocomplete.T.Inst) r2).getI().getAttributes().keySet();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, "<get-keys>(...)");
        r11 = r5.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0144, code lost:
    
        if (r11.hasNext() == false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0146, code lost:
    
        r18 = (java.lang.String) r11.next();
        kotlin.jvm.internal.Intrinsics.checkNotNull(r18);
        r12.add(new com.goydevv.studrblx.ui.Autocomplete.Item(r18, com.goydevv.studrblx.ui.Autocomplete.Kind.PROPERTY, "attribute", null, 8, null));
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x016c, code lost:
    
        r0 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0170, code lost:
    
        monitor-exit(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0179, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x017b, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0180, code lost:
    
        monitor-exit(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0181, code lost:
    
        throw r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final com.goydevv.studrblx.ui.Autocomplete.Result completeString(com.goydevv.studrblx.core.Studio r28, com.goydevv.studrblx.model.Instance r29, java.lang.String r30, int r31, int r32) {
        if (r32 < 0 || r31 < r32 + 1 || r31 > r30.length()) {
            return null;
        }
        int start = r32 + 1;
        String prefix = r30.substring(start, r31);
        String prefLow = prefix.toLowerCase(Locale.ROOT);
        int p = r32 - 1;
        while (p >= 0 && Character.isWhitespace(r30.charAt(p))) {
            p--;
        }
        if (p < 0 || r30.charAt(p) != '(') {
            return null;
        }
        int endFn = p;
        int startFn = endFn;
        while (startFn > 0 && isId(r30.charAt(startFn - 1))) {
            startFn--;
        }
        String fnName = r30.substring(startFn, endFn);
        ArrayList<Item> items = new ArrayList<>();
        if (Intrinsics.areEqual(fnName, "GetService") || Intrinsics.areEqual(fnName, "FindService")) {
            for (Instance ch : r28.getModel().getChildren()) {
                String cn = ch.getClassName();
                if (cn.toLowerCase(Locale.ROOT).startsWith(prefLow)) {
                    items.add(new Item(cn, Kind.SERVICE, "Service", cn));
                }
            }
        } else if (Intrinsics.areEqual(fnName, "new") || Intrinsics.areEqual(fnName, "IsA") || Intrinsics.areEqual(fnName, "FindFirstChildOfClass") || Intrinsics.areEqual(fnName, "FindFirstChildWhichIsA") || Intrinsics.areEqual(fnName, "FindFirstAncestorOfClass") || Intrinsics.areEqual(fnName, "FindFirstAncestorWhichIsA")) {
            for (String cn : classMembers.keySet()) {
                if (cn.toLowerCase(Locale.ROOT).startsWith(prefLow)) {
                    items.add(new Item(cn, Kind.CLASS, "Class", cn));
                }
            }
        } else if (Intrinsics.areEqual(fnName, "FindFirstChild") || Intrinsics.areEqual(fnName, "WaitForChild")) {
            Instance base = r29 != null ? r29.getParent() : r28.getModel().findService("Workspace");
            if (base != null) {
                for (Instance ch : base.getChildren()) {
                    String n = ch.getName();
                    if (n.toLowerCase(Locale.ROOT).startsWith(prefLow)) {
                        items.add(new Item(n, Kind.CHILD, ch.getClassName(), n));
                    }
                }
            }
        }
        if (items.isEmpty()) {
            return null;
        }
        return new Result(start, r31, prefix, items);
    }

    static final Instance completeString$lambda$71$lambda$67(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getParent();
    }
}
