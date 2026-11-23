package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.emoji2.text.EmojiCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Studio;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: OutputPanel.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class OutputPanel extends LinearLayout {
    private final Adapter adapter;
    private final ArrayList<OutputLog.Entry> entries;
    private final SimpleDateFormat fmt;
    private final ArrayList<String> history;
    private int historyIdx;
    private final RecyclerView list;
    private final Function1<OutputLog.Entry, Unit> listener;
    private boolean pending;
    private int filterMode = 0;
    private final Studio studio;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OutputPanel(Context ctx, Studio studio) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.entries = new ArrayList<>();
        this.list = new RecyclerView(ctx);
        this.adapter = new Adapter();
        this.fmt = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        this.history = new ArrayList<>();
        this.listener = new Function1() { // from class: com.goydevv.studrblx.ui.OutputPanel$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OutputPanel.listener$lambda$0(OutputPanel.this, (OutputLog.Entry) obj);
            }
        };
        setOrientation(1);
        setBackgroundColor(Ui.INSTANCE.getPANEL());
        LinearLayout h = Ui.INSTANCE.header(ctx, "Output", Icons.general$default(Icons.INSTANCE, ctx, "Output", null, 4, null));
        h.addView(Ui.iconButton$default(Ui.INSTANCE, ctx, Icons.INSTANCE.general(ctx, "Cleanup", "Clr"), 0.0f, new Function1() { // from class: com.goydevv.studrblx.ui.OutputPanel$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OutputPanel._init_$lambda$1((View) obj);
            }
        }, 4, null));
        final TextView filterBtn = new TextView(ctx);
        filterBtn.setText("All");
        filterBtn.setTextSize(11.0f);
        filterBtn.setTextColor(Ui.INSTANCE.getACCENT());
        filterBtn.setPadding(Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 2.0f));
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputPanel.this.filterMode = (OutputPanel.this.filterMode + 1) % 3;
                filterBtn.setText(OutputPanel.this.filterMode == 0 ? "All" : (OutputPanel.this.filterMode == 1 ? "Warn+" : "Errors"));
                OutputPanel.this.reload();
            }
        });
        h.addView(filterBtn, Ui.INSTANCE.lp(-2, -2, 0.0f));
        final TextView copyBtn = new TextView(ctx);
        copyBtn.setText("Copy");
        copyBtn.setTextSize(11.0f);
        copyBtn.setTextColor(Ui.INSTANCE.getTEXT());
        copyBtn.setPadding(Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 2.0f));
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.widget.PopupMenu pm = new android.widget.PopupMenu(v.getContext(), v);
                pm.getMenu().add(0, 1, 0, "Copy Unique Errors (No Loop)");
                pm.getMenu().add(0, 2, 1, "Copy All Errors");
                pm.getMenu().add(0, 3, 2, "Copy Unique Warn + Errors");
                pm.getMenu().add(0, 4, 3, "Copy Full Output");
                pm.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        OutputPanel.this.copyOutput(item.getItemId());
                        return true;
                    }
                });
                pm.show();
            }
        });
        h.addView(copyBtn, Ui.INSTANCE.lp(-2, -2, 0.0f));
        addView(h, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        RecyclerView recyclerView = this.list;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        this.list.setAdapter(this.adapter);
        this.list.setItemAnimator(null);
        addView(this.list, Ui.INSTANCE.lp(-1, 0, 1.0f));
        LinearLayout bar = new LinearLayout(ctx);
        bar.setOrientation(0);
        bar.setBackgroundColor(Ui.INSTANCE.getHEADER());
        final EditText cmd = Ui.input$default(Ui.INSTANCE, ctx, "", "Run a command (Lua)", false, false, 24, null);
        cmd.setTypeface(Typeface.MONOSPACE);
        cmd.setTextSize(12.0f);
        cmd.setImeOptions(2);
        cmd.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.goydevv.studrblx.ui.OutputPanel$$ExternalSyntheticLambda3
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return OutputPanel._init_$lambda$4(cmd, OutputPanel.this, textView, i, keyEvent);
            }
        });
        bar.addView(cmd, Ui.INSTANCE.lp(0, -2, 1.0f));
        bar.addView(Ui.iconButton$default(Ui.INSTANCE, ctx, Icons.INSTANCE.general(ctx, "Back", "Prev"), 0.0f, new Function1() { // from class: com.goydevv.studrblx.ui.OutputPanel$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OutputPanel._init_$lambda$5(OutputPanel.this, cmd, (View) obj);
            }
        }, 4, null));
        bar.addView(Ui.iconButton$default(Ui.INSTANCE, ctx, Icons.INSTANCE.general(ctx, "RunScript", "Run"), 0.0f, new Function1() { // from class: com.goydevv.studrblx.ui.OutputPanel$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OutputPanel._init_$lambda$6(cmd, OutputPanel.this, (View) obj);
            }
        }, 4, null));
        addView(bar, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        reload();
    }

    static final Unit listener$lambda$0(OutputPanel this$0, OutputLog.Entry it) {
        this$0.schedule();
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$1(View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        OutputLog.INSTANCE.clear();
        return Unit.INSTANCE;
    }

    private static final void _init_$run(EditText cmd, OutputPanel this$0) {
        String code = cmd.getText().toString();
        if (StringsKt.isBlank(code)) {
            return;
        }
        this$0.history.add(code);
        this$0.historyIdx = this$0.history.size();
        cmd.setText("");
        this$0.studio.runCommand(code);
    }

    static final boolean _init_$lambda$4(EditText $cmd, OutputPanel this$0, TextView textView, int id, KeyEvent ev) {
        if (id != 2) {
            if (!(ev != null && ev.getKeyCode() == 66) || ev.getAction() != 0) {
                return false;
            }
        }
        _init_$run($cmd, this$0);
        return true;
    }

    static final Unit _init_$lambda$5(OutputPanel this$0, EditText $cmd, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (!this$0.history.isEmpty()) {
            this$0.historyIdx = RangesKt.coerceAtLeast(this$0.historyIdx - 1, 0);
            $cmd.setText(this$0.history.get(this$0.historyIdx));
            $cmd.setSelection($cmd.getText().length());
        }
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$6(EditText $cmd, OutputPanel this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        _init_$run($cmd, this$0);
        return Unit.INSTANCE;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        OutputLog.INSTANCE.getListeners().add(this.listener);
        reload();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        OutputLog.INSTANCE.getListeners().remove(this.listener);
    }

    private final void schedule() {
        if (this.pending) {
            return;
        }
        this.pending = true;
        postDelayed(new Runnable() { // from class: com.goydevv.studrblx.ui.OutputPanel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OutputPanel.schedule$lambda$7(OutputPanel.this);
            }
        }, 100L);
    }

    static final void schedule$lambda$7(OutputPanel this$0) {
        this$0.pending = false;
        this$0.reload();
    }

    private final void copyOutput(int mode) {
        java.util.List<OutputLog.Entry> snap = OutputLog.INSTANCE.snapshot();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        if (mode == 1 || mode == 3) {
            java.util.LinkedHashMap<String, Integer> dedup = new java.util.LinkedHashMap<>();
            for (OutputLog.Entry e : snap) {
                OutputLog.Level lv = e.getLevel();
                if (mode == 1 && lv != OutputLog.Level.ERROR) continue;
                if (mode == 3 && lv != OutputLog.Level.ERROR && lv != OutputLog.Level.WARN) continue;
                String msg = e.getText().trim();
                String key = (mode == 3 ? "[" + lv.name() + "] " : "") + msg;
                Integer prev = dedup.get(key);
                dedup.put(key, prev == null ? 1 : prev + 1);
            }
            for (java.util.Map.Entry<String, Integer> it : dedup.entrySet()) {
                if (sb.length() > 0) sb.append("\n");
                sb.append(it.getKey());
                if (it.getValue() > 1) {
                    sb.append(" (x").append(it.getValue()).append(")");
                }
                count++;
            }
        } else {
            for (OutputLog.Entry e : snap) {
                if (mode == 2 && e.getLevel() != OutputLog.Level.ERROR) continue;
                if (sb.length() > 0) sb.append("\n");
                sb.append(this.fmt.format(new Date(e.getTime()))).append("  [").append(e.getLevel().name()).append("] ").append(e.getText());
                count++;
            }
        }
        if (count == 0) {
            android.widget.Toast.makeText(getContext(), "Nothing matching to copy", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        android.content.ClipboardManager cm = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(android.content.ClipData.newPlainText("StudioRblx Output", sb.toString()));
        }
        String label = mode == 1 ? " unique error(s)" : (mode == 2 ? " error(s)" : (mode == 3 ? " unique warn/error(s)" : " output line(s)"));
        android.widget.Toast.makeText(getContext(), "Copied " + count + label, android.widget.Toast.LENGTH_SHORT).show();
    }

    private final void reload() {
        this.entries.clear();
        for (OutputLog.Entry e : OutputLog.INSTANCE.snapshot()) {
            if (this.filterMode == 1 && e.getLevel() != OutputLog.Level.WARN && e.getLevel() != OutputLog.Level.ERROR) continue;
            if (this.filterMode == 2 && e.getLevel() != OutputLog.Level.ERROR) continue;
            this.entries.add(e);
        }
        this.adapter.notifyDataSetChanged();
        if (!this.entries.isEmpty()) {
            this.list.scrollToPosition(this.entries.size() - 1);
        }
    }

    /* JADX INFO: compiled from: OutputPanel.kt */
    private final class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        /* JADX INFO: compiled from: OutputPanel.kt */
        public static final /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[OutputLog.Level.values().length];
                try {
                    iArr[OutputLog.Level.PRINT.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[OutputLog.Level.INFO.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[OutputLog.Level.WARN.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[OutputLog.Level.ERROR.ordinal()] = 4;
                } catch (NoSuchFieldError e4) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public Adapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return OutputPanel.this.entries.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Ui ui = Ui.INSTANCE;
            Context context = parent.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            final TextView textViewText$default = Ui.text$default(ui, context, "", 12.0f, 0, false, 24, null);
            textViewText$default.setSingleLine(false);
            textViewText$default.setTypeface(Typeface.MONOSPACE);
            Ui ui2 = Ui.INSTANCE;
            Context context2 = parent.getContext();
            Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
            int iDp = ui2.dp(context2, 8.0f);
            Ui ui3 = Ui.INSTANCE;
            Context context3 = parent.getContext();
            Intrinsics.checkNotNullExpressionValue(context3, "getContext(...)");
            int iDp2 = ui3.dp(context3, 1.0f);
            Ui ui4 = Ui.INSTANCE;
            Context context4 = parent.getContext();
            Intrinsics.checkNotNullExpressionValue(context4, "getContext(...)");
            int iDp3 = ui4.dp(context4, 8.0f);
            Ui ui5 = Ui.INSTANCE;
            Context context5 = parent.getContext();
            Intrinsics.checkNotNullExpressionValue(context5, "getContext(...)");
            textViewText$default.setPadding(iDp, iDp2, iDp3, ui5.dp(context5, 1.0f));
            textViewText$default.setTextIsSelectable(true);
            textViewText$default.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerView.ViewHolder(textViewText$default) { // from class: com.goydevv.studrblx.ui.OutputPanel$Adapter$onCreateViewHolder$1
            };
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
            int text;
            Intrinsics.checkNotNullParameter(h, "h");
            Object obj = OutputPanel.this.entries.get(position);
            Intrinsics.checkNotNullExpressionValue(obj, "get(...)");
            OutputLog.Entry e = (OutputLog.Entry) obj;
            View view = h.itemView;
            Intrinsics.checkNotNull(view, "null cannot be cast to non-null type android.widget.TextView");
            TextView t = (TextView) view;
            t.setText(OutputPanel.this.fmt.format(new Date(e.getTime())) + "  " + e.getText());
            switch (WhenMappings.$EnumSwitchMapping$0[e.getLevel().ordinal()]) {
                case 1:
                    text = Ui.INSTANCE.getTEXT();
                    break;
                case 2:
                    text = Ui.INFO;
                    break;
                case 3:
                    text = Ui.WARN;
                    break;
                case 4:
                    text = Ui.ERROR;
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            t.setTextColor(text);
        }
    }
}
