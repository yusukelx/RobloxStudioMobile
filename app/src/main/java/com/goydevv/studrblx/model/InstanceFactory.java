package com.goydevv.studrblx.model;

import androidx.recyclerview.widget.ItemTouchHelper;
import java.util.LinkedHashMap;
import java.util.Locale;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Instance.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class InstanceFactory {
    public static final InstanceFactory INSTANCE = new InstanceFactory();
    private static final Color3 GREY = Color3.INSTANCE.fromRGB(163, 162, 165);

    private InstanceFactory() {
    }

    public final Instance create(String cls) {
        Intrinsics.checkNotNullParameter(cls, "cls");
        Instance i = new Instance(cls);
        i.set("Name", cls, false);
        if (ClassDb.INSTANCE.isServiceName(cls)) {
            i.setService(true);
        }
        if (ClassDb.INSTANCE.isA(cls, "BasePart")) {
            part(i, cls);
        } else if (ClassDb.INSTANCE.isA(cls, "LuaSourceContainer")) {
            i.set("Source", defaultSource(cls), false);
            if (!Intrinsics.areEqual(cls, "ModuleScript")) {
                i.set("Disabled", false, false);
            }
        } else if (Intrinsics.areEqual(cls, "Model")) {
            i.set("WorldPivotData", new OptionalCFrame(null), false);
            i.set("PrimaryPart", null, false);
        } else if (Intrinsics.areEqual(cls, "Humanoid")) {
            i.set("Health_XML", Float.valueOf(100.0f), false);
            i.set("MaxHealth", Float.valueOf(100.0f), false);
            i.set("WalkSpeed", Float.valueOf(16.0f), false);
            i.set("JumpPower", Float.valueOf(50.0f), false);
            i.set("JumpHeight", Float.valueOf(7.2f), false);
            i.set("UseJumpPower", Boolean.TRUE, false);
            i.set("MaxSlopeAngle", Float.valueOf(89.0f), false);
            i.set("AutoRotate", Boolean.TRUE, false);
            i.set("AutoJumpEnabled", Boolean.TRUE, false);
            i.set("PlatformStand", Boolean.FALSE, false);
            i.set("Sit", Boolean.FALSE, false);
            i.set("HipHeight", Float.valueOf(0.0f), false);
            i.set("DisplayName", "", false);
        } else if (ClassDb.INSTANCE.isA(cls, "GuiObject")) {
            gui(i, cls);
        } else if (Intrinsics.areEqual(cls, "ScreenGui")) {
            i.set("Enabled", true, false);
            i.set("ResetOnSpawn", true, false);
            i.set("IgnoreGuiInset", false, false);
        } else if (ClassDb.INSTANCE.isA(cls, "Light")) {
            i.set("Brightness", Float.valueOf(1.0f), false);
            i.set("Color", new Color3(1.0f, 1.0f, 1.0f), false);
            i.set("Range", Float.valueOf(8.0f), false);
            i.set("Enabled", true, false);
            i.set("Shadows", false, false);
        } else if (Intrinsics.areEqual(cls, "StringValue")) {
            i.set("Value", "", false);
        } else if (Intrinsics.areEqual(cls, "IntValue")) {
            i.set("Value", 0L, false);
        } else if (Intrinsics.areEqual(cls, "NumberValue")) {
            i.set("Value", Double.valueOf(0.0d), false);
        } else if (Intrinsics.areEqual(cls, "BoolValue")) {
            i.set("Value", false, false);
        } else if (Intrinsics.areEqual(cls, "ObjectValue")) {
            i.set("Value", null, false);
        } else if (Intrinsics.areEqual(cls, "Vector3Value")) {
            i.set("Value", Vector3.INSTANCE.getZERO(), false);
        } else if (Intrinsics.areEqual(cls, "CFrameValue")) {
            i.set("Value", CFrame.INSTANCE.getIDENTITY(), false);
        } else if (Intrinsics.areEqual(cls, "Color3Value")) {
            i.set("Value", new Color3(0.0f, 0.0f, 0.0f), false);
        } else if (Intrinsics.areEqual(cls, "Sound")) {
            i.set("SoundId", "", false);
            i.set("Volume", Float.valueOf(0.5f), false);
            i.set("Looped", false, false);
            i.set("Playing", false, false);
            i.set("IsPlaying", false, false);
            i.set("IsPaused", false, false);
            i.set("IsLoaded", false, false);
            i.set("TimeLength", Double.valueOf(0.0d), false);
            i.set("TimePosition", Double.valueOf(0.0d), false);
            i.set("PlaybackSpeed", Float.valueOf(1.0f), false);
            i.set("PlaybackLoudness", Float.valueOf(0.0f), false);
            i.set("RollOffMinDistance", Float.valueOf(10.0f), false);
            i.set("RollOffMaxDistance", Float.valueOf(10000.0f), false);
            i.set("RollOffMode", new EnumValue(0), false);
            i.set("PlayOnRemove", false, false);
            i.set("SoundGroup", null, false);
        } else if (Intrinsics.areEqual(cls, "SoundService")) {
            i.set("AmbientReverb", new EnumValue(0), false);
            i.set("DistanceFactor", Float.valueOf(3.33f), false);
            i.set("DopplerScale", Float.valueOf(1.0f), false);
            i.set("RolloffScale", Float.valueOf(1.0f), false);
            i.set("RespectFilteringEnabled", true, false);
        } else if (Intrinsics.areEqual(cls, "UICorner")) {
            i.set("CornerRadius", new UDim(0.0f, 8), false);
        } else if (Intrinsics.areEqual(cls, "UIStroke")) {
            i.set("Color", new Color3(0.0f, 0.0f, 0.0f), false);
            i.set("Thickness", Float.valueOf(1.0f), false);
            i.set("Transparency", Float.valueOf(0.0f), false);
        } else if (Intrinsics.areEqual(cls, "UIListLayout")) {
            i.set("Padding", new UDim(0.0f, 0), false);
            i.set("FillDirection", new EnumValue(1), false);
            i.set("SortOrder", new EnumValue(2), false);
        } else if (Intrinsics.areEqual(cls, "UIPadding")) {
            for (String s : CollectionsKt.listOf(new String[]{"PaddingTop", "PaddingBottom", "PaddingLeft", "PaddingRight"})) {
                i.set(s, new UDim(0.0f, 0), false);
            }
        } else if (Intrinsics.areEqual(cls, "Attachment")) {
            i.set("CFrame", CFrame.INSTANCE.getIDENTITY(), false);
        } else if (Intrinsics.areEqual(cls, "Weld") || Intrinsics.areEqual(cls, "Motor6D")) {
            i.set("Part0", null, false);
            i.set("Part1", null, false);
            i.set("C0", CFrame.INSTANCE.getIDENTITY(), false);
            i.set("C1", CFrame.INSTANCE.getIDENTITY(), false);
        } else if (Intrinsics.areEqual(cls, "WeldConstraint")) {
            i.set("Part0Internal", null, false);
            i.set("Part1Internal", null, false);
        } else if (Intrinsics.areEqual(cls, "ClickDetector")) {
            i.set("MaxActivationDistance", Float.valueOf(32.0f), false);
        } else if (Intrinsics.areEqual(cls, "Decal") || Intrinsics.areEqual(cls, "Texture")) {
            i.set("Texture", "", false);
            i.set("Face", new EnumValue(5), false);
            i.set("Transparency", Float.valueOf(0.0f), false);
        } else if (Intrinsics.areEqual(cls, "SpecialMesh")) {
            i.set("MeshType", new EnumValue(6), false);
            i.set("Scale", Vector3.INSTANCE.getONE(), false);
            i.set("MeshId", "", false);
        } else if (Intrinsics.areEqual(cls, "Tool")) {
            i.set("RequiresHandle", true, false);
            i.set("CanBeDropped", true, false);
            i.set("ToolTip", "", false);
        } else if (Intrinsics.areEqual(cls, "Camera")) {
            i.set("CFrame", CFrame.INSTANCE.getIDENTITY(), false);
            i.set("FieldOfView", Float.valueOf(70.0f), false);
            i.set("CameraType", new EnumValue(0), false);
        } else if (Intrinsics.areEqual(cls, "Lighting")) {
            i.set("ClockTime", Float.valueOf(14.0f), false);
            i.set("Brightness", Float.valueOf(2.0f), false);
            i.set("Ambient", Color3.INSTANCE.fromRGB(70, 70, 70), false);
            i.set("OutdoorAmbient", Color3.INSTANCE.fromRGB(128, 128, 128), false);
        } else if (Intrinsics.areEqual(cls, "Workspace")) {
            i.set("Gravity", Float.valueOf(196.2f), false);
            i.set("FallenPartsDestroyHeight", Float.valueOf(-500.0f), false);
        } else if (Intrinsics.areEqual(cls, "Players")) {
            i.set("MaxPlayers", 12, false);
            i.set("RespawnTime", Float.valueOf(5.0f), false);
        } else if (Intrinsics.areEqual(cls, "ProximityPrompt")) {
            i.set("ActionText", "Interact", false);
            i.set("ObjectText", "", false);
            i.set("MaxActivationDistance", Float.valueOf(10.0f), false);
            i.set("HoldDuration", Float.valueOf(0.0f), false);
        } else if (Intrinsics.areEqual(cls, "ParticleEmitter")) {
            i.set("Rate", Float.valueOf(20.0f), false);
            i.set("Enabled", true, false);
        } else if (Intrinsics.areEqual(cls, "Highlight")) {
            i.set("FillColor", new Color3(1.0f, 0.0f, 0.0f), false);
            i.set("OutlineColor", new Color3(1.0f, 1.0f, 1.0f), false);
            i.set("FillTransparency", Float.valueOf(0.5f), false);
            i.set("Enabled", true, false);
        } else if (Intrinsics.areEqual(cls, "BillboardGui") || Intrinsics.areEqual(cls, "SurfaceGui")) {
            i.set("Enabled", true, false);
            i.set("Adornee", null, false);
        } else if (Intrinsics.areEqual(cls, "Team")) {
            i.set("TeamColor", new BrickColorV(194), false);
            i.set("AutoAssignable", true, false);
        } else if (Intrinsics.areEqual(cls, "Animation")) {
            i.set("AnimationId", "", false);
        } else if (Intrinsics.areEqual(cls, "MaterialVariant")) {
            i.set("BaseMaterial", new EnumValue(256), false);
            for (String m : CollectionsKt.listOf(new String[]{"ColorMap", "NormalMap", "MetalnessMap", "RoughnessMap"})) {
                i.set(m, "", false);
            }
            i.set("StudsPerTile", Float.valueOf(10.0f), false);
            i.set("MaterialPattern", new EnumValue(0), false);
        } else if (Intrinsics.areEqual(cls, "MaterialService")) {
            i.set("Use2022MaterialsXml", true, false);
            LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get("Material");
            Intrinsics.checkNotNull(linkedHashMap);
            for (String str : linkedHashMap.keySet()) {
                Intrinsics.checkNotNullExpressionValue(str, "next(...)");
                String name = str;
                if (!SetsKt.setOf(new String[]{"Air", "Water", "Neon", "Glass", "ForceField"}).contains(name)) {
                    i.set(name + "Name", name, false);
                }
            }
        }
        extraDefaults(i, cls);
        return i;
    }

    private static final void extraDefaults$s(Instance $i, String n, Object v) {
        if ($i.getProps().containsKey(n)) {
            return;
        }
        $i.set(n, v, false);
    }

    /* JADX WARN: Code duplicated, block: B:233:0x0b2f  */
    /* JADX WARN: Code duplicated, block: B:236:0x0b38  */
    /* JADX WARN: Code duplicated, block: B:239:0x0b56  */
    /* JADX WARN: Code duplicated, block: B:241:0x0b5e  */
    /* JADX WARN: Code duplicated, block: B:242:0x0b6e  */
    /* JADX WARN: Code duplicated, block: B:244:0x0b78  */
    /* JADX WARN: Code duplicated, block: B:245:0x0b7f  */
    /* JADX WARN: Code duplicated, block: B:247:0x0b89  */
    /* JADX WARN: Code duplicated, block: B:248:0x0b93  */
    /* JADX WARN: Code duplicated, block: B:251:0x0b9d  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void extraDefaults(Instance i, String cls) {
        String str;
        Float fValueOf;
        Vector3 v0 = Vector3.INSTANCE.getZERO();
        if (ClassDb.INSTANCE.isA(cls, "Constraint")) {
            extraDefaults$s(i, "Attachment0", null);
            extraDefaults$s(i, "Attachment1", null);
            extraDefaults$s(i, "Enabled", true);
            extraDefaults$s(i, "Visible", Boolean.valueOf(Intrinsics.areEqual(cls, "RopeConstraint") || Intrinsics.areEqual(cls, "RodConstraint") || Intrinsics.areEqual(cls, "SpringConstraint")));
            extraDefaults$s(i, "Color", new BrickColorV(194));
        }
        switch (cls.hashCode()) {
            case -2068335800:
                if (cls.equals("VectorForce")) {
                    extraDefaults$s(i, "Force", new Vector3(1000.0f, 0.0f, 0.0f));
                    extraDefaults$s(i, "RelativeTo", new EnumValue(0));
                    extraDefaults$s(i, "ApplyAtCenterOfMass", false);
                    break;
                }
                break;
            case -1985297413:
                if (!cls.equals("EchoSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case -1973047954:
                if (!cls.equals("EqualizerSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case -1957276939:
                if (cls.equals("Explosion")) {
                    extraDefaults$s(i, "BlastRadius", Float.valueOf(4.0f));
                    extraDefaults$s(i, "BlastPressure", Float.valueOf(500000.0f));
                    extraDefaults$s(i, "Position", v0);
                    extraDefaults$s(i, "Visible", true);
                    extraDefaults$s(i, "DestroyJointRadiusPercent", Float.valueOf(1.0f));
                }
                break;
            case -1954154801:
                if (cls.equals("BrickColorValue")) {
                    extraDefaults$s(i, "Value", new BrickColorV(194));
                }
                break;
            case -1894814529:
                if (cls.equals("BodyVelocity")) {
                    extraDefaults$s(i, "MaxForce", new Vector3(4000.0f, 4000.0f, 4000.0f));
                    extraDefaults$s(i, "Velocity", new Vector3(0.0f, 2.0f, 0.0f));
                    extraDefaults$s(i, "P", Float.valueOf(1250.0f));
                }
                break;
            case -1784099286:
                if (cls.equals("Torque")) {
                    extraDefaults$s(i, "Torque", v0);
                    extraDefaults$s(i, "RelativeTo", new EnumValue(0));
                }
                break;
            case -1768610876:
                if (cls.equals("UIGridLayout")) {
                    extraDefaults$s(i, "CellSize", new UDim2(0.0f, 100, 0.0f, 100));
                    extraDefaults$s(i, "CellPadding", new UDim2(0.0f, 5, 0.0f, 5));
                    extraDefaults$s(i, "SortOrder", new EnumValue(2));
                    extraDefaults$s(i, "FillDirection", new EnumValue(0));
                }
                break;
            case -1731509306:
                if (cls.equals("SunRaysEffect")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Intensity", Float.valueOf(0.25f));
                    extraDefaults$s(i, "Spread", Float.valueOf(1.0f));
                }
                break;
            case -1604142757:
                if (!cls.equals("CompressorSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case -1539376451:
                if (cls.equals("BodyAngularVelocity")) {
                    extraDefaults$s(i, "AngularVelocity", new Vector3(0.0f, 2.0f, 0.0f));
                    extraDefaults$s(i, "MaxTorque", new Vector3(4000.0f, 4000.0f, 4000.0f));
                    extraDefaults$s(i, "P", Float.valueOf(1250.0f));
                }
                break;
            case -1473814200:
                if (cls.equals("HingeConstraint")) {
                    extraDefaults$s(i, "ActuatorType", new EnumValue(0));
                    extraDefaults$s(i, "AngularVelocity", Float.valueOf(0.0f));
                    extraDefaults$s(i, "MotorMaxTorque", Float.valueOf(0.0f));
                    extraDefaults$s(i, "AngularSpeed", Float.valueOf(0.0f));
                    extraDefaults$s(i, "ServoMaxTorque", Float.valueOf(0.0f));
                    extraDefaults$s(i, "TargetAngle", Float.valueOf(0.0f));
                    extraDefaults$s(i, "LimitsEnabled", false);
                    extraDefaults$s(i, "LowerAngle", Float.valueOf(-45.0f));
                    extraDefaults$s(i, "UpperAngle", Float.valueOf(45.0f));
                    extraDefaults$s(i, "Restitution", Float.valueOf(0.0f));
                }
                break;
            case -1465631249:
                if (cls.equals("BallSocketConstraint")) {
                    extraDefaults$s(i, "LimitsEnabled", false);
                    extraDefaults$s(i, "UpperAngle", Float.valueOf(45.0f));
                    extraDefaults$s(i, "TwistLimitsEnabled", false);
                    extraDefaults$s(i, "Restitution", Float.valueOf(0.0f));
                }
                break;
            case -1464734529:
                if (!cls.equals("SelectionBox")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "LineThickness", Float.valueOf(0.15f));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "SurfaceTransparency", Float.valueOf(1.0f));
                break;
            case -1418764294:
                if (!cls.equals("BlockMesh")) {
                }
                extraDefaults$s(i, "Scale", Vector3.INSTANCE.getONE());
                extraDefaults$s(i, "Offset", v0);
                break;
            case -1178427315:
                if (cls.equals("UIPageLayout")) {
                    extraDefaults$s(i, "Animated", true);
                    extraDefaults$s(i, "Circular", false);
                    extraDefaults$s(i, "Padding", new UDim(0.0f, 0));
                    extraDefaults$s(i, "SortOrder", new EnumValue(2));
                }
                break;
            case -1173311438:
                if (cls.equals("ShirtGraphic")) {
                    extraDefaults$s(i, "Graphic", "");
                    extraDefaults$s(i, "Color3", new Color3(1.0f, 1.0f, 1.0f));
                }
                break;
            case -1061210551:
                if (cls.equals("BodyForce")) {
                    extraDefaults$s(i, "Force", v0);
                }
                break;
            case -1036664164:
                if (cls.equals("UIAspectRatioConstraint")) {
                    extraDefaults$s(i, "AspectRatio", Float.valueOf(1.0f));
                    extraDefaults$s(i, "AspectType", new EnumValue(0));
                    extraDefaults$s(i, "DominantAxis", new EnumValue(0));
                }
                break;
            case -942455468:
                if (cls.equals("BloomEffect")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Intensity", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Size", Float.valueOf(24.0f));
                    extraDefaults$s(i, "Threshold", Float.valueOf(2.0f));
                }
                break;
            case -798759950:
                if (cls.equals("ColorCorrectionEffect")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Brightness", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Contrast", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Saturation", Float.valueOf(0.0f));
                    extraDefaults$s(i, "TintColor", new Color3(1.0f, 1.0f, 1.0f));
                }
                break;
            case -775798944:
                if (cls.equals("AudioFader")) {
                    extraDefaults$s(i, "Volume", Float.valueOf(1.0f));
                }
                break;
            case -775064246:
                if (!cls.equals("LineHandleAdornment")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "AlwaysOnTop", false);
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                extraDefaults$s(i, "ZIndex", -1);
                switch (cls.hashCode()) {
                    case -775064246:
                        if (!cls.equals("LineHandleAdornment")) {
                            extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                            str = "Thickness";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                            break;
                        }
                        break;
                    case -545500461:
                        if (cls.equals("BoxHandleAdornment")) {
                            extraDefaults$s(i, "Size", new Vector3(1.0f, 1.0f, 1.0f));
                        }
                        break;
                    case -315262895:
                        if (cls.equals("SphereHandleAdornment")) {
                            str = "Radius";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                        }
                        break;
                    case 146148675:
                        if (cls.equals("ImageHandleAdornment")) {
                            extraDefaults$s(i, "Image", "");
                            extraDefaults$s(i, "Size", new Vector2(1.0f, 1.0f));
                        }
                        break;
                    case 842748475:
                        if (!cls.equals("ConeHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                    case 1008553256:
                        if (!cls.equals("CylinderHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                }
                break;
            case -759592646:
                if (cls.equals("DragDetector")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "MaxActivationDistance", Float.valueOf(32.0f));
                    extraDefaults$s(i, "DragStyle", new EnumValue(0));
                    extraDefaults$s(i, "ResponseStyle", new EnumValue(1));
                }
                break;
            case -715495353:
                if (cls.equals("ViewportFrame")) {
                    extraDefaults$s(i, "CurrentCamera", null);
                    extraDefaults$s(i, "ImageColor3", new Color3(1.0f, 1.0f, 1.0f));
                    extraDefaults$s(i, "Ambient", Color3.INSTANCE.fromRGB(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION));
                }
                break;
            case -597429729:
                if (cls.equals("HumanoidDescription")) {
                    extraDefaults$s(i, "HeadScale", Float.valueOf(1.0f));
                    extraDefaults$s(i, "HeightScale", Float.valueOf(1.0f));
                    extraDefaults$s(i, "WidthScale", Float.valueOf(1.0f));
                    extraDefaults$s(i, "DepthScale", Float.valueOf(1.0f));
                    extraDefaults$s(i, "BodyTypeScale", Float.valueOf(0.3f));
                    extraDefaults$s(i, "ProportionScale", Float.valueOf(1.0f));
                }
                break;
            case -545500461:
                if (!cls.equals("BoxHandleAdornment")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "AlwaysOnTop", false);
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                extraDefaults$s(i, "ZIndex", -1);
                switch (cls.hashCode()) {
                    case -775064246:
                        if (!cls.equals("LineHandleAdornment")) {
                            extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                            str = "Thickness";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                            break;
                        }
                        break;
                    case -545500461:
                        if (cls.equals("BoxHandleAdornment")) {
                            extraDefaults$s(i, "Size", new Vector3(1.0f, 1.0f, 1.0f));
                        }
                        break;
                    case -315262895:
                        if (cls.equals("SphereHandleAdornment")) {
                            str = "Radius";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                        }
                        break;
                    case 146148675:
                        if (cls.equals("ImageHandleAdornment")) {
                            extraDefaults$s(i, "Image", "");
                            extraDefaults$s(i, "Size", new Vector2(1.0f, 1.0f));
                        }
                        break;
                    case 842748475:
                        if (!cls.equals("ConeHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                    case 1008553256:
                        if (!cls.equals("CylinderHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                }
                break;
            case -315262895:
                if (!cls.equals("SphereHandleAdornment")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "AlwaysOnTop", false);
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                extraDefaults$s(i, "ZIndex", -1);
                switch (cls.hashCode()) {
                    case -775064246:
                        if (!cls.equals("LineHandleAdornment")) {
                            extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                            str = "Thickness";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                            break;
                        }
                        break;
                    case -545500461:
                        if (cls.equals("BoxHandleAdornment")) {
                            extraDefaults$s(i, "Size", new Vector3(1.0f, 1.0f, 1.0f));
                        }
                        break;
                    case -315262895:
                        if (cls.equals("SphereHandleAdornment")) {
                            str = "Radius";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                        }
                        break;
                    case 146148675:
                        if (cls.equals("ImageHandleAdornment")) {
                            extraDefaults$s(i, "Image", "");
                            extraDefaults$s(i, "Size", new Vector2(1.0f, 1.0f));
                        }
                        break;
                    case 842748475:
                        if (!cls.equals("ConeHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                    case 1008553256:
                        if (!cls.equals("CylinderHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                }
                break;
            case -226963481:
                if (cls.equals("CanvasGroup")) {
                    extraDefaults$s(i, "GroupColor3", new Color3(1.0f, 1.0f, 1.0f));
                    extraDefaults$s(i, "GroupTransparency", Float.valueOf(0.0f));
                }
                break;
            case -202441402:
                if (cls.equals("TextChatCommand")) {
                    extraDefaults$s(i, "PrimaryAlias", "");
                    extraDefaults$s(i, "SecondaryAlias", "");
                    extraDefaults$s(i, "Enabled", true);
                }
                break;
            case -181039584:
                cls.equals("AnimationController");
                break;
            case -130011794:
                if (cls.equals("SurfaceGui")) {
                    extraDefaults$s(i, "Face", new EnumValue(5));
                    extraDefaults$s(i, "SizingMode", new EnumValue(1));
                    extraDefaults$s(i, "PixelsPerStud", Float.valueOf(50.0f));
                    extraDefaults$s(i, "CanvasSize", new Vector2(800.0f, 600.0f));
                    extraDefaults$s(i, "LightInfluence", Float.valueOf(1.0f));
                    extraDefaults$s(i, "AlwaysOnTop", false);
                }
                break;
            case -111055118:
                if (cls.equals("Accessory")) {
                    extraDefaults$s(i, "AttachmentPoint", CFrame.INSTANCE.getIDENTITY());
                }
                break;
            case -70112565:
                if (!cls.equals("CylindricalConstraint")) {
                }
                extraDefaults$s(i, "ActuatorType", new EnumValue(0));
                extraDefaults$s(i, "LimitsEnabled", false);
                extraDefaults$s(i, "LowerLimit", Float.valueOf(0.0f));
                extraDefaults$s(i, "UpperLimit", Float.valueOf(5.0f));
                extraDefaults$s(i, "Velocity", Float.valueOf(0.0f));
                extraDefaults$s(i, "Speed", Float.valueOf(0.0f));
                extraDefaults$s(i, "TargetPosition", Float.valueOf(0.0f));
                extraDefaults$s(i, "MotorMaxForce", Float.valueOf(0.0f));
                extraDefaults$s(i, "ServoMaxForce", Float.valueOf(0.0f));
                break;
            case 83201:
                if (cls.equals("Sky")) {
                    for (String f : CollectionsKt.listOf(new String[]{"Bk", "Dn", "Ft", "Lf", "Rt", "Up"})) {
                        String lowerCase = f.toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                        extraDefaults$s(i, "Skybox" + f, "rbxasset://textures/sky/sky512_" + lowerCase + ".tex");
                    }
                    extraDefaults$s(i, "CelestialBodiesShown", true);
                    extraDefaults$s(i, "StarCount", Integer.valueOf(3000));
                    extraDefaults$s(i, "SunAngularSize", Float.valueOf(21.0f));
                    extraDefaults$s(i, "MoonAngularSize", Float.valueOf(11.0f));
                    extraDefaults$s(i, "SkyboxOrientation", v0);
                    extraDefaults$s(i, "SunTextureId", "rbxasset://sky/sun.jpg");
                    extraDefaults$s(i, "MoonTextureId", "rbxasset://sky/moon.jpg");
                }
                break;
            case 2066383:
                if (cls.equals("Beam")) {
                    extraDefaults$s(i, "Attachment0", null);
                    extraDefaults$s(i, "Attachment1", null);
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Width0", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Width1", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Segments", 10);
                    extraDefaults$s(i, "CurveSize0", Float.valueOf(0.0f));
                    extraDefaults$s(i, "CurveSize1", Float.valueOf(0.0f));
                    extraDefaults$s(i, "FaceCamera", false);
                    extraDefaults$s(i, "Texture", "");
                    extraDefaults$s(i, "LightEmission", Float.valueOf(0.0f));
                }
                break;
            case 2076388:
                if (cls.equals("Bone")) {
                    extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                }
                break;
            case 2189910:
                if (cls.equals("Fire")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Heat", Float.valueOf(9.0f));
                    extraDefaults$s(i, "Size", Float.valueOf(5.0f));
                    extraDefaults$s(i, "Color", Color3.INSTANCE.fromRGB(236, 139, 70));
                    extraDefaults$s(i, "SecondaryColor", Color3.INSTANCE.fromRGB(139, 80, 55));
                }
                break;
            case 2696357:
                if (cls.equals("Wire")) {
                    extraDefaults$s(i, "SourceInstance", null);
                    extraDefaults$s(i, "TargetInstance", null);
                }
                break;
            case 27852911:
                if (cls.equals("KeyframeSequence")) {
                    extraDefaults$s(i, "Loop", true);
                    extraDefaults$s(i, "Priority", new EnumValue(2));
                }
                break;
            case 76880828:
                if (cls.equals("Pants")) {
                    extraDefaults$s(i, "PantsTemplate", "");
                    extraDefaults$s(i, "Color3", new Color3(1.0f, 1.0f, 1.0f));
                }
                break;
            case 79855062:
                if (cls.equals("Shirt")) {
                    extraDefaults$s(i, "ShirtTemplate", "");
                    extraDefaults$s(i, "Color3", new Color3(1.0f, 1.0f, 1.0f));
                }
                break;
            case 80009551:
                if (cls.equals("Smoke")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Opacity", Float.valueOf(0.5f));
                    extraDefaults$s(i, "RiseVelocity", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Size", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Color", new Color3(1.0f, 1.0f, 1.0f));
                }
                break;
            case 81068518:
                if (cls.equals("Trail")) {
                    extraDefaults$s(i, "Attachment0", null);
                    extraDefaults$s(i, "Attachment1", null);
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Lifetime", Float.valueOf(2.0f));
                    extraDefaults$s(i, "MinLength", Float.valueOf(0.1f));
                    extraDefaults$s(i, "FaceCamera", false);
                    extraDefaults$s(i, "Texture", "");
                    extraDefaults$s(i, "LightEmission", Float.valueOf(0.0f));
                }
                break;
            case 87459113:
                if (cls.equals("DialogChoice")) {
                    extraDefaults$s(i, "UserDialog", "");
                    extraDefaults$s(i, "ResponseDialog", "");
                    extraDefaults$s(i, "GoodbyeDialog", "");
                }
                break;
            case 126027586:
                if (cls.equals("LinearVelocity")) {
                    extraDefaults$s(i, "MaxForce", Float.valueOf(1000.0f));
                    extraDefaults$s(i, "VectorVelocity", v0);
                    extraDefaults$s(i, "VelocityConstraintMode", new EnumValue(2));
                    extraDefaults$s(i, "RelativeTo", new EnumValue(2));
                    extraDefaults$s(i, "LineVelocity", Float.valueOf(0.0f));
                    extraDefaults$s(i, "ForceLimitsEnabled", true);
                }
                break;
            case 146148675:
                if (!cls.equals("ImageHandleAdornment")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "AlwaysOnTop", false);
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                extraDefaults$s(i, "ZIndex", -1);
                switch (cls.hashCode()) {
                    case -775064246:
                        if (!cls.equals("LineHandleAdornment")) {
                            extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                            str = "Thickness";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                            break;
                        }
                        break;
                    case -545500461:
                        if (cls.equals("BoxHandleAdornment")) {
                            extraDefaults$s(i, "Size", new Vector3(1.0f, 1.0f, 1.0f));
                        }
                        break;
                    case -315262895:
                        if (cls.equals("SphereHandleAdornment")) {
                            str = "Radius";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                        }
                        break;
                    case 146148675:
                        if (cls.equals("ImageHandleAdornment")) {
                            extraDefaults$s(i, "Image", "");
                            extraDefaults$s(i, "Size", new Vector2(1.0f, 1.0f));
                        }
                        break;
                    case 842748475:
                        if (!cls.equals("ConeHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                    case 1008553256:
                        if (!cls.equals("CylinderHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                }
                break;
            case 158759324:
                if (cls.equals("BillboardGui")) {
                    extraDefaults$s(i, "Size", new UDim2(0.0f, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0.0f, 50));
                    extraDefaults$s(i, "AlwaysOnTop", false);
                    extraDefaults$s(i, "StudsOffset", v0);
                    extraDefaults$s(i, "MaxDistance", Float.valueOf(Float.MAX_VALUE));
                    extraDefaults$s(i, "LightInfluence", Float.valueOf(0.0f));
                }
                break;
            case 184673079:
                if (cls.equals("LineForce")) {
                    extraDefaults$s(i, "Magnitude", Float.valueOf(1000.0f));
                    extraDefaults$s(i, "ApplyAtCenterOfMass", false);
                    extraDefaults$s(i, "InverseSquareLaw", false);
                    extraDefaults$s(i, "ReactionForceEnabled", false);
                }
                break;
            case 197757176:
                if (cls.equals("BlurEffect")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Size", Float.valueOf(24.0f));
                }
                break;
            case 212713098:
                if (!cls.equals("AudioListener")) {
                }
                extraDefaults$s(i, "AudioInteractionGroup", "");
                break;
            case 239868547:
                if (!cls.equals("DistortionSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case 247127823:
                if (!cls.equals("FlangeSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case 258273070:
                if (cls.equals("AlignPosition")) {
                    extraDefaults$s(i, "MaxForce", Float.valueOf(10000.0f));
                    extraDefaults$s(i, "MaxVelocity", Float.valueOf(Float.MAX_VALUE));
                    extraDefaults$s(i, "Responsiveness", Float.valueOf(10.0f));
                    extraDefaults$s(i, "RigidityEnabled", false);
                    extraDefaults$s(i, "Mode", new EnumValue(1));
                    extraDefaults$s(i, "Position", v0);
                    extraDefaults$s(i, "ApplyAtCenterOfMass", false);
                }
                break;
            case 298027798:
                if (cls.equals("UIScale")) {
                    extraDefaults$s(i, "Scale", Float.valueOf(1.0f));
                }
                break;
            case 352458194:
                if (cls.equals("VideoFrame")) {
                    extraDefaults$s(i, "Video", "");
                    extraDefaults$s(i, "Looped", false);
                    extraDefaults$s(i, "Playing", false);
                    extraDefaults$s(i, "Volume", Float.valueOf(1.0f));
                }
                break;
            case 464782595:
                if (cls.equals("RocketPropulsion")) {
                    extraDefaults$s(i, "Target", null);
                    extraDefaults$s(i, "MaxSpeed", Float.valueOf(30.0f));
                    extraDefaults$s(i, "MaxThrust", Float.valueOf(4000.0f));
                    extraDefaults$s(i, "ThrustP", Float.valueOf(5.0f));
                    extraDefaults$s(i, "ThrustD", Float.valueOf(0.001f));
                    extraDefaults$s(i, "CartoonFactor", Float.valueOf(0.7f));
                }
                break;
            case 531860926:
                if (cls.equals("PathfindingLink")) {
                    extraDefaults$s(i, "Attachment0", null);
                    extraDefaults$s(i, "Attachment1", null);
                    extraDefaults$s(i, "IsBidirectional", true);
                    extraDefaults$s(i, "Label", "");
                }
                break;
            case 616617863:
                if (cls.equals("RayValue")) {
                    extraDefaults$s(i, "Value", new Ray(v0, v0));
                }
                break;
            case 624633259:
                if (cls.equals("AlignOrientation")) {
                    extraDefaults$s(i, "MaxTorque", Float.valueOf(10000.0f));
                    extraDefaults$s(i, "MaxAngularVelocity", Float.valueOf(Float.MAX_VALUE));
                    extraDefaults$s(i, "Responsiveness", Float.valueOf(10.0f));
                    extraDefaults$s(i, "RigidityEnabled", false);
                    extraDefaults$s(i, "Mode", new EnumValue(1));
                    extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                }
                break;
            case 720648514:
                if (cls.equals("Atmosphere")) {
                    extraDefaults$s(i, "Density", Float.valueOf(0.395f));
                    extraDefaults$s(i, "Offset", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Color", Color3.INSTANCE.fromRGB(199, 199, 199));
                    extraDefaults$s(i, "Decay", Color3.INSTANCE.fromRGB(106, 112, 125));
                    extraDefaults$s(i, "Glare", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Haze", Float.valueOf(0.0f));
                }
                break;
            case 842748475:
                if (!cls.equals("ConeHandleAdornment")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "AlwaysOnTop", false);
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                extraDefaults$s(i, "ZIndex", -1);
                switch (cls.hashCode()) {
                    case -775064246:
                        if (!cls.equals("LineHandleAdornment")) {
                            extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                            str = "Thickness";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                            break;
                        }
                        break;
                    case -545500461:
                        if (cls.equals("BoxHandleAdornment")) {
                            extraDefaults$s(i, "Size", new Vector3(1.0f, 1.0f, 1.0f));
                        }
                        break;
                    case -315262895:
                        if (cls.equals("SphereHandleAdornment")) {
                            str = "Radius";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                        }
                        break;
                    case 146148675:
                        if (cls.equals("ImageHandleAdornment")) {
                            extraDefaults$s(i, "Image", "");
                            extraDefaults$s(i, "Size", new Vector2(1.0f, 1.0f));
                        }
                        break;
                    case 842748475:
                        if (!cls.equals("ConeHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                    case 1008553256:
                        if (!cls.equals("CylinderHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                }
                break;
            case 939740415:
                if (cls.equals("UITextSizeConstraint")) {
                    extraDefaults$s(i, "MaxTextSize", 100);
                    extraDefaults$s(i, "MinTextSize", 1);
                }
                break;
            case 978161873:
                if (cls.equals("SurfaceAppearance")) {
                    extraDefaults$s(i, "ColorMap", "");
                    extraDefaults$s(i, "NormalMap", "");
                    extraDefaults$s(i, "MetalnessMap", "");
                    extraDefaults$s(i, "RoughnessMap", "");
                    extraDefaults$s(i, "AlphaMode", new EnumValue(0));
                }
                break;
            case 1008553256:
                if (!cls.equals("CylinderHandleAdornment")) {
                }
                extraDefaults$s(i, "Adornee", null);
                extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                extraDefaults$s(i, "AlwaysOnTop", false);
                extraDefaults$s(i, "Visible", true);
                extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                extraDefaults$s(i, "ZIndex", -1);
                switch (cls.hashCode()) {
                    case -775064246:
                        if (!cls.equals("LineHandleAdornment")) {
                            extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                            str = "Thickness";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                            break;
                        }
                        break;
                    case -545500461:
                        if (cls.equals("BoxHandleAdornment")) {
                            extraDefaults$s(i, "Size", new Vector3(1.0f, 1.0f, 1.0f));
                        }
                        break;
                    case -315262895:
                        if (cls.equals("SphereHandleAdornment")) {
                            str = "Radius";
                            fValueOf = Float.valueOf(1.0f);
                            extraDefaults$s(i, str, fValueOf);
                        }
                        break;
                    case 146148675:
                        if (cls.equals("ImageHandleAdornment")) {
                            extraDefaults$s(i, "Image", "");
                            extraDefaults$s(i, "Size", new Vector2(1.0f, 1.0f));
                        }
                        break;
                    case 842748475:
                        if (!cls.equals("ConeHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                    case 1008553256:
                        if (!cls.equals("CylinderHandleAdornment")) {
                        }
                        extraDefaults$s(i, "Height", Float.valueOf(2.0f));
                        extraDefaults$s(i, "Radius", Float.valueOf(0.5f));
                        break;
                }
                break;
            case 1013696779:
                if (cls.equals("BodyPosition")) {
                    extraDefaults$s(i, "MaxForce", new Vector3(4000.0f, 4000.0f, 4000.0f));
                    extraDefaults$s(i, "Position", new Vector3(0.0f, 50.0f, 0.0f));
                    extraDefaults$s(i, "D", Float.valueOf(1250.0f));
                    extraDefaults$s(i, "P", Float.valueOf(10000.0f));
                }
                break;
            case 1047820017:
                if (cls.equals("DepthOfFieldEffect")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "FarIntensity", Float.valueOf(0.75f));
                    extraDefaults$s(i, "FocusDistance", Float.valueOf(0.05f));
                    extraDefaults$s(i, "InFocusRadius", Float.valueOf(10.0f));
                    extraDefaults$s(i, "NearIntensity", Float.valueOf(0.75f));
                }
                break;
            case 1108929283:
                if (!cls.equals("CylinderMesh")) {
                }
                extraDefaults$s(i, "Scale", Vector3.INSTANCE.getONE());
                extraDefaults$s(i, "Offset", v0);
                break;
            case 1114910254:
                if (!cls.equals("ReverbSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case 1120150719:
                if (cls.equals("AngularVelocity")) {
                    extraDefaults$s(i, "MaxTorque", Float.valueOf(0.0f));
                    extraDefaults$s(i, "AngularVelocity", v0);
                    extraDefaults$s(i, "RelativeTo", new EnumValue(2));
                }
                break;
            case 1242640088:
                if (!cls.equals("AudioEmitter")) {
                }
                extraDefaults$s(i, "AudioInteractionGroup", "");
                break;
            case 1258517419:
                if (!cls.equals("PrismaticConstraint")) {
                }
                extraDefaults$s(i, "ActuatorType", new EnumValue(0));
                extraDefaults$s(i, "LimitsEnabled", false);
                extraDefaults$s(i, "LowerLimit", Float.valueOf(0.0f));
                extraDefaults$s(i, "UpperLimit", Float.valueOf(5.0f));
                extraDefaults$s(i, "Velocity", Float.valueOf(0.0f));
                extraDefaults$s(i, "Speed", Float.valueOf(0.0f));
                extraDefaults$s(i, "TargetPosition", Float.valueOf(0.0f));
                extraDefaults$s(i, "MotorMaxForce", Float.valueOf(0.0f));
                extraDefaults$s(i, "ServoMaxForce", Float.valueOf(0.0f));
                break;
            case 1262893199:
                if (cls.equals("RopeConstraint")) {
                    extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                    extraDefaults$s(i, "Restitution", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Thickness", Float.valueOf(0.1f));
                    extraDefaults$s(i, "WinchEnabled", false);
                    extraDefaults$s(i, "WinchSpeed", Float.valueOf(2.0f));
                    extraDefaults$s(i, "WinchTarget", Float.valueOf(5.0f));
                    extraDefaults$s(i, "WinchForce", Float.valueOf(10000.0f));
                }
                break;
            case 1316258612:
                if (!cls.equals("TremoloSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case 1355648740:
                if (cls.equals("RodConstraint")) {
                    extraDefaults$s(i, "Length", Float.valueOf(5.0f));
                    extraDefaults$s(i, "Thickness", Float.valueOf(0.1f));
                    extraDefaults$s(i, "LimitsEnabled", false);
                    extraDefaults$s(i, "LimitAngle0", Float.valueOf(90.0f));
                    extraDefaults$s(i, "LimitAngle1", Float.valueOf(90.0f));
                }
                break;
            case 1376157138:
                if (cls.equals("BodyColors")) {
                    for (String k : CollectionsKt.listOf(new String[]{"HeadColor3", "LeftArmColor3", "RightArmColor3", "LeftLegColor3", "RightLegColor3", "TorsoColor3"})) {
                        extraDefaults$s(i, k, Color3.INSTANCE.fromRGB(163, 162, 165));
                    }
                }
                break;
            case 1382795739:
                if (cls.equals("IKControl")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Weight", Float.valueOf(1.0f));
                    extraDefaults$s(i, "ChainRoot", null);
                    extraDefaults$s(i, "EndEffector", null);
                    extraDefaults$s(i, "Target", null);
                }
                break;
            case 1448617049:
                if (cls.equals("SelectionSphere")) {
                    extraDefaults$s(i, "Adornee", null);
                    extraDefaults$s(i, "Color3", Color3.INSTANCE.fromRGB(13, 105, 172));
                    extraDefaults$s(i, "LineThickness", Float.valueOf(0.15f));
                    extraDefaults$s(i, "Transparency", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Visible", true);
                    extraDefaults$s(i, "SurfaceTransparency", Float.valueOf(1.0f));
                }
                break;
            case 1628793552:
                if (cls.equals("SoundGroup")) {
                    extraDefaults$s(i, "Volume", Float.valueOf(0.5f));
                }
                break;
            case 1744980082:
                if (cls.equals("UISizeConstraint")) {
                    extraDefaults$s(i, "MaxSize", new Vector2(Float.MAX_VALUE, Float.MAX_VALUE));
                    extraDefaults$s(i, "MinSize", new Vector2(0.0f, 0.0f));
                }
                break;
            case 1766922129:
                if (cls.equals("BodyGyro")) {
                    extraDefaults$s(i, "MaxTorque", new Vector3(400000.0f, 0.0f, 400000.0f));
                    extraDefaults$s(i, "CFrame", CFrame.INSTANCE.getIDENTITY());
                    extraDefaults$s(i, "D", Float.valueOf(500.0f));
                    extraDefaults$s(i, "P", Float.valueOf(3000.0f));
                }
                break;
            case 1830326756:
                if (cls.equals("UIGradient")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "Offset", new Vector2(0.0f, 0.0f));
                    extraDefaults$s(i, "Rotation", Float.valueOf(0.0f));
                }
                break;
            case 1856572602:
                if (cls.equals("BodyThrust")) {
                    extraDefaults$s(i, "Force", v0);
                    extraDefaults$s(i, "Location", v0);
                }
                break;
            case 1877237403:
                if (cls.equals("PathfindingModifier")) {
                    extraDefaults$s(i, "Label", "");
                    extraDefaults$s(i, "PassThrough", false);
                }
                break;
            case 1969796318:
                if (!cls.equals("PitchShiftSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
            case 2016416311:
                if (cls.equals("AudioPlayer")) {
                    extraDefaults$s(i, "Asset", "");
                    extraDefaults$s(i, "Volume", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Looping", false);
                    extraDefaults$s(i, "PlaybackSpeed", Float.valueOf(1.0f));
                    extraDefaults$s(i, "AutoLoad", true);
                }
                break;
            case 2021315838:
                if (cls.equals("Clouds")) {
                    extraDefaults$s(i, "Cover", Float.valueOf(0.5f));
                    extraDefaults$s(i, "Density", Float.valueOf(0.7f));
                    extraDefaults$s(i, "Color", new Color3(1.0f, 1.0f, 1.0f));
                    extraDefaults$s(i, "Enabled", true);
                }
                break;
            case 2046749032:
                if (cls.equals("Dialog")) {
                    extraDefaults$s(i, "InitialPrompt", "");
                    extraDefaults$s(i, "GoodbyeDialog", "");
                    extraDefaults$s(i, "ConversationDistance", Float.valueOf(25.0f));
                    extraDefaults$s(i, "InUse", false);
                }
                break;
            case 2090650602:
                if (cls.equals("SpringConstraint")) {
                    extraDefaults$s(i, "FreeLength", Float.valueOf(1.0f));
                    extraDefaults$s(i, "Stiffness", Float.valueOf(0.0f));
                    extraDefaults$s(i, "Damping", Float.valueOf(0.0f));
                    extraDefaults$s(i, "MaxForce", Float.valueOf(Float.MAX_VALUE));
                    extraDefaults$s(i, "LimitsEnabled", false);
                    extraDefaults$s(i, "MinLength", Float.valueOf(0.0f));
                    extraDefaults$s(i, "MaxLength", Float.valueOf(5.0f));
                    extraDefaults$s(i, "Coils", Float.valueOf(8.0f));
                    extraDefaults$s(i, "Radius", Float.valueOf(0.4f));
                    extraDefaults$s(i, "Thickness", Float.valueOf(0.1f));
                }
                break;
            case 2104827119:
                if (cls.equals("ForceField")) {
                    extraDefaults$s(i, "Visible", true);
                }
                break;
            case 2126137917:
                if (cls.equals("Sparkles")) {
                    extraDefaults$s(i, "Enabled", true);
                    extraDefaults$s(i, "SparkleColor", Color3.INSTANCE.fromRGB(144, 25, 255));
                }
                break;
            case 2127440346:
                if (!cls.equals("ChorusSoundEffect")) {
                }
                extraDefaults$s(i, "Enabled", true);
                extraDefaults$s(i, "Priority", 0);
                break;
        }
    }

    private final String defaultSource(String cls) {
        return Intrinsics.areEqual(cls, "ModuleScript") ? "local module = {}\n\nreturn module\n" : "print(\"Hello world!\")\n";
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0100  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void part(Instance i, String cls) {
        i.set("Anchored", false, false);
        i.set("CanCollide", true, false);
        i.set("CanTouch", true, false);
        i.set("CanQuery", true, false);
        i.set("CastShadow", true, false);
        i.set("Locked", false, false);
        i.set("Massless", false, false);
        i.set("CFrame", CFrame.INSTANCE.getIDENTITY(), false);
        Float fValueOf = Float.valueOf(0.0f);
        i.set("Transparency", fValueOf, false);
        i.set("Reflectance", fValueOf, false);
        i.set("Material", new EnumValue(256), false);
        i.set("Color3uint8", GREY, false);
        i.getPropTypes().put("Color3uint8", 26);
        i.set("size", new Vector3(4.0f, 1.0f, 2.0f), false);
        for (String s : CollectionsKt.listOf(new String[]{"TopSurface", "BottomSurface", "FrontSurface", "BackSurface", "LeftSurface", "RightSurface"})) {
            i.set(s, new EnumValue(0), false);
        }
        switch (cls.hashCode()) {
            case -711951312:
                if (cls.equals("SpawnLocation")) {
                    i.set("shape", new EnumValue(1), false);
                }
                break;
            case -525651254:
                if (cls.equals("TrussPart")) {
                    i.set("size", new Vector3(2.0f, 10.0f, 2.0f), false);
                }
                break;
            case -408120416:
                if (cls.equals("MeshPart")) {
                    i.set("MeshId", "", false);
                    i.set("TextureID", "", false);
                }
                break;
            case 2480147:
                if (cls.equals("Part")) {
                    i.set("shape", new EnumValue(1), false);
                }
                break;
            case 2572837:
                if (cls.equals("Seat")) {
                    i.set("shape", new EnumValue(1), false);
                }
                break;
        }
        if (Intrinsics.areEqual(cls, "SpawnLocation")) {
            i.set("size", new Vector3(12.0f, 1.0f, 12.0f), false);
            i.set("Anchored", true, false);
            i.set("Duration", 10, false);
            i.set("Neutral", true, false);
            i.set("Enabled", true, false);
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:19:0x00bd  */
    /* JADX WARN: Code duplicated, block: B:20:0x00bf  */
    /* JADX WARN: Code duplicated, block: B:23:0x010f  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void gui(Instance i, String cls) {
        String str;
        Float fValueOf = Float.valueOf(0.0f);
        i.set("Position", new UDim2(0.0f, 0, 0.0f, 0), false);
        i.set("Size", new UDim2(0.0f, 100, 0.0f, 100), false);
        i.set("AnchorPoint", new Vector2(0.0f, 0.0f), false);
        i.set("BackgroundColor3", new Color3(1.0f, 1.0f, 1.0f), false);
        i.set("BackgroundTransparency", fValueOf, false);
        i.set("BorderColor3", Color3.INSTANCE.fromRGB(27, 42, 53), false);
        i.set("BorderSizePixel", 1, false);
        i.set("Visible", true, false);
        i.set("ZIndex", 1, false);
        i.set("LayoutOrder", 0, false);
        i.set("ClipsDescendants", false, false);
        if (ClassDb.INSTANCE.isA(cls, "GuiButton")) {
            i.set("AutoButtonColor", true, false);
            i.set("Modal", false, false);
        }
        switch (cls.hashCode()) {
            case -935338273:
                if (cls.equals("TextButton")) {
                    i.set("Size", new UDim2(0.0f, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0.0f, 50), false);
                    if (Intrinsics.areEqual(cls, "TextBox")) {
                        str = "";
                    } else {
                        str = cls;
                    }
                    i.set("Text", str, false);
                    i.set("TextColor3", new Color3(0.0f, 0.0f, 0.0f), false);
                    i.set("TextSize", Float.valueOf(14.0f), false);
                    i.set("TextScaled", false, false);
                    i.set("TextWrapped", false, false);
                    i.set("TextXAlignment", new EnumValue(2), false);
                    i.set("TextYAlignment", new EnumValue(1), false);
                    i.set("TextTransparency", fValueOf, false);
                    i.set("Font", new EnumValue(3), false);
                    if (Intrinsics.areEqual(cls, "TextBox")) {
                        i.set("PlaceholderText", "", false);
                        i.set("ClearTextOnFocus", true, false);
                    }
                }
                break;
            case 246787390:
                if (cls.equals("TextBox")) {
                    i.set("Size", new UDim2(0.0f, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0.0f, 50), false);
                    if (Intrinsics.areEqual(cls, "TextBox")) {
                        str = "";
                    } else {
                        str = cls;
                    }
                    i.set("Text", str, false);
                    i.set("TextColor3", new Color3(0.0f, 0.0f, 0.0f), false);
                    i.set("TextSize", Float.valueOf(14.0f), false);
                    i.set("TextScaled", false, false);
                    i.set("TextWrapped", false, false);
                    i.set("TextXAlignment", new EnumValue(2), false);
                    i.set("TextYAlignment", new EnumValue(1), false);
                    i.set("TextTransparency", fValueOf, false);
                    i.set("Font", new EnumValue(3), false);
                    if (Intrinsics.areEqual(cls, "TextBox")) {
                        i.set("PlaceholderText", "", false);
                        i.set("ClearTextOnFocus", true, false);
                    }
                }
                break;
            case 948280743:
                if (cls.equals("TextLabel")) {
                    i.set("Size", new UDim2(0.0f, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0.0f, 50), false);
                    if (Intrinsics.areEqual(cls, "TextBox")) {
                        str = "";
                    } else {
                        str = cls;
                    }
                    i.set("Text", str, false);
                    i.set("TextColor3", new Color3(0.0f, 0.0f, 0.0f), false);
                    i.set("TextSize", Float.valueOf(14.0f), false);
                    i.set("TextScaled", false, false);
                    i.set("TextWrapped", false, false);
                    i.set("TextXAlignment", new EnumValue(2), false);
                    i.set("TextYAlignment", new EnumValue(1), false);
                    i.set("TextTransparency", fValueOf, false);
                    i.set("Font", new EnumValue(3), false);
                    if (Intrinsics.areEqual(cls, "TextBox")) {
                        i.set("PlaceholderText", "", false);
                        i.set("ClearTextOnFocus", true, false);
                    }
                }
                break;
        }
        if (Intrinsics.areEqual(cls, "ImageLabel") || Intrinsics.areEqual(cls, "ImageButton")) {
            i.set("Image", "", false);
            i.set("ImageColor3", new Color3(1.0f, 1.0f, 1.0f), false);
            i.set("ImageTransparency", fValueOf, false);
        }
        if (Intrinsics.areEqual(cls, "ScrollingFrame")) {
            i.set("CanvasSize", new UDim2(0.0f, 0, 2.0f, 0), false);
            i.set("ScrollBarThickness", 12, false);
        }
    }
}
