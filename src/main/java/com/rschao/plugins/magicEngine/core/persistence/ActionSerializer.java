package com.rschao.plugins.magicEngine.core.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Serializa ParamList -> JSON y comprime con GZIP para almacenar seguro en PDC
 * (BYTE_ARRAY). Limita tamaño para evitar exploits tipo "book ban".
 */
public class ActionSerializer {

    private static final Gson GSON = new Gson();
    private static final int MAX_COMPRESSED_BYTES = 4096; // 4 KB

    public static byte[] compress(String json) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
            gos.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        return bos.toByteArray();
    }

    public static String decompress(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try (GZIPInputStream gis = new GZIPInputStream(bis)) {
            return new String(gis.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    public static SerializedAction toSerialized(String typeId, ParamList params) {
        SerializedAction sa = new SerializedAction(typeId);
        for (Param p : params.getAll()) {
            JsonElement je = valueToJson(p.getValue());
            String t = detectType(p.getValue());
            sa.params.add(new SerializedParam(p.getName(), t, je));
        }
        return sa;
    }

    private static String detectType(Object v) {
        if (v == null) return "string";
        if (v instanceof Integer || v instanceof Long || v instanceof Short) return "int";
        if (v instanceof Float || v instanceof Double) return "double";
        if (v instanceof Boolean) return "boolean";
        if (v instanceof UUID) return "uuid";
        if (v instanceof List) {
            List<?> l = (List<?>) v;
            if (!l.isEmpty()) {
                Object e = l.get(0);
                if (e instanceof UUID) return "uuidList";
                if (e instanceof String) return "stringList";
                if (e instanceof Player) return "playerList";
            }
            return "stringList";
        }
        return "string";
    }

    private static JsonElement valueToJson(Object v) {
        if (v == null) return JsonParser.parseString("null");
        if (v instanceof Player) {
            UUID id = ((Player) v).getUniqueId();
            return JsonParser.parseString(GSON.toJson(id.toString()));
        }
        if (v instanceof UUID) return JsonParser.parseString(GSON.toJson(v.toString()));
        if (v instanceof List) {
            List<Object> out = new ArrayList<>();
            for (Object e : (List<?>) v) {
                if (e instanceof Player) out.add(((Player) e).getUniqueId().toString());
                else if (e instanceof UUID) out.add(e.toString());
                else out.add(String.valueOf(e));
            }
            return JsonParser.parseString(GSON.toJson(out));
        }
        // fallback to string/primitive via Gson
        return JsonParser.parseString(GSON.toJson(v));
    }

    public static ParamList serializedParamsToParamList(SerializedAction sa) {
        ParamList pl = new ParamList();
        for (SerializedParam sp : sa.params) {
            Object val = jsonElementToObject(sp.type, sp.value);
            pl.put(sp.name, val);
        }
        return pl;
    }

    private static Object jsonElementToObject(String type, JsonElement el) {
        try {
            switch (type) {
                case "int": return el.getAsInt();
                case "double": return el.getAsDouble();
                case "boolean": return el.getAsBoolean();
                case "uuid": return UUID.fromString(el.getAsString());
                case "uuidList": {
                    List<String> raw = GSON.fromJson(el, new TypeToken<List<String>>(){}.getType());
                    List<UUID> uuids = new ArrayList<>();
                    if (raw != null) for (String s : raw) try { uuids.add(UUID.fromString(s)); } catch (Exception ignore) {}
                    return uuids;
                }
                case "playerList": {
                    List<String> raw = GSON.fromJson(el, new TypeToken<List<String>>(){}.getType());
                    List<Player> players = new ArrayList<>();
                    if (raw != null) for (String s : raw) {
                        try { Player p = Bukkit.getPlayer(UUID.fromString(s)); if (p != null) players.add(p); }
                        catch (Exception ignore) {}
                    }
                    return players;
                }
                case "stringList": return GSON.fromJson(el, new TypeToken<List<String>>(){}.getType());
                default: return el.isJsonNull() ? null : el.getAsString();
            }
        } catch (Exception ex) {
            return el.isJsonNull() ? null : el.getAsString();
        }
    }

}


