package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.core.Theme;
import com.goydevv.studrblx.lua.luau.LuauState;
import com.goydevv.studrblx.model.Instance;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.Typography;

/* JADX INFO: compiled from: ScriptEditor.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class ScriptEditor extends LinearLayout {
    private static final Set<String> builtins;
    private static final Map<Character, Character> pairs;
    private int autoClosedAt;
    private final Runnable commitRunnable;
    private final Runnable completeRunnable;
    private Autocomplete.Result completion;
    private boolean dirtyText;
    private CodeEdit editor;
    private final Runnable highlightRunnable;
    private boolean highlighting;
    private HorizontalScrollView hsv;
    private int lineCount;
    private final TextView lines;
    private PopupWindow popup;
    private ListView popupList;
    private final Instance script;
    private ScrollView scroll;
    private int selectedIdx;
    private TextView status;
    private final Studio studio;
    private boolean suppressCompletion;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Set<String> keywords = SetsKt.setOf(new String[]{"and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if", "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while", "continue", "export", "type", "self"});

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ScriptEditor(Context ctx, Studio studio, Instance script) {
        super(ctx);
String str;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(script, "script");
        this.studio = studio;
        this.script = script;
        this.highlightRunnable = new Runnable() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ScriptEditor.this.highlight();
            }
        };
        this.commitRunnable = new Runnable() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ScriptEditor.this.commit();
            }
        };
        this.completeRunnable = new Runnable() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ScriptEditor.this.updateCompletion(false);
            }
        };
        this.autoClosedAt = -1;
        setOrientation(1);
        setBackgroundColor(Ui.INSTANCE.getEDITOR_BG());
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setBackgroundColor(Ui.INSTANCE.getHEADER());
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, "", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setPadding(Ui.INSTANCE.dp(ctx, 8.0f), 0, Ui.INSTANCE.dp(ctx, 8.0f), 0);
        this.status = textViewText$default;
        ImageView imageView = new ImageView(ctx);
        imageView.setImageDrawable(Ui.INSTANCE.icon(ctx, this.script));
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(ctx, 16.0f), Ui.INSTANCE.dp(ctx, 16.0f), 0.0f, 4, null);
        layoutParamsLp$default.leftMargin = Ui.INSTANCE.dp(ctx, 8.0f);
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(imageView, layoutParamsLp$default);
        TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, this.script.fullName(), 12.0f, 0, false, 24, null);
        textViewText$default2.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), 0, 0, 0);
        linearLayout.addView(textViewText$default2, Ui.INSTANCE.lp(0, -2, 1.0f));
        linearLayout.addView(this.status);
        linearLayout.addView(Ui.INSTANCE.iconTextButton(ctx, Icons.INSTANCE.general(ctx, "ScriptAnalysis", "Chk"), "Check", 0, new Function1() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ScriptEditor._init_$lambda$8(ScriptEditor.this, (View) obj);
            }
        }));
        linearLayout.addView(Ui.iconButton$default(Ui.INSTANCE, ctx, Icons.INSTANCE.general(ctx, "FindAll", "Find"), 0.0f, new Function1() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ScriptEditor._init_$lambda$9(ScriptEditor.this, (View) obj);
            }
        }, 4, null));
        addView(linearLayout, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        float fontSize = Settings.INSTANCE.getEditorFontSize();
        TextView textView = new TextView(ctx);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setTextSize(fontSize);
        textView.setTextColor(Ui.INSTANCE.blend(Ui.INSTANCE.getEDITOR_GUTTER(), Ui.INSTANCE.getEDITOR_TEXT(), 0.45f));
        textView.setGravity(GravityCompat.END);
        textView.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 8.0f));
        textView.setBackgroundColor(Ui.INSTANCE.getEDITOR_GUTTER());
        this.lines = textView;
        CodeEdit codeEdit = new CodeEdit(this, ctx);
        codeEdit.setTypeface(Typeface.MONOSPACE);
        codeEdit.setTextSize(fontSize);
        codeEdit.setTextColor(Ui.INSTANCE.getEDITOR_TEXT());
        codeEdit.setBackground(null);
        codeEdit.setGravity(8388659);
        codeEdit.setInputType(655361);
        codeEdit.setHorizontallyScrolling(false);
        codeEdit.setPadding(Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 24.0f), Ui.INSTANCE.dp(ctx, 200.0f));
        codeEdit.setMinWidth(codeEdit.getResources().getDisplayMetrics().widthPixels);
        synchronized (this.studio.getLock()) {
            try {
                Object obj = this.script.get("Source");
                if (obj instanceof String) {
                    try {
                        str = (String) obj;
                    } catch (Throwable th) {
                        throw th;
                    }
                } else {
                    str = null;
                }
                codeEdit.setText(str == null ? "" : str);
                this.editor = codeEdit;
                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx);
                horizontalScrollView.setFillViewport(true);
                horizontalScrollView.addView(this.editor);
                this.hsv = horizontalScrollView;
                LinearLayout linearLayout2 = new LinearLayout(ctx);
                linearLayout2.setOrientation(0);
                linearLayout2.addView(this.lines, Ui.lp$default(Ui.INSTANCE, -2, -1, 0.0f, 4, null));
                linearLayout2.addView(this.hsv, Ui.INSTANCE.lp(0, -2, 1.0f));
                ScrollView scrollView = new ScrollView(ctx);
                scrollView.setFillViewport(true);
                scrollView.addView(linearLayout2);
                this.scroll = scrollView;
                addView(this.scroll, Ui.INSTANCE.lp(-1, 0, 1.0f));
                this.scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda8
                    @Override // android.view.ViewTreeObserver.OnScrollChangedListener
                    public final void onScrollChanged() {
                        ScriptEditor._init_$lambda$16(ScriptEditor.this);
                    }
                });
                LinearLayout symbols = new LinearLayout(ctx);
                symbols.setOrientation(0);
                symbols.setBackgroundColor(Ui.INSTANCE.getHEADER());
                for (final String s : CollectionsKt.listOf(new String[]{"Tab", "(", ")", "{", "}", "[", "]", "=", "\"", ":", ".", ",", "~", "#", "end", "local", "function", "then"})) {
                    symbols.addView(Ui.INSTANCE.smallIconButton(ctx, s, new Function1() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda9
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj2) {
                            return ScriptEditor._init_$lambda$18(ScriptEditor.this, s, (View) obj2);
                        }
                    }));
                }
                HorizontalScrollView horizontalScrollView2 = new HorizontalScrollView(ctx);
                horizontalScrollView2.addView(symbols);
                addView(horizontalScrollView2, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
                ListView listView = new ListView(ctx);
                listView.setDivider(null);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Ui.INSTANCE.blend(Ui.INSTANCE.getEDITOR_BG(), Ui.INSTANCE.getLIGHT() ? -16777216 : -1, 0.07f));
                gradientDrawable.setStroke(Ui.INSTANCE.dp(ctx, 1.0f), Ui.INSTANCE.getBORDER());
                gradientDrawable.setCornerRadius(Ui.INSTANCE.dp(ctx, 4.0f));
                listView.setBackground(gradientDrawable);
                listView.setAdapter((ListAdapter) new CompletionAdapter());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda10
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                        ScriptEditor.this.acceptCompletion(i);
                    }
                });
                this.popupList = listView;
                this.editor.addTextChangedListener(new TextWatcher() { // from class: com.goydevv.studrblx.ui.ScriptEditor.15
                    private boolean autoIndent;
                    private Character typed;
                    private int typedAt = -1;

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s2, int start, int count, int after) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s2, int start, int before, int count) {
                        this.autoIndent = count == 1 && before == 0 && s2 != null && start < s2.length() && s2.charAt(start) == '\n';
                        this.typed = (count != 1 || before != 0 || s2 == null || start >= s2.length()) ? null : Character.valueOf(s2.charAt(start));
                        this.typedAt = start;
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s2) {
                        try {
                            if (ScriptEditor.this.highlighting || s2 == null) {
                                return;
                            }
                            if (this.autoIndent && Settings.INSTANCE.getAutoIndent()) {
                                this.autoIndent = false;
                                ScriptEditor.this.handleNewline(s2);
                            } else if (this.typed != null && Settings.INSTANCE.getAutoCloseBrackets()) {
                                ScriptEditor scriptEditor = ScriptEditor.this;
                                Character ch = this.typed;
                                Intrinsics.checkNotNull(ch);
                                scriptEditor.handleTypedChar(s2, ch.charValue(), this.typedAt);
                            }
                            this.typed = null;
                            ScriptEditor.this.dirtyText = true;
                            ScriptEditor.this.updateLines();
                            ScriptEditor.this.removeCallbacks(ScriptEditor.this.highlightRunnable);
                            ScriptEditor.this.postDelayed(ScriptEditor.this.highlightRunnable, 250L);
                            ScriptEditor.this.removeCallbacks(ScriptEditor.this.commitRunnable);
                            ScriptEditor.this.postDelayed(ScriptEditor.this.commitRunnable, 800L);
                            if (Settings.INSTANCE.getAutocomplete() && !ScriptEditor.this.suppressCompletion) {
                                ScriptEditor.this.removeCallbacks(ScriptEditor.this.completeRunnable);
                                ScriptEditor.this.postDelayed(ScriptEditor.this.completeRunnable, 70L);
                            }
                        } catch (Throwable _t) {
                            if (_t instanceof RuntimeException) throw (RuntimeException) _t;
                            if (_t instanceof Error) throw (Error) _t;
                            throw new RuntimeException(_t);
                        }
                    }
                });
                updateLines();
                highlight();
            } catch (Throwable th2) {
                Throwable th = th2;
            }
        }
    }

    public final Instance getScript() {
        return this.script;
    }

    /* JADX INFO: compiled from: ScriptEditor.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

    }

    static {
        Character chValueOf = Character.valueOf(Typography.quote);
        builtins = SetsKt.setOf(new String[]{"game", "workspace", "script", "plugin", "Instance", "Vector3", "Vector2", "CFrame", "Color3", "BrickColor", "UDim", "UDim2", "Enum", "TweenInfo", "Ray", "task", "wait", "print", "warn", "error", "pairs", "ipairs", "math", "string", "table", "require", "typeof", "type", "tostring", "tonumber", "pcall", "xpcall", "spawn", "delay", "tick", "time", "os", "coroutine", "setmetatable", "getmetatable", "rawget", "rawset", "select", "next", "unpack", "assert", "shared", "_G", "NumberRange", "NumberSequence", "ColorSequence", "Random", "RaycastParams", "OverlapParams", "utf8", "bit32", "debug", "buffer", "DateTime"});
        pairs = MapsKt.mapOf(TuplesKt.to('(', ')'), TuplesKt.to('[', ']'), TuplesKt.to('{', '}'), TuplesKt.to(chValueOf, chValueOf), TuplesKt.to('\'', '\''));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: ScriptEditor.kt */
    final class CodeEdit extends EditText {
        final /* synthetic */ ScriptEditor this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CodeEdit(ScriptEditor this$0, Context c) {
            super(c);
            Intrinsics.checkNotNullParameter(c, "c");
            this.this$0 = this$0;
        }

        @Override // android.widget.TextView
        protected void onSelectionChanged(int selStart, int selEnd) {
            super.onSelectionChanged(selStart, selEnd);
            if (this.this$0 == null) {
                return;
            }
            Autocomplete.Result r = this.this$0.completion;
            if (r != null) {
                if (selStart != selEnd || selStart < r.getStart() || selStart > r.getEnd() + 1) {
                    this.this$0.hidePopup();
                }
            }
        }

        @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            Intrinsics.checkNotNullParameter(event, "event");
            if (this.this$0 == null) {
                return super.onKeyDown(keyCode, event);
            }
            PopupWindow popupWindow = this.this$0.popup;
            boolean z = false;
            if (popupWindow != null && popupWindow.isShowing()) {
                z = true;
            }
            if (z) {
                switch (keyCode) {
                    case 19:
                        this.this$0.moveSelection(-1);
                        return true;
                    case 20:
                        this.this$0.moveSelection(1);
                        return true;
                    case 61:
                    case 66:
                        this.this$0.acceptCompletion(this.this$0.selectedIdx);
                        return true;
                    case 111:
                        this.this$0.hidePopup();
                        return true;
                }
            }
            if (keyCode == 62 && event.isCtrlPressed()) {
                this.this$0.updateCompletion(true);
                return true;
            }
            if (keyCode == 61) {
                this.this$0.insert("    ");
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    static final Unit _init_$lambda$8(ScriptEditor this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.checkSyntax();
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$9(ScriptEditor this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.find();
        return Unit.INSTANCE;
    }

    static final void _init_$lambda$16(ScriptEditor this$0) {
        PopupWindow popupWindow = this$0.popup;
        boolean z = false;
        if (popupWindow != null && popupWindow.isShowing()) {
            z = true;
        }
        if (z) {
            this$0.positionPopup();
        }
    }

    static final Unit _init_$lambda$18(ScriptEditor this$0, String $s, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.insert(Intrinsics.areEqual($s, "Tab") ? "    " : $s);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:136:0x0267  */
    /* JADX WARN: Code duplicated, block: B:161:0x02dc  */
    /* JADX WARN: Code duplicated, block: B:172:0x010a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:182:0x012c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:46:0x00e5  */
    /* JADX WARN: Code duplicated, block: B:50:0x0105  */
    /* JADX WARN: Code duplicated, block: B:51:0x0107  */
    /* JADX WARN: Code duplicated, block: B:57:0x011c A[Catch: all -> 0x02f0, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x02f0, blocks: (B:48:0x00fc, B:57:0x011c), top: B:170:0x00fc }] */
    /* JADX WARN: Code duplicated, block: B:63:0x0132  */
    /* JADX WARN: Code duplicated, block: B:66:0x013b A[Catch: all -> 0x01ab, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x01ab, blocks: (B:61:0x012c, B:77:0x0165, B:85:0x0183, B:87:0x019c, B:88:0x01a2, B:66:0x013b), top: B:182:0x012c }] */
    /* JADX WARN: Code duplicated, block: B:70:0x0145  */
    /* JADX WARN: Code duplicated, block: B:72:0x0148 A[Catch: all -> 0x02e8, TRY_ENTER, TryCatch #5 {all -> 0x02e8, blocks: (B:59:0x0126, B:72:0x0148, B:74:0x014e, B:79:0x016a, B:94:0x01b8, B:64:0x0135), top: B:180:0x0126 }] */
    public final void handleNewline(Editable s) {
        try {
        String prevLine;
        String baseIndent;
        boolean opensFunction;
        boolean opensBlock = false;
        String indent = null;
        boolean z;
        boolean needsEnd;
        String nextLine;
        boolean closed;
        String close = "end";
        int pos = this.editor.getSelectionStart();
        if (pos <= 0) {
            return;
        }
        int iLastIndexOf$default = StringsKt.lastIndexOf((CharSequence) s, '\n', pos - 2, false);
        int prevStart = iLastIndexOf$default < 0 ? 0 : iLastIndexOf$default + 1;
        String prevLine2 = s.subSequence(prevStart, Math.max(prevStart, pos - 1)).toString();
        String baseIndent2 = prevLine2;
        int length = baseIndent2.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = baseIndent2.charAt(i);
            if (((cCharAt == ' ' || cCharAt == '\t') ? (char) 1 : (char) 0) == 0) {
                String strSubstring = baseIndent2.substring(0, i);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                baseIndent2 = strSubstring;
                break;
            }
        }
        String t = StringsKt.trim((CharSequence) prevLine2).toString();
        boolean opensFunction2 = new Regex("(^|[^\\w])function\\s*[\\w.:]*\\s*\\([^)]*\\)\\s*$").containsMatchIn(t);
        try {
            if (StringsKt.endsWith(t, " then", false) || Intrinsics.areEqual(t, "then")) {
                prevLine = prevLine2;
                baseIndent = baseIndent2;
                opensFunction = opensFunction2;
            } else {
                prevLine = prevLine2;
                opensFunction = opensFunction2;
                if (!StringsKt.endsWith(t, " do", false) && !Intrinsics.areEqual(t, "do") && !opensFunction && !Intrinsics.areEqual(t, "repeat")) {
                    baseIndent = baseIndent2;
                    if (!StringsKt.endsWith(t, "{", false) && !Intrinsics.areEqual(t, "else") && (!StringsKt.startsWith(t, "elseif", false) || !StringsKt.endsWith(t, "then", false))) {
                        opensBlock = false;
                    }
                    indent = baseIndent;
                    if (opensBlock) {
                        indent = indent + "    ";
                    }
                    this.suppressCompletion = true;
                    if (indent.length() > 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        int cur = indent.length() + pos;
                        if (StringsKt.endsWith(t, " then", false)) {
                            if (StringsKt.endsWith(t, " do", false)) {
                                needsEnd = true;
                            } else {
                                needsEnd = true;
                            }
                            if (!needsEnd) {
                            }
                            this.suppressCompletion = false;
                            return;
                        }
                        if (StringsKt.startsWith(t, "elseif", false)) {
                            needsEnd = true;
                        } else if (StringsKt.endsWith(t, " do", false)) {
                            needsEnd = true;
                        } else {
                            needsEnd = true;
                        }
                        if (!needsEnd) {
                        }
                        this.suppressCompletion = false;
                        return;
                    }
                    try {
                        s.insert(pos, indent);
                        int cur2 = indent.length() + pos;
                        try {
                            if (StringsKt.endsWith(t, " then", false)) {
                                if (StringsKt.endsWith(t, " do", false)) {
                                    needsEnd = true;
                                } else {
                                    needsEnd = true;
                                }
                                if (!needsEnd) {
                                }
                                this.suppressCompletion = false;
                                return;
                            }
                            try {
                                if (StringsKt.startsWith(t, "elseif", false)) {
                                    needsEnd = true;
                                } else if (StringsKt.endsWith(t, " do", false) || Intrinsics.areEqual(t, "do") || opensFunction) {
                                    needsEnd = true;
                                } else {
                                    needsEnd = false;
                                }
                                if (!needsEnd && !Intrinsics.areEqual(t, "else")) {
                                    int restLineEnd = StringsKt.indexOf((CharSequence) s, '\n', cur2, false);
                                    if (restLineEnd < 0) {
                                        restLineEnd = s.length();
                                    }
                                    String afterCursor = s.subSequence(cur2, restLineEnd).toString();
                                    int nextLineStart = restLineEnd < s.length() ? restLineEnd + 1 : -1;
                                    if (nextLineStart >= 0) {
                                        Editable editable = s;
                                        int iIndexOf$default = StringsKt.indexOf((CharSequence) s, '\n', nextLineStart, false);
                                        if (iIndexOf$default < 0) {
                                            iIndexOf$default = s.length();
                                        }
                                        nextLine = editable.subSequence(nextLineStart, iIndexOf$default).toString();
                                    } else {
                                        nextLine = "";
                                    }
                                    String str = nextLine;
                                    int length2 = str.length();
                                    int i2 = 0;
                                    while (i2 < length2) {
                                        try {
                                            char cCharAt2 = str.charAt(i2);
                                            int i3 = length2;
                                            String indent2 = indent;
                                            if (((cCharAt2 == ' ' || cCharAt2 == '\t') ? (char) 1 : (char) 0) == 0) {
                                                try {
                                                    String strSubstring2 = str.substring(0, i2);
                                                    Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
                                                    str = strSubstring2;
                                                    break;
                                                } catch (Throwable th) {
                                                }
                                            } else {
                                                i2++;
                                                indent = indent2;
                                                length2 = i3;
                                            }
                                        } catch (Throwable th2) {
                                            Throwable th = th2;
                                        }
                                    }
                                    try {
                                        int nextIndent = str.length();
                                        String nt = StringsKt.trim((CharSequence) nextLine).toString();
                                        if (StringsKt.startsWith(nt, "end", false) || StringsKt.startsWith(nt, "else", false) || StringsKt.startsWith(nt, "until", false) || StringsKt.startsWith(nt, ")", false)) {
                                            closed = true;
                                        } else if ((nt.length() > 0) && nextIndent > baseIndent.length()) {
                                            closed = true;
                                        } else if ((StringsKt.trim((CharSequence) afterCursor).toString().length() > 0) || unbalancedEnds(s.toString()) <= 0) {
                                            closed = true;
                                        } else {
                                            closed = false;
                                        }
                                        if (!closed) {
                                            if ((opensFunction && new Regex("\\(\\s*function").containsMatchIn(t)) || (StringsKt.endsWith(t, "function()", false) && StringsKt.contains((CharSequence) prevLine, (CharSequence) "(", false))) {
                                                close = "end)";
                                            }
                                            try {
                                                s.insert(cur2, "\n" + baseIndent + close);
                                                this.editor.setSelection(cur2);
                                            } catch (Throwable th3) {
                                                Throwable th = th3;
                                            }
                                        }
                                    } catch (Throwable th4) {
                                        Throwable th = th4;
                                    }
                                }
                                this.suppressCompletion = false;
                                return;
                            } catch (Throwable th5) {
                                this.suppressCompletion = false;
                                throw th5;
                            }
                        } catch (Throwable th6) {
                            this.suppressCompletion = false;
                            throw th6;
                        }
                    } catch (Throwable th7) {
                        this.suppressCompletion = false;
                        throw th7;
                    }
                }
                baseIndent = baseIndent2;
            }
            if (indent.length() > 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                int cur3 = indent.length() + pos;
                if (StringsKt.endsWith(t, " then", false)) {
                    if (StringsKt.endsWith(t, " do", false)) {
                        needsEnd = true;
                    } else {
                        needsEnd = true;
                    }
                    if (!needsEnd) {
                    }
                    this.suppressCompletion = false;
                    return;
                }
                if (StringsKt.startsWith(t, "elseif", false)) {
                    needsEnd = true;
                } else if (StringsKt.endsWith(t, " do", false)) {
                    needsEnd = true;
                } else {
                    needsEnd = true;
                }
                if (!needsEnd) {
                }
                this.suppressCompletion = false;
                return;
            }
            s.insert(pos, indent);
            int cur4 = indent.length() + pos;
            if (StringsKt.endsWith(t, " then", false)) {
                if (StringsKt.endsWith(t, " do", false)) {
                    needsEnd = true;
                } else {
                    needsEnd = true;
                }
                if (!needsEnd) {
                }
                this.suppressCompletion = false;
                return;
            }
            if (StringsKt.startsWith(t, "elseif", false)) {
                needsEnd = true;
            } else if (StringsKt.endsWith(t, " do", false)) {
                needsEnd = true;
            } else {
                needsEnd = true;
            }
            if (!needsEnd) {
            }
            this.suppressCompletion = false;
            return;
        } catch (Throwable th8) {
            this.suppressCompletion = false;
            throw th8;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final int unbalancedEnds(String text) {
        int opens = 0;
        int ends = 0;
        String noStrings = new Regex("--\\[(=*)\\[[\\s\\S]*?]\\1]|--[^\\n]*|\"(\\\\.|[^\"\\\\\\n])*\"|'(\\\\.|[^'\\\\\\n])*'|\\[(=*)\\[[\\s\\S]*?]\\5]").replace(text, " ");
        for (MatchResult m : SequencesKt.asIterable((new Regex("\\b(function|do|then|end|repeat|until|elseif)\\b")).findAll(noStrings, 0))) {
            String value = m.getValue();
            switch (value.hashCode()) {
                case -1300156394:
                    if (value.equals("elseif")) {
                        opens--;
                    }
                    break;
                case -934531685:
                    if (value.equals("repeat")) {
                        opens++;
                    }
                    break;
                case 3211:
                    if (value.equals("do")) {
                        opens++;
                    }
                    break;
                case 100571:
                    if (value.equals("end")) {
                        ends++;
                    }
                    break;
                case 3558941:
                    if (value.equals("then")) {
                        opens++;
                    }
                    break;
                case 111443806:
                    if (value.equals("until")) {
                        ends++;
                    }
                    break;
                case 1380938712:
                    if (value.equals("function")) {
                        opens++;
                    }
                    break;
            }
        }
        return opens - ends;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleTypedChar(Editable s, char c, int at) {
        this.suppressCompletion = true;
        try {
            if (this.autoClosedAt == at && at + 1 < s.length() && s.charAt(at + 1) == c && SetsKt.setOf(new Character[]{')', ']', '}', Character.valueOf(Typography.quote), '\''}).contains(Character.valueOf(c))) {
                s.delete(at + 1, at + 2);
                this.autoClosedAt = -1;
                return;
            }
            if (this.autoClosedAt >= 0 && at <= this.autoClosedAt) {
                this.autoClosedAt++;
            }
            if (c == '\n') {
                this.autoClosedAt = -1;
            }
            Character ch = pairs.get(Character.valueOf(c));
            if (ch != null) {
                char close = ch.charValue();
                char next = at + 1 < s.length() ? s.charAt(at + 1) : ' ';
                if (Character.isLetterOrDigit(next) || next == '_') {
                    return;
                }
                if ((c == '\"' || c == '\'') && at > 0 && (Character.isLetterOrDigit(s.charAt(at - 1)) || s.charAt(at - 1) == c)) {
                    return;
                }
                s.insert(at + 1, String.valueOf(close));
                this.editor.setSelection(at + 1);
                this.autoClosedAt = at + 1;
            }
        } finally {
            this.suppressCompletion = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void insert(String s) {
        int st = RangesKt.coerceAtLeast(this.editor.getSelectionStart(), 0);
        int en = RangesKt.coerceAtLeast(this.editor.getSelectionEnd(), 0);
        this.editor.getText().replace(Math.min(st, en), Math.max(st, en), s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateCompletion(boolean explicit) {
        Autocomplete.Result r = null;
        if (!Settings.INSTANCE.getAutocomplete() && !explicit) {
            hidePopup();
            return;
        }
        int cur = this.editor.getSelectionStart();
        if (cur != this.editor.getSelectionEnd() || cur < 0) {
            hidePopup();
            return;
        }
        try {
            try {
                r = Autocomplete.INSTANCE.complete(this.studio, this.script, this.editor.getText().toString(), cur, explicit);
            } catch (Exception e) {
                r = null;
            }
        } catch (Exception e2) {
        }
        if (r == null || r.getItems().isEmpty()) {
            hidePopup();
            return;
        }
        this.completion = r;
        this.selectedIdx = 0;
        ListAdapter adapter = this.popupList.getAdapter();
        Intrinsics.checkNotNull(adapter, "null cannot be cast to non-null type android.widget.BaseAdapter");
        ((BaseAdapter) adapter).notifyDataSetChanged();
        this.popupList.setSelection(0);
        showPopup();
    }

    private final Pair<Integer, Integer> popupSize() {
        List<Autocomplete.Item> items;
        Autocomplete.Result result = this.completion;
        int n = (result == null || (items = result.getItems()) == null) ? 0 : items.size();
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        int rowH = ui.dp(context, 30.0f);
        Ui ui2 = Ui.INSTANCE;
        Context context2 = getContext();
        Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
        Integer numValueOf = Integer.valueOf(ui2.dp(context2, 280.0f));
        int iMin = Math.min(n, 7) * rowH;
        Ui ui3 = Ui.INSTANCE;
        Context context3 = getContext();
        Intrinsics.checkNotNullExpressionValue(context3, "getContext(...)");
        return TuplesKt.to(numValueOf, Integer.valueOf(iMin + ui3.dp(context3, 4.0f)));
    }

    private final void showPopup() {
        Pair<Integer, Integer> pairPopupSize = popupSize();
        int w = pairPopupSize.component1().intValue();
        int h = pairPopupSize.component2().intValue();
        PopupWindow p = this.popup;
        if (p == null) {
            p = new PopupWindow((View) this.popupList, w, h, false);
            p.setOutsideTouchable(true);
            p.setInputMethodMode(2);
            p.setElevation(8.0f);
            p.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda2
                @Override // android.widget.PopupWindow.OnDismissListener
                public final void onDismiss() {
                    ScriptEditor.this.completion = null;
                }
            });
            this.popup = p;
        }
        Pair<Integer, Integer> pairCursorScreenPos = cursorScreenPos(h);
        int x = pairCursorScreenPos.component1().intValue();
        int y = pairCursorScreenPos.component2().intValue();
        if (p.isShowing()) {
            p.update(x, y, w, h);
        } else {
            p.showAtLocation(this.editor, 0, x, y);
        }
    }

    private final void positionPopup() {
        PopupWindow p = this.popup;
        if (p == null) {
            return;
        }
        Pair<Integer, Integer> pairPopupSize = popupSize();
        int w = pairPopupSize.component1().intValue();
        int h = pairPopupSize.component2().intValue();
        Pair<Integer, Integer> pairCursorScreenPos = cursorScreenPos(h);
        int x = pairCursorScreenPos.component1().intValue();
        int y = pairCursorScreenPos.component2().intValue();
        p.update(x, y, w, h);
    }

    private final Pair<Integer, Integer> cursorScreenPos(int popupH) {
        Layout layout = this.editor.getLayout();
        if (layout == null) {
            return TuplesKt.to(0, 0);
        }
        int cur = RangesKt.coerceIn(this.editor.getSelectionStart(), 0, this.editor.getText().length());
        int line = layout.getLineForOffset(cur);
        int[] loc = new int[2];
        this.editor.getLocationOnScreen(loc);
        int x = loc[0] + this.editor.getTotalPaddingLeft() + ((int) layout.getPrimaryHorizontal(cur));
        int below = loc[1] + this.editor.getTotalPaddingTop() + layout.getLineBottom(line);
        int above = ((loc[1] + this.editor.getTotalPaddingTop()) + layout.getLineTop(line)) - popupH;
        Rect frame = new Rect();
        this.editor.getWindowVisibleDisplayFrame(frame);
        int w = popupSize().component1().intValue();
        int cx = RangesKt.coerceIn(x, frame.left, Math.max(frame.left, frame.right - w));
        int cy = (below + popupH <= frame.bottom || above <= frame.top) ? below : above;
        return TuplesKt.to(Integer.valueOf(cx), Integer.valueOf(cy));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void hidePopup() {
        this.completion = null;
        PopupWindow popupWindow = this.popup;
        if (popupWindow == null || !popupWindow.isShowing()) {
            return;
        }
        popupWindow.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void moveSelection(int delta) {
        List<Autocomplete.Item> items;
        Autocomplete.Result result = this.completion;
        if (result == null || (items = result.getItems()) == null) {
            return;
        }
        int n = items.size();
        this.selectedIdx = RangesKt.coerceIn(this.selectedIdx + delta, 0, n - 1);
        ListAdapter adapter = this.popupList.getAdapter();
        Intrinsics.checkNotNull(adapter, "null cannot be cast to non-null type android.widget.BaseAdapter");
        ((BaseAdapter) adapter).notifyDataSetChanged();
        this.popupList.smoothScrollToPosition(this.selectedIdx);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void acceptCompletion(int index) {
        Autocomplete.Item item;
        Autocomplete.Result r = this.completion;
        if (r == null || (item = (Autocomplete.Item) CollectionsKt.getOrNull(r.getItems(), index)) == null) {
            return;
        }
        Editable text = this.editor.getText();
        int end = RangesKt.coerceIn(this.editor.getSelectionStart(), r.getStart(), text.length());
        int wordEnd = end;
        while (wordEnd < text.length() && (Character.isLetterOrDigit(text.charAt(wordEnd)) || text.charAt(wordEnd) == '_')) {
            wordEnd++;
        }
        removeCallbacks(this.completeRunnable);
        hidePopup();
        this.suppressCompletion = true;
        try {
            text.replace(r.getStart(), wordEnd, item.getInsert());
            this.editor.setSelection(RangesKt.coerceAtMost(r.getStart() + item.getInsert().length(), this.editor.getText().length()));
        } finally {
            this.suppressCompletion = false;
        }
    }

    /* JADX INFO: compiled from: ScriptEditor.kt */
    private final class CompletionAdapter extends BaseAdapter {
        public CompletionAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<Autocomplete.Item> items;
            Autocomplete.Result result = ScriptEditor.this.completion;
            if (result == null || (items = result.getItems()) == null) {
                return 0;
            }
            return items.size();
        }

        @Override // android.widget.Adapter
        public Autocomplete.Item getItem(int p) {
            List<Autocomplete.Item> items;
            Autocomplete.Result result = ScriptEditor.this.completion;
            if (result == null || (items = result.getItems()) == null) {
                return null;
            }
            return items.get(p);
        }

        @Override // android.widget.Adapter
        public long getItemId(int p) {
            return p;
        }

        @Override // android.widget.Adapter
        public View getView(int p, View convert, ViewGroup parent) {
            List<Autocomplete.Item> items;
            Autocomplete.Item item;
            String prefix;
            Intrinsics.checkNotNullParameter(parent, "parent");
            Context ctx = parent.getContext();
            LinearLayout row = convert instanceof LinearLayout ? (LinearLayout) convert : null;
            if (row == null) {
                row = new LinearLayout(ctx);
                row.setOrientation(0);
                row.setGravity(16);
                Ui ui = Ui.INSTANCE;
                Intrinsics.checkNotNull(ctx);
                row.setPadding(ui.dp(ctx, 6.0f), 0, Ui.INSTANCE.dp(ctx, 8.0f), 0);
                row.setMinimumHeight(Ui.INSTANCE.dp(ctx, 30.0f));
                TextView textView = new TextView(ctx);
                textView.setTextSize(9.0f);
                textView.setGravity(17);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextColor(-14803426);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Ui.INSTANCE.dp(ctx, 22.0f), Ui.INSTANCE.dp(ctx, 16.0f));
                layoutParams.rightMargin = Ui.INSTANCE.dp(ctx, 8.0f);
                Unit unit = Unit.INSTANCE;
                row.addView(textView, layoutParams);
                TextView textView2 = new TextView(ctx);
                textView2.setTextSize(13.0f);
                textView2.setTypeface(Typeface.MONOSPACE);
                textView2.setSingleLine(true);
                row.addView(textView2, new LinearLayout.LayoutParams(0, -2, 1.0f));
                TextView textView3 = new TextView(ctx);
                textView3.setTextSize(10.0f);
                textView3.setSingleLine(true);
                textView3.setTextColor(Ui.INSTANCE.getDIM());
                row.addView(textView3);
            }
            Autocomplete.Result result = ScriptEditor.this.completion;
            if (result == null || (items = result.getItems()) == null || (item = (Autocomplete.Item) CollectionsKt.getOrNull(items, p)) == null) {
                return row;
            }
            View childAt = row.getChildAt(0);
            Intrinsics.checkNotNull(childAt, "null cannot be cast to non-null type android.widget.TextView");
            TextView textView4 = (TextView) childAt;
            textView4.setText(item.getKind().getBadge());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(item.getKind().getColor());
            Ui ui2 = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            gradientDrawable.setCornerRadius(ui2.dp(ctx, 3.0f));
            textView4.setBackground(gradientDrawable);
            View childAt2 = row.getChildAt(1);
            Intrinsics.checkNotNull(childAt2, "null cannot be cast to non-null type android.widget.TextView");
            TextView textView5 = (TextView) childAt2;
            Autocomplete.Result result2 = ScriptEditor.this.completion;
            if (result2 == null || (prefix = result2.getPrefix()) == null) {
                prefix = "";
            }
            SpannableString spannableString = new SpannableString(item.getLabel());
            String lowerCase = item.getLabel().toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            String str = lowerCase;
            String lowerCase2 = prefix.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
            int iIndexOf$default = StringsKt.indexOf((CharSequence) str, lowerCase2, 0, false);
            if ((prefix.length() > 0) && iIndexOf$default >= 0) {
                spannableString.setSpan(new StyleSpan(1), iIndexOf$default, prefix.length() + iIndexOf$default, 33);
            }
            textView5.setText(spannableString);
            textView5.setTextColor(Ui.INSTANCE.getEDITOR_TEXT());
            View childAt3 = row.getChildAt(2);
            Intrinsics.checkNotNull(childAt3, "null cannot be cast to non-null type android.widget.TextView");
            ((TextView) childAt3).setText(item.getDetail());
            row.setBackgroundColor(p == ScriptEditor.this.selectedIdx ? Ui.INSTANCE.blend(Ui.INSTANCE.getACCENT(), Ui.INSTANCE.getEDITOR_BG(), 0.55f) : 0);
            return row;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateLines() {
        CharSequence text = this.editor.getText();
        Intrinsics.checkNotNullExpressionValue(text, "getText(...)");
        CharSequence charSequence = text;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= charSequence.length()) {
                break;
            }
            if (charSequence.charAt(i2) == '\n') {
                i++;
            }
            i2++;
        }
        int n = i + 1;
        if (n == this.lineCount) {
            return;
        }
        this.lineCount = n;
        StringBuilder sb = new StringBuilder();
        int i3 = 1;
        if (1 <= n) {
            while (true) {
                sb.append(i3);
                if (i3 < n) {
                    sb.append('\n');
                }
                if (i3 == n) {
                    break;
                } else {
                    i3++;
                }
            }
        }
        this.lines.setText(sb);
        this.status.setText(n + " lines");
    }

    public final void commit() {
        if (this.dirtyText) {
            this.dirtyText = false;
            String text = this.editor.getText().toString();
            synchronized (this.studio.getLock()) {
                if (!Intrinsics.areEqual(this.script.get("Source"), text)) {
                    Instance.set$default(this.script, "Source", text, false, 4, null);
                    this.studio.markDirty();
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    private final void checkSyntax() {
        commit();
        List<LuauState.SyntaxError> errors = LuauState.parseErrors(this.editor.getText().toString());
        if (errors.isEmpty()) {
            String compileError = LuauState.compileError(this.editor.getText().toString());
            if (compileError == null) {
                this.status.setText("No syntax errors");
                this.status.setTextColor(-13726889);
                return;
            }
            this.status.setText(this.script.getName() + compileError);
            this.status.setTextColor(Ui.ERROR);
            return;
        }
        LuauState.SyntaxError first = errors.get(0);
        String text = this.script.getName() + ":" + first.line + ": " + first.message;
        if (errors.size() > 1) {
            text = text + " (+" + (errors.size() - 1) + " more)";
        }
        this.status.setText(text);
        this.status.setTextColor(Ui.ERROR);
        goToLine(first.line);
    }

    private final void goToLine(int line) {
        Editable t = this.editor.getText();
        int i = line - 1;
        int idx = 0;
        for (int idx2 = 0; idx2 < i; idx2++) {
            Intrinsics.checkNotNull(t);
            int iIndexOf$default = StringsKt.indexOf((CharSequence) t, '\n', idx, false);
            if (iIndexOf$default >= 0) {
                idx = iIndexOf$default + 1;
            }
        }
        this.editor.requestFocus();
        this.editor.setSelection(RangesKt.coerceIn(idx, 0, t.length()));
    }

    private final void find() {
        Dialogs dialogs = Dialogs.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        dialogs.prompt(context, "Find", "", new Function1() { // from class: com.goydevv.studrblx.ui.ScriptEditor$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ScriptEditor.find$lambda$35(ScriptEditor.this, (String) obj);
            }
        });
    }

    static final Unit find$lambda$35(ScriptEditor this$0, String q) {
        Intrinsics.checkNotNullParameter(q, "q");
        if (q.length() == 0) {
            return Unit.INSTANCE;
        }
        String t = this$0.editor.getText().toString();
        int i = StringsKt.indexOf((CharSequence) t, q, RangesKt.coerceAtLeast(this$0.editor.getSelectionEnd(), 0), false);
        if (i < 0) {
            i = StringsKt.indexOf((CharSequence) t, q, 0, false);
        }
        if (i >= 0) {
            this$0.editor.requestFocus();
            this$0.editor.setSelection(i, q.length() + i);
        } else {
            this$0.status.setText("\"" + q + "\" not found");
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:133:0x0178 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:84:0x0176 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0027, B:4:0x0035, B:6:0x003b, B:7:0x0045, B:9:0x0051, B:11:0x005a, B:13:0x005e, B:15:0x0066, B:17:0x006e, B:22:0x0092, B:18:0x0078, B:76:0x015c, B:78:0x0160, B:80:0x0166, B:82:0x016e, B:84:0x0176, B:85:0x0178, B:86:0x017b, B:32:0x00ab, B:34:0x00b1, B:35:0x00c1, B:39:0x00d0, B:41:0x00da, B:43:0x00e0, B:45:0x00e6, B:46:0x00ea, B:47:0x00f3, B:50:0x00fb, B:53:0x0105, B:55:0x010f, B:57:0x0115, B:58:0x0118, B:60:0x0123, B:62:0x012b, B:67:0x013b, B:69:0x0143, B:71:0x0149, B:73:0x0151), top: B:93:0x0027 }] */
    public final void highlight() {
        int j;
        int e;
        Editable s = this.editor.getText();
        Theme th = Ui.INSTANCE.getTheme();
        int kw = th.getSynKeyword();
        int str = th.getSynString();
        int num = th.getSynNumber();
        int comment = th.getSynComment();
        int builtin = th.getSynBuiltin();
        boolean z = true;
        this.highlighting = true;
        try {
            Iterator it = ArrayIteratorKt.iterator(s.getSpans(0, s.length(), ForegroundColorSpan.class));
            while (it.hasNext()) {
                ForegroundColorSpan sp = (ForegroundColorSpan) it.next();
                s.removeSpan(sp);
            }
            String text = s.toString();
            int n = text.length();
            int i = 0;
            while (i < n) {
                char c = text.charAt(i);
                if (c == '-' && i + 1 < n && text.charAt(i + 1) == '-') {
                    int lvl = highlight$longLevel(n, text, i + 2);
                    if (lvl >= 0) {
                        e = highlight$longClose(text, n, i + 4 + lvl, lvl);
                    } else {
                        int iIndexOf$default = StringsKt.indexOf((CharSequence) text, '\n', i, false);
                        e = iIndexOf$default < 0 ? n : iIndexOf$default;
                    }
                    highlight$span(s, n, i, e, comment);
                    i = e;
                } else if (c == '\"' || c == '\'') {
                    j = i + 1;
                    while (j < n && text.charAt(j) != c && text.charAt(j) != '\n') {
                        if (text.charAt(j) == '\\') {
                            j++;
                        }
                        j++;
                    }
                    int j2 = Math.min(j + 1, n);
                    highlight$span(s, n, i, j2, str);
                    i = j2;
                } else if (c == '`') {
                    j = i + 1;
                    while (j < n) {
                        if (text.charAt(j) == '\\') {
                            j++;
                        }
                        j++;
                    }
                    int j3 = Math.min(j + 1, n);
                    highlight$span(s, n, i, j3, str);
                    i = j3;
                } else if (c != '[' || highlight$longLevel(n, text, i) < 0) {
                    boolean z2 = z;
                    if (Character.isDigit(c)) {
                        int j4 = i;
                        while (j4 < n && (Character.isLetterOrDigit(text.charAt(j4)) || text.charAt(j4) == '.' || text.charAt(j4) == '_')) {
                            j4++;
                        }
                        highlight$span(s, n, i, j4, num);
                        i = j4;
                        z = z2;
                    } else if (Character.isLetter(c) || c == '_') {
                        int j5 = i;
                        while (j5 < n && (Character.isLetterOrDigit(text.charAt(j5)) || text.charAt(j5) == '_')) {
                            j5++;
                        }
                        String w = text.substring(i, j5);
                        Intrinsics.checkNotNullExpressionValue(w, "substring(...)");
                        boolean prevDot = (i <= 0 || !(text.charAt(i + (-1)) == '.' || text.charAt(i + (-1)) == ':')) ? false : z2;
                        if (!prevDot && keywords.contains(w)) {
                            highlight$span(s, n, i, j5, kw);
                        } else if (!prevDot && builtins.contains(w)) {
                            highlight$span(s, n, i, j5, builtin);
                        }
                        i = j5;
                        z = z2;
                    } else {
                        i++;
                        z = z2;
                    }
                } else {
                    int l = highlight$longLevel(n, text, i);
                    int e2 = highlight$longClose(text, n, i + 2 + l, l);
                    highlight$span(s, n, i, e2, str);
                    i = e2;
                }
            }
        } finally {
            this.highlighting = false;
        }
    }

    private static final void highlight$span(Editable s, int n, int a, int b, int c) {
        if (b > a) {
            s.setSpan(new ForegroundColorSpan(c), a, Math.min(b, n), 33);
        }
    }

    private static final int highlight$longClose(String text, int n, int from, int lvl) {
        String close = "]" + StringsKt.repeat("=", lvl) + "]";
        int e = StringsKt.indexOf((CharSequence) text, close, from, false);
        return e < 0 ? n : close.length() + e;
    }

    private static final int highlight$longLevel(int n, String text, int at) {
        if (at >= n || text.charAt(at) != '[') {
            return -1;
        }
        int j = at + 1;
        int l = 0;
        while (j < n && text.charAt(j) == '=') {
            l++;
            j++;
        }
        if (j >= n || text.charAt(j) != '[') {
            return -1;
        }
        return l;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.completeRunnable);
        hidePopup();
        commit();
        super.onDetachedFromWindow();
    }
}
