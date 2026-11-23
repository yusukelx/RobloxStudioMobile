package com.goydevv.studrblx.model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Instance.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class ClassDb {
    public static final ClassDb INSTANCE = new ClassDb();
    private static final List<String> insertable;
    private static final Set<String> otherServices;
    private static final HashMap<String, String> parents;
    private static final List<String> serviceNames;

    private ClassDb() {
    }

    static {
        HashMap<String, String> map = new HashMap<>();
        parents$lambda$0$p(map, "Instance", "PVInstance", "LuaSourceContainer", "GuiBase", "Light", "ValueBase", "Humanoid", "Folder", "Configuration", "JointInstance", "WeldConstraint", "Constraint", "Attachment", "Sound", "ClickDetector", "DataModelMesh", "FaceInstance", "UIBase", "BackpackItem", "Accoutrement", "Player", "Camera", "Animator", "Animation", "Team", "BindableEvent", "BindableFunction", "RemoteEvent", "RemoteFunction", "ParticleEmitter", "Fire", "Smoke", "Sparkles", "Trail", "Beam", "Highlight", "ProximityPrompt", "Explosion", "ForceField", "Plugin", "PluginToolbar", "PluginToolbarButton", "Sky", "Atmosphere", "PostEffect", "Clouds", "Script Context", "DataModel");
        parents$lambda$0$p(map, "PVInstance", "BasePart", "Model");
        parents$lambda$0$p(map, "Model", "WorldRoot", "Status");
        parents$lambda$0$p(map, "WorldRoot", "Workspace", "WorldModel");
        parents$lambda$0$p(map, "BasePart", "FormFactorPart", "TriangleMeshPart", "Terrain", "TrussPart", "VehicleSeat");
        parents$lambda$0$p(map, "FormFactorPart", "Part", "WedgePart", "CornerWedgePart");
        parents$lambda$0$p(map, "Part", "SpawnLocation", "Seat", "Platform", "SkateboardPlatform");
        parents$lambda$0$p(map, "TriangleMeshPart", "MeshPart", "PartOperation");
        parents$lambda$0$p(map, "PartOperation", "UnionOperation", "NegateOperation", "IntersectOperation");
        parents$lambda$0$p(map, "LuaSourceContainer", "BaseScript", "ModuleScript");
        parents$lambda$0$p(map, "BaseScript", "Script");
        parents$lambda$0$p(map, "Script", "LocalScript");
        parents$lambda$0$p(map, "GuiBase", "GuiBase2d", "GuiBase3d");
        parents$lambda$0$p(map, "GuiBase2d", "GuiObject", "LayerCollector");
        parents$lambda$0$p(map, "LayerCollector", "ScreenGui", "BillboardGui", "SurfaceGuiBase", "PluginGui");
        parents$lambda$0$p(map, "SurfaceGuiBase", "SurfaceGui");
        parents$lambda$0$p(map, "PluginGui", "DockWidgetPluginGui", "QWidgetPluginGui");
        parents$lambda$0$p(map, "GuiObject", "Frame", "GuiLabel", "GuiButton", "TextBox", "ScrollingFrame", "ViewportFrame", "CanvasGroup", "VideoFrame");
        parents$lambda$0$p(map, "GuiLabel", "TextLabel", "ImageLabel");
        parents$lambda$0$p(map, "GuiButton", "TextButton", "ImageButton");
        parents$lambda$0$p(map, "Light", "PointLight", "SpotLight", "SurfaceLight");
        parents$lambda$0$p(map, "ValueBase", "StringValue", "IntValue", "NumberValue", "BoolValue", "ObjectValue", "Vector3Value", "CFrameValue", "Color3Value", "BrickColorValue", "RayValue");
        parents$lambda$0$p(map, "JointInstance", "Weld", "Snap", "Motor", "ManualWeld", "Glue");
        parents$lambda$0$p(map, "Motor", "Motor6D");
        parents$lambda$0$p(map, "DataModelMesh", "FileMesh", "BevelMesh");
        parents$lambda$0$p(map, "FileMesh", "SpecialMesh");
        parents$lambda$0$p(map, "BevelMesh", "BlockMesh", "CylinderMesh");
        parents$lambda$0$p(map, "FaceInstance", "Decal");
        parents$lambda$0$p(map, "Decal", "Texture");
        parents$lambda$0$p(map, "UIBase", "UIComponent");
        parents$lambda$0$p(map, "UIComponent", "UILayout", "UICorner", "UIStroke", "UIPadding", "UIGradient", "UIScale", "UIConstraint", "UIAspectRatioConstraint", "UISizeConstraint");
        parents$lambda$0$p(map, "UILayout", "UIGridStyleLayout");
        parents$lambda$0$p(map, "UIGridStyleLayout", "UIListLayout", "UIGridLayout", "UITableLayout", "UIPageLayout");
        parents$lambda$0$p(map, "BackpackItem", "Tool", "HopperBin");
        parents$lambda$0$p(map, "Accoutrement", "Accessory", "Hat");
        parents$lambda$0$p(map, "Constraint", "PrismaticConstraint", "CylindricalConstraint", "TorsionSpringConstraint", "PlaneConstraint", "RopeConstraint", "RodConstraint", "SpringConstraint", "HingeConstraint", "BallSocketConstraint", "AlignPosition", "AlignOrientation", "LinearVelocity", "VectorForce", "AngularVelocity", "LineForce", "NoCollisionConstraint", "RigidConstraint", "Torque", "UniversalConstraint", "SlidingBallConstraint");
        parents$lambda$0$p(map, "SlidingBallConstraint", "PrismaticConstraint", "CylindricalConstraint");
        parents$lambda$0$p(map, "Instance", "BodyMover", "PostEffect", "SoundEffect", "SoundGroup", "AnimationController", "HumanoidDescription", "BodyColors", "CharacterAppearance", "SurfaceAppearance", "Keyframe", "KeyframeSequence", "Pose", "KeyframeMarker", "PathfindingModifier", "PathfindingLink", "Dialog", "DialogChoice", "DragDetector", "UnreliableRemoteEvent", "Wire", "AudioPlayer", "AudioEmitter", "AudioListener", "AudioDeviceOutput", "AudioDeviceInput", "AudioFader", "AudioReverb", "AudioEqualizer", "AudioEcho", "AudioAnalyzer", "IKControl", "WrapLayer", "WrapTarget", "TextChatCommand", "TextChannel", "Actor", "StyleSheet", "StyleRule", "ClickDetector", "MaterialVariant", "AdvancedDragger", "BubbleChatConfiguration", "TextChatMessageProperties", "BubbleChatMessageProperties");
        parents$lambda$0$p(map, "Model", "Actor");
        parents$lambda$0$p(map, "Attachment", "Bone");
        parents$lambda$0$p(map, "CharacterAppearance", "Clothing", "ShirtGraphic", "CharacterMesh", "BodyColors");
        parents$lambda$0$p(map, "Clothing", "Shirt", "Pants");
        parents$lambda$0$p(map, "BodyMover", "BodyAngularVelocity", "BodyForce", "BodyGyro", "BodyPosition", "BodyThrust", "BodyVelocity", "RocketPropulsion");
        parents$lambda$0$p(map, "PostEffect", "BloomEffect", "BlurEffect", "ColorCorrectionEffect", "DepthOfFieldEffect", "SunRaysEffect");
        parents$lambda$0$p(map, "SoundEffect", "ChorusSoundEffect", "CompressorSoundEffect", "DistortionSoundEffect", "EchoSoundEffect", "EqualizerSoundEffect", "FlangeSoundEffect", "PitchShiftSoundEffect", "ReverbSoundEffect", "TremoloSoundEffect");
        parents$lambda$0$p(map, "GuiBase3d", "PVAdornment", "SelectionLasso", "InstanceAdornment");
        parents$lambda$0$p(map, "PVAdornment", "HandleAdornment", "SelectionBox", "SelectionSphere", "HandlesBase", "SurfaceSelection");
        parents$lambda$0$p(map, "HandleAdornment", "BoxHandleAdornment", "ConeHandleAdornment", "CylinderHandleAdornment", "SphereHandleAdornment", "LineHandleAdornment", "ImageHandleAdornment", "WireframeHandleAdornment");
        parents$lambda$0$p(map, "HandlesBase", "Handles", "ArcHandles");
        parents$lambda$0$p(map, "InstanceAdornment", "Highlight");
        parents$lambda$0$p(map, "UIConstraint", "UIAspectRatioConstraint", "UISizeConstraint", "UITextSizeConstraint");
        parents$lambda$0$p(map, "UIComponent", "UIFlexItem", "UIDragDetector");
        parents$lambda$0$p(map, "ValueBase", "IntConstrainedValue", "DoubleConstrainedValue");
        parents = map;
        serviceNames = CollectionsKt.listOf(new String[]{"Workspace", "Players", "Lighting", "MaterialService", "ReplicatedFirst", "ReplicatedStorage", "ServerScriptService", "ServerStorage", "StarterGui", "StarterPack", "StarterPlayer", "Teams", "SoundService", "Chat", "TextChatService", "LocalizationService", "TestService"});
        otherServices = SetsKt.setOf(new String[]{"RunService", "TweenService", "Debris", "HttpService", "UserInputService", "ContextActionService", "CollectionService", "MarketplaceService", "DataStoreService", "Selection", "ChangeHistoryService", "PathfindingService", "TextService", "GuiService", "BadgeService", "PhysicsService", "InsertService", "CoreGui", "StudioService", "ProximityPromptService", "VRService", "HapticService", "GroupService", "MessagingService", "AssetService", "SocialService", "PolicyService", "ScriptContext", "KeyframeSequenceProvider", "AnimationClipProvider", "TeleportService", "MemoryStoreService", "RbxAnalyticsService", "AnalyticsService", "ContentProvider", "LogService", "ScriptEditorService", "Stats", "DraggerService", "PluginGuiService", "StudioAssetService", "AvatarEditorService", "VoiceChatService", "ExperienceNotificationService", "LocalizationService", "NotificationService", "GamepadService", "KeyboardService", "MouseService", "AssetImportService"});
        insertable = CollectionsKt.sorted(CollectionsKt.distinct(CollectionsKt.listOf(new String[]{"Part", "WedgePart", "CornerWedgePart", "TrussPart", "SpawnLocation", "Seat", "VehicleSeat", "MeshPart", "Model", "Folder", "Actor", "WorldModel", "Configuration", "Camera", "Attachment", "Bone", "Script", "LocalScript", "ModuleScript", "ScreenGui", "BillboardGui", "SurfaceGui", "Frame", "ScrollingFrame", "TextLabel", "TextButton", "TextBox", "ImageLabel", "ImageButton", "ViewportFrame", "VideoFrame", "CanvasGroup", "UICorner", "UIStroke", "UIListLayout", "UIGridLayout", "UIPageLayout", "UITableLayout", "UIPadding", "UIGradient", "UIScale", "UIAspectRatioConstraint", "UISizeConstraint", "UITextSizeConstraint", "UIFlexItem", "UIDragDetector", "PointLight", "SpotLight", "SurfaceLight", "ParticleEmitter", "Fire", "Smoke", "Sparkles", "Trail", "Beam", "Explosion", "ForceField", "Highlight", "SelectionBox", "SelectionSphere", "BoxHandleAdornment", "ConeHandleAdornment", "CylinderHandleAdornment", "SphereHandleAdornment", "LineHandleAdornment", "ImageHandleAdornment", "Sky", "Atmosphere", "Clouds", "BloomEffect", "BlurEffect", "ColorCorrectionEffect", "DepthOfFieldEffect", "SunRaysEffect", "Sound", "SoundGroup", "ChorusSoundEffect", "CompressorSoundEffect", "DistortionSoundEffect", "EchoSoundEffect", "EqualizerSoundEffect", "FlangeSoundEffect", "PitchShiftSoundEffect", "ReverbSoundEffect", "TremoloSoundEffect", "AudioPlayer", "AudioEmitter", "AudioListener", "AudioDeviceOutput", "AudioFader", "AudioReverb", "AudioEqualizer", "Wire", "WeldConstraint", "Weld", "Motor6D", "RopeConstraint", "RodConstraint", "SpringConstraint", "HingeConstraint", "BallSocketConstraint", "PrismaticConstraint", "CylindricalConstraint", "TorsionSpringConstraint", "UniversalConstraint", "PlaneConstraint", "RigidConstraint", "NoCollisionConstraint", "AlignPosition", "AlignOrientation", "LinearVelocity", "AngularVelocity", "VectorForce", "LineForce", "Torque", "BodyVelocity", "BodyPosition", "BodyGyro", "BodyForce", "BodyAngularVelocity", "BodyThrust", "RocketPropulsion", "SpecialMesh", "BlockMesh", "CylinderMesh", "SurfaceAppearance", "Decal", "Texture", "MaterialVariant", "Humanoid", "HumanoidDescription", "Animator", "AnimationController", "Animation", "KeyframeSequence", "IKControl", "Shirt", "Pants", "ShirtGraphic", "BodyColors", "Accessory", "Tool", "ClickDetector", "ProximityPrompt", "DragDetector", "Dialog", "DialogChoice", "PathfindingModifier", "PathfindingLink", "RemoteEvent", "RemoteFunction", "UnreliableRemoteEvent", "BindableEvent", "BindableFunction", "StringValue", "IntValue", "NumberValue", "BoolValue", "ObjectValue", "Vector3Value", "CFrameValue", "Color3Value", "BrickColorValue", "RayValue", "Team", "TextChatCommand"})));
    }

    private static final void parents$lambda$0$p(HashMap<String, String> map, String base, String... cs) {
        for (String c : cs) {
            map.put(c, base);
        }
    }

    public final String superclass(String c) {
        Intrinsics.checkNotNullParameter(c, "c");
        String str = parents.get(c);
        if (str != null) {
            return str;
        }
        if (Intrinsics.areEqual(c, "Instance")) {
            return null;
        }
        isServiceName(c);
        return "Instance";
    }

    public final boolean isA(String c, String target) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(target, "target");
        if (Intrinsics.areEqual(target, "Instance")) {
            return true;
        }
        String cur = c;
        int guard = 0;
        while (true) {
            int guard2 = guard + 1;
            if (guard >= 20) {
                return false;
            }
            if (Intrinsics.areEqual(cur, target)) {
                return true;
            }
            if (Intrinsics.areEqual(cur, "Instance")) {
                return false;
            }
            String str = parents.get(cur);
            if (str == null) {
                str = "Instance";
            }
            cur = str;
            guard = guard2;
        }
    }

    public final List<String> getServiceNames() {
        return serviceNames;
    }

    public final boolean isServiceName(String n) {
        Intrinsics.checkNotNullParameter(n, "n");
        return serviceNames.contains(n) || otherServices.contains(n);
    }

    public final List<String> getInsertable() {
        return insertable;
    }
}
