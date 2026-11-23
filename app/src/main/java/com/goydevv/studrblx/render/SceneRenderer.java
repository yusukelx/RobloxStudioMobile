package com.goydevv.studrblx.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.SolidModeling;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.core.Tool;
import com.goydevv.studrblx.engine.Avatar;
import com.goydevv.studrblx.engine.ClothingCompositor;
import com.goydevv.studrblx.engine.Geometry;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.ui.ViewportView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: SceneRenderer.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class SceneRenderer implements GLSurfaceView.Renderer {
    public static final long BASEPLATE_GRID_ID = 6372755229L;
    private static final String DECAL_FS = "\nprecision mediump float;\n\nvarying vec2 vUv;\nuniform sampler2D uTex; uniform vec4 uTint; uniform vec3 uNormal; uniform vec3 uLight; uniform vec3 uSun; uniform float uAmbient;\nvoid main() {\n  vec4 t = texture2D(uTex, vUv);\n  float diff = max(dot(normalize(uNormal), uLight), 0.0);\n  vec3 light = vec3(uAmbient) + uSun * diff;\n  gl_FragColor = vec4(t.rgb * uTint.rgb * light, t.a) * uTint.a;\n}";
    private static final String DECAL_VS = "\nuniform mat4 uVP; uniform mat4 uModel; uniform vec4 uUv;\nattribute vec3 aPos; attribute vec2 aUv;\nvarying vec2 vUv;\nvoid main() { vUv = aUv * uUv.xy + uUv.zw; gl_Position = uVP * uModel * vec4(aPos, 1.0); }";
    private static final String DEPTH_FS = "\n#ifdef GL_FRAGMENT_PRECISION_HIGH\nprecision highp float;\n#else\nprecision mediump float;\n#endif\n\nvoid main() {\n  vec4 enc = vec4(1.0, 255.0, 65025.0, 16581375.0) * gl_FragCoord.z;\n  enc = fract(enc);\n  enc -= enc.yzww * vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);\n  gl_FragColor = enc;\n}";
    private static final String DEPTH_VS = "\nuniform mat4 uVP; uniform mat4 uModel;\nattribute vec3 aPos;\nvoid main() { gl_Position = uVP * uModel * vec4(aPos, 1.0); }";
    private static final int GL_MAX_ANISOTROPY = 34047;
    private static final int GL_TEXTURE_MAX_ANISOTROPY = 34046;
    private static final String LINE_FS = "\nprecision mediump float;\nuniform vec4 uColor;\nvoid main() { gl_FragColor = uColor; }";
    private static final String LINE_VS = "\nuniform mat4 uVP; uniform mat4 uModel;\nattribute vec3 aPos;\nvoid main() { gl_Position = uVP * uModel * vec4(aPos, 1.0); }";
    private static final String LIT_FS = "\nprecision mediump float;\n\n#ifdef BATCH\nvarying vec3 vN; varying vec3 vW; varying vec4 vS; varying vec3 vA; varying vec4 vCol; varying vec4 vMat;\n#else\nvarying vec3 vN; varying vec3 vW; varying vec3 vL; varying vec3 vLN; varying vec4 vS;\n#endif\nuniform vec4 uColor; uniform vec3 uLight; uniform vec3 uCam; uniform float uEmissive;\nuniform float uGrid; uniform vec3 uSize; uniform vec3 uHorizon; uniform vec3 uZenith; uniform vec3 uSun;\nuniform float uAmbient; uniform vec3 uFogColor; uniform vec3 uFog; uniform vec4 uMat; uniform vec3 uSurfA; uniform vec3 uSurfB;\nuniform sampler2D uShadow; uniform float uShadowOn; uniform vec2 uShadowTexel; uniform float uShadowSoft; uniform vec3 uShiftBot; uniform float uEnvSpec; uniform int uPtCount; uniform vec4 uPtPos[8]; uniform vec4 uPtCol[8]; uniform vec4 uPtDir[8]; uniform vec4 uCCParams; uniform vec3 uCCTint; uniform vec4 uBloom; uniform vec4 uDof; uniform vec3 uAmbRgb; uniform float uExposure; uniform float uTime; uniform float uTruss;\nuniform sampler2D uSurfTex; uniform float uHasSurfTex;\n#ifdef TEXTURED\nvarying vec3 vT; varying vec3 vB;\nuniform sampler2D uMatTex; uniform sampler2D uMatNrm; uniform float uTexScale; uniform float uHasNrm;\n#endif\n\nfloat hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453); }\nvec2 hash2(vec2 p) { return fract(sin(vec2(dot(p, vec2(127.1, 311.7)), dot(p, vec2(269.5, 183.3)))) * 43758.5453); }\nfloat noise(vec2 p) {\n  vec2 i = floor(p); vec2 f = fract(p); f = f * f * (3.0 - 2.0 * f);\n  return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), f.x), mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), f.x), f.y);\n}\nfloat fbm(vec2 p) { return noise(p) * 0.5 + noise(p * 2.03 + 7.1) * 0.3 + noise(p * 4.1 + 3.3) * 0.2; }\n#ifdef ADVANCED\n// Cellular noise: x = distance to the nearest cell point, y = distance to the cell edge (approx).\nvec3 voro(vec2 x) {\n  vec2 n = floor(x); vec2 f = fract(x);\n  float d1 = 8.0; float d2 = 8.0; vec2 id = n;\n  for (int j = -1; j <= 1; j++) for (int i = -1; i <= 1; i++) {\n    vec2 g = vec2(float(i), float(j));\n    vec2 r = g + hash2(n + g) - f;\n    float d = dot(r, r);\n    if (d < d1) { d2 = d1; d1 = d; id = n + g; } else if (d < d2) { d2 = d; }\n  }\n  return vec3(sqrt(d1), sqrt(d2) - sqrt(d1), hash(id));\n}\n#endif\n// Roblox surface types on one face (sp = stud coordinates on the face, fsz = face size).\n// Roblox's stud look: a raised disc lit from the upper left with a soft shadow to the lower right.\nfloat studShade(vec2 f, float R) {\n  vec2 L = vec2(-0.62, 0.78);\n  float r = length(f);\n  float inside = smoothstep(R + 0.012, R - 0.012, r);\n  float rim = smoothstep(R - 0.09, R, r) * inside;\n  float side = dot(f / max(r, 0.001), L);\n  float shadow = smoothstep(R + 0.1, R, length(f + L * 0.05)) * (1.0 - inside);\n  return 1.0 + inside * 0.05 + rim * side * 0.22 - shadow * 0.2;\n}\nvec3 surface(vec3 c, float s, vec2 sp, vec2 fsz) {\n  if (s < 0.5 || s > 9.5) return c;\n  if (uHasSurfTex > 0.5 && s < 5.5) {\n    // Roblox's own surface textures: one 2x8-stud tile per type, shaded around grey 128\n    // (R = studs, G = weld/glue, B = inlet). Sampled with repeat (no fract) so mipmapping\n    // never sees a seam. s is the same for a whole face, so this branch is uniform per triangle.\n    vec2 st = vec2(sp.x * 0.5, -sp.y * 0.125);\n    vec3 t = texture2D(uSurfTex, st).rgb;\n    float k;\n    if (s < 2.5) k = t.g;\n    else if (s < 3.5) k = t.r;\n    else if (s < 4.5) k = t.b;\n    else { vec2 cell = floor(fract(st) * vec2(2.0, 8.0)); k = mod(cell.x + cell.y, 2.0) < 0.5 ? t.r : t.b; }\n    return c * (k * 2.0);\n  }\n  vec2 f = fract(sp) - 0.5; float r = length(f);\n  if (s > 2.5 && s < 3.5) { // Studs\n    c *= studShade(f, 0.29);\n  } else if (s > 3.5 && s < 4.5) { // Inlet: a sunken hole, shaded the opposite way\n    vec2 L = vec2(-0.62, 0.78);\n    float inside = smoothstep(0.302, 0.278, r);\n    float rim = smoothstep(0.2, 0.29, r) * inside;\n    c *= 1.0 - inside * 0.16 - rim * dot(f / max(r, 0.001), L) * 0.2 + smoothstep(0.36, 0.3, r) * (1.0 - inside) * 0.05;\n  } else if (s > 4.5 && s < 5.5) { // Universal: a stud with a hole in it\n    c *= studShade(f, 0.29) * (1.0 - smoothstep(0.14, 0.12, r) * 0.18);\n  } else if (s > 1.5 && s < 2.5) { // Weld: diagonal hatching\n    float l = fract((sp.x + sp.y) * 1.25);\n    c *= mix(1.0, 0.84, smoothstep(0.82, 0.88, l) * smoothstep(1.0, 0.94, l));\n  } else if (s > 0.5 && s < 1.5) { // Glue: grid of small crosses\n    vec2 g = abs(fract(sp * 2.0) - 0.5);\n    float cr = max(step(g.x, 0.05) * step(g.y, 0.22), step(g.y, 0.05) * step(g.x, 0.22));\n    c *= 1.0 - 0.14 * cr;\n  } else { // Hinge / Motor / SteppingMotor: a yellow joint in the middle of the face\n    float rr = length(sp - fsz * 0.5);\n    vec3 yellow = vec3(0.96, 0.8, 0.16);\n    if (s > 6.5) {\n      if (rr < 0.75) c = mix(c, vec3(0.45, 0.45, 0.47), smoothstep(0.75, 0.7, rr));\n      if (s > 7.5 && rr < 0.75 && rr > 0.5) { float a = atan(sp.y - fsz.y * 0.5, sp.x - fsz.x * 0.5); c *= 0.75 + 0.25 * step(0.5, fract(a * 1.273)); }\n    }\n    if (rr < 0.45) c = mix(c, yellow, smoothstep(0.45, 0.4, rr));\n  }\n  return c;\n}\nfloat unpackDepth(vec4 c) { return dot(c, vec4(1.0, 1.0 / 255.0, 1.0 / 65025.0, 1.0 / 16581375.0)); }\nfloat sampleShadow(vec2 uv, float z) { return z > unpackDepth(texture2D(uShadow, uv)) ? 0.0 : 1.0; }\nfloat shadowAt(float ndl) {\n  if (uShadowOn < 0.5 || ndl <= 0.0) return 1.0;\n  vec3 p = vS.xyz / vS.w * 0.5 + 0.5;\n  if (p.x <= 0.001 || p.x >= 0.999 || p.y <= 0.001 || p.y >= 0.999 || p.z >= 1.0) return 1.0;\n  float z = p.z - (0.00025 + 0.00085 * (1.0 - ndl));\n  vec2 t = uShadowTexel * mix(0.65, 2.85, clamp(uShadowSoft, 0.0, 1.0));\n  float s = sampleShadow(p.xy, z) * 2.0;\n  s += sampleShadow(p.xy + vec2(-t.x, -t.y), z);\n  s += sampleShadow(p.xy + vec2(t.x, -t.y), z);\n  s += sampleShadow(p.xy + vec2(-t.x, t.y), z);\n  s += sampleShadow(p.xy + vec2(t.x, t.y), z);\n  s /= 6.0;\n  vec2 e = min(p.xy, 1.0 - p.xy);\n  float fade = clamp(min(e.x, e.y) * 12.0, 0.0, 1.0);\n  return mix(1.0, s, fade);\n}\nvoid main() {\n  vec3 n = normalize(vN);\n  if (!gl_FrontFacing) n = -n;\n#ifdef BATCH\n  vec3 c = vCol.rgb; vec4 mat = vMat; float emis = vCol.a; float alpha = 1.0;\n  vec2 uv = vA.xy; vec2 fsz = vec2(1.0); float sf = vA.z;\n#else\n  vec3 c = uColor.rgb; vec4 mat = uMat; float emis = uEmissive; float alpha = uColor.a;\n  vec3 a = abs(vLN);\n  vec3 p = vL * uSize + uSize * 0.5;\n  bool yFace = a.y > a.x && a.y > a.z; bool xFace = !yFace && a.x > a.z;\n  vec2 uv = yFace ? p.xz : (xFace ? p.zy : p.xy);\n  vec2 fsz = yFace ? uSize.xz : (xFace ? uSize.zy : uSize.xy);\n  // Surface types (studs, inlets, welds, hinges...) on whichever face this is.\n  float sf = yFace ? (vLN.y > 0.0 ? uSurfA.y : uSurfB.y) : (xFace ? (vLN.x > 0.0 ? uSurfA.x : uSurfB.x) : (vLN.z > 0.0 ? uSurfA.z : uSurfB.z));\n  if (uTruss > 0.5) {\n    // Lattice: frame and diagonals per 2-stud cell; everything else is see-through.\n    vec2 cell = fract(uv / 2.0);\n    float e = min(min(cell.x, cell.y), min(1.0 - cell.x, 1.0 - cell.y));\n    float dg = min(abs(cell.x - cell.y), abs(cell.x + cell.y - 1.0));\n    if (e > 0.11 && dg > 0.07) discard;\n  }\n#endif\n  vec3 glow = vec3(0.0);\n  float type = mat.x;\n#ifdef TEXTURED\n  // Roblox material textures: the color map tints the part color; the normal map bends the light.\n  vec2 tuv = vec2(uv.x, -uv.y) * uTexScale;\n  c *= texture2D(uMatTex, tuv).rgb * 1.08;\n  if (uHasNrm > 0.5) {\n    vec3 tn = texture2D(uMatNrm, tuv).xyz * 2.0 - 1.0;\n    n = normalize(normalize(vT) * tn.x + normalize(vB) * tn.y + n * max(tn.z, 0.25));\n  }\n  type = 0.0;\n#endif\n  if (type > 0.5) {\n    float amt = mat.y; float sc = mat.z;\n#ifdef ADVANCED\n    if (type > 10.5) {\n      if (type < 11.5) { // Marble veins\n        float v = abs(sin((uv.x * 0.6 + uv.y * 0.25 + fbm(uv * 0.35) * 5.0) * 1.7));\n        c *= mix(0.8, 1.05, pow(v, 0.35)) + (noise(uv * 3.0) - 0.5) * 0.04;\n      } else if (type < 12.5) { // Slate / sandstone: layered plates\n        float layer = floor(uv.y * sc * 2.0 + fbm(uv * 0.3) * 2.5);\n        c *= 0.9 + hash(vec2(layer, 3.0)) * 0.16 + (noise(uv * 5.0) - 0.5) * amt;\n      } else if (type < 13.5) { // Cobblestone / pebble / basalt: cells with dark gaps\n        vec3 v = voro(uv * sc);\n        c *= mix(0.62, 0.92 + v.z * 0.16, smoothstep(0.03, 0.12, v.y)) * (1.0 - v.x * 0.12);\n      } else if (type < 14.5) { // Cracked lava: dark rock with glowing cracks\n        vec3 v = voro(uv * sc);\n        c *= 0.55 + fbm(uv * 1.5) * 0.25;\n        glow = vec3(1.0, 0.36, 0.05) * smoothstep(0.08, 0.0, v.y) * (0.7 + 0.3 * sin(uTime * 1.5 + v.z * 6.0));\n      } else if (type < 15.5) { // Corroded metal: rust patches\n        float rust = smoothstep(0.42, 0.66, fbm(uv * sc));\n        c = mix(c * (0.95 + (noise(uv * vec2(18.0, 0.8)) - 0.5) * 0.08), c * vec3(0.7, 0.42, 0.24) + vec3(0.14, 0.05, 0.0), rust * 0.8);\n      } else if (type < 16.5) { // Tiles (ceramic / pavement)\n        vec2 tf = fract(uv / sc); float grout = step(0.035, tf.x) * step(0.035, tf.y);\n        c *= mix(0.72, 1.0 + (hash(floor(uv / sc)) - 0.5) * amt * 2.0 + (noise(uv * 3.0) - 0.5) * 0.04, grout);\n      } else if (type < 17.5) { // Roof tiles / shingles: overlapping rows\n        float row = floor(uv.y / (0.8 * sc));\n        vec2 q = vec2(uv.x / sc + mod(row, 2.0) * 0.5, fract(uv.y / (0.8 * sc)));\n        float edge = smoothstep(0.0, 0.08, fract(q.x)) * smoothstep(1.0, 0.92, fract(q.x));\n        c *= (0.8 + 0.25 * q.y) * mix(0.82, 1.0, edge) * (0.94 + hash(vec2(floor(q.x), row)) * 0.12);\n      } else if (type < 18.5) { // Cardboard corrugation\n        c *= 0.95 + 0.05 * sin(uv.x * 22.0) + (noise(uv * 8.0) - 0.5) * 0.06;\n      } else if (type < 19.5) { // ForceField: moving bands\n        float s = sin((uv.x + uv.y) * 2.5 - uTime * 2.2) * 0.5 + 0.5;\n        c = c * (0.55 + 0.45 * s) + vec3(0.12) * s;\n      } else if (type < 20.5) { // Leather: small creased cells\n        vec3 v = voro(uv * 6.0);\n        c *= 0.9 + 0.1 * smoothstep(0.0, 0.12, v.y) + (v.z - 0.5) * 0.04;\n      } else if (type < 21.5) { // Carpet: dense fibres\n        c *= 0.9 + hash(floor(uv * 28.0)) * 0.12 + (noise(uv * 6.0) - 0.5) * 0.1;\n      } else if (type < 22.5) { // Asphalt: speckled\n        c *= 0.9 + (hash(floor(uv * 16.0)) - 0.5) * 0.25 + (noise(uv * 2.0) - 0.5) * 0.08;\n      } else if (type < 23.5) { // Concrete: blotchy with pores\n        c *= 1.0 + (fbm(uv * sc) - 0.5) * amt * 2.4 - step(0.985, hash(floor(uv * 20.0))) * 0.18;\n      } else if (type < 24.5) { // Granite: mixed grains\n        float g = hash(floor(uv * 12.0));\n        c *= 0.88 + g * 0.22 - step(0.9, g) * 0.3 + (fbm(uv * 1.5) - 0.5) * 0.1;\n      } else { // Sand / snow / salt: fine grain with sparkle\n        float g = hash(floor(uv * 30.0));\n        c *= 1.0 + (g - 0.5) * amt * 2.0 + (fbm(uv * sc) - 0.5) * 0.1;\n        c += vec3(step(0.996, g) * 0.15);\n      }\n    } else\n    if (type < 1.5) { c *= 1.0 + (noise(uv * 2.2) - 0.5) * amt * 2.0; }\n    else if (type < 2.5) {\n      float g = sin((uv.x * 1.3 + fbm(uv * vec2(0.35, 2.2)) * 4.0) * 5.0);\n      c *= 1.0 + g * amt * 0.6 - amt * 0.2;\n    } else if (type < 3.5) {\n      float plank = floor(uv.y);\n      float seamY = smoothstep(0.0, 0.05, fract(uv.y)) * smoothstep(1.0, 0.95, fract(uv.y));\n      float off = hash(vec2(plank, 3.0)) * 8.0;\n      float seamX = smoothstep(0.0, 0.04, fract((uv.x + off) / 6.0) * 6.0);\n      float g = sin((uv.x * 1.6 + fbm(vec2(uv.x * 0.3, uv.y * 3.0 + plank)) * 3.0) * 6.0);\n      c *= (0.9 + hash(vec2(plank, 1.0)) * 0.18) * (1.0 + g * amt * 0.35) * mix(0.72, 1.0, seamY * seamX);\n    } else if (type < 4.5) {\n      // Brick: running bond with light grey mortar, like Roblox's brick material.\n      vec2 b = vec2(uv.x / 1.6, uv.y / 0.66);\n      float row = floor(b.y);\n      b.x += mod(row, 2.0) * 0.5;\n      vec2 f = fract(b);\n      float mortar = smoothstep(0.05, 0.09, f.y) * smoothstep(0.025, 0.05, f.x) * smoothstep(1.0, 0.985, f.x);\n      vec3 brick = c * (0.9 + (hash(floor(b)) - 0.5) * amt * 2.2 + (noise(uv * 7.0) - 0.5) * 0.08);\n      vec3 grout = mix(vec3(dot(c, vec3(0.3, 0.59, 0.11))), vec3(0.82), 0.55);\n      c = mix(grout, brick, mortar);\n    } else if (type < 5.5) { c *= 1.0 + (fbm(uv * sc) - 0.5) * amt * 2.6; }\n    else if (type < 6.5) {\n      vec2 q = vec2(uv.x + uv.y, uv.x - uv.y) * 1.4;\n      vec2 f = fract(q) - 0.5;\n      float bump = smoothstep(0.18, 0.05, abs(f.x) * 0.35 + abs(f.y));\n      c *= 0.92 + bump * 0.22 + (noise(uv * 8.0) - 0.5) * 0.05;\n    } else if (type < 7.5) { c *= 1.0 + (noise(uv * vec2(24.0, 0.6)) - 0.5) * amt * 2.0; }\n    else if (type < 8.5) {\n      float f = fbm(uv * sc);\n      c *= 1.0 + (f - 0.5) * amt * 2.4 + (noise(uv * 9.0) - 0.5) * 0.05;\n      c.r += (noise(uv * 0.25) - 0.5) * 0.04;\n    } else if (type < 9.5) { c *= 1.0 + sin(uv.x * 30.0) * sin(uv.y * 30.0) * amt; }\n    else { c *= 1.0 + (fbm(uv * sc) - 0.5) * amt * 2.0; }\n#else\n    // Lean variant: plastic grain only (other materials use the ADVANCED program).\n    c *= 1.0 + (noise(uv * 2.2) - 0.5) * amt * 2.0;\n#endif\n  }\n  c = surface(c, sf, uv, fsz);\n#ifndef BATCH\n  if (uGrid > 0.5) {\n    vec2 f = abs(fract(uv / 4.0 + 0.5) - 0.5) * 4.0;\n    float d = min(f.x, f.y);\n    c *= mix(0.84, 1.0, smoothstep(0.04, 0.09, d));\n  }\n#endif\n  vec3 col;\n  if (emis > 0.5) {\n    col = c * 1.15 + vec3(0.06);\n  } else {\n    float ndl = dot(n, uLight);\n    float diff = max(ndl, 0.0);\n    float sh = shadowAt(ndl);\n    vec3 skyTop = mix(vec3(1.0), mix(uHorizon, uZenith, 0.5), 0.22);\n    vec3 groundBounce = mix(vec3(0.58, 0.59, 0.62), uHorizon * 0.72, 0.18);\n    float hemi = clamp(n.y * 0.5 + 0.5, 0.0, 1.0);\n    vec3 amb = uAmbient * uAmbRgb * mix(groundBounce, skyTop, hemi);\n    // Subtle back-fill wrap so shadow sides retain Roblox OutdoorAmbient richness\n    float wrap = max(0.0, (-ndl) * 0.14);\n    col = c * (amb + uSun * (diff * sh + wrap * 0.35) + uShiftBot * max(-ndl, 0.0) * 0.35);\n    vec3 v = normalize(uCam - vW);\n    vec3 h = normalize(uLight + v);\n    float rough = clamp(1.0 - mat.w * 0.78, 0.14, 0.94);\n    float specPow = mix(96.0, 12.0, rough * rough);\n    float ndh = max(dot(n, h), 0.0);\n    float a2 = rough * rough * rough * rough;\n    float dDen = (ndh * ndh) * (a2 - 1.0) + 1.0;\n    float ggx = a2 / max(3.14159 * dDen * dDen, 0.002);\n    float schlick = 0.04 + 0.96 * pow(1.0 - max(dot(h, v), 0.0), 5.0);\n    float spec = min(ggx * schlick * (0.28 + mat.w * 0.95), 2.2) * diff * sh * uEnvSpec;\n    col += uSun * spec;\n    if (uPtCount > 0) for (int li = 0; li < 8; li++) {\n      if (li >= uPtCount) break;\n      vec3 L = uPtPos[li].xyz - vW;\n      float d = length(L);\n      float r = uPtPos[li].w;\n      if (d < r) {\n        vec3 ldir = L / max(d, 0.001);\n        float tt = clamp(1.0 - (d / r) * (d / r), 0.0, 1.0);\n        float att = (tt * tt) / (1.0 + 0.04 * d * d);\n        if (uPtDir[li].w > 0.5) {\n          float ca = dot(-ldir, uPtDir[li].xyz);\n          att *= smoothstep(uPtCol[li].w, min(uPtCol[li].w + 0.14, 0.999), ca);\n        }\n        float pndl = max(dot(n, ldir), 0.0);\n        vec3 ph = normalize(ldir + v);\n        float pspec = pow(max(dot(n, ph), 0.0), specPow) * (0.12 + mat.w * 0.55) * uEnvSpec;\n        col += (c * pndl + vec3(pspec)) * uPtCol[li].rgb * att;\n      }\n    }\n    float fres = pow(1.0 - max(dot(n, v), 0.0), 4.5) * (0.04 + mat.w * 0.26) * uEnvSpec;\n    col = mix(col, mix(uHorizon, uZenith, 0.5), fres);\n  }\n  col += glow;\n  col *= uExposure;\n  // ACES Filmic tone mapping (matches Roblox Future/ShadowMap highlight roll-off)\n  if (uBloom.x > 0.001) { float blum = dot(col, vec3(0.2126, 0.7152, 0.0722)); float knee = smoothstep(uBloom.y * 0.75, uBloom.y + 0.65, blum); col += mix(col, vec3(blum), 0.35) * knee * uBloom.x * 0.20; }\n  vec3 mapped = clamp((col * (2.51 * col + 0.03)) / (col * (2.43 * col + 0.59) + 0.14), 0.0, 1.0); col = mix(clamp(col, 0.0, 1.0), mapped, 0.22);\n  if (uCCParams.w > 0.5) {\n    col = clamp(col + vec3(uCCParams.x), 0.0, 1.0);\n    col = clamp((col - 0.5) * (1.0 + uCCParams.y) + 0.5, 0.0, 1.0);\n    float luma = dot(col, vec3(0.2126, 0.7152, 0.0722));\n    col = clamp(mix(vec3(luma), col, 1.0 + uCCParams.z) * uCCTint, 0.0, 1.0);\n  }\n  float dist = length(uCam - vW);\n  if (uDof.z > 0.001 || uDof.w > 0.001) {\n    float coc = max(0.0, (dist - (uDof.x + uDof.y)) / max(uDof.y + 12.0, 1.0)) * uDof.z + max(0.0, ((uDof.x - uDof.y) - dist) / max(uDof.x, 1.0)) * uDof.w;\n    col = mix(col, mix(uHorizon, uFogColor, 0.5), clamp(coc * 0.42, 0.0, 0.55));\n  }\n  float fog;\n  if (uFog.z > 1.5) fog = (1.0 - exp(-dist * uFog.x)) * uFog.y;\n  else if (uFog.z > 0.5) fog = clamp((dist - uFog.x) / max(uFog.y - uFog.x, 1.0), 0.0, 1.0);\n  else fog = smoothstep(140.0, 950.0, dist) * 0.88;\n  vec3 mieFog = uFogColor + uSun * pow(max(dot(normalize(vW - uCam), uLight), 0.0), 6.0) * 0.20;\n  col = mix(col, mieFog, fog);\n  gl_FragColor = vec4(col, alpha);\n}";
    private static final String LIT_VS = "\nuniform mat4 uVP; uniform mat4 uModel; uniform mat3 uNrm; uniform mat4 uShadowVP;\n#ifdef BATCH\n// Static batches: vertices are already in world space and carry the part's look.\nattribute vec3 aPos; attribute vec4 aNrm; attribute vec2 aUv; attribute vec4 aCol; attribute vec4 aMat;\nvarying vec3 vN; varying vec3 vW; varying vec4 vS; varying vec3 vA; varying vec4 vCol; varying vec4 vMat;\n#ifdef TEXTURED\nattribute vec3 aTan; attribute vec3 aBit;\nvarying vec3 vT; varying vec3 vB;\n#endif\nvoid main() {\n  vW = aPos; vN = aNrm.xyz;\n  vS = uShadowVP * vec4(aPos, 1.0);\n  vA = vec3(aUv, floor(aNrm.w * 127.0 + 0.5));\n  vCol = aCol;\n  vMat = vec4(aMat.x, aMat.y / 1000.0, aMat.z / 50.0, aMat.w / 120.0);\n#ifdef TEXTURED\n  vT = aTan; vB = aBit;\n#endif\n  gl_Position = uVP * vec4(aPos, 1.0);\n}\n#else\nattribute vec3 aPos; attribute vec3 aNrm;\nvarying vec3 vN; varying vec3 vW; varying vec3 vL; varying vec3 vLN; varying vec4 vS;\n#ifdef TEXTURED\nvarying vec3 vT; varying vec3 vB;\n#endif\nvoid main() {\n  vec4 w = uModel * vec4(aPos, 1.0);\n  vW = w.xyz; vN = uNrm * aNrm; vL = aPos; vLN = aNrm;\n  vS = uShadowVP * w;\n#ifdef TEXTURED\n  // World directions of the face's texture axes (same box projection the fragment shader uses).\n  vec3 an = abs(aNrm);\n  bool yF = an.y > an.x && an.y > an.z; bool xF = !yF && an.x > an.z;\n  vT = (uModel * vec4(yF ? vec3(1.0, 0.0, 0.0) : (xF ? vec3(0.0, 0.0, 1.0) : vec3(1.0, 0.0, 0.0)), 0.0)).xyz;\n  vB = (uModel * vec4(yF ? vec3(0.0, 0.0, 1.0) : vec3(0.0, 1.0, 0.0), 0.0)).xyz;\n#endif\n  gl_Position = uVP * w;\n}\n#endif";
    private static final String MESH_FS = "\nprecision mediump float;\nvarying vec3 vN; varying vec3 vW; varying vec2 vUv; varying vec4 vS;\nuniform vec4 uColor; uniform vec3 uTint; uniform vec3 uLight; uniform vec3 uSun; uniform float uAmbient; uniform vec3 uCam;\nuniform sampler2D uTex; uniform float uHasTex; uniform sampler2D uShadow; uniform float uShadowOn; uniform vec2 uShadowTexel; uniform float uShadowSoft; uniform vec3 uShiftBot; uniform float uEnvSpec; uniform int uPtCount; uniform vec4 uPtPos[8]; uniform vec4 uPtCol[8]; uniform vec4 uPtDir[8]; uniform vec4 uCCParams; uniform vec3 uCCTint; uniform vec4 uBloom; uniform vec4 uDof; uniform vec3 uAmbRgb; uniform sampler2D uPbrNrm; uniform sampler2D uPbrRough; uniform sampler2D uPbrMetal; uniform vec4 uPbrFlags; uniform vec4 uPbrEmis;\nuniform vec3 uFogColor; uniform vec3 uFog; uniform float uExposure; uniform float uEmissive; uniform vec3 uHorizon; uniform vec3 uZenith;\nfloat unpackDepth(vec4 c) { return dot(c, vec4(1.0, 1.0 / 255.0, 1.0 / 65025.0, 1.0 / 16581375.0)); }\nfloat sampleShadow(vec2 uv, float z) { return z > unpackDepth(texture2D(uShadow, uv)) ? 0.0 : 1.0; }\nfloat shadowAt(float ndl) {\n  if (uShadowOn < 0.5 || ndl <= 0.0) return 1.0;\n  vec3 p = vS.xyz / vS.w * 0.5 + 0.5;\n  if (p.x <= 0.001 || p.x >= 0.999 || p.y <= 0.001 || p.y >= 0.999 || p.z >= 1.0) return 1.0;\n  float z = p.z - (0.00030 + 0.00095 * (1.0 - ndl));\n  vec2 t = uShadowTexel * mix(0.65, 2.85, clamp(uShadowSoft, 0.0, 1.0));\n  float s = sampleShadow(p.xy, z) * 2.0;\n  s += sampleShadow(p.xy + vec2(-t.x, -t.y), z);\n  s += sampleShadow(p.xy + vec2(t.x, -t.y), z);\n  s += sampleShadow(p.xy + vec2(-t.x, t.y), z);\n  s += sampleShadow(p.xy + vec2(t.x, t.y), z);\n  return s / 6.0;\n}\n#ifdef BATCH\nvarying vec4 vCol;\n#endif\nvoid main() {\n  vec3 n = normalize(vN);\n  if (!gl_FrontFacing) n = -n;\n#ifdef BATCH\n  vec3 c = vCol.rgb; float a = 1.0; float emis = vCol.a; vec3 tint = vec3(1.0);\n#else\n  vec3 c = uColor.rgb; float a = uColor.a; float emis = uEmissive; vec3 tint = uTint;\n#endif\n  if (uHasTex > 0.5) {\n    vec4 t = texture2D(uTex, vUv);\n    float am = uPbrFlags.w;\n    if (am > 1.5) { c = mix(t.rgb, t.rgb * c, t.a); }\n    else if (am > 0.5) { if (t.a < 0.04) discard; c = t.rgb; a *= t.a; }\n    else if (uPbrFlags.x + uPbrFlags.y + uPbrFlags.z > 0.5) { c = mix(c, t.rgb, t.a); }\n    else { if (t.a < 0.12) discard; c = t.rgb / max(t.a, 0.001); }\n  }\n  c *= tint;\n  float pbrRough = uPbrFlags.y > 0.5 ? clamp(texture2D(uPbrRough, vUv).r, 0.06, 0.98) : 0.65;\n  float pbrMetal = uPbrFlags.z > 0.5 ? clamp(texture2D(uPbrMetal, vUv).r, 0.0, 1.0) : 0.0;\n  if (uPbrFlags.x > 0.5) {\n    vec3 tn = texture2D(uPbrNrm, vUv).xyz * 2.0 - 1.0;\n    vec3 upRef = abs(n.y) < 0.99 ? vec3(0.0, 1.0, 0.0) : vec3(1.0, 0.0, 0.0);\n    vec3 tang = normalize(cross(upRef, n));\n    vec3 bitang = cross(n, tang);\n    n = normalize(tang * tn.x + bitang * tn.y + n * max(tn.z, 0.25));\n  }\n  vec3 col;\n  if (emis > 0.5) col = c * 1.15;\n  else {\n    float ndl = dot(n, uLight);\n    float sh = shadowAt(ndl);\n    vec3 skyTop = mix(vec3(1.0), mix(uHorizon, uZenith, 0.5), 0.22);\n    vec3 groundBounce = mix(vec3(0.78, 0.79, 0.82), uHorizon * 0.85, 0.14);\n    float hemi = clamp(n.y * 0.5 + 0.5, 0.0, 1.0);\n    vec3 amb = max(uAmbient, 0.48) * uAmbRgb * mix(groundBounce, skyTop, hemi);\n    float wrap = max(0.0, (-ndl) * 0.26) + 0.14;\n    float diffScale = 1.0 - pbrMetal * 0.72;\n    col = c * diffScale * (amb + uSun * (max(ndl, 0.0) * sh + wrap * 0.36) + uShiftBot * max(-ndl, 0.0) * 0.35);\n    vec3 v = normalize(uCam - vW);\n    vec3 h = normalize(uLight + v);\n    float specPow = mix(128.0, 10.0, pbrRough * pbrRough);\n    vec3 specCol = mix(uSun, c * uSun, pbrMetal);\n    float specInt = mix(0.12, 0.82, (1.0 - pbrRough) * 0.65 + pbrMetal * 0.35);\n    col += specCol * pow(max(dot(n, h), 0.0), specPow) * specInt * sh * uEnvSpec;\n    if (uPbrFlags.y + uPbrFlags.z > 0.5) {\n      float fres = pow(1.0 - max(dot(n, v), 0.0), 4.0) * (0.04 + (1.0 - pbrRough) * 0.24 + pbrMetal * 0.38) * uEnvSpec;\n      col = mix(col, mix(uHorizon, uZenith, 0.5) * mix(vec3(1.0), c, pbrMetal), clamp(fres, 0.0, 0.75));\n    }\n    col += uPbrEmis.rgb * uPbrEmis.w;\n    if (uPtCount > 0) for (int li = 0; li < 8; li++) {\n      if (li >= uPtCount) break;\n      vec3 L = uPtPos[li].xyz - vW;\n      float d = length(L);\n      float r = uPtPos[li].w;\n      if (d < r) {\n        vec3 ldir = L / max(d, 0.001);\n        float tt = clamp(1.0 - (d / r) * (d / r), 0.0, 1.0);\n        float att = (tt * tt) / (1.0 + 0.04 * d * d);\n        if (uPtDir[li].w > 0.5) {\n          float ca = dot(-ldir, uPtDir[li].xyz);\n          att *= smoothstep(uPtCol[li].w, min(uPtCol[li].w + 0.14, 0.999), ca);\n        }\n        float pndl = max(dot(n, ldir), 0.0);\n        vec3 ph = normalize(ldir + v);\n        float pspec = pow(max(dot(n, ph), 0.0), 32.0) * 0.16 * uEnvSpec;\n        col += (c * pndl + vec3(pspec)) * uPtCol[li].rgb * att;\n      }\n    }\n  }\n  col *= uExposure;\n  if (uBloom.x > 0.001) { float blum = dot(col, vec3(0.2126, 0.7152, 0.0722)); float knee = smoothstep(uBloom.y * 0.75, uBloom.y + 0.65, blum); col += mix(col, vec3(blum), 0.35) * knee * uBloom.x * 0.20; }\n  vec3 mapped = clamp((col * (2.51 * col + 0.03)) / (col * (2.43 * col + 0.59) + 0.14), 0.0, 1.0); col = mix(clamp(col, 0.0, 1.0), mapped, 0.22);\n  if (uCCParams.w > 0.5) {\n    col = clamp(col + vec3(uCCParams.x), 0.0, 1.0);\n    col = clamp((col - 0.5) * (1.0 + uCCParams.y) + 0.5, 0.0, 1.0);\n    float luma = dot(col, vec3(0.2126, 0.7152, 0.0722));\n    col = clamp(mix(vec3(luma), col, 1.0 + uCCParams.z) * uCCTint, 0.0, 1.0);\n  }\n  float dist = length(uCam - vW);\n  if (uDof.z > 0.001 || uDof.w > 0.001) {\n    float coc = max(0.0, (dist - (uDof.x + uDof.y)) / max(uDof.y + 12.0, 1.0)) * uDof.z + max(0.0, ((uDof.x - uDof.y) - dist) / max(uDof.x, 1.0)) * uDof.w;\n    col = mix(col, mix(uHorizon, uFogColor, 0.5), clamp(coc * 0.42, 0.0, 0.55));\n  }\n  float fog;\n  if (uFog.z > 1.5) fog = (1.0 - exp(-dist * uFog.x)) * uFog.y;\n  else if (uFog.z > 0.5) fog = clamp((dist - uFog.x) / max(uFog.y - uFog.x, 1.0), 0.0, 1.0);\n  else fog = smoothstep(140.0, 950.0, dist) * 0.88;\n  vec3 mieFog = uFogColor + uSun * pow(max(dot(normalize(vW - uCam), uLight), 0.0), 6.0) * 0.20;\n  gl_FragColor = vec4(mix(col, mieFog, fog), a);\n}";
    private static final String MESH_VS = "\nuniform mat4 uVP; uniform mat4 uModel; uniform mat3 uNrm; uniform mat4 uShadowVP;\nvarying vec3 vN; varying vec3 vW; varying vec2 vUv; varying vec4 vS;\n#ifdef BATCH\nattribute vec3 aPos; attribute vec4 aNrm; attribute vec2 aUv; attribute vec4 aCol;\nvarying vec4 vCol;\nvoid main() {\n  vW = aPos; vN = aNrm.xyz; vUv = aUv; vS = uShadowVP * vec4(aPos, 1.0); vCol = aCol;\n  gl_Position = uVP * vec4(aPos, 1.0);\n}\n#else\nattribute vec3 aPos; attribute vec3 aNrm; attribute vec2 aUv;\nvoid main() {\n  vec4 w = uModel * vec4(aPos, 1.0);\n  vW = w.xyz; vN = uNrm * aNrm; vUv = aUv; vS = uShadowVP * w;\n  gl_Position = uVP * w;\n}\n#endif";
    private static final String PRECISION = "\n#ifdef GL_FRAGMENT_PRECISION_HIGH\nprecision highp float;\n#else\nprecision mediump float;\n#endif\n";
    private static final String SKYBOX_FS = "\nprecision mediump float;\nvarying vec2 vUv; uniform sampler2D uTex; uniform vec3 uTint;\nvoid main() { gl_FragColor = vec4(texture2D(uTex, vUv).rgb * uTint, 1.0); }";
    private static final String SKYBOX_VS = "\nuniform mat4 uVP; uniform vec3 uCam; uniform mat3 uRot;\nattribute vec3 aPos; attribute vec2 aUv; varying vec2 vUv;\nvoid main() {\n  vUv = aUv;\n  vec4 p = uVP * vec4(uCam + uRot * aPos * 400.0, 1.0);\n  gl_Position = vec4(p.xy, p.w * 0.9999, p.w);\n}";
    private static final String SKY_FS = "\n#ifdef GL_FRAGMENT_PRECISION_HIGH\nprecision highp float;\n#else\nprecision mediump float;\n#endif\n\nvarying vec2 vNdc;\nuniform mat4 uInvVP; uniform vec3 uCam; uniform vec3 uSunDir; uniform vec3 uZenith; uniform vec3 uHorizon;\nuniform vec3 uSunColor; uniform float uTime; uniform float uDay; uniform vec2 uSunRays; uniform vec4 uCCParams; uniform vec3 uCCTint;\nfloat hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453); }\nfloat noise(vec2 p) {\n  vec2 i = floor(p); vec2 f = fract(p); f = f * f * (3.0 - 2.0 * f);\n  return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), f.x), mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), f.x), f.y);\n}\nfloat fbm(vec2 p) { float v = 0.0; float a = 0.5; for (int i = 0; i < 5; i++) { v += noise(p) * a; p = p * 2.02 + 3.7; a *= 0.5; } return v; }\nvoid main() {\n  vec4 far = uInvVP * vec4(vNdc, 1.0, 1.0);\n  vec3 dir = normalize(far.xyz / far.w - uCam);\n  float y = dir.y;\n  vec3 col = mix(uHorizon, uZenith, pow(clamp(y, 0.0, 1.0), 0.48));\n  float hBand = exp(-abs(y) * 14.0);\n  col = mix(col, mix(uHorizon, vec3(0.92, 0.96, 1.0), 0.25), hBand * 0.35 * uDay);\n  if (y < 0.0) col = mix(uHorizon, uHorizon * 0.72, clamp(-y * 4.5, 0.0, 1.0));\n  float sd = max(dot(dir, uSunDir), 0.0);\n  col += uSunColor * (smoothstep(0.9992, 0.9997, sd) * 2.6 + pow(sd, 90.0) * 0.24 + pow(sd, 8.0) * 0.10) * uDay;\n  if (y > 0.0) {\n    vec2 uv = dir.xz / (y + 0.12) * 1.6 + vec2(uTime * 0.004, uTime * 0.002);\n    float cl = smoothstep(0.52, 0.78, fbm(uv));\n    float fade = smoothstep(0.0, 0.25, y);\n    vec3 cloudCol = mix(vec3(0.55, 0.6, 0.7), vec3(1.0), 0.35 + 0.65 * uDay);\n    col = mix(col, cloudCol * (0.85 + 0.15 * sd), cl * fade * 0.85 * (0.3 + 0.7 * uDay));\n  }\n  if (uDay < 0.6 && y > 0.0) {\n    float nFade = clamp(1.0 - uDay * 1.8, 0.0, 1.0);\n    float st = step(0.9965, hash(floor(dir.xz / (y + 0.25) * 320.0)));\n    float tw = 0.65 + 0.35 * sin(uTime * 2.0 + hash(floor(dir.xz * 120.0)) * 20.0);\n    col += vec3(0.88, 0.93, 1.0) * st * tw * nFade * smoothstep(0.02, 0.28, y);\n    float md = max(dot(dir, normalize(vec3(-uSunDir.x, max(abs(uSunDir.y), 0.28), -uSunDir.z))), 0.0);\n    col += vec3(0.82, 0.88, 1.0) * (smoothstep(0.9988, 0.9994, md) * 1.15 + pow(md, 48.0) * 0.16) * nFade;\n  }\n  if (uSunRays.x > 0.001 && uDay > 0.05) {\n    float srPow = mix(28.0, 2.5, clamp(uSunRays.y, 0.0, 1.0));\n    float ang = atan(dir.y - uSunDir.y, dir.x - uSunDir.x);\n    float shafts = 0.72 + 0.28 * sin(ang * 12.0 + uTime * 0.35) * cos(ang * 7.0 - uTime * 0.2);\n    col += uSunColor * pow(sd, srPow) * uSunRays.x * shafts * 0.45 * uDay;\n  }\n  col = clamp(col + vec3(uCCParams.x), 0.0, 1.0);\n  col = clamp((col - 0.5) * (1.0 + uCCParams.y) + 0.5, 0.0, 1.0);\n  float sluma = dot(col, vec3(0.2126, 0.7152, 0.0722));\n  col = clamp(mix(vec3(sluma), col, 1.0 + uCCParams.z) * uCCTint, 0.0, 1.0);\n  gl_FragColor = vec4(col, 1.0);\n}";
    private static final String SKY_VS = "\nattribute vec2 aPos; varying vec2 vNdc;\nvoid main() { vNdc = aPos; gl_Position = vec4(aPos, 0.9999, 1.0); }";
    private final Set<String> BATCH_CLASSES;
    private final float[] FLOAT4_ZERO;
    private final String[] SKY_FACES;
    private final String[] SURFACES;
    private final float TH;
    private final float TW;
    private int aBit;
    private int aCol;
    private int aMatB;
    private int aNrm;
    private int aPos;
    private int aTan;
    private int aUvB;
    private LitLocs activeLit;
    private float ambient;
    private float aniso;
    private final LitLocs[] batchLit;
    private final StaticBatches batches;
    private Mesh boxLines;
    private int cAmbient;
    private int cLight;
    private int cModel;
    private int cNormal;
    private int cSun;
    private int cTex;
    private int cTint;
    private int cUv;
    private int cVP;
    private int caPos;
    private int caUv;
    private int canDownloadFrame;
    private boolean canDownloadNow;
    private float closest;
    private Mesh corner;
    private float cpuAcc;
    private int cpuN;
    private Mesh cube;
    private Mesh cylinder;
    private Mesh cylinderY;
    private int dModel;
    private int dVP;
    private int daPos;
    private float dayFactor;
    private int decalProg;
    private int depthProg;
    private float detailScale;
    private float exposure;
    private int faceTex;
    private final float[] fogColor;
    private float fogEnd;
    private float fogMode;
    private float fogStart;
    private volatile float fps;
    private float fpsAcc;
    private int fpsFrames;
    private boolean frameShadows;
    private int frames;
    private final Frustum frustum;
    private final HashMap<Long, GpuMesh> gpuMeshes;
    private int gridTex;
    private Mesh head;
    private Mesh headFace;
    private final float[] horizon;
    private int kCam;
    private int kRot;
    private int kTex;
    private int kTint;
    private int kVP;
    private int kaPos;
    private int kaUv;
    private int lColor;
    private int lModel;
    private int lVP;
    private int laPos;
    private final float[] lastLightVP;
    private Object lastModel;
    private long lastNs;
    private long lastShadowChange;
    private int lastShadowMeshEpoch;
    private final Map<Integer, Region> leftLimbRegions;
    private final float[] lightProj;
    private final float[] lightVP;
    private final float[] lightView;
    private int line;
    private int lit;
    private LitLocs litAdv;
    private LitLocs litBasic;
    private LitLocs litTex;
    private int mAmbient;
    private int mCam;
    private int mColor;
    private int mEmissive;
    private int mExposure;
    private int mFog;
    private int mFogColor;
    private int mHasTex;
    private int mHorizon;
    private int mLight;
    private int mModel;
    private int mNrm;
    private int mShadow;
    private int mShadowOn;
    private int mShadowTexel;
    private int mShadowVP;
    private int mSun;
    private int mTex;
    private int mTint;
    private int mVP;
    private int mZenith;
    private int maNrm;
    private int maPos;
    private int maUv;
    private final HashMap<Integer, float[]> matCache;
    private final IdentityHashMap<MaterialTextures.MatTex, TexBind> matTexGpu;
    private final HashMap<String, Integer> mb;
    private int meshBatchProg;
    private int meshEpoch;
    private int meshFrame;
    private int meshProg;
    private final float[] model;
    private boolean modernMaterialsNow;
    private int nLoose;
    private int nSgRender;
    private int nTr;
    private float nearPlane;
    private IdentityHashMap<Instance, String> nextOverride;
    private boolean noBatch;
    private final float[] normal;
    private Function0<Unit> onFrame;
    private volatile Map<Instance, String> partTexOverride;
    private Mesh quad;
    private volatile float quality;
    private float qualityTimer;
    private final ConcurrentHashMap.KeySetView<String, Boolean> r15Pending;
    private final ExecutorService r15Pool;
    private final ConcurrentHashMap<String, Map<String, String>> r15Tex;
    private final HashMap<Long, String> rawKeys;
    private final HashMap<Long, String> rawKeysR;
    private final HashMap<String, String> repeatKeys;
    private final Map<Integer, Region> rightLimbRegions;
    private int sCam;
    private int sDay;
    private int sHorizon;
    private int sInvVP;
    private int sSun;
    private int sSunColor;
    private int sTime;
    private int sZenith;
    private int saPos;
    private Bitmap scratch;
    private Mesh screen;
    private int shadowAge;
    private int shadowFbo;
    private boolean shadowOk;
    private int shadowSize;
    private int shadowTex;
    private boolean shadowValid;
    private boolean shadowsOn;
    private int skyProg;
    private Mesh skyboxMesh;
    private int skyboxProg;
    private float slowAvg;
    private Mesh sphere;
    private float statTimer;
    private final HashMap<String, List<Pair<Mesh, Integer>>> storedUnionGpu;
    private final Studio studio;
    private final float[] sunColor;
    private Vector3 sunDir;
    private int surfH;
    private int surfTex;
    private int surfW;
    private Function4<? super Instance, ? super Float, ? super Float, ? super Bitmap, Unit> surfacePainter;
    private final IdentityHashMap<Instance, SurfaceTex> surfaceTex;
    private float tB;
    private float tClo;
    private float tDec;
    private float tDraw;
    private float tL;
    private float tSg;
    private float tShadow;
    private float tSim;
    private float tTr;
    private float tUpd;
    private final HashMap<String, Integer> textures;
    private float time;
    private final Map<Integer, Region> torsoRegions;
    private int uAmbient;
    private int uCam;
    private int uColor;
    private int uEmissive;
    private int uExposure;
    private int uFog;
    private int uFogColor;
    private int uGrid;
    private int uHasNrm;
    private int uHasSurfTex;
    private int uHorizon;
    private int uLight;
    private int uMat;
    private int uMatNrm;
    private int uMatTex;
    private int uModel;
    private int uNrm;
    private int uShadow;
    private int uShadowOn;
    private int uShadowTexel;
    private int uShadowSoft;
    private int uShiftBot;
    private int uEnvSpec;
    private int uPtCount;
    private int uPtPos;
    private int uPtCol;
    private int uPtDir;
    private int mShadowSoft;
    private int mShiftBot;
    private int mEnvSpec;
    private int mPtCount;
    private int mPtPos;
    private int mPtCol;
    private int mPtDir;
    private float shadowSoftness = 0.2f;
    private final float[] shiftBottom = new float[3];
    private float envSpec = 1.0f;
    private int ptCount = 0;
    private final float[] ptPos = new float[32];
    private final float[] ptCol = new float[32];
    private final float[] ptDir = new float[32];
    private int uCCParams;
    private int uCCTint;
    private int uBloom;
    private int mCCParams;
    private int mCCTint;
    private int mBloom;
    private int mPbrNrm;
    private int mPbrRough;
    private int mPbrMetal;
    private int mPbrFlags;
    private int mPbrEmis;
    private int sCCParams;
    private int sCCTint;
    private int sSunRays;
    private float ccBright = 0.0f;
    private float ccContrast = 0.0f;
    private float ccSat = 0.0f;
    private final float[] ccTint = new float[]{1.0f, 1.0f, 1.0f};
    private float bloomInt = 0.0f;
    private float bloomThresh = 2.0f;
    private float bloomSize = 24.0f;
    private float sunRaysInt = 0.0f;
    private float sunRaysSpread = 0.1f;
    private float atmoGlare = 0.0f;
    private int lightTech = 3;
    private int uDof;
    private int mDof;
    private float dofFocus = 10.0f;
    private float dofRadius = 30.0f;
    private float dofFar = 0.0f;
    private float dofNear = 0.0f;
    private float blurAmt = 0.0f;
    private int uAmbRgb;
    private int mAmbRgb;
    private final float[] ambRgb = new float[]{1.0f, 1.0f, 1.0f};
    private static final String POST_VS = "attribute vec2 aPos; varying vec2 vUv; void main() { vUv = aPos * 0.5 + 0.5; gl_Position = vec4(aPos, 0.0, 1.0); }";
    private static final String BLOOM_EXT_FS = "precision mediump float; varying vec2 vUv; uniform sampler2D uTex; uniform float uThresh; uniform float uBlurOnly; void main() { vec3 c = texture2D(uTex, vUv).rgb; if (uBlurOnly > 0.5) { gl_FragColor = vec4(c, 1.0); return; } float lum = dot(c, vec3(0.2126, 0.7152, 0.0722)); float soft = smoothstep(uThresh * 0.65, uThresh + 0.35, lum); gl_FragColor = vec4(c * soft, 1.0); }";
    private static final String GAUSS_FS = "precision mediump float; varying vec2 vUv; uniform sampler2D uTex; uniform vec2 uDir; void main() { vec3 s = texture2D(uTex, vUv).rgb * 0.227027; s += (texture2D(uTex, vUv + uDir * 1.0).rgb + texture2D(uTex, vUv - uDir * 1.0).rgb) * 0.1945946; s += (texture2D(uTex, vUv + uDir * 2.0).rgb + texture2D(uTex, vUv - uDir * 2.0).rgb) * 0.1216216; s += (texture2D(uTex, vUv + uDir * 3.0).rgb + texture2D(uTex, vUv - uDir * 3.0).rgb) * 0.054054; s += (texture2D(uTex, vUv + uDir * 4.0).rgb + texture2D(uTex, vUv - uDir * 4.0).rgb) * 0.016216; gl_FragColor = vec4(s, 1.0); }";
    private static final String POST_COMP_FS = "precision mediump float; varying vec2 vUv; uniform sampler2D uScene; uniform sampler2D uBloomTex; uniform float uBloomInt; uniform float uBlurMix; void main() { vec3 base = texture2D(uScene, vUv).rgb; vec3 bl = texture2D(uBloomTex, vUv).rgb; vec3 col = uBlurMix > 0.001 ? mix(base, bl, clamp(uBlurMix, 0.0, 1.0)) : (base + bl * uBloomInt); col = col / (vec3(1.0) + col * 0.12); gl_FragColor = vec4(clamp(col, 0.0, 1.0), 1.0); }";
    private int postSceneFbo = 0;
    private int postSceneTex = 0;
    private int postSceneDepth = 0;
    private int postFboA = 0;
    private int postTexA = 0;
    private int postFboB = 0;
    private int postTexB = 0;
    private int postW = 0;
    private int postH = 0;
    private boolean postOk = false;
    private int postExtProg = 0;
    private int postBlurProg = 0;
    private int postCompProg = 0;
    private final IdentityHashMap<Instance, ArrayList<float[]>> trailHist = new IdentityHashMap<>();
    private int uShadowVP;
    private int uSize;
    private int uSun;
    private int uSurfA;
    private int uSurfB;
    private int uSurfTex;
    private int uTexScale;
    private int uTime;
    private int uTruss;
    private int uVP;
    private int uZenith;
    private final IdentityHashMap<Instance, UnionGpu> unionGpu;
    private final IdentityHashMap<Instance, TexBind> variantBinds;
    private Map<String, ? extends List<? extends Instance>> variantIndex;
    private long variantIndexAt;
    private Instance variantService;
    private Mesh wedge;
    private int whiteTexId;
    private final float[] zenith;
    private static final Color3 NEGATE_COLOR = new Color3(0.93f, 0.33f, 0.45f);
    private static final String[] SIG_PROPS = {"Text", "Visible", "BackgroundColor3", "BackgroundTransparency", "TextColor3", "Image", "Size", "Position", "TextTransparency", "ImageTransparency", "Enabled"};

    public SceneRenderer(Studio studio) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.model = new float[16];
        this.normal = new float[9];
        this.sunDir = new Vector3(0.45f, 0.8f, 0.35f).getUnit();
        this.zenith = new float[3];
        this.horizon = new float[3];
        this.sunColor = new float[3];
        this.fogColor = new float[3];
        this.fogEnd = 1.0E9f;
        this.ambient = 0.55f;
        this.exposure = 1.0f;
        this.shadowsOn = true;
        this.dayFactor = 1.0f;
        this.shadowSize = 512;
        this.lightVP = new float[16];
        this.lightView = new float[16];
        this.lightProj = new float[16];
        this.textures = new HashMap<>();
        this.surfaceTex = new IdentityHashMap<>();
        this.matCache = new HashMap<>();
        this.uMatTex = -1;
        this.uMatNrm = -1;
        this.uTexScale = -1;
        this.uHasNrm = -1;
        this.uSurfTex = -1;
        this.uHasSurfTex = -1;
        this.aUvB = -1;
        this.aCol = -1;
        this.aMatB = -1;
        this.aTan = -1;
        this.aBit = -1;
        this.batchLit = new LitLocs[3];
        this.frustum = new Frustum();
        this.detailScale = 400.0f;
        this.quality = 1.0f;
        this.closest = 1.0f;
        this.nearPlane = 0.3f;
        this.batches = new StaticBatches(new StaticBatches.Host() { // from class: com.goydevv.studrblx.render.SceneRenderer$batches$1
            @Override // com.goydevv.studrblx.render.StaticBatches.Host
            public boolean batchable(Instance p) {
                Intrinsics.checkNotNullParameter(p, "p");
                return false;
            }

            @Override // com.goydevv.studrblx.render.StaticBatches.Host
            public List<BatchSpec> spec(Instance p) {
                Intrinsics.checkNotNullParameter(p, "p");
                return SceneRenderer.this.batchSpec(p);
            }

            @Override // com.goydevv.studrblx.render.StaticBatches.Host
            public int loadSignature() {
                return (MaterialTextures.INSTANCE.getEpoch() * 31) + (MeshLoader.INSTANCE.getEpoch() * 17) + (UnionMeshes.INSTANCE.getEpoch() * 13) + (AssetFetcher.INSTANCE.getImageEpoch() * 7) + ImageLoader.INSTANCE.getEpoch();
            }
        });
        this.BATCH_CLASSES = SetsKt.setOf(new String[]{"Part", "WedgePart", "CornerWedgePart", "SpawnLocation", "Seat", "VehicleSeat", "Platform", "SkateboardPlatform", "MeshPart", "UnionOperation", "IntersectOperation"});
        this.SURFACES = new String[]{"TopSurface", "BottomSurface", "FrontSurface", "BackSurface", "LeftSurface", "RightSurface"};
        this.FLOAT4_ZERO = new float[]{0.0f, 0.0f, 1.0f, 0.0f};
        this.mb = new HashMap<>();
        this.matTexGpu = new IdentityHashMap<>();
        this.modernMaterialsNow = true;
        this.variantBinds = new IdentityHashMap<>();
        this.surfW = 1;
        this.surfH = 1;
        this.gpuMeshes = new HashMap<>();
        this.meshEpoch = -1;
        this.unionGpu = new IdentityHashMap<>();
        this.storedUnionGpu = new HashMap<>();
        this.lastLightVP = new float[16];
        this.lastShadowChange = -1L;
        this.lastShadowMeshEpoch = -1;
        this.SKY_FACES = new String[]{"SkyboxFt", "SkyboxBk", "SkyboxRt", "SkyboxLf", "SkyboxUp", "SkyboxDn"};
        this.meshFrame = -1;
        this.rawKeys = new HashMap<>();
        this.rawKeysR = new HashMap<>();
        this.canDownloadFrame = -1;
        this.repeatKeys = new HashMap<>();
        this.TW = 585.0f;
        this.TH = 559.0f;
        int i = 16;
        DefaultConstructorMarker defaultConstructorMarker = null;
        float f = 74.0f;
        float f2 = 128.0f;
        float f3 = 128.0f;
        boolean z = false;
        float f4 = 64.0f;
        this.torsoRegions = MapsKt.mapOf(TuplesKt.to(5, new Region(231.0f, f, f2, f3, z, i, defaultConstructorMarker)), TuplesKt.to(2, new Region(427.0f, f, f2, f3, z, i, defaultConstructorMarker)), TuplesKt.to(0, new Region(165.0f, f, f4, f3, z, i, defaultConstructorMarker)), TuplesKt.to(3, new Region(361.0f, f, f4, f3, z, i, defaultConstructorMarker)), TuplesKt.to(1, new Region(231.0f, 8.0f, 128.0f, 64.0f, true)), TuplesKt.to(4, new Region(231.0f, 204.0f, 128.0f, 64.0f, true)));
        float f5 = 355.0f;
        float f6 = 64.0f;
        float f7 = 128.0f;
        boolean z2 = false;
        this.rightLimbRegions = MapsKt.mapOf(TuplesKt.to(5, new Region(217.0f, f5, f6, f7, z2, i, defaultConstructorMarker)), TuplesKt.to(0, new Region(151.0f, f5, f6, f7, z2, i, defaultConstructorMarker)), TuplesKt.to(2, new Region(85.0f, f5, f6, f7, z2, i, defaultConstructorMarker)), TuplesKt.to(3, new Region(19.0f, f5, f6, f7, z2, i, defaultConstructorMarker)), TuplesKt.to(1, new Region(217.0f, 289.0f, 64.0f, 64.0f, true)), TuplesKt.to(4, new Region(217.0f, 485.0f, 64.0f, 64.0f, true)));
        float f8 = 355.0f;
        float f9 = 128.0f;
        boolean z3 = false;
        int i2 = 16;
        DefaultConstructorMarker defaultConstructorMarker2 = null;
        float f10 = 355.0f;
        float f11 = 64.0f;
        float f12 = 128.0f;
        boolean z4 = false;
        this.leftLimbRegions = MapsKt.mapOf(TuplesKt.to(5, new Region(308.0f, f8, f6, f9, z3, i, defaultConstructorMarker)), TuplesKt.to(3, new Region(374.0f, f8, f6, f9, z3, i, defaultConstructorMarker)), TuplesKt.to(2, new Region(440.0f, f10, f11, f12, z4, i2, defaultConstructorMarker2)), TuplesKt.to(0, new Region(506.0f, f10, f11, f12, z4, i2, defaultConstructorMarker2)), TuplesKt.to(1, new Region(308.0f, 289.0f, 64.0f, 64.0f, true)), TuplesKt.to(4, new Region(308.0f, 485.0f, 64.0f, 64.0f, true)));
        this.r15Tex = new ConcurrentHashMap<>();
        this.r15Pending = ConcurrentHashMap.newKeySet();
        this.r15Pool = Executors.newSingleThreadExecutor();
        this.partTexOverride = MapsKt.emptyMap();
        this.nextOverride = new IdentityHashMap<>();
        this.variantIndex = MapsKt.emptyMap();
    }

    public final float getFps() {
        return this.fps;
    }

    public final void setFps(float f) {
        this.fps = f;
    }

    public final Function0<Unit> getOnFrame() {
        return this.onFrame;
    }

    public final void setOnFrame(Function0<Unit> function0) {
        this.onFrame = function0;
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class SurfaceTex {
        private float checked;
        private int h;
        private long sig;
        private int tex;
        private float used;
        private int w;

        public SurfaceTex(int tex, int w, int h, long sig, float checked, float used) {
            this.tex = tex;
            this.w = w;
            this.h = h;
            this.sig = sig;
            this.checked = checked;
            this.used = used;
        }

        public final float getChecked() {
            return this.checked;
        }

        public final int getH() {
            return this.h;
        }

        public final long getSig() {
            return this.sig;
        }

        public final int getTex() {
            return this.tex;
        }

        public final float getUsed() {
            return this.used;
        }

        public final int getW() {
            return this.w;
        }

        public final void setChecked(float f) {
            this.checked = f;
        }

        public final void setH(int i) {
            this.h = i;
        }

        public final void setSig(long j) {
            this.sig = j;
        }

        public final void setTex(int i) {
            this.tex = i;
        }

        public final void setUsed(float f) {
            this.used = f;
        }

        public final void setW(int i) {
            this.w = i;
        }
    }

    public final Function4<? super Instance, ? super Float, ? super Float, ? super Bitmap, Unit> getSurfacePainter() {
        return this.surfacePainter;
    }

    public final void setSurfacePainter(Function4<? super Instance, ? super Float, ? super Float, ? super Bitmap, Unit> function4) {
        this.surfacePainter = function4;
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class LitLocs {
        private final int[] locs;
        private final int prog;

        public LitLocs(int prog, int[] locs) {
            Intrinsics.checkNotNullParameter(locs, "locs");
            this.prog = prog;
            this.locs = locs;
        }

        public final int[] getLocs() {
            return this.locs;
        }

        public final int getProg() {
            return this.prog;
        }
    }

    private final LitLocs litLocs(int prog) {
        return new LitLocs(prog, new int[]{GLES20.glGetUniformLocation(prog, "uVP"), GLES20.glGetUniformLocation(prog, "uModel"), GLES20.glGetUniformLocation(prog, "uNrm"), GLES20.glGetUniformLocation(prog, "uColor"), GLES20.glGetUniformLocation(prog, "uLight"), GLES20.glGetUniformLocation(prog, "uCam"), GLES20.glGetUniformLocation(prog, "uEmissive"), GLES20.glGetUniformLocation(prog, "uGrid"), GLES20.glGetUniformLocation(prog, "uSize"), GLES20.glGetUniformLocation(prog, "uHorizon"), GLES20.glGetUniformLocation(prog, "uZenith"), GLES20.glGetUniformLocation(prog, "uSun"), GLES20.glGetUniformLocation(prog, "uAmbient"), GLES20.glGetUniformLocation(prog, "uFogColor"), GLES20.glGetUniformLocation(prog, "uFog"), GLES20.glGetUniformLocation(prog, "uMat"), GLES20.glGetUniformLocation(prog, "uSurfA"), GLES20.glGetUniformLocation(prog, "uSurfB"), GLES20.glGetUniformLocation(prog, "uShadow"), GLES20.glGetUniformLocation(prog, "uShadowOn"), GLES20.glGetUniformLocation(prog, "uShadowTexel"), GLES20.glGetUniformLocation(prog, "uShadowVP"), GLES20.glGetUniformLocation(prog, "uExposure"), GLES20.glGetUniformLocation(prog, "uTime"), GLES20.glGetUniformLocation(prog, "uTruss"), GLES20.glGetAttribLocation(prog, "aPos"), GLES20.glGetAttribLocation(prog, "aNrm"), GLES20.glGetUniformLocation(prog, "uMatTex"), GLES20.glGetUniformLocation(prog, "uMatNrm"), GLES20.glGetUniformLocation(prog, "uTexScale"), GLES20.glGetUniformLocation(prog, "uHasNrm"), GLES20.glGetUniformLocation(prog, "uSurfTex"), GLES20.glGetUniformLocation(prog, "uHasSurfTex"), GLES20.glGetAttribLocation(prog, "aUv"), GLES20.glGetAttribLocation(prog, "aCol"), GLES20.glGetAttribLocation(prog, "aMat"), GLES20.glGetAttribLocation(prog, "aTan"), GLES20.glGetAttribLocation(prog, "aBit"), GLES20.glGetUniformLocation(prog, "uShadowSoft"), GLES20.glGetUniformLocation(prog, "uShiftBot"), GLES20.glGetUniformLocation(prog, "uEnvSpec"), GLES20.glGetUniformLocation(prog, "uPtCount"), GLES20.glGetUniformLocation(prog, "uPtPos"), GLES20.glGetUniformLocation(prog, "uPtCol"), GLES20.glGetUniformLocation(prog, "uPtDir"), GLES20.glGetUniformLocation(prog, "uCCParams"), GLES20.glGetUniformLocation(prog, "uCCTint"), GLES20.glGetUniformLocation(prog, "uBloom"), GLES20.glGetUniformLocation(prog, "uDof"), GLES20.glGetUniformLocation(prog, "uAmbRgb")});
    }

    private final void useLit(LitLocs l) {
        this.activeLit = l;
        this.lit = l.getProg();
        GLES20.glUseProgram(l.getProg());
        int[] a = l.getLocs();
        this.uVP = a[0];
        this.uModel = a[1];
        this.uNrm = a[2];
        this.uColor = a[3];
        this.uLight = a[4];
        this.uCam = a[5];
        this.uEmissive = a[6];
        this.uGrid = a[7];
        this.uSize = a[8];
        this.uHorizon = a[9];
        this.uZenith = a[10];
        this.uSun = a[11];
        this.uAmbient = a[12];
        this.uFogColor = a[13];
        this.uFog = a[14];
        this.uMat = a[15];
        this.uSurfA = a[16];
        this.uSurfB = a[17];
        this.uShadow = a[18];
        this.uShadowOn = a[19];
        this.uShadowTexel = a[20];
        this.uShadowVP = a[21];
        this.uExposure = a[22];
        this.uTime = a[23];
        this.uTruss = a[24];
        this.aPos = a[25];
        this.aNrm = a[26];
        this.uMatTex = a[27];
        this.uMatNrm = a[28];
        this.uTexScale = a[29];
        this.uHasNrm = a[30];
        this.uSurfTex = a[31];
        this.uHasSurfTex = a[32];
        this.aUvB = a[33];
        this.aCol = a[34];
        this.aMatB = a[35];
        this.aTan = a[36];
        this.aBit = a[37];
        this.uShadowSoft = a[38];
        this.uShiftBot = a[39];
        this.uEnvSpec = a[40];
        this.uPtCount = a[41];
        this.uPtPos = a[42];
        this.uPtCol = a[43];
        this.uPtDir = a[44];
        this.uCCParams = a[45];
        this.uCCTint = a[46];
        this.uBloom = a[47];
        this.uDof = a[48];
        this.uAmbRgb = a[49];
    }

    private final LitLocs batchProgram(int kind) {
        String str;
        LitLocs litLocs = this.batchLit[kind];
        if (litLocs != null) {
            return litLocs;
        }
        switch (kind) {
            case 1:
                str = "#define ADVANCED 1\n";
                break;
            case 2:
                str = "#define TEXTURED 1\n";
                break;
            default:
                str = "";
                break;
        }
        String def = "#define BATCH 1\n" + str;
        LitLocs litLocs2 = litLocs(program(def + LIT_VS, def + LIT_FS));
        this.batchLit[kind] = litLocs2;
        return litLocs2;
    }

    public final float getQuality() {
        return this.quality;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isBatchable(Instance p) {
        if (!this.BATCH_CLASSES.contains(p.getClassName()) || PartData.INSTANCE.transparency(p) > 0.001f || Intrinsics.areEqual(p.get("CastShadow"), (Object) false)) {
            return false;
        }
        int mat = PartData.INSTANCE.material(p);
        switch (mat) {
            case 1568:
            case 1584:
                return false;
            default:
                String[] strArr = this.SURFACES;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        for (Instance a = p.getParent(); a != null && !(a instanceof DataModel); a = a.getParent()) {
                            if (Intrinsics.areEqual(a.getClassName(), "Model") && a.findFirstChildOfClass("Humanoid") != null) {
                                return false;
                            }
                        }
                        return true;
                    }
                    String s = strArr[i];
                    Object obj = p.get(s);
                    EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
                    int value = enumValue != null ? enumValue.getValue() : 0;
                    if (6 <= value && value < 9) {
                        return false;
                    }
                    i++;
                }
        }
    }

    private final BatchSpec spec(Instance p, float[] data, int stride, boolean isCube, Geo g, Color3 color, boolean pending) {
        int kind;
        int mat = PartData.INSTANCE.material(p);
        float[] mp = material(mat);
        float f = mp[0];
        float f2 = mp[1];
        float f3 = mp[2];
        float f4 = mp[3];
        Object obj = p.get("Reflectance");
        Number number = obj instanceof Number ? (Number) obj : null;
        float[] params = {f, f2, f3, f4 + RangesKt.coerceIn(number != null ? number.floatValue() : 0.0f, 0.0f, 1.0f)};
        TexBind tb = texturesFor(p, mat);
        if (tb != null) {
            kind = 2;
        } else {
            kind = needsAdvanced(mat) ? 1 : 0;
        }
        return new BatchSpec(data, isCube, g.getCf(), g.getScale(), color.getR(), color.getG(), color.getB(), mat == 288, params, isCube ? new Vector3(surf(p, "RightSurface"), surf(p, "TopSurface"), surf(p, "BackSurface")) : null, isCube ? new Vector3(surf(p, "LeftSurface"), surf(p, "BottomSurface"), surf(p, "FrontSurface")) : null, kind, tb, pending || (tb == null && MaterialTextures.INSTANCE.hasTexture(mat)), stride, null, 32768, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:81:0x01dd  */
    /* JADX WARN: Code duplicated, block: B:82:0x01e4  */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x018b, code lost:
    
        if (r4 == false) goto L63;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.goydevv.studrblx.render.BatchSpec> batchSpec(com.goydevv.studrblx.model.Instance p) {
        if (p == null) {
            return null;
        }
        Geo g = geometry(p);
        if (g.getKind() == 2) {
            // Leave textured/file MeshParts in loose list so drawFileMesh renders their textures/SurfaceAppearance
            return null;
        }
        Color3 baseCol = PartData.INSTANCE.color(p);
        List<Pair<Mesh, Color3>> subs = g.getSubs();
        if (subs != null && !subs.isEmpty()) {
            ArrayList<BatchSpec> list = new ArrayList<>(subs.size());
            for (Pair<Mesh, Color3> pair : subs) {
                Mesh sm = pair.getFirst();
                Color3 sc = pair.getSecond() != null ? pair.getSecond() : baseCol;
                float[] cpu = sm.getCpu();
                if (cpu != null && cpu.length > 0) {
                    list.add(spec(p, cpu, sm.getStride(), false, g, sc, false));
                }
            }
            return list.isEmpty() ? null : list;
        }
        Mesh m = g.getMesh();
        float[] cpu = m.getCpu();
        if (cpu == null || cpu.length == 0) {
            return null;
        }
        boolean isCube = (m == this.cube && g.getKind() == 0);
        return CollectionsKt.listOf(spec(p, cpu, m.getStride(), isCube, g, baseCol, false));
    }

    private final int meshBatchProgram() {
        if (this.meshBatchProg != 0) {
            return this.meshBatchProg;
        }
        int p = program("#define BATCH 1\n\nuniform mat4 uVP; uniform mat4 uModel; uniform mat3 uNrm; uniform mat4 uShadowVP;\nvarying vec3 vN; varying vec3 vW; varying vec2 vUv; varying vec4 vS;\n#ifdef BATCH\nattribute vec3 aPos; attribute vec4 aNrm; attribute vec2 aUv; attribute vec4 aCol;\nvarying vec4 vCol;\nvoid main() {\n  vW = aPos; vN = aNrm.xyz; vUv = aUv; vS = uShadowVP * vec4(aPos, 1.0); vCol = aCol;\n  gl_Position = uVP * vec4(aPos, 1.0);\n}\n#else\nattribute vec3 aPos; attribute vec3 aNrm; attribute vec2 aUv;\nvoid main() {\n  vec4 w = uModel * vec4(aPos, 1.0);\n  vW = w.xyz; vN = uNrm * aNrm; vUv = aUv; vS = uShadowVP * w;\n  gl_Position = uVP * w;\n}\n#endif", "#define BATCH 1\n\nprecision mediump float;\nvarying vec3 vN; varying vec3 vW; varying vec2 vUv; varying vec4 vS;\nuniform vec4 uColor; uniform vec3 uTint; uniform vec3 uLight; uniform vec3 uSun; uniform float uAmbient; uniform vec3 uCam;\nuniform sampler2D uTex; uniform float uHasTex; uniform sampler2D uShadow; uniform float uShadowOn; uniform vec2 uShadowTexel; uniform float uShadowSoft; uniform vec3 uShiftBot; uniform float uEnvSpec; uniform int uPtCount; uniform vec4 uPtPos[8]; uniform vec4 uPtCol[8]; uniform vec4 uPtDir[8]; uniform vec4 uCCParams; uniform vec3 uCCTint; uniform vec4 uBloom; uniform vec4 uDof; uniform vec3 uAmbRgb; uniform sampler2D uPbrNrm; uniform sampler2D uPbrRough; uniform sampler2D uPbrMetal; uniform vec4 uPbrFlags; uniform vec4 uPbrEmis;\nuniform vec3 uFogColor; uniform vec3 uFog; uniform float uExposure; uniform float uEmissive; uniform vec3 uHorizon; uniform vec3 uZenith;\nfloat unpackDepth(vec4 c) { return dot(c, vec4(1.0, 1.0 / 255.0, 1.0 / 65025.0, 1.0 / 16581375.0)); }\nfloat sampleShadow(vec2 uv, float z) { return z > unpackDepth(texture2D(uShadow, uv)) ? 0.0 : 1.0; }\nfloat shadowAt(float ndl) {\n  if (uShadowOn < 0.5 || ndl <= 0.0) return 1.0;\n  vec3 p = vS.xyz / vS.w * 0.5 + 0.5;\n  if (p.x <= 0.001 || p.x >= 0.999 || p.y <= 0.001 || p.y >= 0.999 || p.z >= 1.0) return 1.0;\n  float z = p.z - (0.00030 + 0.00095 * (1.0 - ndl));\n  vec2 t = uShadowTexel * mix(0.65, 2.85, clamp(uShadowSoft, 0.0, 1.0));\n  float s = sampleShadow(p.xy, z) * 2.0;\n  s += sampleShadow(p.xy + vec2(-t.x, -t.y), z);\n  s += sampleShadow(p.xy + vec2(t.x, -t.y), z);\n  s += sampleShadow(p.xy + vec2(-t.x, t.y), z);\n  s += sampleShadow(p.xy + vec2(t.x, t.y), z);\n  return s / 6.0;\n}\n#ifdef BATCH\nvarying vec4 vCol;\n#endif\nvoid main() {\n  vec3 n = normalize(vN);\n  if (!gl_FrontFacing) n = -n;\n#ifdef BATCH\n  vec3 c = vCol.rgb; float a = 1.0; float emis = vCol.a; vec3 tint = vec3(1.0);\n#else\n  vec3 c = uColor.rgb; float a = uColor.a; float emis = uEmissive; vec3 tint = uTint;\n#endif\n  if (uHasTex > 0.5) {\n    vec4 t = texture2D(uTex, vUv);\n    float am = uPbrFlags.w;\n    if (am > 1.5) { c = mix(t.rgb, t.rgb * c, t.a); }\n    else if (am > 0.5) { if (t.a < 0.04) discard; c = t.rgb; a *= t.a; }\n    else if (uPbrFlags.x + uPbrFlags.y + uPbrFlags.z > 0.5) { c = mix(c, t.rgb, t.a); }\n    else { if (t.a < 0.12) discard; c = t.rgb / max(t.a, 0.001); }\n  }\n  c *= tint;\n  float pbrRough = uPbrFlags.y > 0.5 ? clamp(texture2D(uPbrRough, vUv).r, 0.06, 0.98) : 0.65;\n  float pbrMetal = uPbrFlags.z > 0.5 ? clamp(texture2D(uPbrMetal, vUv).r, 0.0, 1.0) : 0.0;\n  if (uPbrFlags.x > 0.5) {\n    vec3 tn = texture2D(uPbrNrm, vUv).xyz * 2.0 - 1.0;\n    vec3 upRef = abs(n.y) < 0.99 ? vec3(0.0, 1.0, 0.0) : vec3(1.0, 0.0, 0.0);\n    vec3 tang = normalize(cross(upRef, n));\n    vec3 bitang = cross(n, tang);\n    n = normalize(tang * tn.x + bitang * tn.y + n * max(tn.z, 0.25));\n  }\n  vec3 col;\n  if (emis > 0.5) col = c * 1.15;\n  else {\n    float ndl = dot(n, uLight);\n    float sh = shadowAt(ndl);\n    vec3 skyTop = mix(vec3(1.0), mix(uHorizon, uZenith, 0.5), 0.22);\n    vec3 groundBounce = mix(vec3(0.78, 0.79, 0.82), uHorizon * 0.85, 0.14);\n    float hemi = clamp(n.y * 0.5 + 0.5, 0.0, 1.0);\n    vec3 amb = max(uAmbient, 0.48) * uAmbRgb * mix(groundBounce, skyTop, hemi);\n    float wrap = max(0.0, (-ndl) * 0.26) + 0.14;\n    float diffScale = 1.0 - pbrMetal * 0.72;\n    col = c * diffScale * (amb + uSun * (max(ndl, 0.0) * sh + wrap * 0.36) + uShiftBot * max(-ndl, 0.0) * 0.35);\n    vec3 v = normalize(uCam - vW);\n    vec3 h = normalize(uLight + v);\n    float specPow = mix(128.0, 10.0, pbrRough * pbrRough);\n    vec3 specCol = mix(uSun, c * uSun, pbrMetal);\n    float specInt = mix(0.12, 0.82, (1.0 - pbrRough) * 0.65 + pbrMetal * 0.35);\n    col += specCol * pow(max(dot(n, h), 0.0), specPow) * specInt * sh * uEnvSpec;\n    if (uPbrFlags.y + uPbrFlags.z > 0.5) {\n      float fres = pow(1.0 - max(dot(n, v), 0.0), 4.0) * (0.04 + (1.0 - pbrRough) * 0.24 + pbrMetal * 0.38) * uEnvSpec;\n      col = mix(col, mix(uHorizon, uZenith, 0.5) * mix(vec3(1.0), c, pbrMetal), clamp(fres, 0.0, 0.75));\n    }\n    col += uPbrEmis.rgb * uPbrEmis.w;\n    if (uPtCount > 0) for (int li = 0; li < 8; li++) {\n      if (li >= uPtCount) break;\n      vec3 L = uPtPos[li].xyz - vW;\n      float d = length(L);\n      float r = uPtPos[li].w;\n      if (d < r) {\n        vec3 ldir = L / max(d, 0.001);\n        float tt = clamp(1.0 - (d / r) * (d / r), 0.0, 1.0);\n        float att = (tt * tt) / (1.0 + 0.04 * d * d);\n        if (uPtDir[li].w > 0.5) {\n          float ca = dot(-ldir, uPtDir[li].xyz);\n          att *= smoothstep(uPtCol[li].w, min(uPtCol[li].w + 0.14, 0.999), ca);\n        }\n        float pndl = max(dot(n, ldir), 0.0);\n        vec3 ph = normalize(ldir + v);\n        float pspec = pow(max(dot(n, ph), 0.0), 32.0) * 0.16 * uEnvSpec;\n        col += (c * pndl + vec3(pspec)) * uPtCol[li].rgb * att;\n      }\n    }\n  }\n  col *= uExposure;\n  if (uBloom.x > 0.001) { float blum = dot(col, vec3(0.2126, 0.7152, 0.0722)); float knee = smoothstep(uBloom.y * 0.75, uBloom.y + 0.65, blum); col += mix(col, vec3(blum), 0.35) * knee * uBloom.x * 0.20; }\n  vec3 mapped = clamp((col * (2.51 * col + 0.03)) / (col * (2.43 * col + 0.59) + 0.14), 0.0, 1.0); col = mix(clamp(col, 0.0, 1.0), mapped, 0.22);\n  if (uCCParams.w > 0.5) {\n    col = clamp(col + vec3(uCCParams.x), 0.0, 1.0);\n    col = clamp((col - 0.5) * (1.0 + uCCParams.y) + 0.5, 0.0, 1.0);\n    float luma = dot(col, vec3(0.2126, 0.7152, 0.0722));\n    col = clamp(mix(vec3(luma), col, 1.0 + uCCParams.z) * uCCTint, 0.0, 1.0);\n  }\n  float dist = length(uCam - vW);\n  if (uDof.z > 0.001 || uDof.w > 0.001) {\n    float coc = max(0.0, (dist - (uDof.x + uDof.y)) / max(uDof.y + 12.0, 1.0)) * uDof.z + max(0.0, ((uDof.x - uDof.y) - dist) / max(uDof.x, 1.0)) * uDof.w;\n    col = mix(col, mix(uHorizon, uFogColor, 0.5), clamp(coc * 0.42, 0.0, 0.55));\n  }\n  float fog;\n  if (uFog.z > 1.5) fog = (1.0 - exp(-dist * uFog.x)) * uFog.y;\n  else if (uFog.z > 0.5) fog = clamp((dist - uFog.x) / max(uFog.y - uFog.x, 1.0), 0.0, 1.0);\n  else fog = smoothstep(140.0, 950.0, dist) * 0.88;\n  vec3 mieFog = uFogColor + uSun * pow(max(dot(normalize(vW - uCam), uLight), 0.0), 6.0) * 0.20;\n  gl_FragColor = vec4(mix(col, mieFog, fog), a);\n}");
        for (String n : CollectionsKt.listOf(new String[]{"uVP", "uShadowVP", "uLight", "uSun", "uAmbient", "uCam", "uFogColor", "uFog", "uExposure", "uShadowOn", "uShadowTexel", "uHorizon", "uZenith", "uShadow", "uTex", "uHasTex", "uShadowSoft", "uShiftBot", "uEnvSpec", "uPtCount", "uPtPos", "uPtCol", "uPtDir", "uCCParams", "uCCTint", "uBloom", "uDof", "uAmbRgb", "uPbrFlags", "uPbrEmis"})) {
            this.mb.put(n, Integer.valueOf(GLES20.glGetUniformLocation(p, n)));
        }
        for (String n2 : CollectionsKt.listOf(new String[]{"aPos", "aNrm", "aUv", "aCol"})) {
            this.mb.put(n2, Integer.valueOf(GLES20.glGetAttribLocation(p, n2)));
        }
        this.meshBatchProg = p;
        return p;
    }

    private final void useMeshBatch() {
        GLES20.glUseProgram(meshBatchProgram());
        this.activeLit = null;
        GLES20.glUniformMatrix4fv(useMeshBatch$u$8(this, "uVP"), 1, false, this.studio.getView().getVp(), 0);
        GLES20.glUniformMatrix4fv(useMeshBatch$u$8(this, "uShadowVP"), 1, false, this.lightVP, 0);
        GLES20.glUniform3f(useMeshBatch$u$8(this, "uLight"), this.sunDir.getX(), this.sunDir.getY(), this.sunDir.getZ());
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uSun"), 1, this.sunColor, 0);
        GLES20.glUniform1f(useMeshBatch$u$8(this, "uAmbient"), this.ambient);
        Vector3 cp = this.studio.getView().getCamPos();
        GLES20.glUniform3f(useMeshBatch$u$8(this, "uCam"), cp.getX(), cp.getY(), cp.getZ());
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uFogColor"), 1, this.fogColor, 0);
        GLES20.glUniform3f(useMeshBatch$u$8(this, "uFog"), this.fogStart, this.fogEnd, this.fogMode);
        GLES20.glUniform1f(useMeshBatch$u$8(this, "uExposure"), this.exposure);
        GLES20.glUniform1f(useMeshBatch$u$8(this, "uShadowOn"), this.frameShadows ? 1.0f : 0.0f);
        GLES20.glUniform2f(useMeshBatch$u$8(this, "uShadowTexel"), 1.0f / this.shadowSize, 1.0f / this.shadowSize);
        GLES20.glUniform1f(useMeshBatch$u$8(this, "uShadowSoft"), this.shadowSoftness);
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uShiftBot"), 1, this.shiftBottom, 0);
        GLES20.glUniform1f(useMeshBatch$u$8(this, "uEnvSpec"), this.envSpec);
        GLES20.glUniform4f(useMeshBatch$u$8(this, "uCCParams"), this.ccBright, this.ccContrast, this.ccSat, (this.ccBright != 0.0f || this.ccContrast != 0.0f || this.ccSat != 0.0f || this.ccTint[0] < 0.999f || this.ccTint[1] < 0.999f || this.ccTint[2] < 0.999f) ? 1.0f : 0.0f);
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uCCTint"), 1, this.ccTint, 0);
        GLES20.glUniform4f(useMeshBatch$u$8(this, "uBloom"), this.bloomInt, this.bloomThresh, this.bloomSize, 0.0f);
        GLES20.glUniform4f(useMeshBatch$u$8(this, "uDof"), this.dofFocus, this.dofRadius, Math.max(this.dofFar, this.blurAmt), Math.max(this.dofNear, this.blurAmt));
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uAmbRgb"), 1, this.ambRgb, 0);
        GLES20.glUniform4f(useMeshBatch$u$8(this, "uPbrFlags"), 0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform4f(useMeshBatch$u$8(this, "uPbrEmis"), 0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform1i(useMeshBatch$u$8(this, "uPtCount"), this.ptCount);
        if (this.ptCount > 0) {
            GLES20.glUniform4fv(useMeshBatch$u$8(this, "uPtPos"), this.ptCount, this.ptPos, 0);
            GLES20.glUniform4fv(useMeshBatch$u$8(this, "uPtCol"), this.ptCount, this.ptCol, 0);
            GLES20.glUniform4fv(useMeshBatch$u$8(this, "uPtDir"), this.ptCount, this.ptDir, 0);
        }
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uHorizon"), 1, this.horizon, 0);
        GLES20.glUniform3fv(useMeshBatch$u$8(this, "uZenith"), 1, this.zenith, 0);
        GLES20.glUniform1i(useMeshBatch$u$8(this, "uShadow"), 0);
        GLES20.glUniform1i(useMeshBatch$u$8(this, "uTex"), 1);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.shadowTex);
    }

    private static final int useMeshBatch$u$8(SceneRenderer this$0, String n) {
        Integer num = this$0.mb.get(n);
        if (num != null) {
            return num.intValue();
        }
        return -1;
    }

    private final void drawMeshBatch(StaticBatches.Batch b) {
        GLES20.glActiveTexture(33985);
        TexBind tex = b.getTex();
        GLES20.glBindTexture(3553, tex != null ? tex.getColor() : 0);
        GLES20.glActiveTexture(33984);
        Integer num = this.mb.get("uHasTex");
        int iIntValue = num != null ? num.intValue() : -1;
        TexBind tex2 = b.getTex();
        GLES20.glUniform1f(iIntValue, (tex2 != null ? tex2.getColor() : 0) != 0 ? 1.0f : 0.0f);
        GLES20.glBindBuffer(34962, b.getVbo());
        GLES20.glEnableVertexAttribArray(drawMeshBatch$a(this, "aPos"));
        GLES20.glVertexAttribPointer(drawMeshBatch$a(this, "aPos"), 3, 5126, false, 28, 0);
        if (drawMeshBatch$a(this, "aUv") >= 0) {
            GLES20.glEnableVertexAttribArray(drawMeshBatch$a(this, "aUv"));
            GLES20.glVertexAttribPointer(drawMeshBatch$a(this, "aUv"), 2, 5126, false, 28, 12);
        }
        if (drawMeshBatch$a(this, "aNrm") >= 0) {
            GLES20.glEnableVertexAttribArray(drawMeshBatch$a(this, "aNrm"));
            GLES20.glVertexAttribPointer(drawMeshBatch$a(this, "aNrm"), 4, 5120, true, 28, 20);
        }
        if (drawMeshBatch$a(this, "aCol") >= 0) {
            GLES20.glEnableVertexAttribArray(drawMeshBatch$a(this, "aCol"));
            GLES20.glVertexAttribPointer(drawMeshBatch$a(this, "aCol"), 4, 5121, true, 28, 24);
        }
        GLES20.glDrawArrays(4, 0, b.getCount());
    }

    private static final int drawMeshBatch$a(SceneRenderer this$0, String n) {
        Integer num = this$0.mb.get(n);
        if (num != null) {
            return num.intValue();
        }
        return -1;
    }

    private final void drawBatch(StaticBatches.Batch b) {
        GLES20.glBindBuffer(34962, b.getVbo());
        GLES20.glEnableVertexAttribArray(this.aPos);
        GLES20.glVertexAttribPointer(this.aPos, 3, 5126, false, 40, 0);
        if (this.aUvB >= 0) {
            GLES20.glEnableVertexAttribArray(this.aUvB);
            GLES20.glVertexAttribPointer(this.aUvB, 2, 5126, false, 40, 12);
        }
        if (this.aNrm >= 0) {
            GLES20.glEnableVertexAttribArray(this.aNrm);
            GLES20.glVertexAttribPointer(this.aNrm, 4, 5120, true, 40, 20);
        }
        if (this.aCol >= 0) {
            GLES20.glEnableVertexAttribArray(this.aCol);
            GLES20.glVertexAttribPointer(this.aCol, 4, 5121, true, 40, 24);
        }
        if (this.aMatB >= 0) {
            GLES20.glEnableVertexAttribArray(this.aMatB);
            GLES20.glVertexAttribPointer(this.aMatB, 4, 5121, false, 40, 28);
        }
        if (this.aTan >= 0) {
            GLES20.glEnableVertexAttribArray(this.aTan);
            GLES20.glVertexAttribPointer(this.aTan, 3, 5120, true, 40, 32);
        }
        if (this.aBit >= 0) {
            GLES20.glEnableVertexAttribArray(this.aBit);
            GLES20.glVertexAttribPointer(this.aBit, 3, 5120, true, 40, 36);
        }
        GLES20.glDrawArrays(4, 0, b.getCount());
    }

    private final void endBatches() {
        int iIntValue;
        int[] a;
        for (LitLocs l : this.batchLit) {
            if (l != null && (a = l.getLocs()) != null) {
                for (int i = 33; i < 38; i++) {
                    if (a[i] >= 0) {
                        GLES20.glDisableVertexAttribArray(a[i]);
                    }
                }
            }
        }
        for (String n : CollectionsKt.listOf(new String[]{"aUv", "aCol"})) {
            Integer num = this.mb.get(n);
            if (num != null && (iIntValue = num.intValue()) >= 0) {
                GLES20.glDisableVertexAttribArray(iIntValue);
            }
        }
    }

    private final void drawStaticBatches() {
        if (this.batches.getVisible().isEmpty()) {
            return;
        }
        ArrayList list = new ArrayList();
        Iterator<StaticBatches.Chunk> it = this.batches.getVisible().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            StaticBatches.Chunk next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            StaticBatches.Chunk c = next;
            for (StaticBatches.Batch b : c.getBatches()) {
                if (c.getDist() <= this.batches.tierRange(b.getTier(), this.detailScale)) {
                    list.add(b);
                }
            }
        }
        if (list.isEmpty()) {
            return;
        }
        CollectionsKt.sortWith(list, ComparisonsKt.compareBy(new Function1() { // from class: com.goydevv.studrblx.render.SceneRenderer$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SceneRenderer.drawStaticBatches$lambda$10((StaticBatches.Batch) obj);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.render.SceneRenderer$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SceneRenderer.drawStaticBatches$lambda$11((StaticBatches.Batch) obj);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.render.SceneRenderer$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SceneRenderer.drawStaticBatches$lambda$12((StaticBatches.Batch) obj);
            }
        }));
        int kind = -1;
        TexBind bound = null;
        Iterator it2 = list.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (it2.hasNext()) {
            Object next2 = it2.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            StaticBatches.Batch b2 = (StaticBatches.Batch) next2;
            if (b2.getKind() != kind) {
                if (kind == 3) {
                    GLES20.glEnable(2884);
                }
                kind = b2.getKind();
                this.activeLit = null;
                if (kind == 3) {
                    useMeshBatch();
                    GLES20.glDisable(2884);
                } else {
                    switchLit(batchProgram(kind));
                }
                bound = null;
            }
            if (kind == 3) {
                drawMeshBatch(b2);
            } else {
                TexBind tb = b2.getTex();
                if (tb != null && !Intrinsics.areEqual(tb, bound)) {
                    bindTextures(tb);
                    bound = tb;
                }
                drawBatch(b2);
            }
        }
        if (kind == 3) {
            GLES20.glEnable(2884);
        }
        endBatches();
        this.activeLit = null;
    }

    static final Comparable drawStaticBatches$lambda$10(StaticBatches.Batch it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Integer.valueOf(it.getKind());
    }

    static final Comparable drawStaticBatches$lambda$11(StaticBatches.Batch it) {
        Intrinsics.checkNotNullParameter(it, "it");
        TexBind tex = it.getTex();
        return Integer.valueOf(tex != null ? tex.getColor() : 0);
    }

    static final Comparable drawStaticBatches$lambda$12(StaticBatches.Batch it) {
        Intrinsics.checkNotNullParameter(it, "it");
        TexBind tex = it.getTex();
        return Integer.valueOf(tex != null ? tex.getNormal() : 0);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:21:0x0083  */
    /* JADX WARN: Code duplicated, block: B:23:0x008f  */
    /* JADX WARN: Code duplicated, block: B:27:0x009f  */
    /* JADX WARN: Code duplicated, block: B:30:0x00a6  */
    /* JADX WARN: Code duplicated, block: B:32:0x00a9  */
    /* JADX WARN: Code duplicated, block: B:33:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:35:0x00be  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void updateBatches(float dt, Vector3 shadowAt, float shadowR) {
        float f;
        DataModel m = this.studio.getModel();
        if (m != this.lastModel) {
            this.lastModel = m;
            this.batches.reset(true);
        }
        ViewState v = this.studio.getView();
        this.frustum.set(v.getVp());
        float focal = v.getHeight() / (((float) Math.tan(Math.toRadians(((double) this.studio.getCamera().getFov()) / 2.0d))) * 2.0f);
        String renderDistance = Settings.INSTANCE.getRenderDistance();
        float q = 0.5f;
        switch (renderDistance.hashCode()) {
            case -1994163307:
                if (!renderDistance.equals("Medium")) {
                    this.qualityTimer += dt;
                    if (this.qualityTimer > 1.5f && this.frames > 60) {
                        this.qualityTimer = 0.0f;
                        f = this.fps;
                        if (f > 1.0f && f <= 24.0f) {
                            this.quality = RangesKt.coerceAtLeast(this.quality * 0.8f, 0.5f);
                        } else if (this.fps > 45.0f) {
                            this.quality = RangesKt.coerceAtMost(this.quality * 1.12f, 2.2f);
                        }
                    }
                    q = this.quality;
                } else {
                    q = 1.0f;
                }
                break;
            case 76596:
                if (!renderDistance.equals("Low")) {
                    this.qualityTimer += dt;
                    if (this.qualityTimer > 1.5f) {
                        this.qualityTimer = 0.0f;
                        f = this.fps;
                        if (f > 1.0f && f <= 24.0f) {
                            this.quality = RangesKt.coerceAtLeast(this.quality * 0.8f, 0.5f);
                        } else if (this.fps > 45.0f) {
                            this.quality = RangesKt.coerceAtMost(this.quality * 1.12f, 2.2f);
                        }
                    }
                    q = this.quality;
                }
                break;
            case 2249154:
                if (!renderDistance.equals("High")) {
                    this.qualityTimer += dt;
                    if (this.qualityTimer > 1.5f) {
                        this.qualityTimer = 0.0f;
                        f = this.fps;
                        if (f > 1.0f && f <= 24.0f) {
                            this.quality = RangesKt.coerceAtLeast(this.quality * 0.8f, 0.5f);
                        } else if (this.fps > 45.0f) {
                            this.quality = RangesKt.coerceAtMost(this.quality * 1.12f, 2.2f);
                        }
                    }
                    q = this.quality;
                } else {
                    q = 1.8f;
                }
                break;
            case 81831820:
                if (!renderDistance.equals("Ultra")) {
                    this.qualityTimer += dt;
                    if (this.qualityTimer > 1.5f) {
                        this.qualityTimer = 0.0f;
                        f = this.fps;
                        if (f > 1.0f && f <= 24.0f) {
                            this.quality = RangesKt.coerceAtLeast(this.quality * 0.8f, 0.5f);
                        } else if (this.fps > 45.0f) {
                            this.quality = RangesKt.coerceAtMost(this.quality * 1.12f, 2.2f);
                        }
                    }
                    q = this.quality;
                } else {
                    q = 3.0f;
                }
                break;
            default:
                this.qualityTimer += dt;
                if (this.qualityTimer > 1.5f) {
                    this.qualityTimer = 0.0f;
                    f = this.fps;
                    if (f > 1.0f && f <= 24.0f) {
                        this.quality = RangesKt.coerceAtLeast(this.quality * 0.8f, 0.5f);
                    } else if (this.fps > 45.0f) {
                        this.quality = RangesKt.coerceAtMost(this.quality * 1.12f, 2.2f);
                    }
                }
                q = this.quality;
                break;
        }
        this.detailScale = (focal * q) / 1.5f;
        if (this.frames >= 31 && !this.noBatch) {
            long budgetMs = this.frames < 200 ? 10L : 5L;
            this.batches.update(this.studio.renderParts(), this.studio.getRenderListVersion(), this.frames, v.getCamPos(), this.frustum, this.detailScale, shadowAt, shadowR, budgetMs * 1000000, 167772160L);
        } else {
            this.batches.getLoose().clear();
            this.batches.getLoose().addAll(this.studio.renderParts());
            this.batches.getVisible().clear();
        }
    }

    private final LitLocs texturedLit() {
        LitLocs litLocs = this.litTex;
        if (litLocs != null) {
            return litLocs;
        }
        int p = program("#define TEXTURED 1\n" + LIT_VS, "#define TEXTURED 1\n" + LIT_FS);
        LitLocs litLocs2 = litLocs(p);
        this.litTex = litLocs2;
        return litLocs2;
    }

    private final TexBind materialBind(int mat) {
        MaterialTextures.MatTex t;
        TexBind texBind;
        if (!MaterialTextures.INSTANCE.hasTexture(mat) || (t = MaterialTextures.INSTANCE.get(mat, this.modernMaterialsNow)) == null) {
            return null;
        }
        Map map = this.matTexGpu;
        TexBind texBind2 = (TexBind) map.get(t);
        if (texBind2 == null) {
            Bitmap color = t.getColor();
            int iUpload = color != null ? upload(color, true) : whiteTex();
            Bitmap normal = t.getNormal();
            texBind = new TexBind(iUpload, normal != null ? upload(normal, true) : 0, 1.0f / t.getStudsPerTile());
            map.put(t, texBind);
        } else {
            texBind = texBind2;
        }
        return texBind;
    }

    private final TexBind texturesFor(Instance p, int mat) {
        TexBind bind;
        Instance v = variantFor(p);
        if (v == null) {
            return materialBind(mat);
        }
        TexBind texBind = this.variantBinds.get(v);
        if (texBind != null) {
            return texBind;
        }
        boolean stillLoading = false;
        String colorContent = PartData.INSTANCE.content(v, "ColorMap", "ColorMapContent");
        if (colorContent == null) {
            colorContent = "";
        }
        String strContent = PartData.INSTANCE.content(v, "NormalMap", "NormalMapContent");
        String normalContent = strContent != null ? strContent : "";
        Object obj = v.get("StudsPerTile");
        Number number = obj instanceof Number ? (Number) obj : null;
        float spt = RangesKt.coerceAtLeast(number != null ? number.floatValue() : 10.0f, 0.05f);
        TexBind base = materialBind(mat);
        int color = !StringsKt.isBlank(colorContent) ? assetTexture(colorContent, true) : 0;
        int normal = !StringsKt.isBlank(normalContent) ? assetTexture(normalContent, true) : 0;
        if ((!StringsKt.isBlank(colorContent) && color == 0) || (!StringsKt.isBlank(normalContent) && normal == 0)) {
            stillLoading = true;
        }
        if (color != 0) {
            bind = new TexBind(color, normal, 1.0f / spt);
        } else if (StringsKt.isBlank(colorContent) && normal != 0) {
            bind = new TexBind(base != null ? base.getColor() : whiteTex(), normal, 1.0f / spt);
        } else {
            bind = base;
        }
        if (bind != null && !stillLoading) {
            this.variantBinds.put(v, bind);
        }
        return bind;
    }

    private final int whiteTex() {
        if (this.whiteTexId == 0) {
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
            bitmapCreateBitmap.eraseColor(-1250068);
            Unit unit = Unit.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(bitmapCreateBitmap, "apply(...)");
            this.whiteTexId = upload(bitmapCreateBitmap, true);
        }
        return this.whiteTexId;
    }

    private final void bindTextures(TexBind t) {
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, t.getColor());
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, t.getNormal() != 0 ? t.getNormal() : t.getColor());
        GLES20.glActiveTexture(33984);
        GLES20.glUniform1f(this.uTexScale, t.getScale());
        GLES20.glUniform1f(this.uHasNrm, t.getNormal() != 0 ? 1.0f : 0.0f);
    }

    private final LitLocs advancedLit() {
        LitLocs litLocs = this.litAdv;
        if (litLocs != null) {
            return litLocs;
        }
        int p = program(LIT_VS, "#define ADVANCED 1\n" + LIT_FS);
        LitLocs litLocs2 = litLocs(p);
        this.litAdv = litLocs2;
        return litLocs2;
    }

    private final boolean needsAdvanced(int mat) {
        return material(mat)[0] > 1.5f;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int iUpload;
        LitLocs litLocs = litLocs(program(LIT_VS, LIT_FS));
        useLit(litLocs);
        this.litBasic = litLocs;
        this.litAdv = null;
        this.litTex = null;
        this.matTexGpu.clear();
        this.variantBinds.clear();
        this.whiteTexId = 0;
        this.frames = 0;
        this.line = program("\nuniform mat4 uVP; uniform mat4 uModel;\nattribute vec3 aPos;\nvoid main() { gl_Position = uVP * uModel * vec4(aPos, 1.0); }", LINE_FS);
        this.depthProg = program("\nuniform mat4 uVP; uniform mat4 uModel;\nattribute vec3 aPos;\nvoid main() { gl_Position = uVP * uModel * vec4(aPos, 1.0); }", DEPTH_FS);
        this.skyProg = program(SKY_VS, SKY_FS);
        this.decalProg = program(DECAL_VS, DECAL_FS);
        this.lVP = GLES20.glGetUniformLocation(this.line, "uVP");
        this.lModel = GLES20.glGetUniformLocation(this.line, "uModel");
        this.lColor = GLES20.glGetUniformLocation(this.line, "uColor");
        this.laPos = GLES20.glGetAttribLocation(this.line, "aPos");
        this.dVP = GLES20.glGetUniformLocation(this.depthProg, "uVP");
        this.dModel = GLES20.glGetUniformLocation(this.depthProg, "uModel");
        this.daPos = GLES20.glGetAttribLocation(this.depthProg, "aPos");
        this.sInvVP = GLES20.glGetUniformLocation(this.skyProg, "uInvVP");
        this.sCam = GLES20.glGetUniformLocation(this.skyProg, "uCam");
        this.sSun = GLES20.glGetUniformLocation(this.skyProg, "uSunDir");
        this.sZenith = GLES20.glGetUniformLocation(this.skyProg, "uZenith");
        this.sHorizon = GLES20.glGetUniformLocation(this.skyProg, "uHorizon");
        this.sSunColor = GLES20.glGetUniformLocation(this.skyProg, "uSunColor");
        this.sTime = GLES20.glGetUniformLocation(this.skyProg, "uTime");
        this.sDay = GLES20.glGetUniformLocation(this.skyProg, "uDay");
        this.sSunRays = GLES20.glGetUniformLocation(this.skyProg, "uSunRays");
        this.sCCParams = GLES20.glGetUniformLocation(this.skyProg, "uCCParams");
        this.sCCTint = GLES20.glGetUniformLocation(this.skyProg, "uCCTint");
        this.saPos = GLES20.glGetAttribLocation(this.skyProg, "aPos");
        this.cVP = GLES20.glGetUniformLocation(this.decalProg, "uVP");
        this.cModel = GLES20.glGetUniformLocation(this.decalProg, "uModel");
        this.cTex = GLES20.glGetUniformLocation(this.decalProg, "uTex");
        this.cTint = GLES20.glGetUniformLocation(this.decalProg, "uTint");
        this.cNormal = GLES20.glGetUniformLocation(this.decalProg, "uNormal");
        this.cLight = GLES20.glGetUniformLocation(this.decalProg, "uLight");
        this.cSun = GLES20.glGetUniformLocation(this.decalProg, "uSun");
        this.cAmbient = GLES20.glGetUniformLocation(this.decalProg, "uAmbient");
        this.cUv = GLES20.glGetUniformLocation(this.decalProg, "uUv");
        this.caPos = GLES20.glGetAttribLocation(this.decalProg, "aPos");
        this.caUv = GLES20.glGetAttribLocation(this.decalProg, "aUv");
        this.cube = Meshes.INSTANCE.cube();
        this.sphere = Meshes.sphere$default(Meshes.INSTANCE, 0, 0, 3, null);
        this.cylinder = Meshes.cylinder$default(Meshes.INSTANCE, 0, 1, null);
        this.cylinderY = Meshes.cylinderY$default(Meshes.INSTANCE, 0, 1, null);
        this.wedge = Meshes.INSTANCE.wedge();
        this.corner = Meshes.INSTANCE.cornerWedge();
        this.head = Meshes.head$default(Meshes.INSTANCE, 0, 1, null);
        this.headFace = Meshes.INSTANCE.headFace();
        this.quad = Meshes.INSTANCE.quad();
        this.screen = Meshes.INSTANCE.screenQuad();
        this.boxLines = Meshes.INSTANCE.boxLines();
        setupMeshProgram();
        setupSkybox();
        this.gpuMeshes.clear();
        this.unionGpu.clear();
        this.storedUnionGpu.clear();
        this.meshFrame = -1;
        this.batches.reset(false);
        ArraysKt.fill(this.batchLit, (Object) null, 0, this.batchLit.length);
        this.shadowValid = false;
        this.noBatch = new File(this.studio.getApp().getFilesDir(), "debug_nobatch").exists();
        this.textures.clear();
        this.surfaceTex.clear();
        String ext = GLES20.glGetString(7939);
        if (ext == null) {
            ext = "";
        }
        float fCoerceAtMost = 0.0f;
        if (StringsKt.contains((CharSequence) ext, (CharSequence) "GL_EXT_texture_filter_anisotropic", false)) {
            float[] f = new float[1];
            GLES20.glGetFloatv(GL_MAX_ANISOTROPY, f, 0);
            if (f[0] > 1.0f) {
                fCoerceAtMost = RangesKt.coerceAtMost(f[0], 8.0f);
            }
        }
        this.aniso = fCoerceAtMost;
        Bitmap bitmapSurfaces = MaterialTextures.INSTANCE.surfaces();
        if (bitmapSurfaces == null) {
            iUpload = 0;
        } else {
            iUpload = upload(bitmapSurfaces, true);
        }
        this.surfTex = iUpload;
        this.gridTex = 0;
        this.faceTex = upload(faceBitmap(), false);
        setupShadowMap();
        GLES20.glEnable(2929);
        GLES20.glEnable(2884);
        GLES20.glCullFace(1029);
        this.lastNs = System.nanoTime();
    }

    private final void setupShadowMap() {
        int[] max = new int[1];
        GLES20.glGetIntegerv(3379, max, 0);
        this.shadowSize = 512;
        int[] ids = new int[1];
        GLES20.glGenTextures(1, ids, 0);
        this.shadowTex = ids[0];
        GLES20.glBindTexture(3553, this.shadowTex);
        GLES20.glTexImage2D(3553, 0, 6408, this.shadowSize, this.shadowSize, 0, 6408, 5121, null);
        GLES20.glTexParameteri(3553, 10241, 9728);
        GLES20.glTexParameteri(3553, 10240, 9728);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glGenRenderbuffers(1, ids, 0);
        int depth = ids[0];
        GLES20.glBindRenderbuffer(36161, depth);
        GLES20.glRenderbufferStorage(36161, 33189, this.shadowSize, this.shadowSize);
        GLES20.glGenFramebuffers(1, ids, 0);
        this.shadowFbo = ids[0];
        GLES20.glBindFramebuffer(36160, this.shadowFbo);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.shadowTex, 0);
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, depth);
        this.shadowOk = GLES20.glCheckFramebufferStatus(36160) == 36053;
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glBindTexture(3553, 0);
        if (!this.shadowOk) {
            OutputLog.INSTANCE.warn("Shadows are not supported on this device's GPU");
        }
    }


    private final boolean ensurePostFbos() {
        if (this.surfW <= 0 || this.surfH <= 0) {
            return false;
        }
        if (this.postOk && this.postW == this.surfW && this.postH == this.surfH && this.postExtProg != 0) {
            return true;
        }
        if (this.postExtProg == 0) {
            this.postExtProg = program(POST_VS, BLOOM_EXT_FS);
            this.postBlurProg = program(POST_VS, GAUSS_FS);
            this.postCompProg = program(POST_VS, POST_COMP_FS);
        }
        if (this.postSceneFbo != 0) {
            GLES20.glDeleteFramebuffers(3, new int[]{this.postSceneFbo, this.postFboA, this.postFboB}, 0);
            GLES20.glDeleteTextures(3, new int[]{this.postSceneTex, this.postTexA, this.postTexB}, 0);
            GLES20.glDeleteRenderbuffers(1, new int[]{this.postSceneDepth}, 0);
            this.postSceneFbo = 0;
        }
        int bw = Math.max(this.surfW / 4, 64);
        int bh = Math.max(this.surfH / 4, 64);
        int[] texIds = new int[3];
        int[] fboIds = new int[3];
        int[] rbIds = new int[1];
        GLES20.glGenTextures(3, texIds, 0);
        GLES20.glGenFramebuffers(3, fboIds, 0);
        GLES20.glGenRenderbuffers(1, rbIds, 0);
        this.postSceneTex = texIds[0];
        this.postTexA = texIds[1];
        this.postTexB = texIds[2];
        this.postSceneFbo = fboIds[0];
        this.postFboA = fboIds[1];
        this.postFboB = fboIds[2];
        this.postSceneDepth = rbIds[0];

        int[] ws = {this.surfW, bw, bw};
        int[] hs = {this.surfH, bh, bh};
        for (int i = 0; i < 3; i++) {
            GLES20.glBindTexture(3553, texIds[i]);
            GLES20.glTexImage2D(3553, 0, 6408, ws[i], hs[i], 0, 6408, 5121, null);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLES20.glBindFramebuffer(36160, fboIds[i]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, texIds[i], 0);
            if (i == 0) {
                GLES20.glBindRenderbuffer(36161, this.postSceneDepth);
                GLES20.glRenderbufferStorage(36161, 33189, this.surfW, this.surfH);
                GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.postSceneDepth);
            }
        }
        this.postOk = GLES20.glCheckFramebufferStatus(36160) == 36053;
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glBindTexture(3553, 0);
        this.postW = this.surfW;
        this.postH = this.surfH;
        return this.postOk;
    }

    private final void runPostProcess() {
        Mesh quad = this.screen;
        if (quad == null || !this.postOk) {
            return;
        }
        int bw = Math.max(this.surfW / 4, 64);
        int bh = Math.max(this.surfH / 4, 64);
        boolean blurOnly = this.blurAmt > 0.02f;
        float spread = blurOnly ? (1.0f + this.blurAmt * 3.5f) : (0.8f + (this.bloomSize / 24.0f) * 1.6f);
        GLES20.glDisable(2929);
        GLES20.glDepthMask(false);
        GLES20.glDisable(2884);
        GLES20.glBindBuffer(34962, quad.getVbo());

        // Pass 1: Bright-pass extract (or full downsample for BlurEffect) -> postFboA
        GLES20.glBindFramebuffer(36160, this.postFboA);
        GLES20.glViewport(0, 0, bw, bh);
        GLES20.glUseProgram(this.postExtProg);
        int p1Pos = GLES20.glGetAttribLocation(this.postExtProg, "aPos");
        GLES20.glEnableVertexAttribArray(p1Pos);
        GLES20.glVertexAttribPointer(p1Pos, 2, 5126, false, 8, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.postSceneTex);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.postExtProg, "uTex"), 0);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(this.postExtProg, "uThresh"), Math.min(this.bloomThresh, 1.0f) * 0.78f);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(this.postExtProg, "uBlurOnly"), blurOnly ? 1.0f : 0.0f);
        GLES20.glDrawArrays(4, 0, quad.getCount());

        // Pass 2: Horizontal 9-tap Gaussian blur -> postFboB
        GLES20.glBindFramebuffer(36160, this.postFboB);
        GLES20.glUseProgram(this.postBlurProg);
        int p2Pos = GLES20.glGetAttribLocation(this.postBlurProg, "aPos");
        GLES20.glEnableVertexAttribArray(p2Pos);
        GLES20.glVertexAttribPointer(p2Pos, 2, 5126, false, 8, 0);
        GLES20.glBindTexture(3553, this.postTexA);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.postBlurProg, "uTex"), 0);
        GLES20.glUniform2f(GLES20.glGetUniformLocation(this.postBlurProg, "uDir"), spread / ((float) bw), 0.0f);
        GLES20.glDrawArrays(4, 0, quad.getCount());

        // Pass 3: Vertical 9-tap Gaussian blur -> postFboA
        GLES20.glBindFramebuffer(36160, this.postFboA);
        GLES20.glBindTexture(3553, this.postTexB);
        GLES20.glUniform2f(GLES20.glGetUniformLocation(this.postBlurProg, "uDir"), 0.0f, spread / ((float) bh));
        GLES20.glDrawArrays(4, 0, quad.getCount());

        // Pass 4: Composite Scene + Gaussian Bloom/Blur -> Default Screen FBO 0
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glViewport(0, 0, this.surfW, this.surfH);
        GLES20.glUseProgram(this.postCompProg);
        int p4Pos = GLES20.glGetAttribLocation(this.postCompProg, "aPos");
        GLES20.glEnableVertexAttribArray(p4Pos);
        GLES20.glVertexAttribPointer(p4Pos, 2, 5126, false, 8, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.postSceneTex);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.postCompProg, "uScene"), 0);
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, this.postTexA);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.postCompProg, "uBloomTex"), 1);
        GLES20.glActiveTexture(33984);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(this.postCompProg, "uBloomInt"), blurOnly ? 0.0f : (this.bloomInt * 0.55f));
        GLES20.glUniform1f(GLES20.glGetUniformLocation(this.postCompProg, "uBlurMix"), blurOnly ? RangesKt.coerceIn(this.blurAmt * 1.4f, 0.0f, 1.0f) : 0.0f);
        GLES20.glDrawArrays(4, 0, quad.getCount());

        GLES20.glEnable(2884);
        GLES20.glDepthMask(true);
        GLES20.glEnable(2929);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.surfW = width;
        this.surfH = height;
        GLES20.glViewport(0, 0, width, height);
        synchronized (this.studio.getLock()) {
            this.studio.getView().setWidth(width);
            this.studio.getView().setHeight(height);
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl) {
        try {
            long now = System.nanoTime();
            float dt = RangesKt.coerceIn((now - this.lastNs) / 1.0E9f, 0.0f, 0.1f);
            this.lastNs = now;
            this.time += dt;
            this.fpsAcc += dt;
            this.fpsFrames++;
            if (this.fpsAcc >= 0.5f) {
                this.fps = this.fpsFrames / this.fpsAcc;
                this.fpsAcc = 0.0f;
                this.fpsFrames = 0;
            }
            synchronized (this.studio.getLock()) {
                try {
                    try {
                        try {
                            this.studio.frame(dt);
                        } catch (Exception e) {
                            OutputLog.INSTANCE.error("Simulation error: " + e);
                        }
                        FlyCamera camera = this.studio.getCamera();
                        this.studio.getView().update(camera.getPos(), camera.getPos().plus(camera.getForward()), camera.getFov(), this.studio.getView().getWidth(), this.studio.getView().getHeight(), this.nearPlane);
                        updateLighting();
                        boolean z = this.shadowsOn && this.shadowOk && this.studio.getShadowsEnabled() && this.dayFactor > 0.05f;
                        Vector3 first = null;
                        Pair<Vector3, Float> pairShadowFocus = z ? shadowFocus() : null;
                        long jNanoTime = System.nanoTime();
                        if (pairShadowFocus != null) {
                            try {
                                first = pairShadowFocus.getFirst();
                            } catch (Exception e2) {
                                OutputLog outputLog = OutputLog.INSTANCE;
                                String message = e2.getMessage();
                                outputLog.warn("Renderer: " + (message != null ? message : e2));
                                this.batches.reset(true);
                                this.batches.getLoose().addAll(this.studio.renderParts());
                            }
                        }
                        updateBatches(dt, first, (pairShadowFocus != null ? pairShadowFocus.getSecond().floatValue() : 0.0f) * 1.6f);
                        long jNanoTime2 = System.nanoTime();
                        this.closest = Float.MAX_VALUE;
                        if (pairShadowFocus != null) {
                            renderShadowMap(pairShadowFocus.getFirst(), pairShadowFocus.getSecond().floatValue());
                        }
                        long jNanoTime3 = System.nanoTime();
                        try {
                            boolean usePost = (this.bloomInt > 0.001f || this.blurAmt > 0.02f) && ensurePostFbos();
                            GLES20.glBindFramebuffer(36160, usePost ? this.postSceneFbo : 0);
                            GLES20.glViewport(0, 0, this.surfW, this.surfH);
                            GLES20.glClearColor(this.horizon[0], this.horizon[1], this.horizon[2], 1.0f);
                            GLES20.glClear(16640);
                            drawSky();
                            drawParts(z);
                            drawSelection();
                            drawHandles();
                            if (usePost) {
                                runPostProcess();
                            }
                            long jNanoTime4 = System.nanoTime();
                            this.tUpd += (jNanoTime2 - jNanoTime) / 1000000.0f;
                            try {
                                this.tShadow += (jNanoTime3 - jNanoTime2) / 1000000.0f;
                                this.tDraw += (jNanoTime4 - jNanoTime3) / 1000000.0f;
                                this.tSim += (jNanoTime - now) / 1000000.0f;
                                float fCoerceIn = RangesKt.coerceIn(this.closest * 0.35f, 0.1f, 20.0f);
                                this.nearPlane = fCoerceIn < this.nearPlane ? fCoerceIn : this.nearPlane + ((fCoerceIn - this.nearPlane) * 0.25f);
                                Unit unit = Unit.INSTANCE;
                                Function0<Unit> function0 = this.onFrame;
                                if (function0 != null) {
                                    function0.invoke();
                                }
                                this.cpuAcc += (System.nanoTime() - now) / 1000000.0f;
                                this.cpuN++;
                                this.statTimer += dt;
                                if (this.statTimer > 5.0f) {
                                    this.statTimer = 0.0f;
                                    int n = RangesKt.coerceAtLeast(this.cpuN, 1);
                                    String str = String.format("draw: batches %.1f loose %.1f (%d) decals %.1f [clothing %.1f surfaceGuis %.1f, %d repaints] transparent %.1f (%d)", Arrays.copyOf(new Object[]{Float.valueOf(this.tB / n), Float.valueOf(this.tL / n), Integer.valueOf(this.nLoose / n), Float.valueOf(this.tDec / n), Float.valueOf(this.tClo / n), Float.valueOf(this.tSg / n), Integer.valueOf(this.nSgRender), Float.valueOf(this.tTr / n), Integer.valueOf(this.nTr / n)}, 9));
                                    Intrinsics.checkNotNullExpressionValue(str, "format(...)");
                                    Log.i("MobileStudioGfx", str);
                                    this.tB = 0.0f;
                                    this.tL = 0.0f;
                                    this.tDec = 0.0f;
                                    this.tTr = 0.0f;
                                    this.nTr = 0;
                                    this.nLoose = 0;
                                    this.tClo = 0.0f;
                                    this.tSg = 0.0f;
                                    this.nSgRender = 0;
                                    String str2 = String.format("fps=%.1f cpuMs=%.1f (sim %.1f upd %.1f shadow %.1f draw %.1f) parts=%d loose=%d chunks=%d visibleChunks=%d gpuMB=%.1f quality=%.2f near=%.2f depth=%d", Arrays.copyOf(new Object[]{Float.valueOf(this.fps), Float.valueOf(this.cpuAcc / n), Float.valueOf(this.tSim / n), Float.valueOf(this.tUpd / n), Float.valueOf(this.tShadow / n), Float.valueOf(this.tDraw / n), Integer.valueOf(this.studio.renderParts().size()), Integer.valueOf(this.batches.getLoose().size()), Integer.valueOf(this.batches.getChunks().size()), Integer.valueOf(this.batches.getVisible().size()), Float.valueOf(this.batches.getTotalBytes() / 1048576.0f), Float.valueOf(this.quality), Float.valueOf(this.nearPlane), Integer.valueOf(ViewportView.INSTANCE.getDepthBits())}, 14));
                                    Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
                                    Log.i("MobileStudioGfx", str2);
                                    this.cpuAcc = 0.0f;
                                    this.cpuN = 0;
                                    this.tSim = 0.0f;
                                    this.tUpd = 0.0f;
                                    this.tShadow = 0.0f;
                                    this.tDraw = 0.0f;
                                }
                                this.slowAvg = (this.slowAvg * 0.9f) + (dt * 0.1f);
                                if (this.slowAvg > 0.045f) {
                                    GLES20.glFinish();
                                    try {
                                        Thread.sleep(RangesKt.coerceIn((long) (this.slowAvg * 1000.0f * 0.35f), 8L, 40L));
                                    } catch (Exception e3) {
                                    }
                                }
                            } catch (Throwable th) {
                                throw th;
                            }
                        } catch (Throwable th2) {
                            Throwable th = th2;
                        }
                    } catch (Throwable th3) {
                        Throwable th = th3;
                        throw th;
                    }
                } catch (Throwable th4) {
                    Throwable th = th4;
                    throw th;
                }
            }
        } catch (Throwable _t) {
            if (_t instanceof RuntimeException) throw (RuntimeException) _t;
            if (_t instanceof Error) throw (Error) _t;
            throw new RuntimeException(_t);
        }
    }

    private final Color3 c3(Object v) {
        if (v instanceof Color3) {
            return (Color3) v;
        }
        return null;
    }

    private final float luma(Color3 c) {
        return (c.getR() * 0.3f) + (c.getG() * 0.59f) + (c.getB() * 0.11f);
    }

    private final float smooth(float a, float b, float x) {
        float t = RangesKt.coerceIn((x - a) / (b - a), 0.0f, 1.0f);
        return t * t * (3.0f - (2.0f * t));
    }

    private final void mix(float[] a, float[] b, float t, float[] out) {
        for (int i = 0; i < 3; i++) {
            out[i] = a[i] + ((b[i] - a[i]) * t);
        }
    }

    private final void updateLighting() {
        float clock;
        List listSplit$default;
        Float floatOrNull;
        Float floatOrNull2;
        Instance atmo;
        Iterable children;
        Object next;
        Instance l = this.studio.getModel().findService("Lighting");
        Object obj = l != null ? l.get("ClockTime") : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        if (number != null) {
            clock = number.floatValue();
        } else {
            Object obj2 = l != null ? l.get("TimeOfDay") : null;
            String str = obj2 instanceof String ? (String) obj2 : null;
            float fFloatValue = 14.0f;
            if (str != null && (listSplit$default = StringsKt.split((CharSequence) str, new String[]{":"}, false, 0)) != null) {
                String str2 = (String) CollectionsKt.getOrNull(listSplit$default, 0);
                if (str2 != null && (floatOrNull2 = StringsKt.toFloatOrNull(str2)) != null) {
                    fFloatValue = floatOrNull2.floatValue();
                }
                String str3 = (String) CollectionsKt.getOrNull(listSplit$default, 1);
                clock = fFloatValue + (((str3 == null || (floatOrNull = StringsKt.toFloatOrNull(str3)) == null) ? 0.0f : floatOrNull.floatValue()) / 60.0f);
            } else {
                clock = 14.0f;
            }
        }
        Object obj3 = l != null ? l.get("GeographicLatitude") : null;
        Number number2 = obj3 instanceof Number ? (Number) obj3 : null;
        float lat = (float) Math.toRadians(number2 != null ? number2.doubleValue() : 41.733d);
        float t = (float) (((double) ((clock - 6.0f) / 12.0f)) * 3.141592653589793d);
        float sunUp = (float) Math.sin(t);
        Vector3 dir = new Vector3(-((float) Math.cos(t)), ((float) Math.cos(lat)) * sunUp, (((float) Math.sin(lat)) * sunUp) + 1.0E-4f).getUnit();
        this.dayFactor = smooth(-0.08f, 0.18f, dir.getY());
        if (dir.getY() < 0.0f) {
            dir = new Vector3(-dir.getX(), -dir.getY(), -dir.getZ());
        }
        this.sunDir = new Vector3(dir.getX(), Math.max(dir.getY(), 0.08f), dir.getZ()).getUnit();
        float[] dayZ = {0.23f, 0.50f, 0.90f};
        float[] dayH = {0.68f, 0.81f, 0.95f};
        float[] nightZ = {0.007f, 0.012f, 0.035f};
        float[] nightH = {0.018f, 0.030f, 0.072f};
        float[] dusk = {0.96f, 0.52f, 0.28f};
        mix(nightZ, dayZ, this.dayFactor, this.zenith);
        mix(nightH, dayH, this.dayFactor, this.horizon);
        float sunset = smooth(0.35f, 0.0f, Math.abs(sunUp)) * smooth(-0.18f, 0.06f, sunUp);
        mix(this.horizon, dusk, sunset * 0.68f, this.horizon);
        Object obj4 = l != null ? l.get("Brightness") : null;
        Number number3 = obj4 instanceof Number ? (Number) obj4 : null;
        float brightness = RangesKt.coerceIn(number3 != null ? number3.floatValue() : 2.0f, 0.0f, 10.0f);
        Color3 cst = c3(l != null ? l.get("ColorShift_Top") : null);
        float sunStrength = RangesKt.coerceAtMost((brightness * 0.22f) + 0.42f, 1.18f) * ((this.dayFactor * 0.88f) + 0.12f);
        float[] warm = {1.0f, 0.97f - (sunset * 0.24f), 0.93f - (0.42f * sunset)};
        if (cst != null && (cst.getR() > 0.01f || cst.getG() > 0.01f || cst.getB() > 0.01f)) {
            warm[0] = warm[0] * 0.70f + cst.getR() * 0.30f;
            warm[1] = warm[1] * 0.70f + cst.getG() * 0.30f;
            warm[2] = warm[2] * 0.70f + cst.getB() * 0.30f;
        }
        for (int i = 0; i < 3; i++) {
            this.sunColor[i] = warm[i] * sunStrength;
        }
        if (this.dayFactor < 0.08f) {
            this.sunColor[0] = 0.13f;
            this.sunColor[1] = 0.17f;
            this.sunColor[2] = 0.28f;
        }
        Color3 oa = c3(l != null ? l.get("OutdoorAmbient") : null);
        if (oa == null) {
            oa = Color3.INSTANCE.fromRGB(128, 128, 128);
        }
        Color3 am = c3(l != null ? l.get("Ambient") : null);
        if (am == null) {
            am = Color3.INSTANCE.fromRGB(70, 70, 70);
        }
        boolean gShad = !(l != null ? Intrinsics.areEqual(l.get("GlobalShadows"), (Object) false) : false);
        float effAmbR = gShad ? Math.max(oa.getR(), am.getR()) : am.getR();
        float effAmbG = gShad ? Math.max(oa.getG(), am.getG()) : am.getG();
        float effAmbB = gShad ? Math.max(oa.getB(), am.getB()) : am.getB();
        float effAmbLuma = (effAmbR * 0.299f) + (effAmbG * 0.587f) + (effAmbB * 0.114f);
        float maxC = Math.max(Math.max(effAmbR, effAmbG), Math.max(effAmbB, 0.05f));
        this.ambRgb[0] = 0.55f + (0.45f * (effAmbR / maxC));
        this.ambRgb[1] = 0.55f + (0.45f * (effAmbG / maxC));
        this.ambRgb[2] = 0.55f + (0.45f * (effAmbB / maxC));
        Object edsObj = l != null ? l.get("EnvironmentDiffuseScale") : null;
        float envDiff = (edsObj instanceof Number) ? RangesKt.coerceIn(((Number) edsObj).floatValue(), 0.0f, 1.0f) : 0.75f;
        this.ambient = RangesKt.coerceIn((effAmbLuma * 0.36f) + (envDiff * 0.14f) + 0.18f, 0.20f, 0.56f) * ((this.dayFactor * 0.65f) + 0.35f);
        Object obj5 = l != null ? l.get("ExposureCompensation") : null;
        Number number4 = obj5 instanceof Number ? (Number) obj5 : null;
        this.exposure = (float) Math.pow(2.0d, RangesKt.coerceIn(number4 != null ? number4.doubleValue() : 0.0d, -3.0d, 3.0d));
        this.shadowsOn = !(l != null ? Intrinsics.areEqual(l.get("GlobalShadows"), (Object) false) : false);
        Object ssObj = l != null ? l.get("ShadowSoftness") : null;
        this.shadowSoftness = (ssObj instanceof Number) ? RangesKt.coerceIn(((Number) ssObj).floatValue(), 0.0f, 1.0f) : 0.2f;
        Color3 csb = c3(l != null ? l.get("ColorShift_Bottom") : null);
        if (csb != null) {
            this.shiftBottom[0] = csb.getR();
            this.shiftBottom[1] = csb.getG();
            this.shiftBottom[2] = csb.getB();
        } else {
            this.shiftBottom[0] = 0.0f;
            this.shiftBottom[1] = 0.0f;
            this.shiftBottom[2] = 0.0f;
        }
        Object essObj = l != null ? l.get("EnvironmentSpecularScale") : null;
        this.envSpec = (essObj instanceof Number) ? RangesKt.coerceIn(((Number) essObj).floatValue(), 0.0f, 1.0f) : 1.0f;
        Object techObj = l != null ? l.get("Technology") : null;
        if (techObj instanceof EnumValue) {
            this.lightTech = ((EnumValue) techObj).getValue();
        } else if (techObj instanceof Number) {
            this.lightTech = ((Number) techObj).intValue();
        } else {
            this.lightTech = 3;
        }
        if (this.lightTech >= 4) {
            this.envSpec = RangesKt.coerceIn(this.envSpec * 1.25f, 0.0f, 1.5f);
        } else if (this.lightTech == 1 || this.lightTech == 2) {
            this.envSpec *= 0.65f;
        }
        collectLocalLights();
        this.ccBright = 0.0f;
        this.ccContrast = 0.0f;
        this.ccSat = 0.0f;
        this.ccTint[0] = 1.0f;
        this.ccTint[1] = 1.0f;
        this.ccTint[2] = 1.0f;
        this.bloomInt = 0.0f;
        this.bloomThresh = 2.0f;
        this.bloomSize = 24.0f;
        this.sunRaysInt = 0.0f;
        this.sunRaysSpread = 0.1f;
        this.atmoGlare = 0.0f;
        this.dofFocus = 10.0f;
        this.dofRadius = 30.0f;
        this.dofFar = 0.0f;
        this.dofNear = 0.0f;
        this.blurAmt = 0.0f;
        atmo = null;
        ArrayList<Instance> fxRoots = new ArrayList<>();
        if (l != null) {
            fxRoots.add(l);
        }
        Instance wsFx = this.studio.getModel().findService("Workspace");
        if (wsFx != null) {
            Instance camFx = Instance.findFirstChild$default(wsFx, "Camera", false, 2, null);
            if (camFx != null) {
                fxRoots.add(camFx);
            }
        }
        for (Instance fxRoot : fxRoots) {
            for (Instance li : Instance.descendants$default(fxRoot, null, 1, null)) {
                String lcn = li.getClassName();
                if (atmo == null && "Atmosphere".equals(lcn)) {
                    atmo = li;
                } else if ("ColorCorrectionEffect".equals(lcn) && !Intrinsics.areEqual(li.get("Enabled"), (Object) false)) {
                    Object ccB = li.get("Brightness");
                    if (ccB instanceof Number) {
                        this.ccBright = RangesKt.coerceIn(this.ccBright + ((Number) ccB).floatValue(), -1.0f, 1.0f);
                    }
                    Object ccC = li.get("Contrast");
                    if (ccC instanceof Number) {
                        this.ccContrast = RangesKt.coerceIn(this.ccContrast + ((Number) ccC).floatValue(), -1.0f, 2.0f);
                    }
                    Object ccS = li.get("Saturation");
                    if (ccS instanceof Number) {
                        this.ccSat = RangesKt.coerceIn(this.ccSat + ((Number) ccS).floatValue(), -1.0f, 2.0f);
                    }
                    Color3 tc = c3(li.get("TintColor"));
                    if (tc != null) {
                        this.ccTint[0] *= tc.getR();
                        this.ccTint[1] *= tc.getG();
                        this.ccTint[2] *= tc.getB();
                    }
                } else if ("BloomEffect".equals(lcn) && !Intrinsics.areEqual(li.get("Enabled"), (Object) false)) {
                    Object bI = li.get("Intensity");
                    float bi = (bI instanceof Number) ? RangesKt.coerceIn(((Number) bI).floatValue(), 0.0f, 4.0f) : 1.0f;
                    Object bT = li.get("Threshold");
                    float bt = (bT instanceof Number) ? RangesKt.coerceIn(((Number) bT).floatValue(), 0.0f, 4.0f) : 2.0f;
                    Object bS = li.get("Size");
                    float bs = (bS instanceof Number) ? RangesKt.coerceIn(((Number) bS).floatValue(), 0.0f, 56.0f) : 24.0f;
                    if (bi > this.bloomInt) {
                        this.bloomInt = bi;
                        this.bloomThresh = bt;
                        this.bloomSize = bs;
                    }
                } else if ("SunRaysEffect".equals(lcn) && !Intrinsics.areEqual(li.get("Enabled"), (Object) false)) {
                    Object sI = li.get("Intensity");
                    float si = (sI instanceof Number) ? RangesKt.coerceIn(((Number) sI).floatValue(), 0.0f, 1.0f) : 0.25f;
                    Object sSp = li.get("Spread");
                    float ssp = (sSp instanceof Number) ? RangesKt.coerceIn(((Number) sSp).floatValue(), 0.0f, 1.0f) : 0.1f;
                    if (si > this.sunRaysInt) {
                        this.sunRaysInt = si;
                        this.sunRaysSpread = ssp;
                    }
                } else if ("DepthOfFieldEffect".equals(lcn) && !Intrinsics.areEqual(li.get("Enabled"), (Object) false)) {
                    Object fd = li.get("FocusDistance");
                    Object ir = li.get("InFocusRadius");
                    Object fi = li.get("FarIntensity");
                    Object ni = li.get("NearIntensity");
                    this.dofFocus = (fd instanceof Number) ? Math.max(((Number) fd).floatValue(), 0.05f) : 10.0f;
                    this.dofRadius = (ir instanceof Number) ? Math.max(((Number) ir).floatValue(), 0.0f) : 30.0f;
                    this.dofFar = (fi instanceof Number) ? RangesKt.coerceIn(((Number) fi).floatValue(), 0.0f, 1.0f) : 0.1f;
                    this.dofNear = (ni instanceof Number) ? RangesKt.coerceIn(((Number) ni).floatValue(), 0.0f, 1.0f) : 0.75f;
                } else if ("BlurEffect".equals(lcn) && !Intrinsics.areEqual(li.get("Enabled"), (Object) false)) {
                    Object bs = li.get("Size");
                    float bsz = (bs instanceof Number) ? RangesKt.coerceIn(((Number) bs).floatValue(), 0.0f, 56.0f) : 24.0f;
                    float normB = bsz / 56.0f;
                    if (normB > this.blurAmt) {
                        this.blurAmt = normB;
                    }
                }
            }
        }
        Object obj6 = l != null ? l.get("FogEnd") : null;
        Number number5 = obj6 instanceof Number ? (Number) obj6 : null;
        float fe = number5 != null ? number5.floatValue() : 100000.0f;
        if (atmo != null) {
            Object obj7 = atmo.get("Density");
            float density = (obj7 instanceof Number) ? RangesKt.coerceIn(((Number) obj7).floatValue(), 0.0f, 1.0f) : 0.3f;
            Object offObj = atmo.get("Offset");
            float offset = (offObj instanceof Number) ? RangesKt.coerceIn(((Number) offObj).floatValue(), 0.0f, 1.0f) : 0.25f;
            Color3 ac = c3(atmo.get("Color"));
            if (ac == null) {
                ac = Color3.INSTANCE.fromRGB(199, 170, 107);
            }
            Color3 dc = c3(atmo.get("Decay"));
            Object obj8 = atmo.get("Haze");
            float haze2 = (obj8 instanceof Number) ? RangesKt.coerceIn(((Number) obj8).floatValue(), 0.0f, 10.0f) : 0.0f;
            Object glareObj = atmo.get("Glare");
            float glareVal = (glareObj instanceof Number) ? RangesKt.coerceIn(((Number) glareObj).floatValue(), 0.0f, 10.0f) : 0.0f;
            this.atmoGlare = haze2 > 0.01f ? RangesKt.coerceIn((glareVal * 0.12f) * Math.min(haze2, 2.0f), 0.0f, 1.2f) : 0.0f;
            this.fogMode = 2.0f;
            this.fogStart = ((7.5E-4f * haze2) + 0.0038f) * density;
            this.fogEnd = RangesKt.coerceIn(0.92f - (offset * 0.45f), 0.30f, 0.92f);
            float[] mixC = {ac.getR(), ac.getG(), ac.getB()};
            if (dc != null && haze2 > 0.01f) {
                mixC[0] = mixC[0] * 0.72f + dc.getR() * 0.28f;
                mixC[1] = mixC[1] * 0.72f + dc.getG() * 0.28f;
                mixC[2] = mixC[2] * 0.72f + dc.getB() * 0.28f;
            }
            float nightScale = 0.10f + (0.90f * this.dayFactor);
            mixC[0] *= nightScale;
            mixC[1] *= nightScale;
            mixC[2] *= nightScale;
            float tintAmt = RangesKt.coerceIn(0.22f + haze2 * 0.07f, 0.22f, 0.80f) * (0.25f + 0.75f * this.dayFactor);
            mix(this.horizon, mixC, tintAmt, this.fogColor);
            mix(this.horizon, this.fogColor, 0.40f * this.dayFactor, this.horizon);
            return;
        }
        if (fe < 20000.0f) {
            this.fogMode = 1.0f;
            Object obj9 = l != null ? l.get("FogStart") : null;
            Number number8 = obj9 instanceof Number ? (Number) obj9 : null;
            this.fogStart = number8 != null ? number8.floatValue() : 0.0f;
            this.fogEnd = Math.max(fe, this.fogStart + 1.0f);
            Color3 fc = c3(l != null ? l.get("FogColor") : null);
            if (fc == null) {
                fc = Color3.INSTANCE.fromRGB(191, 191, 191);
            }
            this.fogColor[0] = fc.getR();
            this.fogColor[1] = fc.getG();
            this.fogColor[2] = fc.getB();
            if (fe < 2000.0f) {
                mix(this.horizon, this.fogColor, 0.7f, this.horizon);
                return;
            }
            return;
        }
        this.fogMode = 0.0f;
        this.fogColor[0] = this.horizon[0];
        this.fogColor[1] = this.horizon[1];
        this.fogColor[2] = this.horizon[2];
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class Geo {
        private final CFrame cf;
        private final int kind;
        private final Mesh mesh;
        private final Vector3 scale;
        private final List<Pair<Mesh, Color3>> subs;
        private final String tex;
        private final Vector3 tint;

        public Geo(Mesh mesh, CFrame cf, Vector3 scale, int kind, String tex, List<Pair<Mesh, Color3>> list, Vector3 tint) {
            Intrinsics.checkNotNullParameter(mesh, "mesh");
            Intrinsics.checkNotNullParameter(cf, "cf");
            Intrinsics.checkNotNullParameter(scale, "scale");
            this.mesh = mesh;
            this.cf = cf;
            this.scale = scale;
            this.kind = kind;
            this.tex = tex;
            this.subs = list;
            this.tint = tint;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        public /* synthetic */ Geo(Mesh mesh, CFrame cFrame, Vector3 vector3, int i, String str, List list, Vector3 vector4, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(mesh, cFrame, vector3, i, (i2 & 16) != 0 ? null : str, (i2 & 32) != 0 ? null : list, (i2 & 64) == 0 ? vector4 : null);
        }

        public final CFrame getCf() {
            return this.cf;
        }

        public final int getKind() {
            return this.kind;
        }

        public final Mesh getMesh() {
            return this.mesh;
        }

        public final Vector3 getScale() {
            return this.scale;
        }

        public final String getTex() {
            return this.tex;
        }

        public final List<Pair<Mesh, Color3>> getSubs() {
            return this.subs;
        }

        public final Vector3 getTint() {
            return this.tint;
        }

        public String saNormal;
        public String saRough;
        public String saMetal;
        public int saAlphaMode;
        public float saEmissive;
        public Vector3 saEmissiveTint;
    }

    private final Mesh meshFor(int shape) {
        Mesh mesh;
        String str;
        switch (shape) {
            case 0:
                mesh = this.sphere;
                if (mesh == null) {
                    str = "sphere";
                    break;
                }
                return mesh;
            case 1:
            default:
                mesh = this.cube;
                if (mesh == null) {
                    str = "cube";
                    break;
                }
                return mesh;
            case 2:
                mesh = this.cylinder;
                if (mesh == null) {
                    str = "cylinder";
                    break;
                }
                return mesh;
            case 3:
                mesh = this.wedge;
                if (mesh == null) {
                    str = "wedge";
                    break;
                }
                return mesh;
            case 4:
                mesh = this.corner;
                if (mesh == null) {
                    str = "corner";
                    break;
                }
                return mesh;
        }
        Intrinsics.throwUninitializedPropertyAccessException(str);
        return null;
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class GpuMesh {
        private final Vector3 center;
        private final Mesh mesh;
        private final Vector3 size;

        public GpuMesh(Mesh mesh, Vector3 center, Vector3 size) {
            Intrinsics.checkNotNullParameter(mesh, "mesh");
            Intrinsics.checkNotNullParameter(center, "center");
            Intrinsics.checkNotNullParameter(size, "size");
            this.mesh = mesh;
            this.center = center;
            this.size = size;
        }

        public final Vector3 getCenter() {
            return this.center;
        }

        public final Mesh getMesh() {
            return this.mesh;
        }

        public final Vector3 getSize() {
            return this.size;
        }
    }

    private final GpuMesh fileMesh(Object content) {
        Long lIdOf = AssetFetcher.INSTANCE.idOf(content);
        if (lIdOf == null) {
            return null;
        }
        long id = lIdOf.longValue();
        GpuMesh gpuMesh = this.gpuMeshes.get(Long.valueOf(id));
        if (gpuMesh != null) {
            return gpuMesh;
        }
        MeshData data = MeshLoader.INSTANCE.get(id);
        if (data == null || data.getTriangles() == 0) {
            return null;
        }
        Mesh m = Meshes.upload$default(Meshes.INSTANCE, data.getVerts(), 8, 4, null, 8, null);
        GpuMesh g = new GpuMesh(m, new Vector3(data.center(0), data.center(1), data.center(2)), new Vector3(data.size(0), data.size(1), data.size(2)));
        this.gpuMeshes.put(Long.valueOf(id), g);
        return g;
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class UnionGpu {
        private final List<Pair<Mesh, Integer>> meshes;
        private final long sig;
        private final List<SolidModeling.Style> styles;

        public UnionGpu(long sig, List<Pair<Mesh, Integer>> meshes, List<SolidModeling.Style> styles) {
            Intrinsics.checkNotNullParameter(meshes, "meshes");
            Intrinsics.checkNotNullParameter(styles, "styles");
            this.sig = sig;
            this.meshes = meshes;
            this.styles = styles;
        }

        public final List<Pair<Mesh, Integer>> getMeshes() {
            return this.meshes;
        }

        public final long getSig() {
            return this.sig;
        }

        public final List<SolidModeling.Style> getStyles() {
            return this.styles;
        }
    }

    private final Geo storedUnionGeo(Instance p, CFrame cf) {
        Geo geo;
        List<Pair<Mesh, Integer>> list;
        Instance instance = p;
        UnionMeshes.Data d = UnionMeshes.INSTANCE.get(instance);
        Geo geo2 = null;
        if (d == null) {
            return null;
        }
        Map map = this.storedUnionGpu;
        String key = d.getKey();
        List<Pair<Mesh, Integer>> list2 = (List<Pair<Mesh, Integer>>) map.get(key);
        if (list2 == null) {
            Iterable<Pair<Integer, float[]>> groups = d.getGroups();
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(groups, 10));
            for (Pair<Integer, float[]> pair : groups) {
                arrayList.add(TuplesKt.to(Meshes.upload$default(Meshes.INSTANCE, (float[]) pair.component2(), 8, 4, null, 8, null), Integer.valueOf(((Number) pair.component1()).intValue())));
            }
            geo = geo2;
            list = (List) arrayList;
            map.put(key, list);
        } else {
            geo = null;
            list = list2;
        }
        List<Pair<Mesh, Integer>> list3 = list;
        if (list3.isEmpty()) {
            return geo;
        }
        Vector3 size = PartData.INSTANCE.size(instance);
        Vector3 sc = new Vector3(storedUnionGeo$k(size.getX(), d.getSize().getX()), storedUnionGeo$k(size.getY(), d.getSize().getY()), storedUnionGeo$k(size.getZ(), d.getSize().getZ()));
        boolean usePart = Intrinsics.areEqual(instance.get("UsePartColor"), (Object) true);
        List<Pair<Mesh, Integer>> list4 = list3;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list4, 10));
        Iterator it = list4.iterator();
        while (it.hasNext()) {
            Pair pair2 = (Pair) it.next();
            UnionMeshes.Data d2 = d;
            Mesh mesh = (Mesh) pair2.component1();
            int iIntValue = ((Number) pair2.component2()).intValue();
            arrayList2.add(TuplesKt.to(mesh, usePart ? PartData.INSTANCE.color(instance) : Color3.INSTANCE.fromRGB((iIntValue >> 16) & 255, (iIntValue >> 8) & 255, iIntValue & 255)));
            instance = p;
            d = d2;
        }
        List subs = (List) arrayList2;
        return new Geo((Mesh) ((Pair) subs.get(0)).getFirst(), cf.times(CFrame.INSTANCE.at(d.getCenter().times(sc).unaryMinus())), sc, 3, null, subs, null, 80, null);
    }

    private static final float storedUnionGeo$k(float a, float b) {
        if (b > 1.0E-4f) {
            return a / b;
        }
        return 1.0f;
    }

    private final Geo unionGeo(Instance p, CFrame cf) {
        boolean z;
        Geo geo;
        Iterable meshes;
        Color3 color3Color;
        SolidModeling.Style style;
        SolidModeling.Result r = SolidModeling.INSTANCE.result(p);
        if (r == null) {
            if (UnionMeshes.INSTANCE.isStored(p)) {
                return storedUnionGeo(p, cf);
            }
            return null;
        }
        UnionGpu unionGpu = this.unionGpu.get(p);
        boolean z2 = true;
        if (unionGpu == null || unionGpu.getSig() != r.getSig()) {
            UnionGpu unionGpu2 = unionGpu;
            if (unionGpu2 != null && (meshes = unionGpu2.getMeshes()) != null) {
                Iterator it = meshes.iterator();
                while (it.hasNext()) {
                    GLES20.glDeleteBuffers(1, new int[]{((Mesh) ((Pair) it.next()).getFirst()).getVbo()}, 0);
                }
            }
            long sig = r.getSig();
            Map<Integer, float[]> tris = r.getTris();
            Map linkedHashMap = new LinkedHashMap();
            for (Map.Entry<Integer, float[]> entry : tris.entrySet()) {
                if (!(entry.getValue().length == 0 ? z2 : false)) {
                    linkedHashMap.put(entry.getKey(), entry.getValue());
                }
            }
            z = z2;
            geo = null;
            int i = 0;
            Collection arrayList = new ArrayList(linkedHashMap.size());
            for (Map.Entry entry2 : ((Map<?, ?>) linkedHashMap).entrySet()) {
                arrayList.add(TuplesKt.to(Meshes.upload$default(Meshes.INSTANCE, (float[]) entry2.getValue(), 8, 4, null, 8, null), Integer.valueOf(((Number) entry2.getKey()).intValue())));
            }
            unionGpu = new UnionGpu(sig, (List) arrayList, r.getStyles());
            this.unionGpu.put(p, unionGpu);
        } else {
            z = true;
            geo = null;
        }
        if (unionGpu.getMeshes().isEmpty()) {
            return geo;
        }
        boolean usePart = Intrinsics.areEqual(p.get("UsePartColor"), Boolean.valueOf(z));
        Iterable<Pair<Mesh, Integer>> meshes2 = unionGpu.getMeshes();
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(meshes2, 10));
        for (Pair<Mesh, Integer> pair : meshes2) {
            Mesh mesh = (Mesh) pair.component1();
            int iIntValue = ((Number) pair.component2()).intValue();
            if (usePart || (style = (SolidModeling.Style) CollectionsKt.getOrNull(unionGpu.getStyles(), iIntValue)) == null || (color3Color = style.getColor()) == null) {
                color3Color = PartData.INSTANCE.color(p);
            }
            arrayList2.add(TuplesKt.to(mesh, color3Color));
        }
        List subs = (List) arrayList2;
        return new Geo((Mesh) ((Pair) subs.get(0)).getFirst(), cf, SolidModeling.INSTANCE.scaleOf(p, r), 3, null, subs, null, 80, null);
    }

    private final Geo geometry(Instance p) {
        int i;
        Geo geoUnionGeo;
        Instance instance;
        Vector3 s;
        int type;
        Mesh mesh;
        Mesh mesh2;
        Mesh mesh3;
        Mesh mesh4;
        Mesh mesh5;
        String string;
        String strContent;
        char c;
        Object next;
        CFrame cf = PartData.INSTANCE.cframe(p);
        Vector3 size = PartData.INSTANCE.size(p);
        if (Intrinsics.areEqual(p.getClassName(), "MeshPart")) {
            GpuMesh gpuMeshFileMesh = fileMesh(PartData.INSTANCE.meshContent(p));
            if (gpuMeshFileMesh != null) {
                Vector3 vector3 = new Vector3(geometry$lambda$40$k$37(size.getX(), gpuMeshFileMesh.getSize().getX()), geometry$lambda$40$k$37(size.getY(), gpuMeshFileMesh.getSize().getY()), geometry$lambda$40$k$37(size.getZ(), gpuMeshFileMesh.getSize().getZ()));
                String strContent2 = this.partTexOverride.get(p);
                Vector3 saTint = null;
                Instance sa = null;
                for (Instance ch : p.getChildren()) {
                    if (Intrinsics.areEqual(ch.getClassName(), "SurfaceAppearance")) {
                        sa = ch;
                        break;
                    }
                }
                if (strContent2 == null) {
                    if (sa != null) {
                        strContent2 = PartData.INSTANCE.content(sa, "ColorMap", "ColorMapContent");
                        Object saColObj = sa.get("Color");
                        if (saColObj instanceof Color3) {
                            Color3 sc3 = (Color3) saColObj;
                            saTint = new Vector3(sc3.getR(), sc3.getG(), sc3.getB());
                        }
                    }
                    if (strContent2 == null) {
                        strContent2 = PartData.INSTANCE.textureContent(p);
                    }
                }
                Geo geoOut = new Geo(gpuMeshFileMesh.getMesh(), cf.times(CFrame.INSTANCE.at(gpuMeshFileMesh.getCenter().times(vector3).unaryMinus())), vector3, 2, strContent2, null, saTint, 32, null);
                if (sa != null) {
                    geoOut.saNormal = PartData.INSTANCE.content(sa, "NormalMap", "NormalMapContent");
                    geoOut.saRough = PartData.INSTANCE.content(sa, "RoughnessMap", "RoughnessMapContent");
                    geoOut.saMetal = PartData.INSTANCE.content(sa, "MetalnessMap", "MetalnessMapContent");
                    Object amObj = sa.get("AlphaMode");
                    if (amObj instanceof EnumValue) {
                        geoOut.saAlphaMode = ((EnumValue) amObj).getValue();
                    } else if (amObj instanceof Number) {
                        geoOut.saAlphaMode = ((Number) amObj).intValue();
                    }
                    Object esObj = sa.get("EmissiveStrength");
                    if (esObj instanceof Number) {
                        geoOut.saEmissive = RangesKt.coerceIn(((Number) esObj).floatValue(), 0.0f, 10.0f);
                    }
                    Color3 et = c3(sa.get("EmissiveTint"));
                    if (et != null) {
                        geoOut.saEmissiveTint = new Vector3(et.getR(), et.getG(), et.getB());
                    }
                }
                return geoOut;
            }
            i = 0;
        } else {
            i = 0;
            if (p.isA("PartOperation") && !Intrinsics.areEqual(p.getClassName(), "NegateOperation") && (geoUnionGeo = unionGeo(p, cf)) != null) {
                return geoUnionGeo;
            }
        }
        Iterator<Instance> it2 = p.getChildren().iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (true) {
            if (!it2.hasNext()) {
                instance = null;
                break;
            }
            Instance c2 = it2.next();
            if (Intrinsics.areEqual(c2.getClassName(), "SpecialMesh") || Intrinsics.areEqual(c2.getClassName(), "BlockMesh") || Intrinsics.areEqual(c2.getClassName(), "CylinderMesh") || Intrinsics.areEqual(c2.getClassName(), "FileMesh")) {
                instance = c2;
                break;
            }
        }
        if (instance != null) {
            Object obj = instance.get("Scale");
            Vector3 vector4 = obj instanceof Vector3 ? (Vector3) obj : null;
            if (vector4 == null) {
                vector4 = new Vector3(1.0f, 1.0f, 1.0f);
            }
            Vector3 sc = vector4;
            Object obj2 = instance.get("Offset");
            Vector3 off = obj2 instanceof Vector3 ? (Vector3) obj2 : null;
            if (off == null) {
                off = Vector3.INSTANCE.getZERO();
            }
            CFrame ocf = !Intrinsics.areEqual(off, Vector3.INSTANCE.getZERO()) ? cf.times(CFrame.INSTANCE.at(off)) : cf;
            if (Intrinsics.areEqual(instance.getClassName(), "SpecialMesh")) {
                Object obj3 = instance.get("MeshType");
                EnumValue enumValue = obj3 instanceof EnumValue ? (EnumValue) obj3 : null;
                type = enumValue != null ? enumValue.getValue() : i;
            } else if (Intrinsics.areEqual(instance.getClassName(), "CylinderMesh")) {
                type = 4;
            } else {
                type = Intrinsics.areEqual(instance.getClassName(), "FileMesh") ? 5 : 6;
            }
            switch (type) {
                case 0:
                    float dia = Math.min(size.getX(), size.getZ());
                    Mesh mesh6 = this.head;
                    if (mesh6 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("head");
                        mesh = null;
                    } else {
                        mesh = mesh6;
                    }
                    return new Geo(mesh, ocf, new Vector3(sc.getX() * dia, size.getY() * sc.getY(), sc.getZ() * dia), 1, null, null, null, 112, null);
                case 1:
                case 6:
                    Mesh mesh7 = this.cube;
                    if (mesh7 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                        mesh2 = null;
                    } else {
                        mesh2 = mesh7;
                    }
                    return new Geo(mesh2, ocf, size.times(sc), 0, null, null, null, 112, null);
                case 2:
                    Mesh mesh8 = this.wedge;
                    if (mesh8 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("wedge");
                        mesh3 = null;
                    } else {
                        mesh3 = mesh8;
                    }
                    return new Geo(mesh3, ocf, size.times(sc), 0, null, null, null, 112, null);
                case 3:
                    Mesh mesh9 = this.sphere;
                    if (mesh9 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("sphere");
                        mesh4 = null;
                    } else {
                        mesh4 = mesh9;
                    }
                    return new Geo(mesh4, ocf, size.times(sc), 0, null, null, null, 112, null);
                case 4:
                    Mesh mesh10 = this.cylinderY;
                    if (mesh10 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("cylinderY");
                        mesh5 = null;
                    } else {
                        mesh5 = mesh10;
                    }
                    return new Geo(mesh5, ocf, size.times(sc), 0, null, null, null, 112, null);
                case 5:
                    GpuMesh gpuMeshFileMesh2 = fileMesh(PartData.INSTANCE.meshContent(instance));
                    if (gpuMeshFileMesh2 != null) {
                        String str = PartData.INSTANCE.textureContent(instance);
                        Mesh mesh11 = gpuMeshFileMesh2.getMesh();
                        Object obj5 = instance.get("VertexColor");
                        return new Geo(mesh11, ocf, sc, 2, str, null, obj5 instanceof Vector3 ? (Vector3) obj5 : null, 32, null);
                    }
                    break;
            }
        }
        int shape = PartData.INSTANCE.shape(p);
        switch (shape) {
            case 0:
                float d = Math.min(size.getX(), Math.min(size.getY(), size.getZ()));
                s = new Vector3(d, d, d);
                break;
            case 1:
            default:
                s = size;
                break;
            case 2:
                float d2 = Math.min(size.getY(), size.getZ());
                s = new Vector3(size.getX(), d2, d2);
                break;
        }
        return new Geo(meshFor(shape), cf, s, 0, null, null, null, 112, null);
    }

    private static final float geometry$lambda$40$k$37(float a, float b) {
        if (b > 1.0E-5f) {
            return a / b;
        }
        return 1.0f;
    }

    private final void bind(Mesh m, int posLoc, int nrmLoc) {
        GLES20.glBindBuffer(34962, m.getVbo());
        GLES20.glEnableVertexAttribArray(posLoc);
        GLES20.glVertexAttribPointer(posLoc, 3, 5126, false, m.getStride() * 4, 0);
        if (nrmLoc >= 0) {
            GLES20.glEnableVertexAttribArray(nrmLoc);
            GLES20.glVertexAttribPointer(nrmLoc, 3, 5126, false, m.getStride() * 4, 12);
        }
    }

    private final void setModel(CFrame cf, Vector3 s) {
        this.model[0] = cf.getR00() * s.getX();
        this.model[1] = cf.getR10() * s.getX();
        this.model[2] = cf.getR20() * s.getX();
        this.model[3] = 0.0f;
        this.model[4] = cf.getR01() * s.getY();
        this.model[5] = cf.getR11() * s.getY();
        this.model[6] = cf.getR21() * s.getY();
        this.model[7] = 0.0f;
        this.model[8] = cf.getR02() * s.getZ();
        this.model[9] = cf.getR12() * s.getZ();
        this.model[10] = cf.getR22() * s.getZ();
        this.model[11] = 0.0f;
        this.model[12] = cf.getX();
        this.model[13] = cf.getY();
        this.model[14] = cf.getZ();
        this.model[15] = 1.0f;
        float ix = !((s.getX() > 0.0f ? 1 : (s.getX() == 0.0f ? 0 : -1)) == 0) ? 1.0f / s.getX() : 0.0f;
        float iy = !((s.getY() > 0.0f ? 1 : (s.getY() == 0.0f ? 0 : -1)) == 0) ? 1.0f / s.getY() : 0.0f;
        float iz = (s.getZ() > 0.0f ? 1 : (s.getZ() == 0.0f ? 0 : -1)) == 0 ? 0.0f : 1.0f / s.getZ();
        this.normal[0] = cf.getR00() * ix;
        this.normal[1] = cf.getR10() * ix;
        this.normal[2] = cf.getR20() * ix;
        this.normal[3] = cf.getR01() * iy;
        this.normal[4] = cf.getR11() * iy;
        this.normal[5] = cf.getR21() * iy;
        this.normal[6] = cf.getR02() * iz;
        this.normal[7] = cf.getR12() * iz;
        this.normal[8] = cf.getR22() * iz;
    }

    private final boolean visible(CFrame cf, Vector3 size, Vector3 camPos, Vector3 fwd) {
        return inView(cf, size, camPos);
    }

    private final boolean inView(CFrame cf, Vector3 size, Vector3 camPos) {
        float r = size.getMagnitude() * 0.5f;
        if (!this.frustum.sphere(cf.getX(), cf.getY(), cf.getZ(), r)) {
            return false;
        }
        float dx = cf.getX() - camPos.getX();
        float dy = cf.getY() - camPos.getY();
        float dz = cf.getZ() - camPos.getZ();
        float d = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        if (r < 5.0f && d > this.detailScale * r) {
            if (d > (r < 1.2f ? 150.0f : 900.0f)) {
                return false;
            }
        }
        float near = d - r;
        if (near < this.closest) {
            this.closest = RangesKt.coerceAtLeast(near, 0.0f);
            return true;
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:11:0x002f  */
    private final Pair<Vector3, Float> shadowFocus() {
        Vector3 focus;
        Instance instanceFindFirstChild$default;
        FlyCamera cam = this.studio.getCamera();
        PlaySession play = this.studio.getPlay();
        if (play != null) {
            Instance character = play.getCharacter();
            focus = null;
            if (character != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(character, "HumanoidRootPart", false, 2, null)) != null) {
                focus = PartData.INSTANCE.cframe(instanceFindFirstChild$default).getPosition();
            }
            if (focus == null) {
                focus = cam.getPos().plus(cam.getForward().times(45.0f));
            }
        } else {
            focus = cam.getPos().plus(cam.getForward().times(45.0f));
        }
        float radius = play != null ? 28.0f : RangesKt.coerceIn((RangesKt.coerceAtLeast(cam.getPos().getY(), 0.0f) * 0.55f) + 32.0f, 44.0f, 150.0f);
        return TuplesKt.to(focus, Float.valueOf(radius));
    }

    private final void renderShadowMap(Vector3 focus, float radius) {
        List meshes;
        PlaySession play = this.studio.getPlay();
        Vector3 up = Math.abs(this.sunDir.getY()) > 0.99f ? Vector3.INSTANCE.getZ() : Vector3.INSTANCE.getY();
        Vector3 fwdL = this.sunDir.unaryMinus().getUnit();
        Vector3 rightL = fwdL.cross(up).getUnit();
        Vector3 upL = rightL.cross(fwdL).getUnit();
        float texel = (2.0f * radius) / this.shadowSize;
        float fx = ((float) Math.floor(focus.dot(rightL) / texel)) * texel;
        float fy = ((float) Math.floor(focus.dot(upL) / texel)) * texel;
        float fz = focus.dot(fwdL);
        Vector3 center = rightL.times(fx).plus(upL.times(fy)).plus(fwdL.times(fz));
        Vector3 eye = center.plus(this.sunDir.times(360.0f));
        Matrix.setLookAtM(this.lightView, 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), up.getX(), up.getY(), up.getZ());
        float fz2 = -radius;
        Matrix.orthoM(this.lightProj, 0, fz2, radius, -radius, radius, 40.0f, 780.0f);
        Matrix.multiplyMM(this.lightVP, 0, this.lightProj, 0, this.lightView, 0);
        long change = Instance.changeCount;
        this.shadowAge++;
        if (this.shadowValid && this.studio.isPlaying() && this.fps < 40.0f && this.frames % 2 == 1) {
            ArraysKt.copyInto(this.lastLightVP, this.lightVP, 0, 0, this.lastLightVP.length);
            return;
        }
        if (this.shadowValid && change == this.lastShadowChange && MeshLoader.INSTANCE.getEpoch() == this.lastShadowMeshEpoch && this.shadowAge < 45 && Arrays.equals(this.lightVP, this.lastLightVP)) {
            return;
        }
        this.shadowValid = true;
        this.lastShadowChange = change;
        this.lastShadowMeshEpoch = MeshLoader.INSTANCE.getEpoch();
        this.shadowAge = 0;
        ArraysKt.copyInto(this.lightVP, this.lastLightVP, 0, 0, this.lightVP.length);
        GLES20.glBindFramebuffer(36160, this.shadowFbo);
        GLES20.glViewport(0, 0, this.shadowSize, this.shadowSize);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(16640);
        GLES20.glUseProgram(this.depthProg);
        GLES20.glUniformMatrix4fv(this.dVP, 1, false, this.lightVP, 0);
        GLES20.glCullFace(1029);
        GLES20.glEnable(32823);
        GLES20.glPolygonOffset(2.0f, 3.0f);
        float r2 = radius * 1.6f * 1.6f * radius;
        Matrix.setIdentityM(this.model, 0);
        GLES20.glUniformMatrix4fv(this.dModel, 1, false, this.model, 0);
        GLES20.glEnableVertexAttribArray(this.daPos);
        Iterator<StaticBatches.Chunk> it = this.batches.getVisible().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            StaticBatches.Chunk next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            StaticBatches.Chunk c = next;
            float dx = Math.max(Math.max(c.getX0() - center.getX(), 0.0f), center.getX() - c.getX1());
            float dz = Math.max(Math.max(c.getZ0() - center.getZ(), 0.0f), center.getZ() - c.getZ1());
            if ((dx * dx) + (dz * dz) <= r2) {
                for (StaticBatches.Batch b : c.getBatches()) {
                    if (b.getTier() >= 2 || c.getDist() <= this.batches.tierRange(b.getTier(), this.detailScale)) {
                        GLES20.glBindBuffer(34962, b.getVbo());
                        GLES20.glVertexAttribPointer(this.daPos, 3, 5126, false, b.getStrideBytes(), 0);
                        if (b.getKind() == 3) {
                            GLES20.glDisable(2884);
                        }
                        GLES20.glDrawArrays(4, 0, b.getCount());
                        if (b.getKind() == 3) {
                            GLES20.glEnable(2884);
                        }
                    }
                }
            }
        }
        Mesh current = null;
        Iterator<Instance> it2 = this.batches.getLoose().iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (it2.hasNext()) {
            Instance p = it2.next();
            if (PartData.INSTANCE.transparency(p) > 0.5f || Intrinsics.areEqual(p.get("CastShadow"), (Object) false)) {
            } else {
                if (!Intrinsics.areEqual(p.getClassName(), "NegateOperation")) {
                    if (!(play != null && play.hiddenInFirstPerson(p))) {
                        Geo g = this.geometry(p);
                        float dx2 = g.getCf().getX() - center.getX();
                        float dz2 = g.getCf().getZ() - center.getZ();
                        float ext = g.getScale().getMagnitude() * 0.5f;
                        if ((dx2 * dx2) + (dz2 * dz2) <= r2 + (ext * ext * 4.0f) || ext >= radius) {
                            Mesh current2 = current;
                            this.setModel(g.getCf(), g.getScale());
                            Iterator<Instance> it3 = it2;
                            GLES20.glUniformMatrix4fv(this.dModel, 1, false, this.model, 0);
                            Iterable subs = g.getSubs();
                            Mesh current3 = current2;
                            if (subs != null) {
                                for (Object so : subs) {
                                    Mesh m = (Mesh) ((Pair) so).getFirst();
                                    if (m != current3) {
                                        this.bind(m, this.daPos, -1);
                                        current3 = m;
                                    }
                                    if (m.getCount() <= 3600) { GLES20.glDrawArrays(m.getMode(), 0, m.getCount()); }
                                }
                            } else {
                                Mesh m = g.getMesh();
                                if (m != current3) {
                                    this.bind(m, this.daPos, -1);
                                    current3 = m;
                                }
                                if (m.getCount() <= 3600) { GLES20.glDrawArrays(m.getMode(), 0, m.getCount()); }
                            }
                            current = current3;
                            it2 = it3;
                        }
                    }
                }
            }
        }
        GLES20.glDisable(32823);
        GLES20.glCullFace(1029);
        GLES20.glBindFramebuffer(36160, 0);
    }

    private final void setupSkybox() {
        this.skyboxProg = program(SKYBOX_VS, SKYBOX_FS);
        this.kVP = setupSkybox$u$47(this, "uVP");
        this.kCam = setupSkybox$u$47(this, "uCam");
        this.kRot = setupSkybox$u$47(this, "uRot");
        this.kTex = setupSkybox$u$47(this, "uTex");
        this.kTint = setupSkybox$u$47(this, "uTint");
        this.kaPos = GLES20.glGetAttribLocation(this.skyboxProg, "aPos");
        this.kaUv = GLES20.glGetAttribLocation(this.skyboxProg, "aUv");
        int i = 6;
        Float fValueOf = Float.valueOf(-1.0f);
        Vector3 vector3 = new Vector3(0.0f, 0.0f, -1.0f);
        float sy = 1.0f;
        Float fValueOf2 = Float.valueOf(1.0f);
        char c = 1;
        char c2 = 2;
        char c3 = 3;
        char c4 = 5;
        List<Triple> faces = CollectionsKt.listOf(new Triple[]{new Triple(vector3, new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f)), new Triple(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(-1.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f)), new Triple(new Vector3(-1.0f, 0.0f, 0.0f), new Vector3(0.0f, 0.0f, -1.0f), new Vector3(0.0f, 1.0f, 0.0f)), new Triple(new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 1.0f, 0.0f)), new Triple(new Vector3(0.0f, 1.0f, 0.0f), new Vector3(0.0f, 0.0f, -1.0f), new Vector3(1.0f, 0.0f, 0.0f)), new Triple(new Vector3(0.0f, -1.0f, 0.0f), new Vector3(0.0f, 0.0f, -1.0f), new Vector3(-1.0f, 0.0f, 0.0f))});
        float[] out = new float[180];
        int o = 0;
        for (Triple triple : faces) {
            Vector3 n = (Vector3) triple.component1();
            char c5 = c4;
            Vector3 r = (Vector3) triple.component2();
            Vector3 up = (Vector3) triple.component3();
            char c6 = c;
            Pair[] pairArr = new Pair[i];
            pairArr[0] = TuplesKt.to(fValueOf, fValueOf);
            pairArr[c6] = TuplesKt.to(fValueOf2, fValueOf);
            pairArr[c2] = TuplesKt.to(fValueOf2, fValueOf2);
            pairArr[c3] = TuplesKt.to(fValueOf, fValueOf);
            pairArr[4] = TuplesKt.to(fValueOf2, fValueOf2);
            pairArr[c5] = TuplesKt.to(fValueOf, fValueOf2);
            for (Pair pair : CollectionsKt.listOf(pairArr)) {
                float sx = ((Number) pair.component1()).floatValue();
                float f = sy;
                float sy2 = ((Number) pair.component2()).floatValue();
                Vector3 p = n.plus(r.times(sx)).plus(up.times(sy2));
                int o2 = o + 1;
                out[o] = p.getX();
                int o3 = o2 + 1;
                out[o2] = p.getY();
                int o4 = o3 + 1;
                out[o3] = p.getZ();
                int o5 = o4 + 1;
                out[o4] = (sx + f) / 2.0f;
                out[o5] = (f - sy2) / 2.0f;
                o = o5 + 1;
                sy = f;
            }
            c = c6;
            c4 = c5;
            i = 6;
            c2 = 2;
            c3 = 3;
        }
        this.skyboxMesh = Meshes.upload$default(Meshes.INSTANCE, out, 5, 4, null, 8, null);
    }

    private static final int setupSkybox$u$47(SceneRenderer this$0, String n) {
        return GLES20.glGetUniformLocation(this$0.skyboxProg, n);
    }

    private final boolean drawSkybox() {
        Iterable children;
        Mesh mesh;
        Object next;
        boolean z;
        String string;
        Instance instanceFindService = this.studio.getModel().findService("Lighting");
        if (instanceFindService != null && (children = instanceFindService.getChildren()) != null) {
            Iterator it = children.iterator();
            do {
                mesh = null;
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!Intrinsics.areEqual(((Instance) next).getClassName(), "Sky"));
            Instance sky = (Instance) next;
            if (sky != null) {
                int[] tex = new int[6];
                int i = 0;
                while (true) {
                    if (i >= 6) {
                        int length = tex.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                z = false;
                                break;
                            }
                            if ((tex[i2] == 0 ? 1 : 0) != 0) {
                                z = true;
                                break;
                            }
                            i2++;
                        }
                        if (z) {
                            return false;
                        }
                        GLES20.glDisable(2929);
                        GLES20.glDepthMask(false);
                        GLES20.glDisable(2884);
                        GLES20.glUseProgram(this.skyboxProg);
                        GLES20.glUniformMatrix4fv(this.kVP, 1, false, this.studio.getView().getVp(), 0);
                        Vector3 cp = this.studio.getView().getCamPos();
                        GLES20.glUniform3f(this.kCam, cp.getX(), cp.getY(), cp.getZ());
                        Object obj = sky.get("SkyboxOrientation");
                        Vector3 o = obj instanceof Vector3 ? (Vector3) obj : null;
                        if (o == null) {
                            o = Vector3.INSTANCE.getZERO();
                        }
                        CFrame rot = Intrinsics.areEqual(o, Vector3.INSTANCE.getZERO()) ? CFrame.INSTANCE.getIDENTITY() : CFrame.INSTANCE.fromOrientation((float) Math.toRadians(o.getX()), (float) Math.toRadians(o.getY()), (float) Math.toRadians(o.getZ()));
                        float[] m = {rot.getR00(), rot.getR10(), rot.getR20(), rot.getR01(), rot.getR11(), rot.getR21(), rot.getR02(), rot.getR12(), rot.getR22()};
                        GLES20.glUniformMatrix3fv(this.kRot, 1, false, m, 0);
                        float k = (this.dayFactor * 0.84f) + 0.16f;
                        GLES20.glUniform3f(this.kTint, k, k, k);
                        GLES20.glUniform1i(this.kTex, 0);
                        GLES20.glActiveTexture(33984);
                        Mesh mesh2 = this.skyboxMesh;
                        if (mesh2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("skyboxMesh");
                        } else {
                            mesh = mesh2;
                        }
                        GLES20.glBindBuffer(34962, mesh.getVbo());
                        GLES20.glEnableVertexAttribArray(this.kaPos);
                        GLES20.glVertexAttribPointer(this.kaPos, 3, 5126, false, 20, 0);
                        GLES20.glEnableVertexAttribArray(this.kaUv);
                        GLES20.glVertexAttribPointer(this.kaUv, 2, 5126, false, 20, 12);
                        for (int i3 = 0; i3 < 6; i3++) {
                            GLES20.glBindTexture(3553, tex[i3]);
                            GLES20.glDrawArrays(4, i3 * 6, 6);
                        }
                        GLES20.glDisableVertexAttribArray(this.kaUv);
                        GLES20.glBindTexture(3553, this.shadowTex);
                        GLES20.glEnable(2884);
                        GLES20.glDepthMask(true);
                        GLES20.glEnable(2929);
                        return true;
                    }
                    Object obj2 = sky.get(this.SKY_FACES[i]);
                    String content = (obj2 == null || (string = obj2.toString()) == null) ? null : StringsKt.trim((CharSequence) string).toString();
                    if (content == null) {
                        content = "";
                    }
                    if ((content.length() == 0) || AssetFetcher.INSTANCE.idOf(content) == null) {
                        return false;
                    }
                    tex[i] = assetTexture(content, false);
                    i++;
                }
            }
        }
        return false;
    }

    private final void drawSky() {
        if (drawSkybox()) {
            return;
        }
        GLES20.glDisable(2929);
        GLES20.glDepthMask(false);
        GLES20.glUseProgram(this.skyProg);
        GLES20.glUniformMatrix4fv(this.sInvVP, 1, false, this.studio.getView().getInvVp(), 0);
        Vector3 cp = this.studio.getView().getCamPos();
        GLES20.glUniform3f(this.sCam, cp.getX(), cp.getY(), cp.getZ());
        GLES20.glUniform3f(this.sSun, this.sunDir.getX(), this.sunDir.getY(), this.sunDir.getZ());
        GLES20.glUniform3fv(this.sZenith, 1, this.zenith, 0);
        GLES20.glUniform3fv(this.sHorizon, 1, this.horizon, 0);
        GLES20.glUniform3f(this.sSunColor, 1.0f, 0.95f, 0.85f);
        GLES20.glUniform1f(this.sTime, this.time);
        GLES20.glUniform1f(this.sDay, this.dayFactor);
        GLES20.glUniform2f(this.sSunRays, Math.max(this.sunRaysInt, this.atmoGlare * 0.75f), this.sunRaysInt > 0.001f ? this.sunRaysSpread : 0.55f);
        GLES20.glUniform4f(this.sCCParams, this.ccBright, this.ccContrast, this.ccSat, (this.ccBright != 0.0f || this.ccContrast != 0.0f || this.ccSat != 0.0f || this.ccTint[0] < 0.999f || this.ccTint[1] < 0.999f || this.ccTint[2] < 0.999f) ? 1.0f : 0.0f);
        GLES20.glUniform3fv(this.sCCTint, 1, this.ccTint, 0);
        Mesh mesh = this.screen;
        Mesh mesh2 = null;
        if (mesh == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screen");
            mesh = null;
        }
        GLES20.glBindBuffer(34962, mesh.getVbo());
        GLES20.glEnableVertexAttribArray(this.saPos);
        GLES20.glVertexAttribPointer(this.saPos, 2, 5126, false, 8, 0);
        Mesh mesh3 = this.screen;
        if (mesh3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screen");
        } else {
            mesh2 = mesh3;
        }
        GLES20.glDrawArrays(4, 0, mesh2.getCount());
        GLES20.glDepthMask(true);
        GLES20.glEnable(2929);
    }

    private final float[] material(int m) {
        float[] fArrMaterialParams;
        Map map = this.matCache;
        Integer numValueOf = Integer.valueOf(m);
        float[] fArr = (float[]) map.get(numValueOf);
        if (fArr == null) {
            fArrMaterialParams = materialParams(m);
            map.put(numValueOf, fArrMaterialParams);
        } else {
            fArrMaterialParams = fArr;
        }
        return fArrMaterialParams;
    }

    private final float[] materialParams(int m) {
        switch (m) {
            case ClothingCompositor.TORSO_H /* 272 */:
                return new float[]{0.0f, 0.0f, 1.0f, 0.22f};
            case 288:
                return new float[]{0.0f, 0.0f, 1.0f, 0.0f};
            case 512:
                return new float[]{2.0f, 0.14f, 1.0f, 0.06f};
            case 528:
                return new float[]{3.0f, 0.12f, 1.0f, 0.06f};
            case 784:
                return new float[]{11.0f, 0.1f, 0.25f, 0.45f};
            case 788:
                return new float[]{13.0f, 0.16f, 0.9f, 0.08f};
            case 800:
                return new float[]{12.0f, 0.16f, 0.35f, 0.12f};
            case 804:
                return new float[]{14.0f, 0.22f, 0.7f, 0.1f};
            case 816:
                return new float[]{23.0f, 0.1f, 1.4f, 0.04f};
            case 820:
                return new float[]{5.0f, 0.09f, 0.5f, 0.05f};
            case 832:
                return new float[]{24.0f, 0.13f, 2.5f, 0.2f};
            case 836:
                return new float[]{16.0f, 0.08f, 4.0f, 0.04f};
            case 848:
                return new float[]{4.0f, 0.12f, 1.0f, 0.04f};
            case 864:
                return new float[]{13.0f, 0.2f, 2.2f, 0.12f};
            case 880:
                return new float[]{13.0f, 0.24f, 0.8f, 0.08f};
            case 896:
                return new float[]{5.0f, 0.17f, 0.4f, 0.08f};
            case 912:
                return new float[]{12.0f, 0.1f, 0.8f, 0.05f};
            case 1040:
                return new float[]{15.0f, 0.2f, 0.6f, 0.3f};
            case 1056:
                return new float[]{6.0f, 0.14f, 1.0f, 0.7f};
            case 1072:
                return new float[]{7.0f, 0.07f, 2.0f, 0.9f};
            case 1088:
                return new float[]{7.0f, 0.06f, 1.5f, 0.7f};
            case 1280:
                return new float[]{8.0f, 0.16f, 1.5f, 0.04f};
            case 1284:
                return new float[]{8.0f, 0.2f, 1.2f, 0.04f};
            case 1296:
                return new float[]{25.0f, 0.08f, 3.0f, 0.04f};
            case 1312:
                return new float[]{9.0f, 0.08f, 4.0f, 0.02f};
            case 1328:
                return new float[]{25.0f, 0.05f, 1.0f, 0.3f};
            case 1344:
                return new float[]{5.0f, 0.14f, 0.6f, 0.15f};
            case 1360:
                return new float[]{5.0f, 0.15f, 0.8f, 0.03f};
            case 1376:
                return new float[]{22.0f, 0.09f, 4.0f, 0.05f};
            case 1392:
                return new float[]{25.0f, 0.07f, 2.0f, 0.15f};
            case 1536:
                return new float[]{10.0f, 0.05f, 0.3f, 0.9f};
            case 1552:
                return new float[]{10.0f, 0.08f, 0.3f, 0.8f};
            case 1568:
                return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
            case 1584:
                return new float[]{19.0f, 0.0f, 1.0f, 0.0f};
            case 2304:
                return new float[]{18.0f, 0.06f, 1.0f, 0.03f};
            case 2305:
                return new float[]{21.0f, 0.12f, 1.0f, 0.01f};
            case 2306:
                return new float[]{16.0f, 0.06f, 2.0f, 0.5f};
            case 2307:
                return new float[]{17.0f, 0.12f, 1.0f, 0.1f};
            case 2308:
                return new float[]{17.0f, 0.16f, 0.7f, 0.05f};
            case 2309:
                return new float[]{20.0f, 0.1f, 1.0f, 0.15f};
            case 2310:
                return new float[]{5.0f, 0.05f, 0.4f, 0.04f};
            case 2311:
                return new float[]{1.0f, 0.02f, 3.0f, 0.08f};
            default:
                return new float[]{0.0f, 0.0f, 1.0f, 0.28f};
        }
    }

    private final float surf(Instance p, String name) {
        Object obj = p.get(name);
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        return enumValue != null ? enumValue.getValue() : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: SceneRenderer.kt */
    static final class Draw {
        private final float dist;
        private final Instance part;

        public Draw(Instance part, float dist) {
            Intrinsics.checkNotNullParameter(part, "part");
            this.part = part;
            this.dist = dist;
        }

        public final float getDist() {
            return this.dist;
        }

        public final Instance getPart() {
            return this.part;
        }
    }

    private final void switchLit(LitLocs l) {
        if (this.activeLit == l) {
            return;
        }
        useLit(l);
        setLitFrameUniforms(this.frameShadows);
    }

    /* JADX WARN: Code duplicated, block: B:123:0x040c  */
    /* JADX WARN: Code duplicated, block: B:130:0x0446  */
    /* JADX WARN: Code duplicated, block: B:133:0x0458  */
    /* JADX WARN: Code duplicated, block: B:134:0x045b  */
    /* JADX WARN: Code duplicated, block: B:137:0x045f  */
    /* JADX WARN: Code duplicated, block: B:138:0x0464 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:139:0x0466  */
    /* JADX WARN: Code duplicated, block: B:140:0x046c  */
    /* JADX WARN: Code duplicated, block: B:143:0x0473  */
    /* JADX WARN: Code duplicated, block: B:144:0x0478  */
    /* JADX WARN: Code duplicated, block: B:146:0x047c  */
    /* JADX WARN: Code duplicated, block: B:149:0x0489  */
    /* JADX WARN: Code duplicated, block: B:150:0x048c  */
    /* JADX WARN: Code duplicated, block: B:185:0x042f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:186:0x0422 A[SYNTHETIC] */
    private final void drawParts(boolean shadows) {
        String str;
        String str2;
        String str3;
        String str4;
        boolean negate;
        Geo g;
        TexBind texBindTexturesFor;
        TexBind tb;
        LitLocs litLocs;
        LitLocs adv;
        LitLocs want;
        Mesh mesh;
        Object current;
        Mesh mesh2;
        Color3 color3;
        float f = 0.0f;
        Mesh mesh3;
        TexBind bound = null;
        Mesh meshDrawPart$default;
        PlaySession play;
        Mesh mesh4;
        TexBind texBindTexturesFor2;
        ArrayList<Instance> loose = this.batches.getLoose();
        this.frames++;
        this.frameShadows = shadows;
        LitLocs litLocs2 = this.litBasic;
        if (litLocs2 == null) {
            return;
        }
        useLit(litLocs2);
        setLitFrameUniforms(shadows);
        Vector3 cp = this.studio.getView().getCamPos();
        ArrayList transparent = new ArrayList();
        ArrayList advanced = new ArrayList();
        ArrayList textured = new ArrayList();
        ArrayList fileMeshes = new ArrayList();
        PlaySession play2 = this.studio.getPlay();
        if (this.meshEpoch != MeshLoader.INSTANCE.getEpoch()) {
            this.meshEpoch = MeshLoader.INSTANCE.getEpoch();
        }
        refreshVariants();
        LitLocs texProg = texturedLit();
        Iterator<StaticBatches.Chunk> it = this.batches.getVisible().iterator();
        String str5 = "iterator(...)";
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (true) {
            str = "next(...)";
            if (!it.hasNext()) {
                break;
            }
            StaticBatches.Chunk next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            StaticBatches.Chunk c = next;
            if (c.getDist() < this.closest && this.frustum.box(c.getX0(), c.getY0(), c.getZ0(), c.getX1(), c.getY1(), c.getZ1())) {
                this.closest = c.getDist();
            }
        }
        long m0 = System.nanoTime();
        drawStaticBatches();
        long m1 = System.nanoTime();
        LitLocs texProg2 = texProg;
        this.tB += (m1 - m0) / 1000000.0f;
        LitLocs litLocs3 = this.litBasic;
        if (litLocs3 == null) {
            return;
        }
        useLit(litLocs3);
        setLitFrameUniforms(shadows);
        Iterator<Instance> it2 = loose.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        Mesh meshDrawPart$default2 = null;
        while (true) {
            str2 = str5;
            str3 = "NegateOperation";
            boolean z = false;
            if (!it2.hasNext()) {
                break;
            }
            Instance p = it2.next();
            float t = PartData.INSTANCE.transparency(p);
            if (t >= 0.999f) {
                play = play2;
                mesh4 = meshDrawPart$default2;
            } else {
                if (play2 != null) {
                    play = play2;
                    if (play2.hiddenInFirstPerson(p)) {
                        z = true;
                    }
                } else {
                    play = play2;
                }
                if (z) {
                    mesh4 = meshDrawPart$default2;
                } else {
                    CFrame cf = PartData.INSTANCE.cframe(p);
                    Vector3 size = PartData.INSTANCE.size(p);
                    if (inView(cf, size, cp)) {
                        int mat = PartData.INSTANCE.material(p);
                        if (t <= 0.01f) {
                            mesh4 = meshDrawPart$default2;
                            if (mat != 1568 && mat != 1584) {
                                if (!Intrinsics.areEqual(p.getClassName(), "NegateOperation")) {
                                    Geo g2 = geometry(p);
                                    if (g2.getKind() == 2) {
                                        fileMeshes.add(TuplesKt.to(p, g2));
                                    } else if (texProg2 != null && (texBindTexturesFor2 = texturesFor(p, mat)) != null) {
                                        textured.add(new Triple(p, g2, texBindTexturesFor2));
                                    } else if (needsAdvanced(mat)) {
                                        advanced.add(p);
                                    } else {
                                        meshDrawPart$default2 = drawPart$default(this, p, 1.0f, mesh4, g2, null, 16, null);
                                        str5 = str2;
                                        play2 = play;
                                        continue;
                                    }
                                    str5 = str2;
                                    play2 = play;
                                    meshDrawPart$default2 = mesh4;
                                    continue;
                                }
                            }
                        } else {
                            mesh4 = meshDrawPart$default2;
                        }
                        transparent.add(new Draw(p, cf.getPosition().minus(cp).getMagnitude()));
                    } else {
                        mesh4 = meshDrawPart$default2;
                    }
                }
            }
            str5 = str2;
            play2 = play;
            meshDrawPart$default2 = mesh4;
        }
        Mesh mesh5 = meshDrawPart$default2;
        String str6 = str;
        char c2 = 2;
        LitLocs texProg3 = texProg2;
        if (advanced.isEmpty()) {
            str4 = "NegateOperation";
        } else {
            LitLocs litLocsAdvancedLit = advancedLit();
            if (litLocsAdvancedLit != null) {
                switchLit(litLocsAdvancedLit);
                meshDrawPart$default = null;
            } else {
                meshDrawPart$default = mesh5;
            }
            Iterator it3 = advanced.iterator();
            Intrinsics.checkNotNullExpressionValue(it3, str2);
            while (it3.hasNext()) {
                Instance p2 = (Instance) it3.next();
                meshDrawPart$default = drawPart$default(this, p2, 1.0f, meshDrawPart$default, null, null, 24, null);
                c2 = 2;
            }
            str4 = str3;
        }
        if (!textured.isEmpty() && texProg3 != null) {
            switchLit(texProg3);
            Mesh meshDrawPart$default3 = null;
            ArrayList arrayList = textured;
            if (arrayList.size() > 1) {
                CollectionsKt.sortWith(arrayList, new Comparator() { // from class: com.goydevv.studrblx.render.SceneRenderer$drawParts$$inlined$sortBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t2, Object t3) {
                        return ComparisonsKt.compareValues(Integer.valueOf(((TexBind) ((Triple) t2).getThird()).getColor()), Integer.valueOf(((TexBind) ((Triple) t3).getThird()).getColor()));
                    }
                });
            }
            TexBind bound2 = null;
            Iterator it4 = textured.iterator();
            Intrinsics.checkNotNullExpressionValue(it4, str2);
            while (it4.hasNext()) {
                Object next2 = it4.next();
                Intrinsics.checkNotNullExpressionValue(next2, str6);
                Triple triple = (Triple) next2;
                Instance p3 = (Instance) triple.component1();
                Geo g3 = (Geo) triple.component2();
                TexBind tb2 = (TexBind) triple.component3();
                mesh3 = meshDrawPart$default3;
                if (bound2 == null || bound2.getColor() != tb2.getColor() || bound2.getNormal() != tb2.getNormal() || bound2.getScale() != tb2.getScale()) {
                    bindTextures(tb2);
                    bound2 = tb2;
                }
                meshDrawPart$default3 = drawPart$default(this, p3, 1.0f, mesh3, g3, null, 16, null);
            }
        }
        float f2 = 1.0f;
        LitLocs adv2 = null;
        if (!fileMeshes.isEmpty()) {
            Iterator it5 = fileMeshes.iterator();
            Intrinsics.checkNotNullExpressionValue(it5, str2);
            while (it5.hasNext()) {
                Object next3 = it5.next();
                Intrinsics.checkNotNullExpressionValue(next3, str6);
                Pair pair = (Pair) next3;
                Instance p4 = (Instance) pair.component1();
                drawFileMesh(p4, (Geo) pair.component2(), 1.0f);
            }
            this.activeLit = null;
            LitLocs litLocs4 = this.litBasic;
            if (litLocs4 == null) {
                return;
            }
            useLit(litLocs4);
            setLitFrameUniforms(shadows);
        }
        long m2 = System.nanoTime();
        this.tL += (m2 - m1) / 1000000.0f;
        drawDecals();
        drawConstraints();
        drawBeams();
        drawTrails();
        drawParticles();
        drawAdornments();
        long m3 = System.nanoTime();
        this.tDec += (m3 - m2) / 1000000.0f;
        this.activeLit = null;
        Mesh meshDrawPart = null;
        if (!transparent.isEmpty()) {
            ArrayList arrayList2 = transparent;
            if (arrayList2.size() > 1) {
                CollectionsKt.sortWith(arrayList2, new Comparator() { // from class: com.goydevv.studrblx.render.SceneRenderer$drawParts$$inlined$sortByDescending$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t2, Object t3) {
                        return ComparisonsKt.compareValues(Float.valueOf(((SceneRenderer.Draw) t3).getDist()), Float.valueOf(((SceneRenderer.Draw) t2).getDist()));
                    }
                });
            }
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 771);
            GLES20.glDepthMask(false);
            Iterator it6 = transparent.iterator();
            Intrinsics.checkNotNullExpressionValue(it6, str2);
            while (it6.hasNext()) {
                Object next4 = it6.next();
                Intrinsics.checkNotNullExpressionValue(next4, str6);
                Draw d = (Draw) next4;
                float a = f2 - PartData.INSTANCE.transparency(d.getPart());
                switch (PartData.INSTANCE.material(d.getPart())) {
                    case 1568:
                        f = 0.45f;
                        break;
                    case 1584:
                        f = 0.5f;
                        break;
                    default:
                        negate = Intrinsics.areEqual(d.getPart().getClassName(), str4);
                        if (negate) {
                            a = Math.min(a, 0.42f);
                        }
                        g = geometry(d.getPart());
                        if (g.getKind() == 2) {
                            drawFileMesh(d.getPart(), g, a);
                            this.activeLit = adv2;
                            meshDrawPart = null;
                            f2 = 1.0f;
                        } else {
                            if (texProg3 == null || negate) {
                                texBindTexturesFor = null;
                            } else {
                                texBindTexturesFor = texturesFor(d.getPart(), PartData.INSTANCE.material(d.getPart()));
                            }
                            tb = texBindTexturesFor;
                            if (needsAdvanced(PartData.INSTANCE.material(d.getPart()))) {
                                litLocs = this.litAdv;
                            } else {
                                litLocs = null;
                            }
                            adv = litLocs;
                            if (tb != null) {
                                Intrinsics.checkNotNull(texProg3);
                                want = texProg3;
                            } else if (adv == null) {
                                want = this.litBasic;
                                Intrinsics.checkNotNull(want);
                            } else {
                                want = adv;
                            }
                            mesh = meshDrawPart;
                            current = this.activeLit;
                            if (current != want) {
                                switchLit(want);
                                mesh2 = null;
                            } else {
                                mesh2 = mesh;
                            }
                            if (tb != null) {
                                bindTextures(tb);
                            }
                            Iterator it7 = it6;
                            float a2 = a;
                            Mesh mesh6 = mesh2;
                            Instance part = d.getPart();
                            if (negate) {
                                color3 = NEGATE_COLOR;
                            } else {
                                color3 = null;
                            }
                            meshDrawPart = drawPart(part, a2, mesh6, g, color3);
                            it6 = it7;
                            f2 = 1.0f;
                            adv2 = null;
                        }
                        break;
                }
                a = Math.min(a, f);
                negate = Intrinsics.areEqual(d.getPart().getClassName(), str4);
                if (negate) {
                    a = Math.min(a, 0.42f);
                }
                g = geometry(d.getPart());
                if (g.getKind() == 2) {
                    drawFileMesh(d.getPart(), g, a);
                    this.activeLit = adv2;
                    meshDrawPart = null;
                    f2 = 1.0f;
                } else {
                    if (texProg3 != null) {
                        texBindTexturesFor = null;
                    } else {
                        texBindTexturesFor = null;
                    }
                    tb = texBindTexturesFor;
                    if (needsAdvanced(PartData.INSTANCE.material(d.getPart()))) {
                        litLocs = this.litAdv;
                    } else {
                        litLocs = null;
                    }
                    adv = litLocs;
                    if (tb != null) {
                        Intrinsics.checkNotNull(texProg3);
                        want = texProg3;
                    } else if (adv == null) {
                        want = this.litBasic;
                        Intrinsics.checkNotNull(want);
                    } else {
                        want = adv;
                    }
                    mesh = meshDrawPart;
                    current = this.activeLit;
                    if (current != want) {
                        switchLit(want);
                        mesh2 = null;
                    } else {
                        mesh2 = mesh;
                    }
                    if (tb != null) {
                        bindTextures(tb);
                    }
                    Iterator it8 = it6;
                    float a3 = a;
                    Mesh mesh7 = mesh2;
                    Instance part2 = d.getPart();
                    if (negate) {
                        color3 = NEGATE_COLOR;
                    } else {
                        color3 = null;
                    }
                    meshDrawPart = drawPart(part2, a3, mesh7, g, color3);
                    it6 = it8;
                    f2 = 1.0f;
                    adv2 = null;
                }
            }
            GLES20.glDepthMask(true);
            GLES20.glDisable(3042);
        }
        this.tTr += (System.nanoTime() - m3) / 1000000.0f;
        this.nTr += transparent.size();
        this.nLoose += textured.size() + advanced.size() + fileMeshes.size();
        LitLocs litLocs5 = this.litBasic;
        if (litLocs5 != null) {
            useLit(litLocs5);
        }
    }

    private final void setLitFrameUniforms(boolean shadows) {
        GLES20.glUniformMatrix4fv(this.uVP, 1, false, this.studio.getView().getVp(), 0);
        GLES20.glUniformMatrix4fv(this.uShadowVP, 1, false, this.lightVP, 0);
        GLES20.glUniform3f(this.uLight, this.sunDir.getX(), this.sunDir.getY(), this.sunDir.getZ());
        Vector3 cp = this.studio.getView().getCamPos();
        GLES20.glUniform3f(this.uCam, cp.getX(), cp.getY(), cp.getZ());
        GLES20.glUniform3fv(this.uHorizon, 1, this.horizon, 0);
        GLES20.glUniform3fv(this.uZenith, 1, this.zenith, 0);
        GLES20.glUniform3fv(this.uSun, 1, this.sunColor, 0);
        GLES20.glUniform1f(this.uAmbient, this.ambient);
        GLES20.glUniform3fv(this.uFogColor, 1, this.fogColor, 0);
        GLES20.glUniform3f(this.uFog, this.fogStart, this.fogEnd, this.fogMode);
        GLES20.glUniform1f(this.uExposure, this.exposure);
        GLES20.glUniform1f(this.uShadowOn, shadows ? 1.0f : 0.0f);
        GLES20.glUniform2f(this.uShadowTexel, 1.0f / this.shadowSize, 1.0f / this.shadowSize);
        GLES20.glUniform1f(this.uShadowSoft, this.shadowSoftness);
        GLES20.glUniform3fv(this.uShiftBot, 1, this.shiftBottom, 0);
        GLES20.glUniform1f(this.uEnvSpec, this.envSpec);
        GLES20.glUniform4f(this.uCCParams, this.ccBright, this.ccContrast, this.ccSat, (this.ccBright != 0.0f || this.ccContrast != 0.0f || this.ccSat != 0.0f || this.ccTint[0] < 0.999f || this.ccTint[1] < 0.999f || this.ccTint[2] < 0.999f) ? 1.0f : 0.0f);
        GLES20.glUniform3fv(this.uCCTint, 1, this.ccTint, 0);
        GLES20.glUniform4f(this.uBloom, this.bloomInt, this.bloomThresh, this.bloomSize, 0.0f);
        GLES20.glUniform4f(this.uDof, this.dofFocus, this.dofRadius, Math.max(this.dofFar, this.blurAmt), Math.max(this.dofNear, this.blurAmt));
        GLES20.glUniform3fv(this.uAmbRgb, 1, this.ambRgb, 0);
        GLES20.glUniform1i(this.uPtCount, this.ptCount);
        if (this.ptCount > 0) {
            GLES20.glUniform4fv(this.uPtPos, this.ptCount, this.ptPos, 0);
            GLES20.glUniform4fv(this.uPtCol, this.ptCount, this.ptCol, 0);
            GLES20.glUniform4fv(this.uPtDir, this.ptCount, this.ptDir, 0);
        }
        GLES20.glUniform1f(this.uTime, this.time);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.shadowTex);
        GLES20.glUniform1i(this.uShadow, 0);
        GLES20.glUniform1i(this.uMatTex, 1);
        GLES20.glUniform1i(this.uMatNrm, 2);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.surfTex);
        GLES20.glActiveTexture(33984);
        GLES20.glUniform1i(this.uSurfTex, 3);
        GLES20.glUniform1f(this.uHasSurfTex, this.surfTex != 0 ? 1.0f : 0.0f);
    }

    static /* synthetic */ Mesh drawPart$default(SceneRenderer sceneRenderer, Instance instance, float f, Mesh mesh, Geo geo, Color3 color3, int i, Object obj) {
        if ((i & 8) != 0) {
            geo = sceneRenderer.geometry(instance);
        }
        Geo geo2 = geo;
        if ((i & 16) != 0) {
            color3 = null;
        }
        return sceneRenderer.drawPart(instance, f, mesh, geo2, color3);
    }

    /* JADX WARN: Code duplicated, block: B:35:0x0124  */
    private final Mesh drawPart(Instance p, float alpha, Mesh current, Geo g, Color3 colorOverride) {
        float f;
        boolean truss;
        SceneRenderer sceneRenderer = this;
        if (g.getMesh() != current) {
            sceneRenderer.bind(g.getMesh(), sceneRenderer.aPos, sceneRenderer.aNrm);
        }
        sceneRenderer.setModel(g.getCf(), g.getScale());
        GLES20.glUniformMatrix4fv(sceneRenderer.uModel, 1, false, sceneRenderer.model, 0);
        GLES20.glUniformMatrix3fv(sceneRenderer.uNrm, 1, false, sceneRenderer.normal, 0);
        Color3 c = colorOverride == null ? PartData.INSTANCE.color(p) : colorOverride;
        GLES20.glUniform4f(sceneRenderer.uColor, c.getR(), c.getG(), c.getB(), alpha);
        int mat = PartData.INSTANCE.material(p);
        int i = sceneRenderer.uEmissive;
        switch (mat) {
            case 288:
            case 1584:
                f = 1.0f;
                break;
            default:
                f = 0.0f;
                break;
        }
        GLES20.glUniform1f(i, f);
        float[] m = sceneRenderer.material(mat);
        int i2 = sceneRenderer.uMat;
        float f2 = m[0];
        float f3 = m[1];
        float f4 = m[2];
        float f5 = m[3];
        Object obj = p.get("Reflectance");
        Number number = obj instanceof Number ? (Number) obj : null;
        GLES20.glUniform4f(i2, f2, f3, f4, f5 + RangesKt.coerceIn(number != null ? number.floatValue() : 0.0f, 0.0f, 1.0f));
        GLES20.glUniform1f(sceneRenderer.uGrid, 0.0f);
        GLES20.glUniform3f(sceneRenderer.uSize, g.getScale().getX(), g.getScale().getY(), g.getScale().getZ());
        Mesh mesh = g.getMesh();
        Mesh mesh2 = sceneRenderer.cube;
        if (mesh2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("cube");
            mesh2 = null;
        }
        if (mesh == mesh2 && g.getKind() == 0) {
            GLES20.glUniform3f(sceneRenderer.uSurfA, sceneRenderer.surf(p, "RightSurface"), sceneRenderer.surf(p, "TopSurface"), sceneRenderer.surf(p, "BackSurface"));
            GLES20.glUniform3f(sceneRenderer.uSurfB, sceneRenderer.surf(p, "LeftSurface"), sceneRenderer.surf(p, "BottomSurface"), sceneRenderer.surf(p, "FrontSurface"));
        } else {
            GLES20.glUniform3f(sceneRenderer.uSurfA, 0.0f, 0.0f, 0.0f);
            GLES20.glUniform3f(sceneRenderer.uSurfB, 0.0f, 0.0f, 0.0f);
        }
        if (Intrinsics.areEqual(p.getClassName(), "TrussPart")) {
            Mesh mesh3 = g.getMesh();
            Mesh mesh4 = sceneRenderer.cube;
            if (mesh4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("cube");
                mesh4 = null;
            }
            if (mesh3 == mesh4) {
                truss = true;
            } else {
                truss = false;
            }
        } else {
            truss = false;
        }
        GLES20.glUniform1f(sceneRenderer.uTruss, truss ? 1.0f : 0.0f);
        int i3 = 2884;
        if (truss) {
            GLES20.glDisable(2884);
        }
        List<Pair<Mesh, Color3>> subs = g.getSubs();
        if (subs != null) {
            Mesh last = g.getMesh();
            for (Pair<Mesh, Color3> pair : subs) {
                Mesh sm = pair.component1();
                Color3 col = pair.component2();
                if (sm != last) {
                    sceneRenderer.bind(sm, sceneRenderer.aPos, sceneRenderer.aNrm);
                    last = sm;
                }
                Color3 cc = colorOverride == null ? col : colorOverride;
                GLES20.glUniform4f(sceneRenderer.uColor, cc.getR(), cc.getG(), cc.getB(), alpha);
                GLES20.glDrawArrays(sm.getMode(), 0, sm.getCount());
                sceneRenderer = this;
            }
            int i4 = i3;
            if (truss) {
                GLES20.glEnable(i4);
            }
            return last;
        }
        GLES20.glDrawArrays(g.getMesh().getMode(), 0, g.getMesh().getCount());
        if (truss) {
            GLES20.glEnable(2884);
        }
        return g.getMesh();
    }

    private final void setupMeshProgram() {
        this.meshProg = program(MESH_VS, MESH_FS);
        this.mVP = setupMeshProgram$u$56(this, "uVP");
        this.mModel = setupMeshProgram$u$56(this, "uModel");
        this.mNrm = setupMeshProgram$u$56(this, "uNrm");
        this.mColor = setupMeshProgram$u$56(this, "uColor");
        this.mLight = setupMeshProgram$u$56(this, "uLight");
        this.mSun = setupMeshProgram$u$56(this, "uSun");
        this.mAmbient = setupMeshProgram$u$56(this, "uAmbient");
        this.mTex = setupMeshProgram$u$56(this, "uTex");
        this.mHasTex = setupMeshProgram$u$56(this, "uHasTex");
        this.mShadow = setupMeshProgram$u$56(this, "uShadow");
        this.mShadowVP = setupMeshProgram$u$56(this, "uShadowVP");
        this.mShadowOn = setupMeshProgram$u$56(this, "uShadowOn");
        this.mShadowTexel = setupMeshProgram$u$56(this, "uShadowTexel");
        this.mShadowSoft = setupMeshProgram$u$56(this, "uShadowSoft");
        this.mShiftBot = setupMeshProgram$u$56(this, "uShiftBot");
        this.mEnvSpec = setupMeshProgram$u$56(this, "uEnvSpec");
        this.mPtCount = setupMeshProgram$u$56(this, "uPtCount");
        this.mPtPos = setupMeshProgram$u$56(this, "uPtPos");
        this.mPtCol = setupMeshProgram$u$56(this, "uPtCol");
        this.mPtDir = setupMeshProgram$u$56(this, "uPtDir");
        this.mCCParams = setupMeshProgram$u$56(this, "uCCParams");
        this.mCCTint = setupMeshProgram$u$56(this, "uCCTint");
        this.mBloom = setupMeshProgram$u$56(this, "uBloom");
        this.mDof = setupMeshProgram$u$56(this, "uDof");
        this.mAmbRgb = setupMeshProgram$u$56(this, "uAmbRgb");
        this.mPbrNrm = setupMeshProgram$u$56(this, "uPbrNrm");
        this.mPbrRough = setupMeshProgram$u$56(this, "uPbrRough");
        this.mPbrMetal = setupMeshProgram$u$56(this, "uPbrMetal");
        this.mPbrFlags = setupMeshProgram$u$56(this, "uPbrFlags");
        this.mPbrEmis = setupMeshProgram$u$56(this, "uPbrEmis");
        this.mCam = setupMeshProgram$u$56(this, "uCam");
        this.mFogColor = setupMeshProgram$u$56(this, "uFogColor");
        this.mFog = setupMeshProgram$u$56(this, "uFog");
        this.mExposure = setupMeshProgram$u$56(this, "uExposure");
        this.mTint = setupMeshProgram$u$56(this, "uTint");
        this.mEmissive = setupMeshProgram$u$56(this, "uEmissive");
        this.mHorizon = setupMeshProgram$u$56(this, "uHorizon");
        this.mZenith = setupMeshProgram$u$56(this, "uZenith");
        this.maPos = GLES20.glGetAttribLocation(this.meshProg, "aPos");
        this.maNrm = GLES20.glGetAttribLocation(this.meshProg, "aNrm");
        this.maUv = GLES20.glGetAttribLocation(this.meshProg, "aUv");
    }

    private static final int setupMeshProgram$u$56(SceneRenderer this$0, String n) {
        return GLES20.glGetUniformLocation(this$0.meshProg, n);
    }

    private final int assetTexture(String content, boolean repeat) {
        String str;
        if (content != null && StringsKt.startsWith(content, Avatar.TEX_PREFIX, false)) {
            Integer num = this.textures.get(content);
            if (num != null) {
                return num.intValue();
            }
            Bitmap bmp = Avatar.INSTANCE.texture(content);
            if (bmp == null) {
                return 0;
            }
            int iUpload = upload(bmp, repeat);
            this.textures.put(content, Integer.valueOf(iUpload));
            return iUpload;
        }
        Long lIdOf = AssetFetcher.INSTANCE.idOf(content);
        if (lIdOf == null) {
            return textureFor(content, repeat);
        }
        long id = lIdOf.longValue();
        Map map = repeat ? this.rawKeysR : this.rawKeys;
        Long lValueOf = Long.valueOf(id);
        String str2 = (String) map.get(lValueOf);
        if (str2 == null) {
            str = "raw:" + id + (repeat ? "#r" : "");
            map.put(lValueOf, str);
        } else {
            str = str2;
        }
        String key = str;
        Integer num2 = this.textures.get(key);
        if (num2 != null) {
            return num2.intValue();
        }
        if (canDownload()) {
            Bitmap bmp2 = AssetFetcher.INSTANCE.image(id);
            if (bmp2 != null) {
                int t = upload(bmp2, repeat);
                this.textures.put(key, Integer.valueOf(t));
                return t;
            }
            if (AssetFetcher.INSTANCE.imageFailed(id)) {
                return textureFor(content, repeat);
            }
            return 0;
        }
        return textureFor(content, repeat);
    }

    private final boolean canDownload() {
        return true;
    }

    private final void drawFileMesh(Instance p, Geo g, float alpha) {
        GLES20.glUseProgram(this.meshProg);
        this.activeLit = null;
        if (this.meshFrame != this.frames) {
            this.meshFrame = this.frames;
            GLES20.glUniformMatrix4fv(this.mVP, 1, false, this.studio.getView().getVp(), 0);
            GLES20.glUniformMatrix4fv(this.mShadowVP, 1, false, this.lightVP, 0);
            GLES20.glUniform3f(this.mLight, this.sunDir.getX(), this.sunDir.getY(), this.sunDir.getZ());
            GLES20.glUniform3fv(this.mSun, 1, this.sunColor, 0);
            GLES20.glUniform1f(this.mAmbient, this.ambient);
            Vector3 cp = this.studio.getView().getCamPos();
            GLES20.glUniform3f(this.mCam, cp.getX(), cp.getY(), cp.getZ());
            GLES20.glUniform3fv(this.mFogColor, 1, this.fogColor, 0);
            GLES20.glUniform3f(this.mFog, this.fogStart, this.fogEnd, this.fogMode);
            GLES20.glUniform1f(this.mExposure, this.exposure);
            GLES20.glUniform1f(this.mShadowOn, this.frameShadows ? 1.0f : 0.0f);
            GLES20.glUniform2f(this.mShadowTexel, 1.0f / this.shadowSize, 1.0f / this.shadowSize);
            GLES20.glUniform1f(this.mShadowSoft, this.shadowSoftness);
            GLES20.glUniform3fv(this.mShiftBot, 1, this.shiftBottom, 0);
            GLES20.glUniform1f(this.mEnvSpec, this.envSpec);
            GLES20.glUniform1i(this.mPtCount, this.ptCount);
            if (this.ptCount > 0) {
                GLES20.glUniform4fv(this.mPtPos, this.ptCount, this.ptPos, 0);
                GLES20.glUniform4fv(this.mPtCol, this.ptCount, this.ptCol, 0);
                GLES20.glUniform4fv(this.mPtDir, this.ptCount, this.ptDir, 0);
            }
            GLES20.glUniform3fv(this.mHorizon, 1, this.horizon, 0);
            GLES20.glUniform3fv(this.mZenith, 1, this.zenith, 0);
            GLES20.glUniform1i(this.mShadow, 0);
            GLES20.glUniform1i(this.mTex, 1);
            GLES20.glUniform1i(this.mPbrNrm, 2);
            GLES20.glUniform1i(this.mPbrRough, 3);
            GLES20.glUniform1i(this.mPbrMetal, 4);
            GLES20.glUniform4f(this.mCCParams, this.ccBright, this.ccContrast, this.ccSat, (this.ccBright != 0.0f || this.ccContrast != 0.0f || this.ccSat != 0.0f || this.ccTint[0] < 0.999f || this.ccTint[1] < 0.999f || this.ccTint[2] < 0.999f) ? 1.0f : 0.0f);
            GLES20.glUniform3fv(this.mCCTint, 1, this.ccTint, 0);
            GLES20.glUniform4f(this.mBloom, this.bloomInt, this.bloomThresh, this.bloomSize, 0.0f);
            GLES20.glUniform4f(this.mDof, this.dofFocus, this.dofRadius, Math.max(this.dofFar, this.blurAmt), Math.max(this.dofNear, this.blurAmt));
            GLES20.glUniform3fv(this.mAmbRgb, 1, this.ambRgb, 0);
        }
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.shadowTex);
        String tex = g.getTex();
        int tex2 = tex != null ? assetTexture(tex, true) : 0;
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, tex2);
        GLES20.glActiveTexture(33984);
        GLES20.glUniform1f(this.mHasTex, tex2 != 0 ? 1.0f : 0.0f);
        int nrmId = g.saNormal != null ? assetTexture(g.saNormal, true) : 0;
        int rghId = g.saRough != null ? assetTexture(g.saRough, true) : 0;
        int mtlId = g.saMetal != null ? assetTexture(g.saMetal, true) : 0;
        if (nrmId != 0) {
            GLES20.glActiveTexture(33986);
            GLES20.glBindTexture(3553, nrmId);
        }
        if (rghId != 0) {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, rghId);
        }
        if (mtlId != 0) {
            GLES20.glActiveTexture(33988);
            GLES20.glBindTexture(3553, mtlId);
        }
        GLES20.glActiveTexture(33984);
        GLES20.glUniform4f(this.mPbrFlags, nrmId != 0 ? 1.0f : 0.0f, rghId != 0 ? 1.0f : 0.0f, mtlId != 0 ? 1.0f : 0.0f, (float) g.saAlphaMode);
        if (g.saEmissive > 0.001f) {
            Vector3 etv = g.saEmissiveTint != null ? g.saEmissiveTint : new Vector3(1.0f, 1.0f, 1.0f);
            GLES20.glUniform4f(this.mPbrEmis, etv.getX(), etv.getY(), etv.getZ(), g.saEmissive);
        } else {
            GLES20.glUniform4f(this.mPbrEmis, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        Color3 c = PartData.INSTANCE.color(p);
        GLES20.glUniform4f(this.mColor, c.getR(), c.getG(), c.getB(), alpha);
        Vector3 t = g.getTint();
        if (t == null) {
            t = new Vector3(1.0f, 1.0f, 1.0f);
        }
        GLES20.glUniform3f(this.mTint, t.getX(), t.getY(), t.getZ());
        int mat = PartData.INSTANCE.material(p);
        GLES20.glUniform1f(this.mEmissive, mat == 288 ? 1.0f : 0.0f);
        setModel(g.getCf(), g.getScale());
        GLES20.glUniformMatrix4fv(this.mModel, 1, false, this.model, 0);
        GLES20.glUniformMatrix3fv(this.mNrm, 1, false, this.normal, 0);
        Mesh m = g.getMesh();
        GLES20.glBindBuffer(34962, m.getVbo());
        GLES20.glEnableVertexAttribArray(this.maPos);
        GLES20.glVertexAttribPointer(this.maPos, 3, 5126, false, 32, 0);
        GLES20.glEnableVertexAttribArray(this.maNrm);
        GLES20.glVertexAttribPointer(this.maNrm, 3, 5126, false, 32, 12);
        GLES20.glEnableVertexAttribArray(this.maUv);
        GLES20.glVertexAttribPointer(this.maUv, 2, 5126, false, 32, 24);
        boolean ds = Intrinsics.areEqual(p.get("DoubleSided"), Boolean.TRUE) || g.saAlphaMode == 1;
        if (ds) {
            GLES20.glDisable(2884);
        }
        GLES20.glDrawArrays(m.getMode(), 0, m.getCount());
        if (ds) {
            GLES20.glEnable(2884);
        }
        GLES20.glDisableVertexAttribArray(this.maUv);
    }

    private final void collectLocalLights() {
        List<Instance> lights = this.studio.renderLights();
        if (lights.isEmpty()) {
            this.ptCount = 0;
            return;
        }
        Vector3 cam = this.studio.getView().getCamPos();
        int n = 0;
        float[] dists = new float[8];
        for (int i = 0; i < lights.size(); i++) {
            Instance li = lights.get(i);
            if (Intrinsics.areEqual(li.get("Enabled"), (Object) false)) {
                continue;
            }
            Instance parent = li.getParent();
            if (parent == null) {
                continue;
            }
            Vector3 pos;
            CFrame baseCf;
            if (Intrinsics.areEqual(parent.getClassName(), "Attachment")) {
                pos = attachmentWorld(parent);
                Instance pp = parent.getParent();
                if (pos == null || pp == null || !pp.isA("BasePart")) {
                    continue;
                }
                baseCf = PartData.INSTANCE.cframe(pp);
                Object acf = parent.get("CFrame");
                if (acf instanceof CFrame) {
                    baseCf = baseCf.times((CFrame) acf);
                }
            } else if (parent.isA("BasePart")) {
                baseCf = PartData.INSTANCE.cframe(parent);
                pos = baseCf.getPosition();
            } else {
                continue;
            }
            Object rObj = li.get("Range");
            float range = (rObj instanceof Number) ? RangesKt.coerceIn(((Number) rObj).floatValue(), 0.1f, 60.0f) : 16.0f;
            Object bObj = li.get("Brightness");
            float br = (bObj instanceof Number) ? RangesKt.coerceIn(((Number) bObj).floatValue(), 0.0f, 40.0f) : 1.0f;
            if (br <= 0.001f) {
                continue;
            }
            Color3 col = c3(li.get("Color"));
            float cr = (col != null ? col.getR() : 1.0f) * br;
            float cg = (col != null ? col.getG() : 1.0f) * br;
            float cb = (col != null ? col.getB() : 1.0f) * br;
            float dx = pos.getX() - cam.getX();
            float dy = pos.getY() - cam.getY();
            float dz = pos.getZ() - cam.getZ();
            float d2 = (dx * dx) + (dy * dy) + (dz * dz);
            int slot;
            if (n < 8) {
                slot = n;
                n++;
            } else {
                int worst = 0;
                float worstD = dists[0];
                for (int k = 1; k < 8; k++) {
                    if (dists[k] > worstD) {
                        worstD = dists[k];
                        worst = k;
                    }
                }
                if (d2 >= worstD) {
                    continue;
                }
                slot = worst;
            }
            dists[slot] = d2;
            String cn = li.getClassName();
            boolean directional = Intrinsics.areEqual(cn, "SpotLight") || Intrinsics.areEqual(cn, "SurfaceLight");
            float cosHalf = -2.0f;
            float dirX = 0.0f;
            float dirY = -1.0f;
            float dirZ = 0.0f;
            if (directional) {
                Object aObj = li.get("Angle");
                float ang = (aObj instanceof Number) ? RangesKt.coerceIn(((Number) aObj).floatValue(), 1.0f, 180.0f) : 90.0f;
                cosHalf = (float) Math.cos(Math.toRadians(ang * 0.5d));
                int face = 5; // Enum.NormalId.Front (-Z) default
                Object fObj = li.get("Face");
                if (fObj instanceof EnumValue) {
                    face = ((EnumValue) fObj).getValue();
                } else if (fObj instanceof Number) {
                    face = ((Number) fObj).intValue();
                }
                Vector3 localDir;
                switch (face) {
                    case 0: localDir = new Vector3(1.0f, 0.0f, 0.0f); break;  // Right (+X)
                    case 1: localDir = new Vector3(0.0f, 1.0f, 0.0f); break;  // Top (+Y)
                    case 2: localDir = new Vector3(0.0f, 0.0f, 1.0f); break;  // Back (+Z)
                    case 3: localDir = new Vector3(-1.0f, 0.0f, 0.0f); break; // Left (-X)
                    case 4: localDir = new Vector3(0.0f, -1.0f, 0.0f); break; // Bottom (-Y)
                    default: localDir = new Vector3(0.0f, 0.0f, -1.0f); break; // Front (-Z)
                }
                Vector3 wDir = baseCf.times(localDir).minus(baseCf.getPosition()).getUnit();
                dirX = wDir.getX();
                dirY = wDir.getY();
                dirZ = wDir.getZ();
            }
            int o = slot * 4;
            this.ptPos[o] = pos.getX();
            this.ptPos[o + 1] = pos.getY();
            this.ptPos[o + 2] = pos.getZ();
            this.ptPos[o + 3] = range;
            this.ptCol[o] = cr;
            this.ptCol[o + 1] = cg;
            this.ptCol[o + 2] = cb;
            this.ptCol[o + 3] = cosHalf;
            this.ptDir[o] = dirX;
            this.ptDir[o + 1] = dirY;
            this.ptDir[o + 2] = dirZ;
            this.ptDir[o + 3] = directional ? 1.0f : 0.0f;
        }
        this.ptCount = n;
    }

    private final Vector3 attachmentWorld(Instance a) {
        Instance part;
        Vector3 zero;
        if (a != null && (part = a.getParent()) != null) {
            if (!part.isA("BasePart")) {
                part = null;
            }
            if (part != null) {
                CFrame cframe = PartData.INSTANCE.cframe(part);
                Object obj = a.get("CFrame");
                CFrame cFrame = obj instanceof CFrame ? (CFrame) obj : null;
                if (cFrame == null || (zero = cFrame.getPosition()) == null) {
                    zero = Vector3.INSTANCE.getZERO();
                }
                return cframe.times(zero);
            }
        }
        return null;
    }

    private final void segment(Vector3 a, Vector3 b, float thick) {
        Vector3 d = b.minus(a);
        float len = d.getMagnitude();
        if (len < 1.0E-4f) {
            return;
        }
        Vector3 up = Math.abs(d.getY() / len) > 0.95f ? Vector3.INSTANCE.getZ() : Vector3.INSTANCE.getY();
        setModel(CFrame.INSTANCE.lookAt(a.plus(b).div(2.0f), b, up), new Vector3(thick, thick, (0.5f * thick) + len));
        GLES20.glUniformMatrix4fv(this.uModel, 1, false, this.model, 0);
        GLES20.glUniformMatrix3fv(this.uNrm, 1, false, this.normal, 0);
        Mesh mesh = this.cube;
        if (mesh == null) {
            Intrinsics.throwUninitializedPropertyAccessException("cube");
            mesh = null;
        }
        GLES20.glDrawArrays(4, 0, mesh.getCount());
    }

    private final CFrame attachmentWorldCf(Instance a) {
        if (a == null) {
            return null;
        }
        Instance part = a.getParent();
        if (part == null || !part.isA("BasePart")) {
            return null;
        }
        CFrame pcf = PartData.INSTANCE.cframe(part);
        Object obj = a.get("CFrame");
        if (obj instanceof CFrame) {
            return pcf.times((CFrame) obj);
        }
        return pcf;
    }

    private final void drawBeams() {
        List<Instance> beams = this.studio.renderBeams();
        if (beams.isEmpty() || this.litBasic == null) {
            return;
        }
        useLit(this.litBasic);
        setLitFrameUniforms(false);
        Mesh mesh = this.cube;
        if (mesh == null) {
            return;
        }
        bind(mesh, this.aPos, this.aNrm);
        GLES20.glUniform1f(this.uEmissive, 1.0f);
        GLES20.glUniform1f(this.uGrid, 0.0f);
        GLES20.glUniform1f(this.uTruss, 0.0f);
        GLES20.glUniform4f(this.uMat, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfA, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfB, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSize, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(3042);
        GLES20.glDepthMask(false);
        for (int i = 0; i < beams.size(); i++) {
            Instance b = beams.get(i);
            if (b.getParent() == null || Intrinsics.areEqual(b.get("Enabled"), (Object) false)) {
                continue;
            }
            Object a0Obj = b.get("Attachment0");
            Object a1Obj = b.get("Attachment1");
            CFrame cf0 = attachmentWorldCf(a0Obj instanceof Instance ? (Instance) a0Obj : null);
            CFrame cf1 = attachmentWorldCf(a1Obj instanceof Instance ? (Instance) a1Obj : null);
            if (cf0 == null || cf1 == null) {
                continue;
            }
            Vector3 p0 = cf0.getPosition();
            Vector3 p3 = cf1.getPosition();
            float cs0 = (b.get("CurveSize0") instanceof Number) ? ((Number) b.get("CurveSize0")).floatValue() : 0.0f;
            float cs1 = (b.get("CurveSize1") instanceof Number) ? ((Number) b.get("CurveSize1")).floatValue() : 0.0f;
            Vector3 p1 = cf0.times(new Vector3(cs0, 0.0f, 0.0f));
            Vector3 p2 = cf1.times(new Vector3(-cs1, 0.0f, 0.0f));
            float w0 = (b.get("Width0") instanceof Number) ? RangesKt.coerceIn(((Number) b.get("Width0")).floatValue(), 0.02f, 20.0f) : 1.0f;
            float w1 = (b.get("Width1") instanceof Number) ? RangesKt.coerceIn(((Number) b.get("Width1")).floatValue(), 0.02f, 20.0f) : 1.0f;
            int segs = (b.get("Segments") instanceof Number) ? RangesKt.coerceIn(((Number) b.get("Segments")).intValue(), 1, 32) : 10;
            float cr = 1.0f, cg = 1.0f, cb = 1.0f;
            Object colObj = b.get("Color");
            if (colObj instanceof Color3) {
                cr = ((Color3) colObj).getR();
                cg = ((Color3) colObj).getG();
                cb = ((Color3) colObj).getB();
            } else if (colObj instanceof com.goydevv.studrblx.model.ColorSequence) {
                List<com.goydevv.studrblx.model.ColorKey> ks = ((com.goydevv.studrblx.model.ColorSequence) colObj).getKeys();
                if (!ks.isEmpty()) {
                    Color3 k0 = ks.get(0).getColor();
                    cr = k0.getR();
                    cg = k0.getG();
                    cb = k0.getB();
                }
            }
            float alpha = 0.85f;
            Object trObj = b.get("Transparency");
            if (trObj instanceof Number) {
                alpha = 1.0f - RangesKt.coerceIn(((Number) trObj).floatValue(), 0.0f, 1.0f);
            } else if (trObj instanceof com.goydevv.studrblx.model.NumberSequence) {
                List<com.goydevv.studrblx.model.NumberKey> nks = ((com.goydevv.studrblx.model.NumberSequence) trObj).getKeys();
                if (!nks.isEmpty()) {
                    alpha = 1.0f - RangesKt.coerceIn(nks.get(0).getValue(), 0.0f, 1.0f);
                }
            }
            if (alpha <= 0.01f) {
                continue;
            }
            float le = (b.get("LightEmission") instanceof Number) ? ((Number) b.get("LightEmission")).floatValue() : 0.0f;
            if (le > 0.5f) {
                GLES20.glBlendFunc(770, 1);
            } else {
                GLES20.glBlendFunc(770, 771);
            }
            GLES20.glUniform4f(this.uColor, cr, cg, cb, alpha);
            Vector3 prev = p0;
            for (int s = 1; s <= segs; s++) {
                float t = ((float) s) / ((float) segs);
                float u = 1.0f - t;
                float u2 = u * u;
                float t2 = t * t;
                Vector3 cur = p0.times(u2 * u).plus(p1.times(3.0f * u2 * t)).plus(p2.times(3.0f * u * t2)).plus(p3.times(t2 * t));
                float wt = (w0 + ((w1 - w0) * (t - (0.5f / ((float) segs))))) * 0.35f;
                segment(prev, cur, Math.max(wt, 0.04f));
                prev = cur;
            }
        }
        GLES20.glBlendFunc(770, 771);
        GLES20.glDepthMask(true);
        GLES20.glDisable(3042);
    }

    private final void drawTrails() {
        List<Instance> trails = this.studio.renderTrails();
        if (trails.isEmpty() || this.litBasic == null) {
            return;
        }
        useLit(this.litBasic);
        setLitFrameUniforms(false);
        Mesh mesh = this.cube;
        if (mesh == null) {
            return;
        }
        bind(mesh, this.aPos, this.aNrm);
        GLES20.glUniform1f(this.uEmissive, 1.0f);
        GLES20.glUniform1f(this.uGrid, 0.0f);
        GLES20.glUniform1f(this.uTruss, 0.0f);
        GLES20.glUniform4f(this.uMat, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfA, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfB, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSize, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(3042);
        GLES20.glDepthMask(false);
        float now = this.time;
        for (int i = 0; i < trails.size(); i++) {
            Instance tr = trails.get(i);
            if (tr.getParent() == null) {
                continue;
            }
            boolean enabled = !Intrinsics.areEqual(tr.get("Enabled"), (Object) false);
            float lifetime = (tr.get("Lifetime") instanceof Number) ? RangesKt.coerceIn(((Number) tr.get("Lifetime")).floatValue(), 0.05f, 20.0f) : 2.0f;
            float minLen = (tr.get("MinLength") instanceof Number) ? Math.max(((Number) tr.get("MinLength")).floatValue(), 0.02f) : 0.1f;
            ArrayList<float[]> hist = this.trailHist.get(tr);
            if (hist == null) {
                hist = new ArrayList<>();
                this.trailHist.put(tr, hist);
            }
            while (!hist.isEmpty() && (now - hist.get(0)[4]) > lifetime) {
                hist.remove(0);
            }
            if (enabled) {
                Object a0Obj = tr.get("Attachment0");
                Object a1Obj = tr.get("Attachment1");
                Vector3 p0 = attachmentWorld(a0Obj instanceof Instance ? (Instance) a0Obj : null);
                Vector3 p1 = attachmentWorld(a1Obj instanceof Instance ? (Instance) a1Obj : null);
                if (p0 != null && p1 != null) {
                    Vector3 mid = p0.plus(p1).div(2.0f);
                    float w = Math.max(p1.minus(p0).getMagnitude() * 0.35f, 0.05f);
                    boolean addPt = hist.isEmpty();
                    if (!addPt) {
                        float[] last = hist.get(hist.size() - 1);
                        float dx = mid.getX() - last[0];
                        float dy = mid.getY() - last[1];
                        float dz = mid.getZ() - last[2];
                        if ((dx * dx) + (dy * dy) + (dz * dz) >= minLen * minLen) {
                            addPt = true;
                        }
                    }
                    if (addPt) {
                        hist.add(new float[]{mid.getX(), mid.getY(), mid.getZ(), w, now});
                        if (hist.size() > 48) {
                            hist.remove(0);
                        }
                    }
                }
            }
            if (hist.size() < 2) {
                continue;
            }
            float cr = 1.0f, cg = 1.0f, cb = 1.0f;
            Object colObj = tr.get("Color");
            if (colObj instanceof Color3) {
                cr = ((Color3) colObj).getR();
                cg = ((Color3) colObj).getG();
                cb = ((Color3) colObj).getB();
            } else if (colObj instanceof com.goydevv.studrblx.model.ColorSequence) {
                List<com.goydevv.studrblx.model.ColorKey> ks = ((com.goydevv.studrblx.model.ColorSequence) colObj).getKeys();
                if (!ks.isEmpty()) {
                    Color3 k0 = ks.get(0).getColor();
                    cr = k0.getR();
                    cg = k0.getG();
                    cb = k0.getB();
                }
            }
            float baseAlpha = 0.85f;
            Object trObj = tr.get("Transparency");
            if (trObj instanceof Number) {
                baseAlpha = 1.0f - RangesKt.coerceIn(((Number) trObj).floatValue(), 0.0f, 1.0f);
            } else if (trObj instanceof com.goydevv.studrblx.model.NumberSequence) {
                List<com.goydevv.studrblx.model.NumberKey> nks = ((com.goydevv.studrblx.model.NumberSequence) trObj).getKeys();
                if (!nks.isEmpty()) {
                    baseAlpha = 1.0f - RangesKt.coerceIn(nks.get(0).getValue(), 0.0f, 1.0f);
                }
            }
            float le = (tr.get("LightEmission") instanceof Number) ? ((Number) tr.get("LightEmission")).floatValue() : 0.0f;
            GLES20.glBlendFunc(770, le > 0.5f ? 1 : 771);
            for (int k = 1; k < hist.size(); k++) {
                float[] prev = hist.get(k - 1);
                float[] cur = hist.get(k);
                float age = RangesKt.coerceIn((now - cur[4]) / lifetime, 0.0f, 1.0f);
                float a = baseAlpha * (1.0f - age);
                if (a <= 0.01f) continue;
                GLES20.glUniform4f(this.uColor, cr, cg, cb, a);
                segment(new Vector3(prev[0], prev[1], prev[2]), new Vector3(cur[0], cur[1], cur[2]), cur[3] * (1.0f - age * 0.5f));
            }
        }
        GLES20.glBlendFunc(770, 771);
        GLES20.glDepthMask(true);
        GLES20.glDisable(3042);
    }

    private final void drawParticles() {
        List<Instance> list = this.studio.renderParticles();
        if (list.isEmpty() || this.litBasic == null) {
            return;
        }
        useLit(this.litBasic);
        setLitFrameUniforms(false);
        Mesh mesh = this.cube;
        if (mesh == null) {
            return;
        }
        bind(mesh, this.aPos, this.aNrm);
        GLES20.glUniform1f(this.uEmissive, 1.0f);
        GLES20.glUniform1f(this.uGrid, 0.0f);
        GLES20.glUniform1f(this.uTruss, 0.0f);
        GLES20.glUniform4f(this.uMat, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfA, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfB, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSize, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(3042);
        GLES20.glDepthMask(false);
        Vector3 cam = this.studio.getView().getCamPos();
        float tNow = this.time;
        for (int i = 0; i < list.size(); i++) {
            Instance fx = list.get(i);
            if (Intrinsics.areEqual(fx.get("Enabled"), (Object) false)) {
                continue;
            }
            Instance parent = fx.getParent();
            if (parent == null) {
                continue;
            }
            Vector3 origin;
            Vector3 emitExt = new Vector3(0.5f, 0.5f, 0.5f);
            if (Intrinsics.areEqual(parent.getClassName(), "Attachment")) {
                origin = attachmentWorld(parent);
            } else if (parent.isA("BasePart")) {
                origin = PartData.INSTANCE.cframe(parent).getPosition();
                emitExt = PartData.INSTANCE.size(parent).div(2.0f);
            } else {
                continue;
            }
            if (origin == null || origin.minus(cam).getMagnitude() > 260.0f) {
                continue;
            }
            String cn = fx.getClassName();
            int count = 8;
            float cr = 1.0f, cg = 1.0f, cb = 1.0f, alpha = 0.75f, pSize = 0.5f, rise = 4.0f, life = 1.2f;
            boolean additive = false;
            if ("Fire".equals(cn)) {
                additive = true;
                Color3 c1 = c3(fx.get("Color"));
                cr = c1 != null ? c1.getR() : 0.95f;
                cg = c1 != null ? c1.getG() : 0.45f;
                cb = c1 != null ? c1.getB() : 0.12f;
                float sz = (fx.get("Size") instanceof Number) ? RangesKt.coerceIn(((Number) fx.get("Size")).floatValue(), 1.0f, 30.0f) : 5.0f;
                float heat = (fx.get("Heat") instanceof Number) ? RangesKt.coerceIn(((Number) fx.get("Heat")).floatValue(), -25.0f, 25.0f) : 9.0f;
                pSize = sz * 0.18f;
                rise = heat * 0.45f;
                life = 0.9f;
                count = 10;
            } else if ("Smoke".equals(cn)) {
                Color3 c1 = c3(fx.get("Color"));
                cr = c1 != null ? c1.getR() : 0.85f;
                cg = c1 != null ? c1.getG() : 0.85f;
                cb = c1 != null ? c1.getB() : 0.85f;
                alpha = (fx.get("Opacity") instanceof Number) ? RangesKt.coerceIn(((Number) fx.get("Opacity")).floatValue(), 0.0f, 1.0f) : 0.5f;
                float sz = (fx.get("Size") instanceof Number) ? RangesKt.coerceIn(((Number) fx.get("Size")).floatValue(), 0.1f, 30.0f) : 1.0f;
                rise = (fx.get("RiseVelocity") instanceof Number) ? ((Number) fx.get("RiseVelocity")).floatValue() * 0.6f : 1.0f;
                pSize = sz * 0.35f;
                life = 2.4f;
                count = 8;
            } else if ("Sparkles".equals(cn)) {
                additive = true;
                Color3 c1 = c3(fx.get("SparkleColor"));
                cr = c1 != null ? c1.getR() : 0.56f;
                cg = c1 != null ? c1.getG() : 0.35f;
                cb = c1 != null ? c1.getB() : 1.0f;
                pSize = 0.22f;
                rise = 1.4f;
                life = 1.1f;
                count = 10;
            } else {
                // ParticleEmitter
                float rate = (fx.get("Rate") instanceof Number) ? ((Number) fx.get("Rate")).floatValue() : 20.0f;
                if (rate <= 0.01f) continue;
                count = RangesKt.coerceIn((int) (rate * 0.35f), 2, 12);
                Object cObj = fx.get("Color");
                if (cObj instanceof com.goydevv.studrblx.model.ColorSequence) {
                    List<com.goydevv.studrblx.model.ColorKey> ks = ((com.goydevv.studrblx.model.ColorSequence) cObj).getKeys();
                    if (!ks.isEmpty()) {
                        Color3 k0 = ks.get(0).getColor();
                        cr = k0.getR(); cg = k0.getG(); cb = k0.getB();
                    }
                }
                float le = (fx.get("LightEmission") instanceof Number) ? ((Number) fx.get("LightEmission")).floatValue() : 0.0f;
                additive = le > 0.5f;
                pSize = 0.45f;
                rise = 3.0f;
                life = 1.5f;
            }
            GLES20.glBlendFunc(770, additive ? 1 : 771);
            int seed = System.identityHashCode(fx);
            for (int k = 0; k < count; k++) {
                float phase = ((tNow / life) + (((float) k) / ((float) count)) + ((seed & 255) * 0.0039f)) % 1.0f;
                float a = alpha * (float) Math.sin(phase * Math.PI);
                if (a <= 0.02f) continue;
                float h1 = (float) Math.sin((k * 12.9898d) + (seed * 0.013d));
                float h2 = (float) Math.cos((k * 78.233d) + (seed * 0.029d));
                float ox = h1 * Math.min(emitExt.getX(), 2.5f) * (0.4f + phase * 0.8f);
                float oz = h2 * Math.min(emitExt.getZ(), 2.5f) * (0.4f + phase * 0.8f);
                float oy = phase * rise * life;
                float sc = pSize * (additive ? (1.0f - phase * 0.65f) : (0.6f + phase * 0.9f));
                Vector3 pos = new Vector3(origin.getX() + ox, origin.getY() + oy, origin.getZ() + oz);
                GLES20.glUniform4f(this.uColor, cr, cg, cb, a);
                setModel(CFrame.INSTANCE.at(pos), new Vector3(sc, sc, sc));
                GLES20.glUniformMatrix4fv(this.uModel, 1, false, this.model, 0);
                GLES20.glUniformMatrix3fv(this.uNrm, 1, false, this.normal, 0);
                GLES20.glDrawArrays(mesh.getMode(), 0, mesh.getCount());
            }
        }
        GLES20.glBlendFunc(770, 771);
        GLES20.glDepthMask(true);
        GLES20.glDisable(3042);
    }

    private final void drawConstraints() {
        LitLocs l;
        Color3 col;
        List<Instance> list;
        LitLocs l2;
        Iterator<Instance> it;
        Vector3 cp = null;
        List<Instance> listRenderConstraints = this.studio.renderConstraints();
        if (listRenderConstraints.isEmpty() || (l = this.litBasic) == null) {
            return;
        }
        useLit(l);
        setLitFrameUniforms(this.frameShadows);
        Mesh mesh = this.cube;
        if (mesh == null) {
            Intrinsics.throwUninitializedPropertyAccessException("cube");
            mesh = null;
        }
        bind(mesh, this.aPos, this.aNrm);
        float thick = 0.0f;
        GLES20.glUniform1f(this.uEmissive, 0.0f);
        GLES20.glUniform1f(this.uGrid, 0.0f);
        GLES20.glUniform1f(this.uTruss, 0.0f);
        float f = 1.0f;
        GLES20.glUniform4f(this.uMat, 0.0f, 0.0f, 1.0f, 0.1f);
        GLES20.glUniform3f(this.uSurfA, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfB, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSize, 1.0f, 1.0f, 1.0f);
        Vector3 cp2 = this.studio.getView().getCamPos();
        Iterator<Instance> it2 = listRenderConstraints.iterator();
        while (it2.hasNext()) {
            Instance c = it2.next();
            if (c.getParent() == null || !Intrinsics.areEqual(c.get("Visible"), (Object) true)) {
            } else if (!Intrinsics.areEqual(c.get("Enabled"), (Object) false)) {
                Object obj = c.get("Attachment0");
                Vector3 p0 = attachmentWorld(obj instanceof Instance ? (Instance) obj : null);
                if (p0 != null) {
                    Object obj2 = c.get("Attachment1");
                    Vector3 p1 = attachmentWorld(obj2 instanceof Instance ? (Instance) obj2 : null);
                    if (p1 != null) {
                        Vector3 mid = p0.plus(p1).div(2.0f);
                        float r = (p1.minus(p0).getMagnitude() / 2.0f) + 2.0f;
                        if (!this.frustum.sphere(mid.getX(), mid.getY(), mid.getZ(), r)) {
                        } else {
                            Object obj3 = c.get("Color");
                            BrickColorV brickColorV = obj3 instanceof BrickColorV ? (BrickColorV) obj3 : null;
                            if (brickColorV == null || (col = brickColorV.getColor()) == null) {
                                col = Color3.INSTANCE.fromRGB(163, 162, 165);
                            }
                            GLES20.glUniform4f(this.uColor, col.getR(), col.getG(), col.getB(), f);
                            Object obj4 = c.get("Thickness");
                            Number number = obj4 instanceof Number ? (Number) obj4 : null;
                            float thick2 = RangesKt.coerceIn(number != null ? number.floatValue() : 0.1f, 0.02f, 5.0f);
                            float dist = p1.minus(p0).getMagnitude();
                            String className = c.getClassName();
                            if (Intrinsics.areEqual(className, "RopeConstraint")) {
                                Object obj5 = c.get("Length");
                                Number number2 = obj5 instanceof Number ? (Number) obj5 : null;
                                float len = RangesKt.coerceAtLeast(number2 != null ? number2.floatValue() : dist, dist);
                                float sag = len > 0.01f + dist ? RangesKt.coerceAtMost((float) Math.sqrt((((len * len) - (dist * dist)) * 3.0f) / 16.0f), len / 2.0f) : 0.0f;
                                int n = sag > 0.05f ? 14 : 1;
                                Vector3 prev = p0;
                                list = listRenderConstraints;
                                int i = 1;
                                if (1 <= n) {
                                    while (true) {
                                        l2 = l;
                                        float t = i / n;
                                        Color3 col2 = col;
                                        float sag2 = sag;
                                        it = it2;
                                        Vector3 pt = p0.plus(p1.minus(p0).times(t)).minus(new Vector3(0.0f, 4.0f * sag * t * (f - t), 0.0f));
                                        segment(prev, pt, thick2);
                                        prev = pt;
                                        if (i == n) {
                                            break;
                                        }
                                        i++;
                                        l = l2;
                                        col = col2;
                                        it2 = it;
                                        sag = sag2;
                                    }
                                } else {
                                    l2 = l;
                                    it = it2;
                                }
                                cp = cp2;
                            } else {
                                list = listRenderConstraints;
                                l2 = l;
                                it = it2;
                                if (Intrinsics.areEqual(className, "SpringConstraint")) {
                                    Object obj6 = c.get("Coils");
                                    Number number3 = obj6 instanceof Number ? (Number) obj6 : null;
                                    float coils = RangesKt.coerceIn(number3 != null ? number3.floatValue() : 8.0f, 0.0f, 64.0f);
                                    Object obj7 = c.get("Radius");
                                    Number number4 = obj7 instanceof Number ? (Number) obj7 : null;
                                    float rad = RangesKt.coerceIn(number4 != null ? number4.floatValue() : 0.4f, 0.0f, 20.0f);
                                    Vector3 axis = p1.minus(p0);
                                    if (axis.getMagnitude() >= 0.001f) {
                                        Vector3 f2 = axis.getUnit();
                                        Vector3 side = (Math.abs(f2.getY()) > 0.9f ? Vector3.INSTANCE.getX() : Vector3.INSTANCE.getY()).cross(f2).getUnit();
                                        Vector3 side2 = f2.cross(side).getUnit();
                                        int steps = RangesKt.coerceIn((int) (12.0f * coils), 8, 600);
                                        Vector3 prev2 = p0;
                                        int i2 = 1;
                                        if (1 <= steps) {
                                            while (true) {
                                                float rad2 = rad;
                                                float rad3 = i2;
                                                float t2 = rad3 / steps;
                                                Vector3 f3 = f2;
                                                float ang = t2 * coils * 2.0f * 3.1415927f;
                                                float ring = (t2 < 0.04f || t2 > 0.96f) ? 0.0f : rad2;
                                                Instance c2 = c;
                                                cp = cp2;
                                                Vector3 axis2 = axis;
                                                Vector3 pt2 = p0.plus(axis.times(t2)).plus(side.times(((float) Math.cos(ang)) * ring)).plus(side2.times(((float) Math.sin(ang)) * ring));
                                                segment(prev2, pt2, thick2);
                                                prev2 = pt2;
                                                if (i2 == steps) {
                                                    break;
                                                }
                                                i2++;
                                                axis = axis2;
                                                rad = rad2;
                                                c = c2;
                                                f2 = f3;
                                                cp2 = cp;
                                            }
                                        } else {
                                            cp = cp2;
                                        }
                                    } else {
                                        listRenderConstraints = list;
                                        l = l2;
                                        it2 = it;
                                        thick = 0.0f;
                                    }
                                } else {
                                    cp = cp2;
                                    segment(p0, p1, thick2);
                                }
                            }
                            Vector3 cp3 = cp;
                            if (mid.minus(cp3).getMagnitude() < this.closest) {
                                this.closest = RangesKt.coerceAtLeast(mid.minus(cp3).getMagnitude() - r, 0.0f);
                                cp2 = cp3;
                                thick = 0.0f;
                                listRenderConstraints = list;
                                l = l2;
                                it2 = it;
                            } else {
                                cp2 = cp3;
                                thick = 0.0f;
                                listRenderConstraints = list;
                                l = l2;
                                it2 = it;
                            }
                        }
                    }
                }
            }
        }
    }

    private final List<Instance> adorneeParts(Instance a) {
        Object obj = a.get("Adornee");
        Instance ad = obj instanceof Instance ? (Instance) obj : null;
        if (ad == null) {
            ad = Intrinsics.areEqual(a.getClassName(), "Highlight") ? a.getParent() : null;
        }
        if (ad == null) {
            return CollectionsKt.emptyList();
        }
        if (ad.isA("BasePart")) {
            return CollectionsKt.listOf(ad);
        }
        if (ad.isA("Model") || Intrinsics.areEqual(ad.getClassName(), "Folder")) {
            Iterable iterableDescendants$default = Instance.descendants$default(ad, null, 1, null);
            Collection arrayList = new ArrayList();
            for (Object obj2 : iterableDescendants$default) {
                Instance instance = (Instance) obj2;
                boolean z = false;
                if (instance.isA("BasePart")) {
                    Instance parent = instance.getParent();
                    if (!(parent != null && parent.isA("PartOperation"))) {
                        z = true;
                    }
                }
                if (z) {
                    arrayList.add(obj2);
                }
            }
            return (List) arrayList;
        }
        return CollectionsKt.emptyList();
    }

    /* JADX WARN: Code duplicated, block: B:155:0x03a2  */
    /* JADX WARN: Code duplicated, block: B:156:0x03a5  */
    /* JADX WARN: Code duplicated, block: B:158:0x03a9  */
    /* JADX WARN: Code duplicated, block: B:161:0x03bd  */
    /* JADX WARN: Code duplicated, block: B:162:0x03c0  */
    /* JADX WARN: Code duplicated, block: B:164:0x03c4  */
    /* JADX WARN: Code duplicated, block: B:165:0x03c9  */
    /* JADX WARN: Code duplicated, block: B:168:0x03d8  */
    /* JADX WARN: Code duplicated, block: B:169:0x03db  */
    /* JADX WARN: Code duplicated, block: B:171:0x03df  */
    /* JADX WARN: Code duplicated, block: B:172:0x03e4  */
    /* JADX WARN: Code duplicated, block: B:175:0x03f9  */
    /* JADX WARN: Code duplicated, block: B:179:0x0412  */
    /* JADX WARN: Code duplicated, block: B:181:0x044e  */
    /* JADX WARN: Code duplicated, block: B:190:0x048e  */
    /* JADX WARN: Code duplicated, block: B:192:0x04cc  */
    /* JADX WARN: Code duplicated, block: B:194:0x04d0  */
    /* JADX WARN: Code duplicated, block: B:197:0x04ee  */
    /* JADX WARN: Code duplicated, block: B:198:0x04f5  */
    /* JADX WARN: Code duplicated, block: B:201:0x0537  */
    /* JADX WARN: Code duplicated, block: B:205:0x054d  */
    /* JADX WARN: Code duplicated, block: B:207:0x0557  */
    /* JADX WARN: Code duplicated, block: B:208:0x055a  */
    /* JADX WARN: Code duplicated, block: B:210:0x055e  */
    /* JADX WARN: Code duplicated, block: B:211:0x0563  */
    /* JADX WARN: Code duplicated, block: B:213:0x0566  */
    /* JADX WARN: Code duplicated, block: B:215:0x056a  */
    /* JADX WARN: Code duplicated, block: B:216:0x0570  */
    /* JADX WARN: Code duplicated, block: B:218:0x0574  */
    /* JADX WARN: Code duplicated, block: B:271:0x0453 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:280:0x0579 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:281:0x0579 A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r22v1 */
    /* JADX WARN: Type inference failed for: r22v2 */
    /* JADX WARN: Type inference failed for: r22v3 */
    /* JADX WARN: Type inference failed for: r22v4 */
    /* JADX WARN: Type inference failed for: r22v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r22v7 */
    /* JADX WARN: Type inference failed for: r22v8 */
    /* JADX WARN: Type inference failed for: r5v33, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v35 */
    /* JADX WARN: Type inference failed for: r5v50 */
    private final void drawAdornments() {
        List<Instance> list;
        int i;
        boolean z;
        float magnitude;
        Mesh mesh;
        Object obj;
        EnumValue enumValue;
        int value;
        Mesh mesh2;
        Vector3 y;
        Mesh mesh3;
        List<Instance> list2;
        int r22;
        float f;
        List<Instance> list3;
        List<Mesh> listListOf;
        Object obj2;
        Color3 color3FromRGB;
        Object obj3;
        Number number;
        float fFloatValue;
        float f2;
        Object obj4;
        Number number2;
        float fFloatValue2;
        Mesh mesh4;
        Mesh mesh5;
        List<Instance> listRenderAdornments = this.studio.renderAdornments();
        if (listRenderAdornments.isEmpty()) {
            return;
        }
        GLES20.glUseProgram(this.line);
        int i2 = 1;
        boolean z2 = false;
        GLES20.glUniformMatrix4fv(this.lVP, 1, false, this.studio.getView().getVp(), 0);
        int i3 = 3042;
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        for (Instance instance : listRenderAdornments) {
            if (instance.getParent() == null || Intrinsics.areEqual(instance.get("Visible"), Boolean.valueOf(z2))) {
                boolean z3 = z2;
            } else if (!Intrinsics.areEqual(instance.get("Enabled"), Boolean.valueOf(z2))) {
                String className = instance.getClassName();
                int i4 = i3;
                Mesh mesh6 = null;
                switch (className.hashCode()) {
                    case -1937510613:
                        list = listRenderAdornments;
                        if (className.equals("Handles")) {
                            GLES20.glDisable(2929);
                            for (AdornHandles.Tip tip : AdornHandles.INSTANCE.tips(instance, this.studio.getView())) {
                                magnitude = tip.getTip().minus(tip.getBase()).getMagnitude();
                                GLES20.glUniform4f(this.lColor, tip.getColor().getR(), tip.getColor().getG(), tip.getColor().getB(), 1.0f);
                                if (magnitude > 0.001f) {
                                    mesh2 = this.cube;
                                    if (mesh2 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh2 = null;
                                    }
                                    bind(mesh2, this.laPos, -1);
                                    if (Math.abs(tip.getAxis().getY()) > 0.9f) {
                                        y = Vector3.INSTANCE.getZ();
                                    } else {
                                        y = Vector3.INSTANCE.getY();
                                    }
                                    setModel(CFrame.INSTANCE.lookAt(tip.getBase().plus(tip.getTip()).div(2.0f), tip.getTip(), y), new Vector3(tip.getRadius() * 0.35f, tip.getRadius() * 0.35f, magnitude));
                                    GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                    mesh3 = this.cube;
                                    if (mesh3 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh3 = null;
                                    }
                                    GLES20.glDrawArrays(4, 0, mesh3.getCount());
                                }
                                if (Intrinsics.areEqual(instance.getClassName(), "Handles")) {
                                    obj = instance.get("Style");
                                    if (obj instanceof EnumValue) {
                                        enumValue = (EnumValue) obj;
                                    } else {
                                        enumValue = null;
                                    }
                                    if (enumValue != null) {
                                        value = enumValue.getValue();
                                    } else {
                                        value = 0;
                                    }
                                    if (value == 0) {
                                        mesh = this.sphere;
                                        if (mesh == null) {
                                            Intrinsics.throwUninitializedPropertyAccessException("sphere");
                                            mesh = null;
                                        }
                                    } else {
                                        mesh = this.cube;
                                        if (mesh == null) {
                                            Intrinsics.throwUninitializedPropertyAccessException("cube");
                                            mesh = null;
                                        }
                                    }
                                } else {
                                    mesh = this.cube;
                                    if (mesh == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh = null;
                                    }
                                }
                                bind(mesh, this.laPos, -1);
                                float radius = tip.getRadius() * 2.0f;
                                setModel(CFrame.INSTANCE.at(tip.getTip()), new Vector3(radius, radius, radius));
                                GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                GLES20.glDrawArrays(4, 0, mesh.getCount());
                            }
                            GLES20.glEnable(2929);
                            i2 = 1;
                            z2 = false;
                            i3 = i4;
                            listRenderAdornments = list;
                        } else {
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        }
                        break;
                    case -1464734529:
                        list = listRenderAdornments;
                        if (className.equals("SelectionBox")) {
                            obj2 = instance.get("Color3");
                            if (obj2 instanceof Color3) {
                                color3FromRGB = (Color3) obj2;
                            } else {
                                color3FromRGB = null;
                            }
                            if (color3FromRGB == null) {
                                color3FromRGB = Color3.INSTANCE.fromRGB(13, 105, 172);
                            }
                            obj3 = instance.get("Transparency");
                            if (obj3 instanceof Number) {
                                number = (Number) obj3;
                            } else {
                                number = null;
                            }
                            if (number != null) {
                                fFloatValue = number.floatValue();
                            } else {
                                fFloatValue = 0.0f;
                            }
                            f2 = 1.0f - fFloatValue;
                            obj4 = instance.get("LineThickness");
                            if (obj4 instanceof Number) {
                                number2 = (Number) obj4;
                            } else {
                                number2 = null;
                            }
                            if (number2 != null) {
                                fFloatValue2 = number2.floatValue();
                            } else {
                                fFloatValue2 = 0.15f;
                            }
                            GLES20.glLineWidth(RangesKt.coerceIn(20.0f * fFloatValue2, 1.5f, 6.0f));
                            mesh4 = this.boxLines;
                            if (mesh4 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                                mesh4 = null;
                            }
                            bind(mesh4, this.laPos, -1);
                            for (Instance instance2 : adorneeParts(instance)) {
                                setModel(PartData.INSTANCE.cframe(instance2), PartData.INSTANCE.size(instance2).plus(new Vector3(fFloatValue2, fFloatValue2, fFloatValue2)));
                                GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                GLES20.glUniform4f(this.lColor, color3FromRGB.getR(), color3FromRGB.getG(), color3FromRGB.getB(), f2);
                                mesh5 = this.boxLines;
                                if (mesh5 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                                    mesh5 = null;
                                }
                                GLES20.glDrawArrays(1, 0, mesh5.getCount());
                            }
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        } else {
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        }
                        break;
                    case -977833799:
                        list = listRenderAdornments;
                        if (className.equals("ArcHandles")) {
                            GLES20.glDisable(2929);
                            for (AdornHandles.Tip tip : AdornHandles.INSTANCE.tips(instance, this.studio.getView())) {
                                magnitude = tip.getTip().minus(tip.getBase()).getMagnitude();
                                GLES20.glUniform4f(this.lColor, tip.getColor().getR(), tip.getColor().getG(), tip.getColor().getB(), 1.0f);
                                if (magnitude > 0.001f) {
                                    mesh2 = this.cube;
                                    if (mesh2 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh2 = null;
                                    }
                                    bind(mesh2, this.laPos, -1);
                                    if (Math.abs(tip.getAxis().getY()) > 0.9f) {
                                        y = Vector3.INSTANCE.getZ();
                                    } else {
                                        y = Vector3.INSTANCE.getY();
                                    }
                                    setModel(CFrame.INSTANCE.lookAt(tip.getBase().plus(tip.getTip()).div(2.0f), tip.getTip(), y), new Vector3(tip.getRadius() * 0.35f, tip.getRadius() * 0.35f, magnitude));
                                    GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                    mesh3 = this.cube;
                                    if (mesh3 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh3 = null;
                                    }
                                    GLES20.glDrawArrays(4, 0, mesh3.getCount());
                                }
                                if (Intrinsics.areEqual(instance.getClassName(), "Handles")) {
                                    obj = instance.get("Style");
                                    if (obj instanceof EnumValue) {
                                        enumValue = (EnumValue) obj;
                                    } else {
                                        enumValue = null;
                                    }
                                    if (enumValue != null) {
                                        value = enumValue.getValue();
                                    } else {
                                        value = 0;
                                    }
                                    if (value == 0) {
                                        mesh = this.sphere;
                                        if (mesh == null) {
                                            Intrinsics.throwUninitializedPropertyAccessException("sphere");
                                            mesh = null;
                                        }
                                    } else {
                                        mesh = this.cube;
                                        if (mesh == null) {
                                            Intrinsics.throwUninitializedPropertyAccessException("cube");
                                            mesh = null;
                                        }
                                    }
                                } else {
                                    mesh = this.cube;
                                    if (mesh == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh = null;
                                    }
                                }
                                bind(mesh, this.laPos, -1);
                                float radius2 = tip.getRadius() * 2.0f;
                                setModel(CFrame.INSTANCE.at(tip.getTip()), new Vector3(radius2, radius2, radius2));
                                GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                GLES20.glDrawArrays(4, 0, mesh.getCount());
                            }
                            GLES20.glEnable(2929);
                            i2 = 1;
                            z2 = false;
                            i3 = i4;
                            listRenderAdornments = list;
                        } else {
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        }
                        break;
                    case -545500461:
                        list = listRenderAdornments;
                        if (className.equals("BoxHandleAdornment")) {
                            Object obj5 = instance.get("Adornee");
                            Instance instance3 = obj5 instanceof Instance ? (Instance) obj5 : null;
                            if (instance3 != null) {
                                if (!instance3.isA("BasePart")) {
                                    instance3 = null;
                                }
                                if (instance3 != null) {
                                    Object obj6 = instance.get("Color3");
                                    Color3 color3 = obj6 instanceof Color3 ? (Color3) obj6 : null;
                                    if (color3 == null) {
                                        color3 = new Color3(1.0f, 1.0f, 1.0f);
                                    }
                                    Object obj7 = instance.get("Transparency");
                                    Number number3 = obj7 instanceof Number ? (Number) obj7 : null;
                                    float fFloatValue3 = 1.0f - (number3 != null ? number3.floatValue() : 0.0f);
                                    Object obj8 = instance.get("Size");
                                    Vector3 vector3 = obj8 instanceof Vector3 ? (Vector3) obj8 : null;
                                    if (vector3 == null) {
                                        vector3 = new Vector3(1.0f, 1.0f, 1.0f);
                                    }
                                    Object obj9 = instance.get("CFrame");
                                    CFrame identity = obj9 instanceof CFrame ? (CFrame) obj9 : null;
                                    if (identity == null) {
                                        identity = CFrame.INSTANCE.getIDENTITY();
                                    }
                                    boolean zAreEqual = Intrinsics.areEqual(instance.get("AlwaysOnTop"), (Object) true);
                                    if (zAreEqual) {
                                        GLES20.glDisable(2929);
                                    }
                                    Mesh mesh7 = this.cube;
                                    if (mesh7 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                        mesh7 = null;
                                    }
                                    bind(mesh7, this.laPos, -1);
                                    setModel(PartData.INSTANCE.cframe(instance3).times(identity), vector3);
                                    GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                    GLES20.glUniform4f(this.lColor, color3.getR(), color3.getG(), color3.getB(), fFloatValue3);
                                    Mesh mesh8 = this.cube;
                                    if (mesh8 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("cube");
                                    } else {
                                        mesh6 = mesh8;
                                    }
                                    GLES20.glDrawArrays(4, 0, mesh6.getCount());
                                    if (zAreEqual) {
                                        GLES20.glEnable(2929);
                                        i = 1;
                                        z = false;
                                        i2 = i;
                                        z2 = z;
                                        i3 = i4;
                                        listRenderAdornments = list;
                                    } else {
                                        i3 = i4;
                                        listRenderAdornments = list;
                                        i2 = 1;
                                        z2 = false;
                                    }
                                    break;
                                }
                            }
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        } else {
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        }
                        break;
                    case 1322757268:
                        if (className.equals("Highlight")) {
                            List<Instance> listAdorneeParts = adorneeParts(instance);
                            Object obj10 = instance.get("DepthMode");
                            EnumValue enumValue2 = obj10 instanceof EnumValue ? (EnumValue) obj10 : null;
                            int i5 = (enumValue2 != null ? enumValue2.getValue() : z2 ? 1 : 0) == 0 ? i2 : z2 ? 1 : 0;
                            if (i5 != 0) {
                                GLES20.glDisable(2929);
                            }
                            Object obj11 = instance.get("FillColor");
                            Color3 color4 = obj11 instanceof Color3 ? (Color3) obj11 : null;
                            if (color4 == null) {
                                color4 = new Color3(1.0f, 0.0f, 0.0f);
                            }
                            Object obj12 = instance.get("FillTransparency");
                            Number number4 = obj12 instanceof Number ? (Number) obj12 : null;
                            float fFloatValue4 = 1.0f - (number4 != null ? number4.floatValue() : 0.5f);
                            float f3 = 0.01f;
                            if (fFloatValue4 > 0.01f) {
                                GLES20.glDepthMask(z2);
                                Iterator<Instance> it = listAdorneeParts.iterator();
                                boolean r5 = z2;
                                while (it.hasNext()) {
                                    float f4 = f3;
                                    Geo geoGeometry = geometry(it.next());
                                    setModel(geoGeometry.getCf(), geoGeometry.getScale());
                                    GLES20.glUniformMatrix4fv(this.lModel, i2, r5, this.model, r5 ? 1 : 0);
                                    int i6 = i2;
                                    GLES20.glUniform4f(this.lColor, color4.getR(), color4.getG(), color4.getB(), fFloatValue4 * 0.6f);
                                    List<Pair<Mesh, Color3>> subs = geoGeometry.getSubs();
                                    if (subs != null) {
                                        List<Pair<Mesh, Color3>> list4 = subs;
                                        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list4, 10));
                                        Iterator it2 = list4.iterator();
                                        while (it2.hasNext()) {
                                            arrayList.add((Mesh) ((Pair) it2.next()).getFirst());
                                        }
                                        list3 = listRenderAdornments;
                                        listListOf = arrayList;
                                    } else {
                                        list3 = listRenderAdornments;
                                        listListOf = CollectionsKt.listOf(geoGeometry.getMesh());
                                    }
                                    for (Mesh mesh9 : listListOf) {
                                        bind(mesh9, this.laPos, -1);
                                        GLES20.glDrawArrays(mesh9.getMode(), 0, mesh9.getCount());
                                    }
                                    f3 = f4;
                                    i2 = i6;
                                    listRenderAdornments = list3;
                                    r5 = false;
                                }
                                list2 = listRenderAdornments;
                                int r23 = i2;
                                f = f3;
                                GLES20.glDepthMask(r23 != 0);
                                r22 = r23;
                            } else {
                                list2 = listRenderAdornments;
                                r22 = i2;
                                f = 0.01f;
                            }
                            Object obj13 = instance.get("OutlineColor");
                            Color3 color5 = obj13 instanceof Color3 ? (Color3) obj13 : null;
                            if (color5 == null) {
                                color5 = new Color3(1.0f, 1.0f, 1.0f);
                            }
                            Object obj14 = instance.get("OutlineTransparency");
                            Number number5 = obj14 instanceof Number ? (Number) obj14 : null;
                            float fFloatValue5 = 1.0f - (number5 != null ? number5.floatValue() : 0.0f);
                            if (fFloatValue5 > f) {
                                GLES20.glLineWidth(2.5f);
                                Mesh mesh10 = this.boxLines;
                                if (mesh10 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                                    mesh10 = null;
                                }
                                bind(mesh10, this.laPos, -1);
                                int r24 = r22;
                                for (Instance instance4 : listAdorneeParts) {
                                    setModel(PartData.INSTANCE.cframe(instance4), PartData.INSTANCE.size(instance4).times(1.01f));
                                    GLES20.glUniformMatrix4fv(this.lModel, r24, false, this.model, 0);
                                    GLES20.glUniform4f(this.lColor, color5.getR(), color5.getG(), color5.getB(), fFloatValue5);
                                    Mesh mesh11 = this.boxLines;
                                    if (mesh11 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                                        mesh11 = null;
                                    }
                                    GLES20.glDrawArrays(1, 0, mesh11.getCount());
                                    r24 = 1;
                                }
                            }
                            if (i5 != 0) {
                                GLES20.glEnable(2929);
                                i3 = i4;
                                listRenderAdornments = list2;
                                i2 = 1;
                                z2 = false;
                            } else {
                                i3 = i4;
                                listRenderAdornments = list2;
                                i2 = 1;
                                z2 = false;
                            }
                        } else {
                            i3 = i4;
                        }
                        break;
                    case 1448617049:
                        if (className.equals("SelectionSphere")) {
                            list = listRenderAdornments;
                            obj2 = instance.get("Color3");
                            if (obj2 instanceof Color3) {
                                color3FromRGB = (Color3) obj2;
                            } else {
                                color3FromRGB = null;
                            }
                            if (color3FromRGB == null) {
                                color3FromRGB = Color3.INSTANCE.fromRGB(13, 105, 172);
                            }
                            obj3 = instance.get("Transparency");
                            if (obj3 instanceof Number) {
                                number = (Number) obj3;
                            } else {
                                number = null;
                            }
                            if (number != null) {
                                fFloatValue = number.floatValue();
                            } else {
                                fFloatValue = 0.0f;
                            }
                            f2 = 1.0f - fFloatValue;
                            obj4 = instance.get("LineThickness");
                            if (obj4 instanceof Number) {
                                number2 = (Number) obj4;
                            } else {
                                number2 = null;
                            }
                            if (number2 != null) {
                                fFloatValue2 = number2.floatValue();
                            } else {
                                fFloatValue2 = 0.15f;
                            }
                            GLES20.glLineWidth(RangesKt.coerceIn(20.0f * fFloatValue2, 1.5f, 6.0f));
                            mesh4 = this.boxLines;
                            if (mesh4 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                                mesh4 = null;
                            }
                            bind(mesh4, this.laPos, -1);
                            for (Instance instance2 : adorneeParts(instance)) {
                                setModel(PartData.INSTANCE.cframe(instance2), PartData.INSTANCE.size(instance2).plus(new Vector3(fFloatValue2, fFloatValue2, fFloatValue2)));
                                GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                                GLES20.glUniform4f(this.lColor, color3FromRGB.getR(), color3FromRGB.getG(), color3FromRGB.getB(), f2);
                                mesh5 = this.boxLines;
                                if (mesh5 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                                    mesh5 = null;
                                }
                                GLES20.glDrawArrays(1, 0, mesh5.getCount());
                            }
                            i3 = i4;
                            listRenderAdornments = list;
                            i2 = 1;
                            z2 = false;
                        } else {
                            i3 = i4;
                        }
                        break;
                    default:
                        list = listRenderAdornments;
                        i = i2;
                        z = z2;
                        i2 = i;
                        z2 = z;
                        i3 = i4;
                        listRenderAdornments = list;
                        break;
                }
            }
        }
        GLES20.glDisable(i3);
        GLES20.glLineWidth(1.0f);
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0026  */
    private final int upload(Bitmap bmp, boolean repeat) {
        Bitmap src;
        if (!repeat) {
            src = bmp;
        } else {
            int w = upload$pot(bmp.getWidth());
            int h = upload$pot(bmp.getHeight());
            if (w != bmp.getWidth() || h != bmp.getHeight()) {
                src = Bitmap.createScaledBitmap(bmp, w, h, true);
            } else {
                src = bmp;
            }
        }
        Intrinsics.checkNotNull(src);
        int[] ids = new int[1];
        GLES20.glGenTextures(1, ids, 0);
        GLES20.glBindTexture(3553, ids[0]);
        GLUtils.texImage2D(3553, 0, src, 0);
        int wrap = repeat ? 10497 : 33071;
        GLES20.glTexParameteri(3553, 10242, wrap);
        GLES20.glTexParameteri(3553, 10243, wrap);
        GLES20.glTexParameteri(3553, 10240, 9729);
        if (repeat) {
            GLES20.glGenerateMipmap(3553);
            GLES20.glTexParameteri(3553, 10241, 9987);
            if (this.aniso > 1.0f) {
                GLES20.glTexParameterf(3553, GL_TEXTURE_MAX_ANISOTROPY, this.aniso);
            }
        } else {
            GLES20.glTexParameteri(3553, 10241, 9729);
        }
        return ids[0];
    }

    private static final int upload$pot(int n) {
        int v = 1;
        while (v < n && v < 1024) {
            v *= 2;
        }
        return v;
    }

    private final int textureFor(String content, boolean repeat) {
        String k2;
        String str;
        String key = ImageLoader.INSTANCE.keyFor(content);
        if (key == null) {
            return 0;
        }
        if (!repeat) {
            k2 = key;
        } else {
            Map map = this.repeatKeys;
            String str2 = (String) map.get(key);
            if (str2 == null) {
                str = key + "#r";
                map.put(key, str);
            } else {
                str = str2;
            }
            k2 = str;
        }
        Integer num = this.textures.get(k2);
        if (num != null) {
            return num.intValue();
        }
        Bitmap bmp = ImageLoader.INSTANCE.getKey(key);
        if (bmp == null) {
            return 0;
        }
        int id = upload(bmp, repeat);
        this.textures.put(k2, Integer.valueOf(id));
        return id;
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class FaceQuad {
        private final float h;
        private final CFrame local;
        private final Vector3 normal;
        private final float w;

        public FaceQuad(CFrame local, float w, float h, Vector3 normal) {
            Intrinsics.checkNotNullParameter(local, "local");
            Intrinsics.checkNotNullParameter(normal, "normal");
            this.local = local;
            this.w = w;
            this.h = h;
            this.normal = normal;
        }

        public final float getH() {
            return this.h;
        }

        public final CFrame getLocal() {
            return this.local;
        }

        public final Vector3 getNormal() {
            return this.normal;
        }

        public final float getW() {
            return this.w;
        }
    }

    private final FaceQuad faceQuad(Geo g, int faceId, float lift) {
        Pair pair;
        Vector3 s = g.getScale();
        switch (faceId) {
            case 0:
                pair = TuplesKt.to(Vector3.INSTANCE.getX(), Vector3.INSTANCE.getY());
                break;
            case 1:
                pair = TuplesKt.to(Vector3.INSTANCE.getY(), new Vector3(0.0f, 0.0f, -1.0f));
                break;
            case 2:
                pair = TuplesKt.to(Vector3.INSTANCE.getZ(), Vector3.INSTANCE.getY());
                break;
            case 3:
                pair = TuplesKt.to(new Vector3(-1.0f, 0.0f, 0.0f), Vector3.INSTANCE.getY());
                break;
            case 4:
                pair = TuplesKt.to(new Vector3(0.0f, -1.0f, 0.0f), Vector3.INSTANCE.getZ());
                break;
            default:
                pair = TuplesKt.to(new Vector3(0.0f, 0.0f, -1.0f), Vector3.INSTANCE.getY());
                break;
        }
        Vector3 nrm = (Vector3) pair.component1();
        Vector3 up = (Vector3) pair.component2();
        Vector3 right = up.cross(nrm);
        float w = faceQuad$extent(s, right);
        float h = faceQuad$extent(s, up);
        float depth = faceQuad$extent(s, nrm);
        return new FaceQuad(CFrame.INSTANCE.fromColumns(nrm.times((depth / 2.0f) + lift), right, up, nrm), w, h, g.getCf().rotationOnly().times(nrm));
    }

    private static final float faceQuad$extent(Vector3 s, Vector3 v) {
        return (Math.abs(v.getX()) * s.getX()) + (Math.abs(v.getY()) * s.getY()) + (Math.abs(v.getZ()) * s.getZ());
    }

    private final long signature(Instance sg) {
        long h = 17;
        boolean images = false;
        Iterator it = Instance.descendants$default(sg, null, 1, null).iterator();
        while (true) {
            long j = 31;
            if (!it.hasNext()) {
                break;
            }
            Instance d = (Instance) it.next();
            long h2 = (h * 31) + ((long) d.getClassName().hashCode());
            String[] strArr = SIG_PROPS;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String k = strArr[i];
                long j2 = h2 * j;
                Object obj = d.getProps().get(k);
                h2 = j2 + ((long) (obj != null ? obj.hashCode() : 0));
                i++;
            }
            if (d.getProps().containsKey("Image")) {
                images = true;
            }
            h = h2 == 0 ? 1L : h2;
        }
        if (images) {
            return (h * 31) + ((long) ImageLoader.INSTANCE.getEpoch());
        }
        return h;
    }

    /* JADX WARN: Code duplicated, block: B:125:0x0261  */
    /* JADX WARN: Code duplicated, block: B:126:0x0264  */
    /* JADX WARN: Code duplicated, block: B:128:0x0267  */
    /* JADX WARN: Code duplicated, block: B:130:0x0276  */
    /* JADX WARN: Code duplicated, block: B:132:0x0281  */
    /* JADX WARN: Code duplicated, block: B:140:0x02d3  */
    /* JADX WARN: Code duplicated, block: B:141:0x02d9  */
    /* JADX WARN: Code duplicated, block: B:144:0x030e  */
    /* JADX WARN: Code duplicated, block: B:145:0x0332  */
    /* JADX WARN: Code duplicated, block: B:195:0x048b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:196:0x048b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:197:0x048b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:198:0x048b A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:28:0x0086  */
    /* JADX WARN: Code duplicated, block: B:36:0x00a7 A[PHI: r0
      0x00a7: PHI (r0v30 com.goydevv.studrblx.model.Instance) = (r0v28 com.goydevv.studrblx.model.Instance), (r0v143 com.goydevv.studrblx.model.Instance) binds: [B:34:0x0097, B:27:0x0084] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:38:0x00ba  */
    /* JADX WARN: Code duplicated, block: B:40:0x00d4  */
    /* JADX WARN: Code duplicated, block: B:41:0x00d7  */
    /* JADX WARN: Code duplicated, block: B:44:0x00db  */
    /* JADX WARN: Code duplicated, block: B:45:0x00e0  */
    /* JADX WARN: Code duplicated, block: B:48:0x00e9  */
    /* JADX WARN: Code duplicated, block: B:51:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:53:0x0104  */
    /* JADX WARN: Code duplicated, block: B:54:0x0107  */
    /* JADX WARN: Code duplicated, block: B:56:0x010a  */
    /* JADX WARN: Code duplicated, block: B:57:0x010f  */
    /* JADX WARN: Code duplicated, block: B:60:0x0122  */
    private final void drawSurfaceGuis() {
        int i;
        int i2;
        Geo g;
        float dist;
        Object obj;
        Number number;
        float fFloatValue;
        float maxD = 0.0f;
        Object obj2;
        EnumValue enumValue;
        int value;
        FaceQuad fq;
        Bitmap bitmap = null;
        boolean z = false;
        Bitmap bitmapCreateBitmap;
        int renders;
        int i3;
        int tex;
        int tex2;
        int bw;
        Bitmap bitmap2;
        Function4<? super Instance, ? super Float, ? super Float, ? super Bitmap, Unit> function4 = this.surfacePainter;
        if (function4 == null) {
            return;
        }
        Function4<? super Instance, ? super Float, ? super Float, ? super Bitmap, Unit> function5 = function4;
        List<Instance> listRenderSurfaceGuis = this.studio.renderSurfaceGuis();
        if (listRenderSurfaceGuis.isEmpty() && this.surfaceTex.isEmpty()) {
            return;
        }
        Vector3 cp = this.studio.getView().getCamPos();
        Vector3 fwd = this.studio.getCamera().getForward();
        Iterator<Instance> it = listRenderSurfaceGuis.iterator();
        int renders2 = 0;
        while (it.hasNext()) {
            Instance sg = it.next();
            if (sg.getParent() != null && !Intrinsics.areEqual(sg.get("Enabled"), (Object) false)) {
                Object obj3 = sg.get("Adornee");
                Instance parent = obj3 instanceof Instance ? (Instance) obj3 : null;
                if (parent == null) {
                    parent = sg.getParent();
                    if (parent == null || !parent.isA("BasePart")) {
                        parent = null;
                    }
                    if (parent != null) {
                        Instance part = parent;
                        g = this.geometry(part);
                        if (this.visible(g.getCf(), g.getScale(), cp, fwd)) {
                            dist = g.getCf().getPosition().minus(cp).getMagnitude();
                            obj = sg.get("MaxDistance");
                            if (obj instanceof Number) {
                                number = (Number) obj;
                            } else {
                                number = null;
                            }
                            if (number != null) {
                                fFloatValue = number.floatValue();
                            } else {
                                fFloatValue = 0.0f;
                            }
                            maxD = fFloatValue > 0.0f ? Math.min(fFloatValue, 450.0f) : 450.0f;
                            if (dist <= g.getScale().getMagnitude() + maxD) {
                                obj2 = sg.get("Face");
                                if (obj2 instanceof EnumValue) {
                                    enumValue = (EnumValue) obj2;
                                } else {
                                    enumValue = null;
                                }
                                if (enumValue != null) {
                                    value = enumValue.getValue();
                                } else {
                                    value = 5;
                                }
                                fq = this.faceQuad(g, value, 0.004f);
                                if (fq.getW() <= 0.01f && fq.getH() > 0.01f) {
                                    Object obj4 = sg.get("PixelsPerStud");
                                    Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
                                    float pps = RangesKt.coerceAtLeast(number2 != null ? number2.floatValue() : 50.0f, 1.0f);
                                    Object obj5 = sg.get("CanvasSize");
                                    Vector2 cs = obj5 instanceof Vector2 ? (Vector2) obj5 : null;
                                    if (cs == null) {
                                        cs = new Vector2(800.0f, 600.0f);
                                    }
                                    Object obj6 = sg.get("SizingMode");
                                    EnumValue enumValue2 = obj6 instanceof EnumValue ? (EnumValue) obj6 : null;
                                    boolean fixed = (enumValue2 != null ? enumValue2.getValue() : 0) == 0 && sg.getProps().containsKey("CanvasSize");
                                    float cw = fixed ? RangesKt.coerceAtLeast(cs.getX(), 1.0f) : fq.getW() * pps;
                                    float ch = fixed ? RangesKt.coerceAtLeast(cs.getY(), 1.0f) : fq.getH() * pps;
                                    float k = Math.min(1.0f, 384.0f / Math.max(cw, ch));
                                    Vector3 cp2 = cp;
                                    Vector3 fwd2 = fwd;
                                    int bw2 = RangesKt.coerceIn((int) (cw * k), 8, 1024);
                                    int bh = RangesKt.coerceIn((int) (ch * k), 8, 1024);
                                    SurfaceTex e = this.surfaceTex.get(sg);
                                    Iterator<Instance> it2 = it;
                                    if (e == null || (this.time - e.getChecked() > 0.5f && renders2 < 4)) {
                                        long sig = (this.signature(sg) * 31) + ((long) (bw2 * 7)) + ((long) bh);
                                        if (e != null && sig == e.getSig()) {
                                            e.setChecked(this.time);
                                        } else if (renders2 < 4 || e == null) {
                                            Bitmap bitmap3 = this.scratch;
                                            try {
                                                if (bitmap3 != null) {
                                                    bitmap = bitmap3;
                                                    if (bitmap3.getWidth() == bw2 && bitmap3.getHeight() == bh) {
                                                        z = true;
                                                    }
                                                    if (z) {
                                                        bitmapCreateBitmap = bitmap;
                                                    } else {
                                                        bitmapCreateBitmap = null;
                                                    }
                                                    if (bitmapCreateBitmap == null) {
                                                        bitmapCreateBitmap = Bitmap.createBitmap(bw2, bh, Bitmap.Config.ARGB_8888);
                                                        bitmap2 = this.scratch;
                                                        if (bitmap2 != null) {
                                                            bitmap2.recycle();
                                                        }
                                                        this.scratch = bitmapCreateBitmap;
                                                        Intrinsics.checkNotNullExpressionValue(bitmapCreateBitmap, "also(...)");
                                                    }
                                                    Bitmap bmp = bitmapCreateBitmap;
                                                    function5.invoke(sg, Float.valueOf(cw), Float.valueOf(ch), bitmap);
                                                    renders = renders2 + 1;
                                                    this.nSgRender++;
                                                    if (e != null) {
                                                        tex = e.getTex();
                                                        i3 = 0;
                                                    } else {
                                                        int[] iArr = new int[1];
                                                        i3 = 0;
                                                        GLES20.glGenTextures(1, iArr, 0);
                                                        tex = iArr[0];
                                                    }
                                                    tex2 = tex;
                                                    GLES20.glBindTexture(3553, tex2);
                                                    GLUtils.texImage2D(3553, i3, bmp, i3);
                                                    GLES20.glTexParameteri(3553, 10241, 9729);
                                                    GLES20.glTexParameteri(3553, 10240, 9729);
                                                    GLES20.glTexParameteri(3553, 10242, 33071);
                                                    GLES20.glTexParameteri(3553, 10243, 33071);
                                                    if (e == null) {
                                                        bw = bw2;
                                                        e = new SurfaceTex(tex2, bw2, bh, sig, this.time, this.time);
                                                        this.surfaceTex.put(sg, e);
                                                        renders2 = renders;
                                                    } else {
                                                        bw = bw2;
                                                        e.setW(bw);
                                                        e.setH(bh);
                                                        e.setSig(sig);
                                                        e.setChecked(this.time);
                                                        renders2 = renders;
                                                    }
                                                } else {
                                                    bitmap = bitmap3;
                                                }
                                                function5.invoke(sg, Float.valueOf(cw), Float.valueOf(ch), bitmap);
                                            } catch (Exception ex) {
                                                OutputLog.INSTANCE.warn("SurfaceGui " + sg.getName() + ": " + ex.getMessage());
                                            }
                                            z = false;
                                            if (z) {
                                                bitmapCreateBitmap = bitmap;
                                            } else {
                                                bitmapCreateBitmap = null;
                                            }
                                            if (bitmapCreateBitmap == null) {
                                                bitmapCreateBitmap = Bitmap.createBitmap(bw2, bh, Bitmap.Config.ARGB_8888);
                                                bitmap2 = this.scratch;
                                                if (bitmap2 != null) {
                                                    bitmap2.recycle();
                                                }
                                                this.scratch = bitmapCreateBitmap;
                                                Intrinsics.checkNotNullExpressionValue(bitmapCreateBitmap, "also(...)");
                                            }
                                            Bitmap bmp2 = bitmapCreateBitmap;
                                            renders = renders2 + 1;
                                            this.nSgRender++;
                                            if (e != null) {
                                                tex = e.getTex();
                                                i3 = 0;
                                            } else {
                                                int[] iArr2 = new int[1];
                                                i3 = 0;
                                                GLES20.glGenTextures(1, iArr2, 0);
                                                tex = iArr2[0];
                                            }
                                            tex2 = tex;
                                            GLES20.glBindTexture(3553, tex2);
                                            GLUtils.texImage2D(3553, i3, bmp2, i3);
                                            GLES20.glTexParameteri(3553, 10241, 9729);
                                            GLES20.glTexParameteri(3553, 10240, 9729);
                                            GLES20.glTexParameteri(3553, 10242, 33071);
                                            GLES20.glTexParameteri(3553, 10243, 33071);
                                            if (e == null) {
                                                bw = bw2;
                                                e = new SurfaceTex(tex2, bw2, bh, sig, this.time, this.time);
                                                this.surfaceTex.put(sg, e);
                                                renders2 = renders;
                                            } else {
                                                bw = bw2;
                                                e.setW(bw);
                                                e.setH(bh);
                                                e.setSig(sig);
                                                e.setChecked(this.time);
                                                renders2 = renders;
                                            }
                                        } else {
                                            e.setChecked(this.time);
                                        }
                                        bw = bw2;
                                    } else {
                                        bw = bw2;
                                    }
                                    SurfaceTex entry = e;
                                    entry.setUsed(this.time);
                                    GLES20.glBindTexture(3553, entry.getTex());
                                    Object obj7 = sg.get("LightInfluence");
                                    Number number3 = obj7 instanceof Number ? (Number) obj7 : null;
                                    float influence = RangesKt.coerceIn(number3 != null ? number3.floatValue() : 1.0f, 0.0f, 1.0f);
                                    Object obj8 = sg.get("Brightness");
                                    Number number4 = obj8 instanceof Number ? (Number) obj8 : null;
                                    float bright = RangesKt.coerceIn(number4 != null ? number4.floatValue() : 1.0f, 0.0f, 5.0f);
                                    GLES20.glUniform1f(this.cAmbient, ((this.ambient * influence) + (1.0f - influence)) * bright);
                                    GLES20.glUniform3f(this.cSun, this.sunColor[0] * influence * bright, this.sunColor[1] * influence * bright, this.sunColor[2] * influence * bright);
                                    GLES20.glUniform4f(this.cTint, 1.0f, 1.0f, 1.0f, 1.0f);
                                    this.setModel(g.getCf().times(fq.getLocal()), new Vector3(fq.getW(), fq.getH(), 1.0f));
                                    GLES20.glUniformMatrix4fv(this.cModel, 1, false, this.model, 0);
                                    GLES20.glUniform3f(this.cNormal, fq.getNormal().getX(), fq.getNormal().getY(), fq.getNormal().getZ());
                                    GLES20.glUniform4f(this.cUv, 1.0f, 1.0f, 0.0f, 0.0f);
                                    boolean onTop = Intrinsics.areEqual(sg.get("AlwaysOnTop"), (Object) true);
                                    if (onTop) {
                                        GLES20.glDisable(2929);
                                    }
                                    Mesh mesh = this.quad;
                                    if (mesh == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("quad");
                                        mesh = null;
                                    }
                                    this.drawUvMesh(mesh);
                                    if (onTop) {
                                        GLES20.glEnable(2929);
                                    }
                                    cp = cp2;
                                    fwd = fwd2;
                                    it = it2;
                                }
                            }
                        }
                    }
                } else {
                    if (!parent.isA("BasePart")) {
                        parent = null;
                    }
                    if (parent == null) {
                        parent = sg.getParent();
                        parent = parent != null ? null : null;
                        if (parent == null) {
                            Instance part2 = parent;
                            g = this.geometry(part2);
                            if (this.visible(g.getCf(), g.getScale(), cp, fwd)) {
                                dist = g.getCf().getPosition().minus(cp).getMagnitude();
                                obj = sg.get("MaxDistance");
                                if (obj instanceof Number) {
                                    number = (Number) obj;
                                } else {
                                    number = null;
                                }
                                if (number != null) {
                                    fFloatValue = number.floatValue();
                                } else {
                                    fFloatValue = 0.0f;
                                }
                                if (fFloatValue > 0.0f) {
                                }
                                if (dist <= g.getScale().getMagnitude() + maxD) {
                                    obj2 = sg.get("Face");
                                    if (obj2 instanceof EnumValue) {
                                        enumValue = (EnumValue) obj2;
                                    } else {
                                        enumValue = null;
                                    }
                                    if (enumValue != null) {
                                        value = enumValue.getValue();
                                    } else {
                                        value = 5;
                                    }
                                    fq = this.faceQuad(g, value, 0.004f);
                                    if (fq.getW() <= 0.01f) {
                                    }
                                }
                            }
                        }
                    } else {
                        Instance part3 = parent;
                        g = this.geometry(part3);
                        if (this.visible(g.getCf(), g.getScale(), cp, fwd)) {
                            dist = g.getCf().getPosition().minus(cp).getMagnitude();
                            obj = sg.get("MaxDistance");
                            if (obj instanceof Number) {
                                number = (Number) obj;
                            } else {
                                number = null;
                            }
                            if (number != null) {
                                fFloatValue = number.floatValue();
                            } else {
                                fFloatValue = 0.0f;
                            }
                            if (fFloatValue > 0.0f) {
                            }
                            if (dist <= g.getScale().getMagnitude() + maxD) {
                                obj2 = sg.get("Face");
                                if (obj2 instanceof EnumValue) {
                                    enumValue = (EnumValue) obj2;
                                } else {
                                    enumValue = null;
                                }
                                if (enumValue != null) {
                                    value = enumValue.getValue();
                                } else {
                                    value = 5;
                                }
                                fq = this.faceQuad(g, value, 0.004f);
                                if (fq.getW() <= 0.01f) {
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.surfaceTex.isEmpty()) {
            i = 1;
            i2 = 0;
        } else {
            Iterator<Map.Entry<Instance, SurfaceTex>> it3 = this.surfaceTex.entrySet().iterator();
            while (it3.hasNext()) {
                Map.Entry<Instance, SurfaceTex> next = it3.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                Map.Entry<Instance, SurfaceTex> entry2 = next;
                Instance k2 = entry2.getKey();
                SurfaceTex v = entry2.getValue();
                if (this.time - v.getUsed() > 6.0f || k2.getParent() == null) {
                    GLES20.glDeleteTextures(1, new int[]{v.getTex()}, 0);
                    it3.remove();
                }
            }
            i = 1;
            i2 = 0;
        }
        GLES20.glUniform3fv(this.cSun, i, this.sunColor, i2);
        GLES20.glUniform1f(this.cAmbient, this.ambient);
    }

    /* JADX INFO: compiled from: SceneRenderer.kt */
    private static final class Region {
        private final boolean flip;
        private final float h;
        private final float w;
        private final float x;
        private final float y;

        public Region(float x, float y, float w, float h, boolean flip) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.flip = flip;
        }

        public /* synthetic */ Region(float f, float f2, float f3, float f4, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(f, f2, f3, f4, (i & 16) != 0 ? false : z);
        }

        public final boolean getFlip() {
            return this.flip;
        }

        public final float getH() {
            return this.h;
        }

        public final float getW() {
            return this.w;
        }

        public final float getX() {
            return this.x;
        }

        public final float getY() {
            return this.y;
        }
    }

    private final void drawTemplateFace(Instance part, Geo g, int face, Region r) {
        FaceQuad fq = faceQuad(g, face, 0.003f);
        setModel(g.getCf().times(fq.getLocal()), new Vector3(fq.getW(), fq.getH(), 1.0f));
        GLES20.glUniformMatrix4fv(this.cModel, 1, false, this.model, 0);
        GLES20.glUniform3f(this.cNormal, fq.getNormal().getX(), fq.getNormal().getY(), fq.getNormal().getZ());
        float x = (r.getX() + 0.5f) / this.TW;
        float y = (r.getY() + 0.5f) / this.TH;
        float w = (r.getW() - 1.0f) / this.TW;
        float h = (r.getH() - 1.0f) / this.TH;
        if (r.getFlip()) {
            GLES20.glUniform4f(this.cUv, -w, -h, x + w, y + h);
        } else {
            GLES20.glUniform4f(this.cUv, w, h, x, y);
        }
        Mesh mesh = this.quad;
        if (mesh == null) {
            Intrinsics.throwUninitializedPropertyAccessException("quad");
            mesh = null;
        }
        drawUvMesh(mesh);
    }

    /* JADX WARN: Code duplicated, block: B:22:0x004a  */
    /* JADX WARN: Code duplicated, block: B:24:0x0052  */
    private final Pair<Boolean, Bitmap> clothingImage(String content) {
        Bitmap bitmapImageBlocking;
        String str = content;
        if (str == null || StringsKt.isBlank(str)) {
            return TuplesKt.to(true, null);
        }
        Long lIdOf = AssetFetcher.INSTANCE.idOf(content);
        if (lIdOf == null) {
            return TuplesKt.to(true, null);
        }
        long id = lIdOf.longValue();
        Context ctx = this.studio.getApp();
        if (RobloxApi.INSTANCE.isGuest(ctx)) {
            if (RobloxApi.INSTANCE.apiKey(ctx).length() > 0) {
                bitmapImageBlocking = AssetFetcher.INSTANCE.imageBlocking(id);
                if (bitmapImageBlocking != null) {
                    return TuplesKt.to(true, bitmapImageBlocking);
                }
            }
        } else {
            bitmapImageBlocking = AssetFetcher.INSTANCE.imageBlocking(id);
            if (bitmapImageBlocking != null) {
                return TuplesKt.to(true, bitmapImageBlocking);
            }
        }
        Bitmap b = ImageLoader.INSTANCE.get(content);
        return TuplesKt.to(Boolean.valueOf(b != null), b);
    }

    private final void r15Clothing(final Instance rig, List<? extends Instance> items) {
        Object next;
        Object next2;
        Object next3;
        String str;
        String strR15ColorKey;
        Object obj;
        String string;
        Object obj2;
        String string2;
        Object obj3;
        String string3;
        final Avatar av = Avatar.INSTANCE;
        Iterator it = items.iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!Intrinsics.areEqual(((Instance) next).getClassName(), "Shirt"));
        Instance instance = (Instance) next;
        final String shirt = (instance == null || (obj3 = instance.get("ShirtTemplate")) == null || (string3 = obj3.toString()) == null || StringsKt.isBlank(string3)) ? null : string3;
        Iterator it2 = items.iterator();
        do {
            if (!it2.hasNext()) {
                next2 = null;
                break;
            }
            next2 = it2.next();
        } while (!Intrinsics.areEqual(((Instance) next2).getClassName(), "Pants"));
        Instance instance2 = (Instance) next2;
        final String pants = (instance2 == null || (obj2 = instance2.get("PantsTemplate")) == null || (string2 = obj2.toString()) == null || StringsKt.isBlank(string2)) ? null : string2;
        Iterator it3 = items.iterator();
        do {
            if (!it3.hasNext()) {
                next3 = null;
                break;
            }
            next3 = it3.next();
        } while (!Intrinsics.areEqual(((Instance) next3).getClassName(), "ShirtGraphic"));
        Instance instance3 = (Instance) next3;
        final String tee = (instance3 == null || (obj = instance3.get("Graphic")) == null || (string = obj.toString()) == null || StringsKt.isBlank(string)) ? null : string;
        if (shirt == null && pants == null && tee == null) {
            return;
        }
        Iterable children = rig.getChildren();
        Collection arrayList = new ArrayList();
        for (Object obj4 : children) {
            if (((Instance) obj4).isA("BasePart")) {
                arrayList.add(obj4);
            }
        }
        List<Instance> parts = (List) arrayList;
        final HashMap colors = new HashMap();
        for (Instance p : parts) {
            String strR15GroupOf = av.r15GroupOf(p.getName());
            if (strR15GroupOf != null && (strR15ColorKey = av.r15ColorKey(strR15GroupOf)) != null) {
            }
        }
        Iterable iterableEntrySet = colors.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        final String key = shirt + "|" + pants + "|" + tee + "|" + CollectionsKt.joinToString(CollectionsKt.sortedWith(iterableEntrySet, new Comparator() { // from class: com.goydevv.studrblx.render.SceneRenderer$r15Clothing$$inlined$sortedBy$1
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues((String) ((Map.Entry) t).getKey(), (String) ((Map.Entry) t2).getKey());
            }
        }), ",", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.render.SceneRenderer$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj5) {
                return SceneRenderer.r15Clothing$lambda$85((Map.Entry) obj5);
            }
        });
        Map<String, String> map = this.r15Tex.get(key);
        if (map == null) {
            if (this.r15Pending.add(key)) {
                this.r15Pool.execute(new Runnable() { // from class: com.goydevv.studrblx.render.SceneRenderer$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            SceneRenderer.r15Clothing$lambda$87(SceneRenderer.this, shirt, pants, tee, key, av, colors, rig);
                        } catch (Throwable _t) {
                            if (_t instanceof RuntimeException) throw (RuntimeException) _t;
                            if (_t instanceof Error) throw (Error) _t;
                            throw new RuntimeException(_t);
                        }
                    }
                });
                return;
            }
            return;
        }
        for (Instance p2 : parts) {
            String g = av.r15GroupOf(p2.getName());
            if (g != null && (str = map.get(g)) != null) {
                this.nextOverride.put(p2, str);
            }
        }
    }

    static final CharSequence r15Clothing$lambda$85(Map.Entry it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getKey() + "=" + ((Color3) it.getValue()).toHex();
    }

    /* JADX WARN: Code duplicated, block: B:50:0x0138 A[Catch: all -> 0x016c, TryCatch #7 {all -> 0x016c, blocks: (B:20:0x00dc, B:48:0x012c, B:50:0x0138, B:52:0x013e, B:51:0x013b, B:21:0x00ec), top: B:58:0x0004 }] */
    /* JADX WARN: Code duplicated, block: B:51:0x013b A[Catch: all -> 0x016c, TryCatch #7 {all -> 0x016c, blocks: (B:20:0x00dc, B:48:0x012c, B:50:0x0138, B:52:0x013e, B:51:0x013b, B:21:0x00ec), top: B:58:0x0004 }] */
    static final void r15Clothing$lambda$87(SceneRenderer this$0, String $shirt, String $pants, String $tee, String $key, Avatar $av, HashMap $colors, Instance $rig) {
        try {
        String message;
        Object exc;
        try {
            try {
                Pair<Boolean, Bitmap> pairClothingImage = this$0.clothingImage($shirt);
                boolean s1 = pairClothingImage.component1().booleanValue();
                Bitmap sb = pairClothingImage.component2();
                try {
                    Pair<Boolean, Bitmap> pairClothingImage2 = this$0.clothingImage($pants);
                    boolean s2 = pairClothingImage2.component1().booleanValue();
                    Bitmap pb = pairClothingImage2.component2();
                    try {
                        Pair<Boolean, Bitmap> pairClothingImage3 = this$0.clothingImage($tee);
                        boolean s3 = pairClothingImage3.component1().booleanValue();
                        Bitmap tb = pairClothingImage3.component2();
                        if (s1 && s2 && s3) {
                            String tag = Integer.toHexString($key.hashCode());
                            try {
                                Map mapComposeClothingBitmaps$default = Avatar.composeClothingBitmaps$default($av, $colors, sb, pb, tb, null, 16, null);
                                int i = 0;
                                Map made = new LinkedHashMap(MapsKt.mapCapacity(mapComposeClothingBitmaps$default.size()));
                                for (Object obj : mapComposeClothingBitmaps$default.entrySet()) {
                                    boolean s4 = s1;
                                    Object key = ((Map.Entry) obj).getKey();
                                    Map.Entry entry = (Map.Entry) obj;
                                    String str = (String) entry.getKey();
                                    Bitmap bitmap = (Bitmap) entry.getValue();
                                    int i2 = i;
                                    Map map = mapComposeClothingBitmaps$default;
                                    String str2 = "mobilestudio-avatar://r15/" + tag + "/" + str;
                                    String tag2 = tag;
                                    try {
                                        $av.putTexture(str2, bitmap);
                                        made.put(key, str2);
                                        i = i2;
                                        s1 = s4;
                                        tag = tag2;
                                        mapComposeClothingBitmaps$default = map;
                                    } catch (Exception e) {
                                        OutputLog outputLog = OutputLog.INSTANCE;
                                        String name = $rig.getName();
                                        message = e.getMessage();
                                        if (message != null) {
                                            exc = message;
                                        } else {
                                            exc = e;
                                        }
                                        outputLog.warn("Clothing on " + name + ": " + exc);
                                        this$0.r15Tex.put($key, MapsKt.emptyMap());
                                        this$0.r15Pending.remove($key);
                                    }
                                }
                                this$0.r15Tex.put($key, made);
                            } catch (Exception e2) {
                            } catch (Throwable th) {
                                this$0.r15Pending.remove($key);
                                throw th;
                            }
                        }
                    } catch (Exception e3) {
                        OutputLog outputLog2 = OutputLog.INSTANCE;
                        String name2 = $rig.getName();
                        message = e3.getMessage();
                        if (message != null) {
                            exc = message;
                        } else {
                            exc = e3;
                        }
                        outputLog2.warn("Clothing on " + name2 + ": " + exc);
                        this$0.r15Tex.put($key, MapsKt.emptyMap());
                        this$0.r15Pending.remove($key);
                    } catch (Throwable th2) {
                        Throwable th = th2;
                        this$0.r15Pending.remove($key);
                        throw th;
                    }
                } catch (Exception e4) {
                    Exception e = e4;
                    OutputLog outputLog3 = OutputLog.INSTANCE;
                    String name3 = $rig.getName();
                    message = e.getMessage();
                    if (message != null) {
                        exc = message;
                    } else {
                        exc = e;
                    }
                    outputLog3.warn("Clothing on " + name3 + ": " + exc);
                    this$0.r15Tex.put($key, MapsKt.emptyMap());
                    this$0.r15Pending.remove($key);
                } catch (Throwable th3) {
                    Throwable th = th3;
                    this$0.r15Pending.remove($key);
                    throw th;
                }
            } catch (Throwable th4) {
                Throwable th = th4;
            }
        } catch (Exception e5) {
            Exception e = e5;
        } catch (Throwable th5) {
            Throwable th = th5;
        }
        this$0.r15Pending.remove($key);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final void drawClothing() {
        int i;
        Instance instance;
        boolean z;
        boolean z2;
        List limbs;
        Instance upper;
        String string;
        ArrayList arrayList;
        List<Instance> listRenderClothing = this.studio.renderClothing();
        if (listRenderClothing.isEmpty()) {
            if (this.partTexOverride.isEmpty()) {
                return;
            }
            this.partTexOverride = MapsKt.emptyMap();
            return;
        }
        this.nextOverride = new IdentityHashMap<>();
        Map linkedHashMap = new LinkedHashMap();
        for (Object obj : listRenderClothing) {
            Instance parent = ((Instance) obj).getParent();
            Object obj2 = linkedHashMap.get(parent);
            if (obj2 == null) {
                arrayList = new ArrayList();
                linkedHashMap.put(parent, arrayList);
            } else {
                arrayList = (ArrayList) obj2;
            }
            ((List) arrayList).add(obj);
        }
        Iterator it = linkedHashMap.entrySet().iterator();
        while (true) {
            i = 2;
            instance = null;
            z = false;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            Instance rig = (Instance) entry.getKey();
            List<? extends Instance> list = (List) entry.getValue();
            if (rig != null && (upper = Instance.findFirstChild$default(rig, "UpperTorso", false, 2, null)) != null) {
                if (!Intrinsics.areEqual(upper.getClassName(), "MeshPart")) {
                    upper = null;
                }
                if (upper != null) {
                    Object obj3 = upper.get("TextureID");
                    if (obj3 == null || (string = obj3.toString()) == null) {
                        string = "";
                    }
                    if (!StringsKt.startsWith(string, Avatar.TEX_PREFIX, false)) {
                        r15Clothing(rig, list);
                    }
                }
            }
        }
        this.partTexOverride = this.nextOverride;
        Vector3 cp = this.studio.getView().getCamPos();
        Vector3 fwd = this.studio.getCamera().getForward();
        PlaySession play = this.studio.getPlay();
        for (Object _cObj : CollectionsKt.sortedWith(listRenderClothing, new Comparator() { // from class: com.goydevv.studrblx.render.SceneRenderer$drawClothing$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                int i2;
                String className = ((Instance) t).getClassName();
                int i3 = 1;
                if (Intrinsics.areEqual(className, "Pants")) {
                    i2 = 0;
                } else {
                    i2 = Intrinsics.areEqual(className, "Shirt") ? 1 : 2;
                }
                Integer numValueOf = Integer.valueOf(i2);
                String className2 = ((Instance) t2).getClassName();
                if (Intrinsics.areEqual(className2, "Pants")) {
                    i3 = 0;
                } else if (!Intrinsics.areEqual(className2, "Shirt")) {
                    i3 = 2;
                }
                return ComparisonsKt.compareValues(numValueOf, Integer.valueOf(i3));
            }
        })) {
            Instance c = (Instance) _cObj;
            Instance rig2 = c.getParent();
            if (rig2 != null) {
                Instance torso = Instance.findFirstChild$default(rig2, "Torso", z, i, instance);
                if (torso != null) {
                    if (!torso.isA("BasePart")) {
                        torso = instance;
                    }
                    if (torso != null) {
                        if (PartData.INSTANCE.transparency(torso) >= 0.99f) {
                            i = 2;
                            instance = null;
                            z = false;
                        } else if ((play == null || !play.hiddenInFirstPerson(torso)) ? z : true) {
                            i = 2;
                            instance = null;
                            z = false;
                        } else {
                            Geo tg = geometry(torso);
                            if (visible(tg.getCf(), tg.getScale().times(3.0f), cp, fwd)) {
                                Object obj4 = c.get("ShirtTemplate");
                                if (obj4 == null && (obj4 = c.get("PantsTemplate")) == null) {
                                    obj4 = c.get("Graphic");
                                }
                                String content = obj4 != null ? obj4.toString() : null;
                                int tex = assetTexture(content, z);
                                if (tex != 0) {
                                    GLES20.glBindTexture(3553, tex);
                                    Object obj5 = c.get("Color3");
                                    Color3 tint = obj5 instanceof Color3 ? (Color3) obj5 : null;
                                    if (tint == null) {
                                        tint = new Color3(1.0f, 1.0f, 1.0f);
                                    }
                                    List<Instance> list2 = listRenderClothing;
                                    Vector3 cp2 = cp;
                                    Vector3 fwd2 = fwd;
                                    GLES20.glUniform4f(this.cTint, tint.getR(), tint.getG(), tint.getB(), 1.0f);
                                    if (Intrinsics.areEqual(c.getClassName(), "ShirtGraphic")) {
                                        FaceQuad fq = faceQuad(tg, 5, 0.006f);
                                        float s = Math.min(fq.getW(), fq.getH()) * 0.98f;
                                        setModel(tg.getCf().times(fq.getLocal()), new Vector3(s, s, 1.0f));
                                        GLES20.glUniformMatrix4fv(this.cModel, 1, false, this.model, 0);
                                        GLES20.glUniform3f(this.cNormal, fq.getNormal().getX(), fq.getNormal().getY(), fq.getNormal().getZ());
                                        GLES20.glUniform4f(this.cUv, 1.0f, 1.0f, 0.0f, 0.0f);
                                        Mesh mesh = this.quad;
                                        if (mesh == null) {
                                            Intrinsics.throwUninitializedPropertyAccessException("quad");
                                            mesh = null;
                                        }
                                        drawUvMesh(mesh);
                                        listRenderClothing = list2;
                                        cp = cp2;
                                        fwd = fwd2;
                                        i = 2;
                                        instance = null;
                                        z = false;
                                    } else {
                                        boolean shirt = Intrinsics.areEqual(c.getClassName(), "Shirt");
                                        for (Map.Entry<Integer, Region> entry2 : this.torsoRegions.entrySet()) {
                                            int f = entry2.getKey().intValue();
                                            Region r = entry2.getValue();
                                            drawTemplateFace(torso, tg, f, r);
                                        }
                                        if (shirt) {
                                            limbs = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Right Arm", this.rightLimbRegions), TuplesKt.to("Left Arm", this.leftLimbRegions)});
                                            z2 = true;
                                        } else {
                                            z2 = true;
                                            limbs = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Right Leg", this.rightLimbRegions), TuplesKt.to("Left Leg", this.leftLimbRegions)});
                                        }
                                        Iterator it2 = limbs.iterator();
                                        while (it2.hasNext()) {
                                            Pair pair = (Pair) it2.next();
                                            String name = (String) pair.component1();
                                            Map regions = (Map) pair.component2();
                                            Instance instanceFindFirstChild$default = Instance.findFirstChild$default(rig2, name, false, 2, null);
                                            if (instanceFindFirstChild$default != null) {
                                                if (!((!instanceFindFirstChild$default.isA("BasePart") || PartData.INSTANCE.transparency(instanceFindFirstChild$default) >= 0.99f) ? false : z2)) {
                                                    instanceFindFirstChild$default = null;
                                                }
                                                if (instanceFindFirstChild$default != null) {
                                                    Instance limb = instanceFindFirstChild$default;
                                                    Geo lg = geometry(limb);
                                                    if (lg.getKind() == 0) {
                                                        Mesh mesh2 = lg.getMesh();
                                                        String content2 = content;
                                                        Mesh mesh3 = this.cube;
                                                        if (mesh3 == null) {
                                                            Intrinsics.throwUninitializedPropertyAccessException("cube");
                                                            mesh3 = null;
                                                        }
                                                        if (mesh2 != mesh3) {
                                                            content = content2;
                                                        } else {
                                                            Iterator it3 = regions.entrySet().iterator();
                                                            while (it3.hasNext()) {
                                                                Map.Entry entry3 = (Map.Entry) it3.next();
                                                                Iterator it4 = it3;
                                                                int f2 = ((Number) entry3.getKey()).intValue();
                                                                Region r2 = (Region) entry3.getValue();
                                                                drawTemplateFace(limb, lg, f2, r2);
                                                                it3 = it4;
                                                            }
                                                            content = content2;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        listRenderClothing = list2;
                                        cp = cp2;
                                        fwd = fwd2;
                                        i = 2;
                                        instance = null;
                                        z = false;
                                    }
                                } else {
                                    i = 2;
                                    instance = null;
                                    z = false;
                                }
                            } else {
                                i = 2;
                                instance = null;
                                z = false;
                            }
                        }
                    }
                }
                i = 2;
                instance = null;
                z = false;
            }
        }
    }

    private final void refreshVariants() {
        LinkedHashMap linkedHashMapEmptyMap;
        Object use2022;
        Iterable iterableDescendants$default;
        Object obj;
        long now = System.currentTimeMillis();
        if (now - this.variantIndexAt < 500) {
            return;
        }
        this.variantIndexAt = now;
        this.variantBinds.clear();
        Instance ms = this.studio.getModel().findService("MaterialService");
        this.variantService = ms;
        if (ms == null || (iterableDescendants$default = Instance.descendants$default(ms, null, 1, null)) == null) {
            linkedHashMapEmptyMap = new LinkedHashMap();
        } else {
            Collection arrayList = new ArrayList();
            for (Object obj2 : iterableDescendants$default) {
                if (Intrinsics.areEqual(((Instance) obj2).getClassName(), "MaterialVariant")) {
                    arrayList.add(obj2);
                }
            }
            linkedHashMapEmptyMap = new LinkedHashMap();
            for (Object obj3 : (List) arrayList) {
                String name = ((Instance) obj3).getName();
                Object obj4 = linkedHashMapEmptyMap.get(name);
                if (obj4 == null) {
                    obj = (List) new ArrayList();
                    linkedHashMapEmptyMap.put(name, obj);
                } else {
                    obj = obj4;
                }
                ((List) obj).add(obj3);
            }
        }
        this.variantIndex = linkedHashMapEmptyMap;
        if (ms != null) {
            Object obj5 = ms.get("Use2022MaterialsXml");
            if (obj5 == null) {
                obj5 = ms.get("Use2022Materials");
            }
            use2022 = obj5;
        } else {
            use2022 = null;
        }
        boolean modern = false;
        if (Settings.INSTANCE.getModernMaterials() && !Intrinsics.areEqual(use2022, (Object) false)) {
            modern = true;
        }
        if (modern != this.modernMaterialsNow) {
            this.modernMaterialsNow = modern;
            this.batches.reset(true);
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x003b  */
    /* JADX WARN: Code duplicated, block: B:21:0x0045  */
    /* JADX WARN: Code duplicated, block: B:23:0x004a  */
    /* JADX WARN: Code duplicated, block: B:24:0x0062  */
    /* JADX WARN: Code duplicated, block: B:34:0x007d  */
    /* JADX WARN: Code duplicated, block: B:36:0x0080 A[RETURN] */
    private final Instance variantFor(Instance p) {
        String strItemName;
        String name;
        Instance instance;
        Object obj;
        Object obj2 = null;
        if (this.variantIndex.isEmpty()) {
            return null;
        }
        int mat = PartData.INSTANCE.material(p);
        Object obj3 = p.get("MaterialVariantSerialized");
        if (obj3 == null) {
            obj3 = p.get("MaterialVariant");
        }
        if (obj3 != null && (name = obj3.toString()) != null) {
            if (!(name.length() > 0)) {
                name = null;
            }
            if (name == null) {
                strItemName = Enums.INSTANCE.itemName("Material", mat);
                if (strItemName == null) {
                    name = null;
                } else {
                    instance = this.variantService;
                    if (instance != null) {
                        obj = instance.get(strItemName + "Name");
                    } else {
                        obj = null;
                    }
                    if (obj != null) {
                        name = null;
                    } else {
                        name = null;
                    }
                }
                if (name == null) {
                    return null;
                }
            }
        } else {
            strItemName = Enums.INSTANCE.itemName("Material", mat);
            if (strItemName == null) {
                name = null;
            } else {
                instance = this.variantService;
                if (instance != null) {
                    obj = instance.get(strItemName + "Name");
                } else {
                    obj = null;
                }
                if (obj == null || (name = obj.toString()) == null) {
                    name = null;
                } else if (!(name.length() > 0)) {
                    name = null;
                }
            }
            if (name == null) {
                return null;
            }
        }
        Iterable iterable = (List) this.variantIndex.get(name);
        if (iterable == null) {
            return null;
        }
        for (Object obj4 : iterable) {
            Object obj5 = ((Instance) obj4).get("BaseMaterial");
            EnumValue enumValue = obj5 instanceof EnumValue ? (EnumValue) obj5 : null;
            if ((enumValue != null ? enumValue.getValue() : mat) == mat) {
                obj2 = obj4;
                break;
            }
        }
        return (Instance) obj2;
    }

    /* JADX WARN: Code duplicated, block: B:54:0x0185  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r22v2 */
    /* JADX WARN: Type inference failed for: r22v3 */
    /* JADX WARN: Type inference failed for: r22v4 */
    /* JADX WARN: Type inference failed for: r26v0 */
    /* JADX WARN: Type inference failed for: r26v2 */
    /* JADX WARN: Type inference failed for: r26v3 */
    /* JADX WARN: Type inference failed for: r26v4 */
    /* JADX WARN: Type inference failed for: r26v5 */
    /* JADX WARN: Type inference failed for: r26v6 */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v35 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v41 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v24 */
    /* JADX WARN: Type inference failed for: r3v25 */
    /* JADX WARN: Type inference failed for: r6v18 */
    /* JADX WARN: Type inference failed for: r6v19 */
    /* JADX WARN: Type inference failed for: r6v27 */
    /* JADX WARN: Type inference failed for: r6v28 */
    /* JADX WARN: Type inference failed for: r6v29 */
    /* JADX WARN: Type inference failed for: r6v35 */
    private final void drawDecals() {
        int r6;
        int iAssetTexture;
        List<Instance> listRenderDecals = this.studio.renderDecals();
        boolean z = true;
        boolean z2 = false;
        boolean r2 = (this.studio.renderSurfaceGuis().isEmpty() && this.surfaceTex.isEmpty()) ? false : true;
        if (listRenderDecals.isEmpty() && r2 == false && this.studio.renderClothing().isEmpty()) {
            if (!this.partTexOverride.isEmpty()) {
                this.partTexOverride = MapsKt.emptyMap();
                return;
            }
            return;
        }
        GLES20.glUseProgram(this.decalProg);
        GLES20.glUniformMatrix4fv(this.cVP, 1, false, this.studio.getView().getVp(), 0);
        GLES20.glUniform3f(this.cLight, this.sunDir.getX(), this.sunDir.getY(), this.sunDir.getZ());
        GLES20.glUniform3fv(this.cSun, 1, this.sunColor, 0);
        GLES20.glUniform1f(this.cAmbient, this.ambient);
        GLES20.glUniform1i(this.cTex, 0);
        int i = 33984;
        GLES20.glActiveTexture(33984);
        int i2 = 3042;
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glDepthMask(false);
        int i3 = 2884;
        GLES20.glDisable(2884);
        int i4 = 32823;
        GLES20.glEnable(32823);
        GLES20.glPolygonOffset(-1.0f, -2.0f);
        Vector3 camPos = this.studio.getView().getCamPos();
        Vector3 forward = this.studio.getCamera().getForward();
        com.goydevv.studrblx.engine.PlaySession fpPlay = this.studio.getPlay();
        Instance fpChar = (fpPlay != null && fpPlay.getFirstPerson()) ? fpPlay.getCharacter() : null;
        for (Instance instance : listRenderDecals) {
            Instance parent = instance.getParent();
            if (parent != null) {
                if (fpChar != null && (parent == fpChar || parent.isDescendantOf(fpChar))) {
                    continue;
                }
                if (!parent.isA("BasePart")) {
                    z = true;
                    z2 = false;
                } else if (PartData.INSTANCE.transparency(parent) < 0.999f) {
                    Object obj = instance.get("Transparency");
                    int i5 = i;
                    Number number = obj instanceof Number ? (Number) obj : null;
                    int i6 = i2;
                    float fFloatValue = 1.0f - (number != null ? number.floatValue() : 0.0f);
                    if (fFloatValue > 0.001f) {
                        int i7 = i3;
                        Geo geoGeometry = geometry(parent);
                        int i8 = i4;
                        if (visible(geoGeometry.getCf(), geoGeometry.getScale(), camPos, forward)) {
                            PartData partData = PartData.INSTANCE;
                            String[] strArr = new String[4];
                            boolean z3 = z2;
                            strArr[z3 ? 1 : 0] = "Texture";
                            strArr[z ? 1 : 0] = "TextureContent";
                            strArr[2] = "ColorMap";
                            strArr[3] = "ColorMapContent";
                            String strContent = partData.content(instance, strArr);
                            if (Intrinsics.areEqual(instance.getName(), "face") && Intrinsics.areEqual(parent.getName(), "Head")) {
                                String str = strContent;
                                if (((str == null || str.length() == 0) ? z ? 1 : 0 : z3 ? 1 : 0) != 0 || StringsKt.startsWith(strContent, "rbxasset://", z) || ImageLoader.INSTANCE.keyFor(strContent) == null) {
                                    r6 = z ? 1 : 0;
                                } else {
                                    r6 = z3 ? 1 : 0;
                                }
                            } else {
                                r6 = z3 ? 1 : 0;
                            }
                            boolean zAreEqual = Intrinsics.areEqual(instance.getClassName(), "Texture");
                            if (r6 != 0) {
                                iAssetTexture = this.faceTex;
                            } else {
                                Long lIdOf = AssetFetcher.INSTANCE.idOf(strContent);
                                iAssetTexture = assetTexture(strContent, zAreEqual); if (iAssetTexture == 0 && lIdOf != null && lIdOf.longValue() == BASEPLATE_GRID_ID) { iAssetTexture = gridTexture(); }
                            }
                            if (iAssetTexture != 0) {
                                GLES20.glBindTexture(3553, iAssetTexture);
                                Object obj2 = instance.get("Color3");
                                Color3 color3 = obj2 instanceof Color3 ? (Color3) obj2 : null;
                                if (color3 == null) {
                                    color3 = new Color3(1.0f, 1.0f, 1.0f);
                                }
                                List<Instance> list = listRenderDecals;
                                boolean r26 = r2;
                                GLES20.glUniform4f(this.cTint, color3.getR(), color3.getG(), color3.getB(), fFloatValue);
                                Object obj3 = instance.get("Face");
                                EnumValue enumValue = obj3 instanceof EnumValue ? (EnumValue) obj3 : null;
                                int value = enumValue != null ? enumValue.getValue() : 5;
                                Mesh mesh = geoGeometry.getMesh();
                                Mesh mesh2 = this.head;
                                if (mesh2 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("head");
                                    mesh2 = null;
                                }
                                if (mesh != mesh2 || value != 5) {
                                    FaceQuad faceQuad = faceQuad(geoGeometry, value, 0.002f);
                                    float w = faceQuad.getW();
                                    float h = faceQuad.getH();
                                    setModel(geoGeometry.getCf().times(faceQuad.getLocal()), new Vector3(w, h, 1.0f));
                                    GLES20.glUniformMatrix4fv(this.cModel, 1, false, this.model, 0);
                                    GLES20.glUniform3f(this.cNormal, faceQuad.getNormal().getX(), faceQuad.getNormal().getY(), faceQuad.getNormal().getZ());
                                    if (!zAreEqual) {
                                        GLES20.glUniform4f(this.cUv, 1.0f, 1.0f, 0.0f, 0.0f);
                                    } else {
                                        Object obj4 = instance.get("StudsPerTileU");
                                        Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
                                        float fCoerceAtLeast = RangesKt.coerceAtLeast(number2 != null ? number2.floatValue() : 2.0f, 0.01f);
                                        Object obj5 = instance.get("StudsPerTileV");
                                        Number number3 = obj5 instanceof Number ? (Number) obj5 : null;
                                        float fCoerceAtLeast2 = RangesKt.coerceAtLeast(number3 != null ? number3.floatValue() : 2.0f, 0.01f);
                                        Object obj6 = instance.get("OffsetStudsU");
                                        Number number4 = obj6 instanceof Number ? (Number) obj6 : null;
                                        float fFloatValue2 = number4 != null ? number4.floatValue() : 0.0f;
                                        Object obj7 = instance.get("OffsetStudsV");
                                        Number number5 = obj7 instanceof Number ? (Number) obj7 : null;
                                        GLES20.glUniform4f(this.cUv, w / fCoerceAtLeast, h / fCoerceAtLeast2, fFloatValue2 / fCoerceAtLeast, (number5 != null ? number5.floatValue() : 0.0f) / fCoerceAtLeast2);
                                    }
                                    Mesh mesh3 = this.quad;
                                    if (mesh3 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("quad");
                                        mesh3 = null;
                                    }
                                    drawUvMesh(mesh3);
                                    i = i5;
                                    i2 = i6;
                                    i3 = i7;
                                    i4 = i8;
                                    listRenderDecals = list;
                                    r2 = r26;
                                    z = true;
                                    z2 = false;
                                } else {
                                    setModel(geoGeometry.getCf(), geoGeometry.getScale());
                                    GLES20.glUniformMatrix4fv(this.cModel, 1, z3, this.model, z3 ? 1 : 0);
                                    Vector3 lookVector = geoGeometry.getCf().getLookVector();
                                    GLES20.glUniform3f(this.cNormal, lookVector.getX(), lookVector.getY(), lookVector.getZ());
                                    GLES20.glUniform4f(this.cUv, 1.0f, 1.0f, 0.0f, 0.0f);
                                    Mesh mesh4 = this.headFace;
                                    if (mesh4 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("headFace");
                                        mesh4 = null;
                                    }
                                    drawUvMesh(mesh4);
                                    i = i5;
                                    i2 = i6;
                                    i3 = i7;
                                    i4 = i8;
                                    listRenderDecals = list;
                                    r2 = r26;
                                    z = true;
                                    z2 = false;
                                }
                            } else {
                                i = i5;
                                i2 = i6;
                                i3 = i7;
                                i4 = i8;
                                z = true;
                                z2 = false;
                            }
                        } else {
                            i = i5;
                            i2 = i6;
                            i3 = i7;
                            i4 = i8;
                            z = true;
                            z2 = false;
                        }
                    } else {
                        i = i5;
                        i2 = i6;
                        z = true;
                        z2 = false;
                    }
                }
            }
        }
        long jNanoTime = System.nanoTime();
        drawClothing();
        long jNanoTime2 = System.nanoTime();
        drawSurfaceGuis();
        this.tClo += (jNanoTime2 - jNanoTime) / 1000000.0f;
        this.tSg += (System.nanoTime() - jNanoTime2) / 1000000.0f;
        GLES20.glDisable(i4);
        GLES20.glEnable(i3);
        GLES20.glDepthMask(true);
        GLES20.glDisable(i2);
        GLES20.glActiveTexture(i);
        GLES20.glBindTexture(3553, this.shadowTex);
    }

    private final void drawUvMesh(Mesh m) {
        GLES20.glBindBuffer(34962, m.getVbo());
        GLES20.glEnableVertexAttribArray(this.caPos);
        GLES20.glVertexAttribPointer(this.caPos, 3, 5126, false, 20, 0);
        GLES20.glEnableVertexAttribArray(this.caUv);
        GLES20.glVertexAttribPointer(this.caUv, 2, 5126, false, 20, 12);
        GLES20.glDrawArrays(4, 0, m.getCount());
        GLES20.glDisableVertexAttribArray(this.caUv);
    }

    private final int gridTexture() {
        if (this.gridTex == 0) {
            Bitmap b = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
            Intrinsics.checkNotNullExpressionValue(b, "createBitmap(...)");
            Paint paint = new Paint(1);
            paint.setColor(-1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6.0f);
            new Canvas(b).drawRect(0.0f, 0.0f, 256, 256, paint);
            this.gridTex = upload(b, true);
        }
        int s = this.gridTex;
        return s;
    }

    private final Bitmap faceBitmap() {
        Bitmap b = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Intrinsics.checkNotNullExpressionValue(b, "createBitmap(...)");
        Canvas c = new Canvas(b);
        Paint p = new Paint(1);
        p.setColor(ViewCompat.MEASURED_STATE_MASK);
        c.drawOval(0.335f * 256, 256 * 0.3f, 0.405f * 256, 256 * 0.46f, p);
        c.drawOval(0.595f * 256, 256 * 0.3f, 0.665f * 256, 256 * 0.46f, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(256 * 0.038f);
        p.setStrokeCap(Paint.Cap.ROUND);
        Path smile = new Path();
        smile.moveTo(256 * 0.29f, 256 * 0.57f);
        smile.cubicTo(256 * 0.36f, 256 * 0.75f, 0.64f * 256, 256 * 0.75f, 256 * 0.71f, 256 * 0.57f);
        c.drawPath(smile, p);
        return b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void drawSelection() {
        if (this.studio.getSelection().isEmpty()) {
            return;
        }
        GLES20.glUseProgram(this.line);
        GLES20.glUniformMatrix4fv(this.lVP, 1, false, this.studio.getView().getVp(), 0);
        Mesh mesh = this.boxLines;
        List list = null;
        if (mesh == null) {
            Intrinsics.throwUninitializedPropertyAccessException("boxLines");
            mesh = null;
        }
        bind(mesh, this.laPos, -1);
        GLES20.glLineWidth(3.0f);
        float[] fArr = {0.1f, 0.65f, 1.0f, 1.0f};
        Iterator<Instance> it = this.studio.getSelection().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance next = it.next();
            if (next.isA("BasePart")) {
                setModel(PartData.INSTANCE.cframe(next), PartData.INSTANCE.size(next).times(1.004f).plus(new Vector3(0.04f, 0.04f, 0.04f)));
                GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                GLES20.glUniform4fv(this.lColor, 1, fArr, 0);
                Mesh mesh2 = this.boxLines;
                Mesh mesh3 = mesh2;
                if (mesh2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                    mesh3 = null;
                }
                GLES20.glDrawArrays(1, 0, mesh3.getCount());
            } else if (next.getIsService()) {
                list = null;
            } else if (next.isA("Model") || Intrinsics.areEqual(next.getClassName(), "Folder")) {
                List listDescendants$default = Instance.descendants$default(next, list, 1, list);
                ArrayList arrayList = new ArrayList();
                for (Object obj : listDescendants$default) {
                    if (((Instance) obj).isA("BasePart")) {
                        arrayList.add(obj);
                    }
                }
                ArrayList<Instance> arrayList2 = arrayList;
                Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(arrayList2);
                if (pairBoundsOf == null) {
                    list = null;
                } else {
                    Vector3 vector3Component1 = pairBoundsOf.component1();
                    Vector3 vector3Component2 = pairBoundsOf.component2();
                    setModel(CFrame.INSTANCE.at(vector3Component1.plus(vector3Component2).div(2.0f)), vector3Component2.minus(vector3Component1).plus(new Vector3(0.1f, 0.1f, 0.1f)));
                    GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                    GLES20.glUniform4fv(this.lColor, 1, new float[]{0.1f, 0.65f, 1.0f, 1.0f}, 0);
                    Mesh mesh4 = this.boxLines;
                    if (mesh4 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                        mesh4 = null;
                    }
                    GLES20.glDrawArrays(1, 0, mesh4.getCount());
                    GLES20.glUniform4fv(this.lColor, 1, new float[]{0.1f, 0.65f, 1.0f, 0.6f}, 0);
                    for (Instance instance : arrayList2) {
                        setModel(PartData.INSTANCE.cframe(instance), PartData.INSTANCE.size(instance).times(1.004f));
                        GLES20.glUniformMatrix4fv(this.lModel, 1, false, this.model, 0);
                        Mesh mesh5 = this.boxLines;
                        if (mesh5 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("boxLines");
                            mesh5 = null;
                        }
                        GLES20.glDrawArrays(1, 0, mesh5.getCount());
                    }
                    list = null;
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:31:0x012c A[PHI: r2
      0x012c: PHI (r2v7 com.goydevv.studrblx.render.Mesh) = (r2v5 com.goydevv.studrblx.render.Mesh), (r2v12 com.goydevv.studrblx.render.Mesh) binds: [B:28:0x0123, B:25:0x011b] A[DONT_GENERATE, DONT_INLINE]] */
    private final void drawHandles() {
        float f;
        Mesh mesh;
        Mesh tipMesh;
        HandleSet hs = Handles.INSTANCE.compute(this.studio);
        if (hs == null) {
            return;
        }
        int i = 2929;
        GLES20.glDisable(2929);
        GLES20.glUseProgram(this.lit);
        GLES20.glUniform1f(this.uGrid, 0.0f);
        GLES20.glUniform1f(this.uEmissive, 1.0f);
        GLES20.glUniform4f(this.uMat, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfA, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(this.uSurfB, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform1f(this.uTruss, 0.0f);
        GLES20.glUniform3f(this.uFog, 0.0f, 1.0E9f, 0.0f);
        for (Handle h : hs.getHandles()) {
            float[] col = Handles.INSTANCE.color(h.getIndex());
            Vector3 shaft = h.getTip().minus(h.getBase());
            float len = shaft.getMagnitude();
            int i2 = i;
            if (len > 0.001f) {
                Mesh mesh2 = this.cube;
                if (mesh2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("cube");
                    mesh2 = null;
                }
                f = 0.9f;
                bind(mesh2, this.aPos, this.aNrm);
                CFrame cf = CFrame.INSTANCE.lookAt(h.getBase().plus(h.getTip()).div(2.0f), h.getTip(), Math.abs(h.getAxis().getY()) > 0.9f ? Vector3.INSTANCE.getZ() : Vector3.INSTANCE.getY());
                setModel(cf, new Vector3(h.getRadius() * 0.3f, 0.3f * h.getRadius(), len));
                GLES20.glUniformMatrix4fv(this.uModel, 1, false, this.model, 0);
                GLES20.glUniformMatrix3fv(this.uNrm, 1, false, this.normal, 0);
                GLES20.glUniform4f(this.uColor, col[0], col[1], col[2], 1.0f);
                Mesh mesh3 = this.cube;
                if (mesh3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("cube");
                    mesh3 = null;
                }
                GLES20.glDrawArrays(4, 0, mesh3.getCount());
            } else {
                f = 0.9f;
            }
            if (hs.getTool() == Tool.SCALE) {
                mesh = this.cube;
                if (mesh == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("cube");
                    tipMesh = null;
                } else {
                    tipMesh = mesh;
                }
            } else {
                mesh = this.sphere;
                if (mesh == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sphere");
                    tipMesh = null;
                } else {
                    tipMesh = mesh;
                }
            }
            bind(tipMesh, this.aPos, this.aNrm);
            float d = h.getRadius() * 2.0f;
            CFrame cf2 = CFrame.INSTANCE.lookAt(h.getTip(), h.getTip().plus(h.getAxis()), Math.abs(h.getAxis().getY()) > f ? Vector3.INSTANCE.getZ() : Vector3.INSTANCE.getY());
            setModel(cf2, new Vector3(d, d, d));
            GLES20.glUniformMatrix4fv(this.uModel, 1, false, this.model, 0);
            GLES20.glUniformMatrix3fv(this.uNrm, 1, false, this.normal, 0);
            GLES20.glUniform4f(this.uColor, col[0], col[1], col[2], 1.0f);
            GLES20.glDrawArrays(4, 0, tipMesh.getCount());
            i = i2;
        }
        GLES20.glEnable(i);
    }

    private final int compile(int type, String src) {
        int s = GLES20.glCreateShader(type);
        GLES20.glShaderSource(s, src);
        GLES20.glCompileShader(s);
        int[] ok = new int[1];
        GLES20.glGetShaderiv(s, 35713, ok, 0);
        if (ok[0] == 0) {
            OutputLog.INSTANCE.error("Shader error: " + GLES20.glGetShaderInfoLog(s));
        }
        return s;
    }

    private final int program(String vs, String fs) {
        int p = GLES20.glCreateProgram();
        GLES20.glAttachShader(p, compile(35633, vs));
        GLES20.glAttachShader(p, compile(35632, fs));
        GLES20.glLinkProgram(p);
        int[] ok = new int[1];
        GLES20.glGetProgramiv(p, 35714, ok, 0);
        if (ok[0] == 0) {
            OutputLog.INSTANCE.error("Shader link error: " + GLES20.glGetProgramInfoLog(p));
        }
        return p;
    }
}
