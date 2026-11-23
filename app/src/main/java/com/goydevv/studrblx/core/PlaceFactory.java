package com.goydevv.studrblx.core;

import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.ClassDb;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.Vector3;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PlaceFactory.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PlaceFactory {
    public static final PlaceFactory INSTANCE = new PlaceFactory();
    private static final List<String> required = CollectionsKt.listOf(new String[]{"Workspace", "Players", "Lighting", "MaterialService", "ReplicatedFirst", "ReplicatedStorage", "ServerScriptService", "ServerStorage", "StarterGui", "StarterPack", "StarterPlayer", "Teams", "SoundService", "TextChatService"});

    private PlaceFactory() {
    }

    /* JADX INFO: compiled from: PlaceFactory.kt */
    public enum Template {
        BASEPLATE("Baseplate", "A large grey baseplate and a spawn"),
        CLASSIC("Classic Baseplate", "The classic studded baseplate"),
        FLAT_TERRAIN("Flat Terrain", "Open grass terrain ready for sculpting"),
        OBBY("Obby", "Obstacle course with stepping stones and finish"),
        PLATFORMER("Platformer", "Floating islands with jump pads"),
        EMPTY("Empty Place", "Just a spawn location, no ground");

        private final String subtitle;
        private final String title;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Template> getEntries() {
            return $ENTRIES;
        }

        Template(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        public final String getSubtitle() {
            return this.subtitle;
        }

        public final String getTitle() {
            return this.title;
        }
    }

    public static /* synthetic */ DataModel newPlace$default(PlaceFactory placeFactory, Template template, int i, Object obj) {
        if ((i & 1) != 0) {
            template = Template.BASEPLATE;
        }
        return placeFactory.newPlace(template);
    }

    private final Instance addPart(Instance ws, String name, Vector3 size, float x, float y, float z, Color3 color, int materialEnum, boolean locked) {
        Instance part = InstanceFactory.INSTANCE.create("Part");
        part.setName(name);
        part.set("Anchored", true, false);
        if (locked) {
            part.set("Locked", true, false);
        }
        part.set("size", size, false);
        part.set("CFrame", new CFrame(x, y, z, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null), false);
        part.set("Color3uint8", color, false);
        if (materialEnum >= 0) {
            part.set("Material", new EnumValue(materialEnum), false);
        }
        part.attachRaw(ws);
        return part;
    }

    private final void addSpawn(Instance ws, float x, float y, float z) {
        Instance spawn = InstanceFactory.INSTANCE.create("SpawnLocation");
        spawn.set("CFrame", new CFrame(x, y, z, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null), false);
        spawn.set("Color3uint8", Color3.INSTANCE.fromRGB(163, 162, 165), false);
        Instance spawnDecal = InstanceFactory.INSTANCE.create("Decal");
        spawnDecal.setName("Decal");
        spawnDecal.set("Texture", "rbxasset://textures/SpawnLocation.png", false);
        spawnDecal.set("Face", new EnumValue(1), false);
        spawnDecal.attachRaw(spawn);
        spawn.attachRaw(ws);
    }

    public final DataModel newPlace(Template template) {
        Intrinsics.checkNotNullParameter(template, "template");
        DataModel dm = new DataModel();
        dm.getMeta().put("ExplicitAutoJoints", "true");
        for (String s : required) {
            service(dm, s);
        }
        Instance lighting = dm.findService("Lighting");
        Intrinsics.checkNotNull(lighting);
        if (template == Template.CLASSIC) {
            lighting.set("EnvironmentDiffuseScale", Float.valueOf(0.0f), false);
            lighting.set("EnvironmentSpecularScale", Float.valueOf(0.0f), false);
            lighting.set("GlobalShadows", true, false);
            lighting.set("ShadowSoftness", Float.valueOf(0.2f), false);
            lighting.set("Technology", new EnumValue(3), false);
        } else {
            lighting.set("EnvironmentDiffuseScale", Float.valueOf(1.0f), false);
            lighting.set("EnvironmentSpecularScale", Float.valueOf(1.0f), false);
            lighting.set("GlobalShadows", true, false);
            lighting.set("ShadowSoftness", Float.valueOf(0.2f), false);
            lighting.set("Technology", new EnumValue(3), false);
        }
        Instance ws = dm.findService("Workspace");
        Intrinsics.checkNotNull(ws);
        Instance cam = InstanceFactory.INSTANCE.create("Camera");
        cam.set("CFrame", CFrame.Companion.lookAt$default(CFrame.INSTANCE, new Vector3(0.0f, 20.0f, 40.0f), Vector3.INSTANCE.getZERO(), null, 4, null), false);
        cam.attachRaw(ws);
        ws.set("CurrentCamera", cam, false);
        ensureTerrain(ws);
        if (template == Template.BASEPLATE) {
            Instance baseplate = addPart(ws, "Baseplate", new Vector3(2048.0f, 16.0f, 2048.0f), 0.0f, -8.0f, 0.0f, Color3.INSTANCE.fromRGB(91, 93, 105), -1, true);
            Instance tex = InstanceFactory.INSTANCE.create("Texture");
            tex.setName("Texture");
            tex.set("Texture", "rbxassetid://6372755229", false);
            tex.set("Face", new EnumValue(1), false);
            tex.set("StudsPerTileU", Float.valueOf(8.0f), false);
            tex.set("StudsPerTileV", Float.valueOf(8.0f), false);
            tex.set("Transparency", Float.valueOf(0.8f), false);
            tex.attachRaw(baseplate);
            addSpawn(ws, 0.0f, 0.5f, 0.0f);
        } else if (template == Template.CLASSIC) {
            Instance baseplate = addPart(ws, "Baseplate", new Vector3(512.0f, 20.0f, 512.0f), 0.0f, -10.0f, 0.0f, Color3.INSTANCE.fromRGB(99, 95, 98), -1, true);
            baseplate.set("TopSurface", new EnumValue(3), false);
            addSpawn(ws, 0.0f, 0.5f, 0.0f);
        } else if (template == Template.FLAT_TERRAIN) {
            addPart(ws, "TerrainBase", new Vector3(2048.0f, 16.0f, 2048.0f), 0.0f, -8.0f, 0.0f, Color3.INSTANCE.fromRGB(91, 154, 76), 1280, true);
            addSpawn(ws, 0.0f, 0.5f, 0.0f);
        } else if (template == Template.OBBY) {
            addPart(ws, "StartPlatform", new Vector3(20.0f, 2.0f, 20.0f), 0.0f, -1.0f, 0.0f, Color3.INSTANCE.fromRGB(91, 93, 105), 256, false);
            addSpawn(ws, 0.0f, 0.5f, 0.0f);
            Color3[] stageColors = new Color3[]{
                Color3.INSTANCE.fromRGB(231, 76, 60),
                Color3.INSTANCE.fromRGB(230, 126, 34),
                Color3.INSTANCE.fromRGB(241, 196, 15),
                Color3.INSTANCE.fromRGB(46, 204, 113),
                Color3.INSTANCE.fromRGB(52, 152, 219),
                Color3.INSTANCE.fromRGB(155, 89, 182)
            };
            for (int i = 0; i < 6; i++) {
                float sx = (i % 2 == 0) ? -4.0f : 4.0f;
                float sy = 1.0f + (i * 2.0f);
                float sz = -18.0f - (i * 14.0f);
                addPart(ws, "Stage" + (i + 1), new Vector3(8.0f, 2.0f, 8.0f), sx, sy, sz, stageColors[i], 256, false);
            }
            addPart(ws, "FinishPlatform", new Vector3(20.0f, 2.0f, 20.0f), 0.0f, 13.0f, -104.0f, Color3.INSTANCE.fromRGB(245, 205, 48), 288, false);
        } else if (template == Template.PLATFORMER) {
            addPart(ws, "StartIsland", new Vector3(24.0f, 4.0f, 24.0f), 0.0f, -2.0f, 0.0f, Color3.INSTANCE.fromRGB(91, 154, 76), 1280, false);
            addSpawn(ws, 0.0f, 0.5f, 0.0f);
            addPart(ws, "JumpPad1", new Vector3(5.0f, 0.6f, 5.0f), 0.0f, 0.3f, -8.0f, Color3.INSTANCE.fromRGB(0, 212, 255), 288, false);
            float[][] islands = new float[][]{
                {18.0f, 6.0f, -24.0f},
                {-14.0f, 14.0f, -46.0f},
                {16.0f, 22.0f, -68.0f},
                {0.0f, 30.0f, -92.0f}
            };
            for (int i = 0; i < islands.length; i++) {
                float ix = islands[i][0];
                float iy = islands[i][1];
                float iz = islands[i][2];
                addPart(ws, "Island" + (i + 1), new Vector3(16.0f, 4.0f, 16.0f), ix, iy, iz, Color3.INSTANCE.fromRGB(91, 154, 76), 1280, false);
                if (i < islands.length - 1) {
                    addPart(ws, "JumpPad" + (i + 2), new Vector3(5.0f, 0.6f, 5.0f), ix, iy + 2.3f, iz - 4.0f, Color3.INSTANCE.fromRGB(0, 212, 255), 288, false);
                }
            }
        } else {
            addSpawn(ws, 0.0f, 0.5f, 0.0f);
        }
        Instance sp = dm.findService("StarterPlayer");
        Intrinsics.checkNotNull(sp);
        InstanceFactory.INSTANCE.create("StarterPlayerScripts").attachRaw(sp);
        InstanceFactory.INSTANCE.create("StarterCharacterScripts").attachRaw(sp);
        return dm;
    }

    private final Instance service(DataModel dm, String cls) {
        Instance instanceFindService = dm.findService(cls);
        if (instanceFindService != null) {
            return instanceFindService;
        }
        Instance s = InstanceFactory.INSTANCE.create(cls);
        s.setService(true);
        s.attachRaw(dm);
        return s;
    }

    public final DataModel fromDocument(RbxDocument doc) {
        Intrinsics.checkNotNullParameter(doc, "doc");
        DataModel dm = new DataModel();
        dm.getMeta().putAll(doc.getMeta());
        for (Instance r : doc.getRoots()) {
            if (ClassDb.INSTANCE.isServiceName(r.getClassName())) {
                r.setService(true);
            }
            r.attachRaw(dm);
        }
        for (String s : required) {
            service(dm, s);
        }
        Instance ws = dm.findService("Workspace");
        Intrinsics.checkNotNull(ws);
        if (ws.get("CurrentCamera") == null) {
            Instance cam = ws.findFirstChildOfClass("Camera");
            if (cam == null) {
                cam = InstanceFactory.INSTANCE.create("Camera");
                cam.attachRaw(ws);
            }
            ws.set("CurrentCamera", cam, false);
        }
        ensureTerrain(ws);
        ensureSoundServiceDefaults(dm.findService("SoundService"));
        return dm;
    }

    private final void ensureSoundServiceDefaults(Instance ss) {
        if (ss == null) {
            return;
        }
        if (!ss.getProps().containsKey("AmbientReverb")) {
            ss.set("AmbientReverb", new EnumValue(0), false);
        }
        if (!ss.getProps().containsKey("DistanceFactor")) {
            ss.set("DistanceFactor", Float.valueOf(3.33f), false);
        }
        if (!ss.getProps().containsKey("DopplerScale")) {
            ss.set("DopplerScale", Float.valueOf(1.0f), false);
        }
        if (!ss.getProps().containsKey("RolloffScale")) {
            ss.set("RolloffScale", Float.valueOf(1.0f), false);
        }
        if (!ss.getProps().containsKey("RespectFilteringEnabled")) {
            ss.set("RespectFilteringEnabled", true, false);
        }
    }

    private final void ensureTerrain(Instance ws) {
        if (ws.findFirstChildOfClass("Terrain") != null) {
            return;
        }
        Instance t = InstanceFactory.INSTANCE.create("Terrain");
        t.setName("Terrain");
        t.set("Anchored", true, false);
        t.set("Locked", true, false);
        t.set("WaterColor", Color3.INSTANCE.fromRGB(12, 84, 92), false);
        t.set("WaterTransparency", Float.valueOf(0.3f), false);
        t.set("WaterReflectance", Float.valueOf(1.0f), false);
        t.set("WaterWaveSize", Float.valueOf(0.15f), false);
        t.set("WaterWaveSpeed", Float.valueOf(10.0f), false);
        t.setService(false);
        t.attachRaw(ws);
    }
}
