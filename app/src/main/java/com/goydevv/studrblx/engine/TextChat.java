package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.lua.EnumItemV;
import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.lua.LuaTypes;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public final class TextChat {

    private TextChat() {
    }

    public static String escape(String s) {
        StringBuilder b = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<': b.append("&lt;"); break;
                case '>': b.append("&gt;"); break;
                case '&': b.append("&amp;"); break;
                case '"': b.append("&quot;"); break;
                case '\'': b.append("&apos;"); break;
                default: b.append(c);
            }
        }
        return b.toString();
    }

    public static Instance config(DataModel dm, String cls) {
        Instance tcs = dm.findService("TextChatService");
        return tcs == null ? null : Instance.findFirstChild$default(tcs, cls, false, 2, null);
    }

    public static boolean flag(Instance cfg, String prop, boolean def) {
        if (cfg == null) return def;
        Object v = cfg.get(prop);
        return v instanceof Boolean ? (Boolean) v : def;
    }

    public static Instance channel(DataModel dm, String name) {
        Instance tcs = dm.findService("TextChatService");
        if (tcs == null) return null;
        Instance folder = Instance.findFirstChild$default(tcs, "TextChannels", false, 2, null);
        return folder == null ? null : Instance.findFirstChild$default(folder, name, false, 2, null);
    }

    private static String displayName(Instance player) {
        Object dn = player.get("DisplayName");
        if (dn instanceof String && !((String) dn).isEmpty()) return (String) dn;
        return player.getName();
    }

    private static long userId(Instance player) {
        Object id = player.get("UserId");
        return id instanceof Number ? ((Number) id).longValue() : 1L;
    }

    public static LuaTable textSource(LuaRuntime rt, Instance player) {
        LuaTable src = new LuaTable();
        src.set("UserId", LuaValue.valueOf((double) userId(player)));
        src.set("Name", player.getName());
        src.set("CanSend", LuaValue.TRUE);
        src.set("ClassName", "TextSource");
        return src;
    }

    private static int rgb(Object c, int def) {
        if (!(c instanceof com.goydevv.studrblx.model.Color3)) return def;
        com.goydevv.studrblx.model.Color3 k = (com.goydevv.studrblx.model.Color3) c;
        int r = Math.max(0, Math.min(255, Math.round(k.getR() * 255.0f)));
        int g = Math.max(0, Math.min(255, Math.round(k.getG() * 255.0f)));
        int bl = Math.max(0, Math.min(255, Math.round(k.getB() * 255.0f)));
        return (r << 16) | (g << 8) | bl;
    }

    static void styleBubble(PlaySession.Bubble b, Instance src) {
        if (src == null) return;
        b.bgColor = rgb(src.get("BackgroundColor3"), b.bgColor);
        b.textColor = rgb(src.get("TextColor3"), b.textColor);
        Object t = src.get("BackgroundTransparency");
        if (t instanceof Number) b.bgTransparency = Math.max(0.0f, Math.min(1.0f, ((Number) t).floatValue()));
        Object ts = src.get("TextSize");
        if (ts instanceof Number && ((Number) ts).floatValue() > 0) b.textSize = Math.min(100.0f, ((Number) ts).floatValue());
        Object tv = src.get("TailVisible");
        if (tv instanceof Boolean) b.tail = (Boolean) tv;
        Object ff = src.get("FontFace");
        if (ff instanceof com.goydevv.studrblx.model.FontValue) {
            com.goydevv.studrblx.model.FontValue f = (com.goydevv.studrblx.model.FontValue) ff;
            b.fontFamily = f.getFamily();
            b.fontWeight = f.getWeight();
            b.italic = f.getStyle() == 1;
        }
    }

    private static LuaValue callback(LuaRuntime rt, Instance owner, String key, LuaValue arg) {
        return callback(rt, owner, key, arg, LuaValue.NIL);
    }

    private static LuaValue callback(LuaRuntime rt, Instance owner, String key, LuaValue arg, LuaValue arg2) {
        if (owner == null) return LuaValue.NIL;
        HashMap<String, LuaValue> cbs = rt.getCallbacks().get(owner);
        LuaValue fn = cbs == null ? null : cbs.get(key);
        if (fn == null || !fn.isfunction()) return LuaValue.NIL;
        try {
            return arg2.isnil() ? fn.call(arg) : fn.call(arg, arg2);
        } catch (LuaError e) {
            com.goydevv.studrblx.core.OutputLog.INSTANCE.error(e.getMessage() == null ? "TextChatService callback error" : e.getMessage());
            return LuaValue.NIL;
        }
    }

    private static void applyProps(LuaRuntime rt, LuaTable msg, LuaValue props, String[] keys) {
        if (!props.isuserdata()) return;
        Object o = props.touserdata();
        if (!(o instanceof Instance)) return;
        Instance p = (Instance) o;
        for (String k : keys) {
            if (p.getProps().containsKey(k)) msg.set(k, rt.toLua(p.get(k), p, k));
        }
    }

    public static boolean runCommand(PlaySession ps, String text) {
        DataModel dm = ps.getDm();
        Instance tcs = dm.findService("TextChatService");
        Instance player = ps.getPlayer();
        if (tcs == null || player == null || !text.startsWith("/")) return false;
        String first = text.split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
        for (Instance d : Instance.descendants$default(tcs, null, 1, null)) {
            if (!"TextChatCommand".equals(d.getClassName())) continue;
            if (Boolean.FALSE.equals(d.get("Enabled"))) continue;
            for (String alias : new String[]{"PrimaryAlias", "SecondaryAlias"}) {
                Object a = d.get(alias);
                if (a instanceof String && !((String) a).isEmpty() && ((String) a).toLowerCase(Locale.ROOT).equals(first)) {
                    LuaRuntime rt = ps.getRt();
                    rt.fire(d, "Triggered", textSource(rt, player), LuaValue.valueOf(text));
                    return true;
                }
            }
        }
        return false;
    }

    public static LuaTable deliver(PlaySession ps, Instance channel, Instance player, String text, String metadata) {
        LuaRuntime rt = ps.getRt();
        DataModel dm = ps.getDm();
        Instance tcs = dm.getService("TextChatService");
        if (channel == null) channel = channel(dm, player == null ? "RBXSystem" : "RBXGeneral");
        LuaTable msg = new LuaTable();
        msg.set("ClassName", "TextChatMessage");
        msg.set("Text", text);
        msg.set("PrefixText", player == null ? "" : displayName(player) + ":");
        msg.set("Metadata", metadata == null ? "" : metadata);
        msg.set("MessageId", UUID.randomUUID().toString());
        msg.set("Translation", "");
        msg.set("Status", LuaTypes.INSTANCE.wrap(new EnumItemV("TextChatMessageStatus", "Success", 1)));
        msg.set("TextSource", player == null ? LuaValue.NIL : textSource(rt, player));
        msg.set("TextChannel", channel == null ? LuaValue.NIL : rt.wrap(channel));
        String[] keys = {"PrefixText", "Text", "Translation"};
        applyProps(rt, msg, callback(rt, tcs, "OnIncomingMessage", msg), keys);
        applyProps(rt, msg, callback(rt, channel, "OnIncomingMessage", msg), keys);
        if (channel != null) rt.fire(channel, "MessageReceived", msg);
        rt.fire(tcs, "MessageReceived", msg);
        LuaTable shown = new LuaTable();
        shown.set("PrefixText", msg.get("PrefixText"));
        shown.set("Text", msg.get("Text"));
        applyProps(rt, shown, callback(rt, tcs, "OnChatWindowAdded", msg), new String[]{"PrefixText", "Text"});
        String prefix = shown.get("PrefixText").optjstring("");
        String body = shown.get("Text").optjstring("");
        if (player == null) {
            ps.addChatLine(prefix.isEmpty() ? null : prefix, body, 0, true);
        } else {
            ps.addChatLine(prefix, body, ps.nameColor(player.getName()), false);
            Instance chr = ps.getCharacter();
            Instance head = (chr != null && player == ps.getPlayer()) ? Instance.findFirstChild$default(chr, "Head", false, 2, null) : null;
            if (head != null && flag(config(dm, "BubbleChatConfiguration"), "Enabled", true)) {
                LuaTable bubbleMsg = new LuaTable();
                bubbleMsg.set("Text", msg.get("Text"));
                LuaValue bp = callback(rt, tcs, "OnBubbleAdded", msg, rt.wrap(head));
                applyProps(rt, bubbleMsg, bp, new String[]{"Text"});
                ps.bubble(head, com.goydevv.studrblx.ui.RichText.INSTANCE.parse(bubbleMsg.get("Text").optjstring("")).toString());
                PlaySession.Bubble b = ps.lastBubble();
                if (b != null) {
                    styleBubble(b, config(dm, "BubbleChatConfiguration"));
                    Object o = bp.isuserdata() ? bp.touserdata() : null;
                    if (o instanceof Instance) styleBubble(b, (Instance) o);
                }
            }
        }
        return msg;
    }

    public static LuaTable send(PlaySession ps, Instance channel, String raw, String metadata) {
        Instance player = ps.getPlayer();
        if (player == null) throw new LuaError("SendAsync can only be called from a client with a LocalPlayer");
        LuaRuntime rt = ps.getRt();
        Instance tcs = ps.getDm().getService("TextChatService");
        String text = escape(raw);
        LuaTable sending = new LuaTable();
        sending.set("ClassName", "TextChatMessage");
        sending.set("Text", text);
        sending.set("PrefixText", displayName(player) + ":");
        sending.set("Metadata", metadata == null ? "" : metadata);
        sending.set("Status", LuaTypes.INSTANCE.wrap(new EnumItemV("TextChatMessageStatus", "Sending", 2)));
        sending.set("TextSource", textSource(rt, player));
        sending.set("TextChannel", channel == null ? LuaValue.NIL : rt.wrap(channel));
        rt.fire(tcs, "SendingMessage", sending);
        LuaTable out = deliver(ps, channel, player, text, metadata);
        rt.fire(player, "Chatted", LuaValue.valueOf(raw));
        return out;
    }

    public static Varargs channelCall(PlaySession ps, Instance channel, String name, Varargs a) {
        if (ps == null) throw new LuaError(name + " is only available while the game is running");
        if ("SendAsync".equals(name)) {
            return send(ps, channel, a.checkjstring(2), a.optjstring(3, ""));
        }
        if ("DisplaySystemMessage".equals(name)) {
            return deliver(ps, channel, null, a.checkjstring(2), a.optjstring(3, ""));
        }
        return null;
    }
}
