package com.goydevv.studrblx.model;

import com.goydevv.studrblx.engine.ClothingCompositor;
import com.goydevv.studrblx.ui.StartScreen;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Enums.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Enums {
    public static final Enums INSTANCE = new Enums();
    private static final Map<String, LinkedHashMap<String, Integer>> types;

    private Enums() {
    }

    static {
        HashMap map = new HashMap();
        Integer num = 12;
        types$lambda$11$e(map, "Material", TuplesKt.to("Plastic", 256), TuplesKt.to("SmoothPlastic", Integer.valueOf(ClothingCompositor.TORSO_H)), TuplesKt.to("Neon", 288), TuplesKt.to("Wood", 512), TuplesKt.to("WoodPlanks", 528), TuplesKt.to("Marble", 784), TuplesKt.to("Basalt", 788), TuplesKt.to("Slate", 800), TuplesKt.to("CrackedLava", 804), TuplesKt.to("Concrete", 816), TuplesKt.to("Limestone", 820), TuplesKt.to("Granite", 832), TuplesKt.to("Pavement", 836), TuplesKt.to("Brick", 848), TuplesKt.to("Pebble", 864), TuplesKt.to("Cobblestone", 880), TuplesKt.to("Rock", 896), TuplesKt.to("Sandstone", 912), TuplesKt.to("CorrodedMetal", 1040), TuplesKt.to("DiamondPlate", 1056), TuplesKt.to("Foil", 1072), TuplesKt.to("Metal", 1088), TuplesKt.to("Grass", 1280), TuplesKt.to("LeafyGrass", 1284), TuplesKt.to("Sand", 1296), TuplesKt.to("Fabric", 1312), TuplesKt.to("Snow", 1328), TuplesKt.to("Mud", 1344), TuplesKt.to("Ground", 1360), TuplesKt.to("Asphalt", 1376), TuplesKt.to("Salt", 1392), TuplesKt.to("Ice", 1536), TuplesKt.to("Glacier", 1552), TuplesKt.to("Glass", 1568), TuplesKt.to("ForceField", 1584), TuplesKt.to("Air", 1792), TuplesKt.to("Water", 2048), TuplesKt.to("Cardboard", 2304), TuplesKt.to("Carpet", 2305), TuplesKt.to("CeramicTiles", 2306), TuplesKt.to("ClayRoofTiles", 2307), TuplesKt.to("RoofShingles", 2308), TuplesKt.to("Leather", 2309), TuplesKt.to("Plaster", 2310), TuplesKt.to("Rubber", 2311));
        types$lambda$11$e(map, "PartType", TuplesKt.to("Ball", 0), TuplesKt.to("Block", 1), TuplesKt.to("Cylinder", 2), TuplesKt.to("Wedge", 3), TuplesKt.to("CornerWedge", 4));
        types$lambda$11$e(map, "MaterialPattern", TuplesKt.to("Regular", 0), TuplesKt.to("Organic", 1));
        types$lambda$11$e(map, "SurfaceType", TuplesKt.to("Smooth", 0), TuplesKt.to("Glue", 1), TuplesKt.to("Weld", 2), TuplesKt.to("Studs", 3), TuplesKt.to("Inlet", 4), TuplesKt.to("Universal", 5), TuplesKt.to("Hinge", 6), TuplesKt.to("Motor", 7), TuplesKt.to("SteppingMotor", 8), TuplesKt.to("SmoothNoOutlines", 10));
        types$lambda$11$seq(map, "EasingStyle", "Linear", "Sine", "Back", "Quad", "Quart", "Quint", "Bounce", "Elastic", "Exponential", "Circular", "Cubic");
        types$lambda$11$seq(map, "EasingDirection", "In", "Out", "InOut");
        types$lambda$11$seq(map, "TextXAlignment", "Left", "Right", "Center");
        types$lambda$11$seq(map, "TextYAlignment", "Top", "Center", "Bottom");
        types$lambda$11$seq(map, "FillDirection", "Horizontal", "Vertical");
        types$lambda$11$seq(map, "HorizontalAlignment", "Center", "Left", "Right");
        types$lambda$11$seq(map, "VerticalAlignment", "Center", "Top", "Bottom");
        types$lambda$11$e(map, "SortOrder", TuplesKt.to("Name", 0), TuplesKt.to("Custom", 1), TuplesKt.to("LayoutOrder", 2));
        types$lambda$11$seq(map, "ZIndexBehavior", "Global", "Sibling");
        types$lambda$11$seq(map, "ScaleType", "Stretch", "Slice", "Tile", "Fit", "Crop");
        types$lambda$11$seq(map, "AutomaticSize", "None", "X", "Y", "XY");
        types$lambda$11$seq(map, "NormalId", "Right", "Top", "Back", "Left", "Bottom", "Front");
        types$lambda$11$e(map, "RunContext", TuplesKt.to("Legacy", 0), TuplesKt.to("Server", 1), TuplesKt.to("Client", 2), TuplesKt.to("Plugin", 3));
        types$lambda$11$e(map, "RaycastFilterType", TuplesKt.to("Exclude", 0), TuplesKt.to("Include", 1), TuplesKt.to("Blacklist", 0), TuplesKt.to("Whitelist", 1));
        types$lambda$11$e(map, "HumanoidStateType", TuplesKt.to("FallingDown", 0), TuplesKt.to("Ragdoll", 1), TuplesKt.to("GettingUp", 2), TuplesKt.to("Jumping", 3), TuplesKt.to("Swimming", 4), TuplesKt.to("Freefall", 5), TuplesKt.to("Flying", 6), TuplesKt.to("Landed", 7), TuplesKt.to("Running", 8), TuplesKt.to("RunningNoPhysics", 10), TuplesKt.to("StrafingNoPhysics", 11), TuplesKt.to("Climbing", num), TuplesKt.to("Seated", 13), TuplesKt.to("PlatformStanding", 14), TuplesKt.to("Dead", 15), TuplesKt.to("Physics", 16), TuplesKt.to("None", 18));
        types$lambda$11$e(map, "UserInputType", TuplesKt.to("MouseButton1", 0), TuplesKt.to("MouseButton2", 1), TuplesKt.to("MouseButton3", 2), TuplesKt.to("MouseWheel", 3), TuplesKt.to("MouseMovement", 4), TuplesKt.to("Touch", 7), TuplesKt.to("Keyboard", 8), TuplesKt.to("Focus", 9), TuplesKt.to("Gamepad1", num), TuplesKt.to("None", 18));
        types$lambda$11$e(map, "UserInputState", TuplesKt.to("Begin", 0), TuplesKt.to("Change", 1), TuplesKt.to("End", 2), TuplesKt.to("Cancel", 3), TuplesKt.to("None", 4));
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("Unknown", 0);
        linkedHashMap.put("Backspace", 8);
        linkedHashMap.put("Tab", 9);
        linkedHashMap.put("Clear", num);
        linkedHashMap.put("Return", 13);
        linkedHashMap.put("Pause", 19);
        linkedHashMap.put("Escape", 27);
        List listListOf = CollectionsKt.listOf(new String[]{"Space", "QuotedDouble", "Hash", "Dollar", "Percent", "Ampersand", "Quote", "LeftParenthesis", "RightParenthesis", "Asterisk", "Plus", "Comma", "Minus", "Period", "Slash"});
        List list = listListOf;
        int i = 0;
        for (Object obj : list) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj, Integer.valueOf(i + 32));
            i = i2;
        }
        Iterable iterableListOf = CollectionsKt.listOf(new String[]{"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"});
        int i3 = 0;
        int i4 = 0;
        for (Object obj2 : iterableListOf) {
            int i5 = i4 + 1;
            if (i4 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj2, Integer.valueOf(i4 + 48));
            i4 = i5;
        }
        Iterable iterableListOf2 = CollectionsKt.listOf(new String[]{"Colon", "Semicolon", "LessThan", "Equals", "GreaterThan", "Question", "At"});
        int i6 = 0;
        int i7 = 0;
        for (Object obj3 : iterableListOf2) {
            int i8 = i7 + 1;
            if (i7 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj3, Integer.valueOf(i7 + 58));
            i7 = i8;
        }
        Iterable iterableListOf3 = CollectionsKt.listOf(new String[]{"LeftBracket", "BackSlash", "RightBracket", "Caret", "Underscore", "Backquote"});
        int i9 = 0;
        int i10 = 0;
        for (Object obj4 : iterableListOf3) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj4, Integer.valueOf(i10 + 91));
            i10 = i11;
        }
        for (char c = 'A'; c < '['; c = (char) (c + 1)) {
            linkedHashMap.put(String.valueOf(c), Integer.valueOf((c - 'A') + 97));
        }
        Iterable iterableListOf4 = CollectionsKt.listOf(new String[]{"LeftCurly", "Pipe", "RightCurly", "Tilde", "Delete"});
        int i12 = 0;
        int i13 = 0;
        for (Object obj5 : iterableListOf4) {
            int i14 = i13 + 1;
            if (i13 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj5, Integer.valueOf(i13 + 123));
            i13 = i14;
        }
        Iterable iterableListOf5 = CollectionsKt.listOf(new String[]{"KeypadZero", "KeypadOne", "KeypadTwo", "KeypadThree", "KeypadFour", "KeypadFive", "KeypadSix", "KeypadSeven", "KeypadEight", "KeypadNine", "KeypadPeriod", "KeypadDivide", "KeypadMultiply", "KeypadMinus", "KeypadPlus", "KeypadEnter", "KeypadEquals", "Up", "Down", "Right", "Left", "Insert", StartScreen.HOME, "End", "PageUp", "PageDown"});
        int i15 = 0;
        int i16 = 0;
        for (Object obj6 : iterableListOf5) {
            int i17 = i16 + 1;
            if (i16 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj6, Integer.valueOf(i16 + 256));
            i16 = i17;
        }
        Integer num2 = num;
        int i18 = 1;
        for (int i19 = 16; i18 < i19; i19 = 16) {
            linkedHashMap.put("F" + i18, Integer.valueOf(i18 + 281));
            i18++;
        }
        Iterable iterableListOf6 = CollectionsKt.listOf(new String[]{"NumLock", "CapsLock", "ScrollLock", "RightShift", "LeftShift", "RightControl", "LeftControl", "RightAlt", "LeftAlt", "RightMeta", "LeftMeta", "LeftSuper", "RightSuper", "Mode", "Compose", "Help", "Print", "SysReq", "Break", "Menu", "Power", "Euro", "Undo"});
        int i20 = 0;
        int i21 = 0;
        for (Object obj7 : iterableListOf6) {
            int i22 = i21 + 1;
            if (i21 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj7, Integer.valueOf(i21 + 300));
            i21 = i22;
        }
        Iterable iterableListOf7 = CollectionsKt.listOf(new String[]{"ButtonX", "ButtonY", "ButtonA", "ButtonB", "ButtonR1", "ButtonL1", "ButtonR2", "ButtonL2", "ButtonR3", "ButtonL3", "ButtonStart", "ButtonSelect", "DPadLeft", "DPadRight", "DPadUp", "DPadDown", "Thumbstick1", "Thumbstick2"});
        int i23 = 0;
        int i24 = 0;
        for (Object obj8 : iterableListOf7) {
            int i25 = i24 + 1;
            if (i24 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            linkedHashMap.put((String) obj8, Integer.valueOf(i24 + 1000));
            i24 = i25;
        }
        Unit unit = Unit.INSTANCE;
        map.put("KeyCode", linkedHashMap);
        types$lambda$11$e(map, "CameraType", TuplesKt.to("Fixed", 0), TuplesKt.to("Attach", 1), TuplesKt.to("Watch", 2), TuplesKt.to("Track", 3), TuplesKt.to("Follow", 4), TuplesKt.to("Custom", 5), TuplesKt.to("Scriptable", 6), TuplesKt.to("Orbital", 7));
        types$lambda$11$e(map, "Font", TuplesKt.to("Legacy", 0), TuplesKt.to("Arial", 1), TuplesKt.to("ArialBold", 2), TuplesKt.to("SourceSans", 3), TuplesKt.to("SourceSansBold", 4), TuplesKt.to("SourceSansLight", 5), TuplesKt.to("SourceSansItalic", 6), TuplesKt.to("Bodoni", 7), TuplesKt.to("Garamond", 8), TuplesKt.to("Cartoon", 9), TuplesKt.to("Code", 10), TuplesKt.to("Highway", 11), TuplesKt.to("SciFi", num2), TuplesKt.to("Arcade", 13), TuplesKt.to("Fantasy", 14), TuplesKt.to("Antique", 15), TuplesKt.to("SourceSansSemibold", 16), TuplesKt.to("Gotham", 17), TuplesKt.to("GothamMedium", 18), TuplesKt.to("GothamBold", 19), TuplesKt.to("GothamBlack", 20), TuplesKt.to("AmaticSC", 21), TuplesKt.to("Bangers", 22), TuplesKt.to("Creepster", 23), TuplesKt.to("DenkOne", 24), TuplesKt.to("Fondamento", 25), TuplesKt.to("FredokaOne", 26), TuplesKt.to("GrenzeGotisch", 27), TuplesKt.to("IndieFlower", 28), TuplesKt.to("JosefinSans", 29), TuplesKt.to("Jura", 30), TuplesKt.to("Kalam", 31), TuplesKt.to("LuckiestGuy", 32), TuplesKt.to("Merriweather", 33), TuplesKt.to("Michroma", 34), TuplesKt.to("Nunito", 35), TuplesKt.to("Oswald", 36), TuplesKt.to("PatrickHand", 37), TuplesKt.to("PermanentMarker", 38), TuplesKt.to("Roboto", 39), TuplesKt.to("RobotoCondensed", 40), TuplesKt.to("RobotoMono", 41), TuplesKt.to("Sarpanch", 42), TuplesKt.to("SpecialElite", 43), TuplesKt.to("TitilliumWeb", 44), TuplesKt.to("Ubuntu", 45), TuplesKt.to("BuilderSans", 46));
        types$lambda$11$e(map, "MeshType", TuplesKt.to("Head", 0), TuplesKt.to("Torso", 1), TuplesKt.to("Wedge", 2), TuplesKt.to("Sphere", 3), TuplesKt.to("Cylinder", 4), TuplesKt.to("FileMesh", 5), TuplesKt.to("Brick", 6), TuplesKt.to("Prism", 7), TuplesKt.to("Pyramid", 8));
        types$lambda$11$e(map, "FormFactor", TuplesKt.to("Symmetric", 0), TuplesKt.to("Brick", 1), TuplesKt.to("Plate", 2), TuplesKt.to("Custom", 3));
        types$lambda$11$e(map, "SizeConstraint", TuplesKt.to("RelativeXY", 0), TuplesKt.to("RelativeXX", 1), TuplesKt.to("RelativeYY", 2));
        types$lambda$11$e(map, "BorderMode", TuplesKt.to("Outline", 0), TuplesKt.to("Middle", 1), TuplesKt.to("Inset", 2));
        types$lambda$11$e(map, "ApplyStrokeMode", TuplesKt.to("Contextual", 0), TuplesKt.to("Border", 1));
        types$lambda$11$e(map, "Technology", TuplesKt.to("Legacy", 0), TuplesKt.to("Voxel", 1), TuplesKt.to("Compatibility", 2), TuplesKt.to("ShadowMap", 3), TuplesKt.to("Future", 4));
        types$lambda$11$e(map, "ScrollingDirection", TuplesKt.to("X", 1), TuplesKt.to("Y", 2), TuplesKt.to("XY", 4));
        types$lambda$11$e(map, "HighlightDepthMode", TuplesKt.to("AlwaysOnTop", 0), TuplesKt.to("Occluded", 1));
        types$lambda$11$e(map, "ScreenInsets", TuplesKt.to("None", 0), TuplesKt.to("DeviceSafeInsets", 1), TuplesKt.to("CoreUISafeInsets", 2), TuplesKt.to("TopbarSafeInsets", 3));
        types$lambda$11$e(map, "ResamplerMode", TuplesKt.to("Default", 0), TuplesKt.to("Pixelated", 1));
        types$lambda$11$e(map, "Limb", TuplesKt.to("Head", 0), TuplesKt.to("Torso", 1), TuplesKt.to("LeftArm", 2), TuplesKt.to("RightArm", 3), TuplesKt.to("LeftLeg", 4), TuplesKt.to("RightLeg", 5), TuplesKt.to("Unknown", 6));
        types$lambda$11$e(map, "HumanoidRigType", TuplesKt.to("R6", 0), TuplesKt.to("R15", 1));
        types$lambda$11$e(map, "GameAvatarType", TuplesKt.to("R6", 0), TuplesKt.to("R15", 1), TuplesKt.to("PlayerChoice", 2));
        types$lambda$11$e(map, "R15CollisionType", TuplesKt.to("OuterBox", 0), TuplesKt.to("InnerBox", 1));
        types$lambda$11$e(map, "AnimationPriority", TuplesKt.to("Idle", 0), TuplesKt.to("Movement", 1), TuplesKt.to("Action", 2), TuplesKt.to("Core", 1000));
        types$lambda$11$e(map, "CoreGuiType", TuplesKt.to("PlayerList", 0), TuplesKt.to("Health", 1), TuplesKt.to("Backpack", 2), TuplesKt.to("Chat", 3), TuplesKt.to("All", 4), TuplesKt.to("EmotesMenu", 5), TuplesKt.to("SelfView", 6), TuplesKt.to("Captures", 7));
        types$lambda$11$e(map, "CameraMode", TuplesKt.to("Classic", 0), TuplesKt.to("LockFirstPerson", 1));
        types$lambda$11$e(map, "TextChatMessageStatus", TuplesKt.to("Unknown", 0), TuplesKt.to("Success", 1), TuplesKt.to("Sending", 2));
        types$lambda$11$e(map, "CollisionFidelity", TuplesKt.to("Default", 0), TuplesKt.to("Hull", 1), TuplesKt.to("Box", 2), TuplesKt.to("PreciseConvexDecomposition", 3));
        types$lambda$11$e(map, "RenderFidelity", TuplesKt.to("Automatic", 0), TuplesKt.to("Precise", 1), TuplesKt.to("Performance", 2));
        types$lambda$11$e(map, "FluidFidelity", TuplesKt.to("Automatic", 0), TuplesKt.to("UseCollisionGeometry", 1), TuplesKt.to("UsePreciseGeometry", 2));
        types$lambda$11$e(map, "RollOffMode", TuplesKt.to("Inverse", 0), TuplesKt.to("Linear", 1), TuplesKt.to("LinearSquare", 2), TuplesKt.to("InverseTapered", 3));
        types$lambda$11$e(map, "ReverbType", TuplesKt.to("NoReverb", 0), TuplesKt.to("GenericReverb", 1), TuplesKt.to("PaddedCell", 2), TuplesKt.to("Room", 3), TuplesKt.to("Bathroom", 4), TuplesKt.to("LivingRoom", 5), TuplesKt.to("StoneRoom", 6), TuplesKt.to("Auditorium", 7), TuplesKt.to("ConcertHall", 8), TuplesKt.to("Cave", 9), TuplesKt.to("Arena", 10), TuplesKt.to("Hangar", 11), TuplesKt.to("CarpettedHallway", 12), TuplesKt.to("Hallway", 13), TuplesKt.to("StoneCorridor", 14), TuplesKt.to("Alley", 15), TuplesKt.to("Forest", 16), TuplesKt.to("City", 17), TuplesKt.to("Mountains", 18), TuplesKt.to("Quarry", 19), TuplesKt.to("Plain", 20), TuplesKt.to("ParkingLot", 21), TuplesKt.to("SewerPipe", 22), TuplesKt.to("UnderWater", 23));
        types$lambda$11$e(map, "ListenerType", TuplesKt.to("Camera", 0), TuplesKt.to("CFrame", 1), TuplesKt.to("ObjectPosition", 2), TuplesKt.to("ObjectCFrame", 3));
        types = map;
    }

    public final Map<String, LinkedHashMap<String, Integer>> getTypes() {
        return types;
    }

    private static final void types$lambda$11$e(HashMap<String, LinkedHashMap<String, Integer>> map, String name, Pair<String, Integer>... pairArr) {
        map.put(name, MapsKt.linkedMapOf((Pair[]) Arrays.copyOf(pairArr, pairArr.length)));
    }

    private static final void types$lambda$11$seq(HashMap<String, LinkedHashMap<String, Integer>> map, String name, String... items) {
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        int i = 0;
        int length = items.length;
        int i2 = 0;
        while (i2 < length) {
            linkedHashMap.put(items[i2], Integer.valueOf(i));
            i2++;
            i++;
        }
        Unit unit = Unit.INSTANCE;
        map.put(name, linkedHashMap);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:112:0x0187 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:126:0x01af A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Code duplicated, block: B:61:0x00d7 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code restructure failed: missing block: B:135:?, code lost:
    
        return "Material";
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0099, code lost:
    
        if (r5.equals("Material") == false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x015c, code lost:
    
        if (r5.equals("BaseMaterial") != false) goto L100;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String typeForProp(java.lang.String cls, java.lang.String prop) {
        if (prop == null) return null;
        switch (prop) {
            case "Material": case "BaseMaterial": return "Material";
            case "Shape": return "PartType";
            case "TopSurface": case "BottomSurface": case "LeftSurface": case "RightSurface": case "FrontSurface": case "BackSurface": return "SurfaceType";
            case "Font": return "Font";
            case "TextXAlignment": return "TextXAlignment";
            case "TextYAlignment": return "TextYAlignment";
            case "ScaleType": return "ScaleType";
            case "RigType": return "HumanoidRigType";
            case "SortOrder": return "SortOrder";
            case "FillDirection": return "FillDirection";
            case "HorizontalAlignment": return "HorizontalAlignment";
            case "VerticalAlignment": return "VerticalAlignment";
            case "DominantAxis": return "DominantAxis";
            case "AutomaticSize": case "AutomaticCanvasSize": return "AutomaticSize";
            case "ScrollingDirection": return "ScrollingDirection";
            case "ApplyStrokeMode": return "ApplyStrokeMode";
            case "LineJoinMode": return "LineJoinMode";
            case "Technology": return "Technology";
            case "MeshType": return "MeshType";
            case "Face": return "NormalId";
            case "RollOffMode": return "RollOffMode";
            case "AmbientReverb": return "ReverbType";
            default: return null;
        }
    }

    public final String itemName(String type, int value) {
        LinkedHashMap<String, Integer> linkedHashMap;
        Object next;
        Integer num;
        Map<String, LinkedHashMap<String, Integer>> map = types;
        if (type == null || (linkedHashMap = map.get(type)) == null) {
            return null;
        }
        Iterable iterableEntrySet = linkedHashMap.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        Iterator it = iterableEntrySet.iterator();
        do {
            if (it.hasNext()) {
                next = it.next();
                num = (Integer) ((Map.Entry) next).getValue();
            } else {
                next = null;
                break;
            }
        } while (!(num != null && num.intValue() == value));
        Map.Entry entry = (Map.Entry) next;
        if (entry != null) {
            return (String) entry.getKey();
        }
        return null;
    }
}
