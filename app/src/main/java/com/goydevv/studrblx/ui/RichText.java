package com.goydevv.studrblx.ui;

import android.text.SpannableStringBuilder;
import androidx.emoji2.text.EmojiCompat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: GuiOverlay.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RichText {
    public static final RichText INSTANCE = new RichText();
    private static final LinkedHashMap<String, CharSequence> cache = new LinkedHashMap<String, CharSequence>() { // from class: com.goydevv.studrblx.ui.RichText$cache$1
        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<String, CharSequence> eldest) {
            return size() > 200;
        }
    };

    private RichText() {
    }

    public final CharSequence parse(String src) {
        CharSequence charSequenceBuild;
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter(src, "src");
        synchronized (cache) {
            Map map = cache;
            CharSequence charSequence2 = (CharSequence) map.get(src);
            if (charSequence2 == null) {
                charSequenceBuild = INSTANCE.build(src);
                map.put(src, charSequenceBuild);
            } else {
                charSequenceBuild = charSequence2;
            }
            charSequence = charSequenceBuild;
        }
        return charSequence;
    }

    private final Integer parseColor(String v) {
        String s = StringsKt.trim((CharSequence) v).toString();
        if (StringsKt.startsWith(s, "#", false)) {
            Long longOrNull = StringsKt.toLongOrNull(StringsKt.removePrefix(s, (CharSequence) "#"), 16);
            if (longOrNull != null) {
                return Integer.valueOf((int) (4278190080L | longOrNull.longValue()));
            }
            return null;
        }
        MatchResult matchResultFind$default = (new Regex("rgb\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)")).find(s, 0);
        if (matchResultFind$default == null) {
            return null;
        }
        MatchResult.Destructured destructured = matchResultFind$default.getDestructured();
        return Integer.valueOf((RangesKt.coerceIn(Integer.parseInt(destructured.getMatch().getGroupValues().get(1)), 0, 255) << 16) | ViewCompat.MEASURED_STATE_MASK | (RangesKt.coerceIn(Integer.parseInt(destructured.getMatch().getGroupValues().get(2)), 0, 255) << 8) | RangesKt.coerceIn(Integer.parseInt(destructured.getMatch().getGroupValues().get(3)), 0, 255));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:35:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:49:0x0124  */
    /* JADX WARN: Code duplicated, block: B:71:0x01c4  */
    /* JADX WARN: Code duplicated, block: B:88:0x01f0 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:89:0x01db A[SYNTHETIC] */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final CharSequence build(String src) {
        Iterator it;
        Pair info;
        Object second;
        int idx;
        Object span = null;
        MatchResult matchResultFind$default;
        Integer color;
        IntRange range;
        src = src.replaceAll("(?s)<!--.*?-->", "");
        java.util.regex.Matcher ucM = java.util.regex.Pattern.compile("(?is)<(?:uppercase|uc)>(.*?)</(?:uppercase|uc)>").matcher(src);
        StringBuffer ucSb = new StringBuffer();
        while (ucM.find()) { ucM.appendReplacement(ucSb, java.util.regex.Matcher.quoteReplacement(ucM.group(1).toUpperCase(Locale.ROOT))); }
        ucM.appendTail(ucSb);
        src = ucSb.toString();
        SpannableStringBuilder out = new SpannableStringBuilder();
        ArrayList stack = new ArrayList();
        int i = 0;
        Regex tag = new Regex("<\\s*(/?)\\s*([a-zA-Z]+)([^>]*)>");
        while (i < src.length()) {
            MatchResult m = tag.find(src, i);
            int end = (m == null || (range = m.getRange()) == null) ? src.length() : range.getFirst();
            if (end > i) {
                String strSubstring = src.substring(i, end);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                out.append((CharSequence) decode(strSubstring));
            }
            if (m == null) {
                it = stack.iterator();
                Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                while (it.hasNext()) {
                    Object next = it.next();
                    Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                    info = (Pair) ((Pair) next).component2();
                    second = info.getSecond();
                    if (second != null) {
                        out.setSpan(second, ((Number) info.getFirst()).intValue(), out.length(), 33);
                    }
                }
                return out;
            }
            boolean closing = Intrinsics.areEqual(m.getGroupValues().get(1), "/");
            String name = m.getGroupValues().get(2).toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(name, "toLowerCase(...)");
            String attrs = m.getGroupValues().get(3);
            if (Intrinsics.areEqual(name, "br")) {
                out.append('\n');
            } else if (closing) {
                List list = stack;
                ListIterator listIterator = list.listIterator(list.size());
                while (true) {
                    if (!listIterator.hasPrevious()) {
                        idx = -1;
                        break;
                    } else if (Intrinsics.areEqual(((Pair) listIterator.previous()).getFirst(), name)) {
                        idx = listIterator.nextIndex();
                        break;
                    }
                }
                if (idx >= 0) {
                    Object objRemove = stack.remove(idx);
                    Intrinsics.checkNotNullExpressionValue(objRemove, "removeAt(...)");
                    Pair info2 = (Pair) ((Pair) objRemove).component2();
                    Object second2 = info2.getSecond();
                    if (second2 != null) {
                        out.setSpan(second2, ((Number) info2.getFirst()).intValue(), out.length(), 33);
                    }
                }
            } else {
                switch (name.hashCode()) {
                    case 98:
                        if (name.equals("b")) {
                            span = new StyleSpan(1);
                        }
                        break;
                    case 105:
                        if (name.equals("i")) {
                            span = new StyleSpan(2);
                        }
                        break;
                    case 115:
                        if (name.equals("s")) {
                            span = new StrikethroughSpan();
                        }
                        break;
                    case 117:
                        if (name.equals("u")) {
                            span = new UnderlineSpan();
                        }
                        break;
                    case 3726:
                        if (name.equals("uc")) {
                            span = null;
                        }
                        break;
                    case 3148879:
                        if (name.equals("font")) {
                            MatchResult cm = (new Regex("color\\s*=\\s*[\"']([^\"']+)[\"']")).find(attrs, 0);
                            if (cm != null && (color = INSTANCE.parseColor(cm.getGroupValues().get(1))) != null) {
                                span = new ForegroundColorSpan(color.intValue());
                            } else {
                                MatchResult sm = (new Regex("size\\s*=\\s*[\"'](\\d+)[\"']")).find(attrs, 0);
                                if (sm != null) {
                                    int px = RangesKt.coerceIn(Integer.parseInt(sm.getGroupValues().get(1)), 6, 120);
                                    span = new android.text.style.AbsoluteSizeSpan(px, true);
                                } else {
                                    span = null;
                                }
                            }
                        } else {
                            span = null;
                        }
                        break;
                    case 3344077:
                        if (name.equals("mark")) {
                            MatchResult mm = (new Regex("color\\s*=\\s*[\"']([^\"']+)[\"']")).find(attrs, 0);
                            if (mm != null && (color = INSTANCE.parseColor(mm.getGroupValues().get(1))) != null) {
                                span = new android.text.style.BackgroundColorSpan(color.intValue());
                            } else {
                                span = new android.text.style.BackgroundColorSpan(0x66FFFF00);
                            }
                        } else {
                            span = null;
                        }
                        break;
                    case 223523538:
                        if (name.equals("uppercase")) {
                            span = null;
                        }
                        break;
                    default:
                        span = null;
                        break;
                }
                stack.add(TuplesKt.to(name, TuplesKt.to(Integer.valueOf(out.length()), span)));
            }
            i = m.getRange().getLast() + 1;
        }
        it = stack.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Object next2 = it.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            info = (Pair) ((Pair) next2).component2();
            second = info.getSecond();
            if (second != null) {
                out.setSpan(second, ((Number) info.getFirst()).intValue(), out.length(), 33);
            }
        }
        return out;
    }

    private final String decode(String s) {
        return StringsKt.replace(StringsKt.replace(StringsKt.replace(StringsKt.replace(StringsKt.replace(s, "&lt;", "<", false), "&gt;", ">", false), "&quot;", "\"", false), "&apos;", "'", false), "&amp;", "&", false);
    }
}
