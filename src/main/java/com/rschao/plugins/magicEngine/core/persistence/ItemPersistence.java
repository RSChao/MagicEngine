package com.rschao.plugins.magicEngine.core.persistence;

import com.google.gson.Gson;
import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ItemPersistence {

    private static final Gson GSON = new Gson();

    public static final String DEFAULT_KEY = "magicengine_action_v1";
    public static final String DEFAULT_KEY_SEQ = "magicengine_spell_sequence_v1";

    /** Guarda una action en el ItemStack usando PDC (BYTE_ARRAY, comprimido). */
    public static boolean saveActionToItem(ItemStack item, Plugin plugin, String keyName, Action action) throws IOException {
        Optional<String> idOpt = ActionRegistry.idForClass(action.getClass());
        if (idOpt.isEmpty()) return false;
        String id = idOpt.get();
        ParamList params = action.getParameters();
        SerializedAction sa = ActionSerializer.toSerialized(id, params);
        String json = GSON.toJson(sa);
        byte[] compressed = ActionSerializer.compress(json);
        if (compressed.length > 4096) {
            // too large; refuse to write
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, keyName == null ? DEFAULT_KEY : keyName);
        pdc.set(key, PersistentDataType.BYTE_ARRAY, compressed);
        item.setItemMeta(meta);
        return true;
    }

    /** Carga una Action desde el ItemStack. */
    public static Optional<Action> loadActionFromItem(ItemStack item, Plugin plugin, String keyName) throws IOException {
        if (item == null || !item.hasItemMeta()) return Optional.empty();
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, keyName == null ? DEFAULT_KEY : keyName);
        byte[] data = pdc.get(key, PersistentDataType.BYTE_ARRAY);
        if (data == null) return Optional.empty();
        String json = ActionSerializer.decompress(data);
        SerializedAction sa = GSON.fromJson(json, SerializedAction.class);
        ParamList pl = ActionSerializer.serializedParamsToParamList(sa);
        return ActionRegistry.create(sa.type, pl);
    }

    /**
     * Extrae únicamente la id (type) de la acción almacenada en el ItemStack sin instanciar la Action.
     */
    public static Optional<String> getActionIdFromItem(ItemStack item, Plugin plugin, String keyName) {
        try {
            if (item == null || !item.hasItemMeta()) return Optional.empty();
            ItemMeta meta = item.getItemMeta();
            var pdc = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, keyName == null ? DEFAULT_KEY : keyName);
            byte[] data = pdc.get(key, PersistentDataType.BYTE_ARRAY);
            if (data == null) return Optional.empty();
            String json = ActionSerializer.decompress(data);
            // parse minimal JSON to get "type"
            com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            if (obj.has("type") && !obj.get("type").isJsonNull()) {
                return Optional.of(obj.get("type").getAsString());
            }
            return Optional.empty();
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static Optional<String> getActionIdFromItem(ItemStack item, Plugin plugin) {
        return getActionIdFromItem(item, plugin, null);
    }

    /**
     * Carga una ActionSequence desde un único ItemStack. El ItemStack debe contener
     * un JSON comprimido que represente una lista de SerializedAction.
     * Si algún elemento no puede instanciarse como Action, devuelve Optional.empty().
     */
    public static Optional<ActionSequence> loadActionSequenceFromItem(ItemStack item, Plugin plugin, String keyName) {
        try {
            if (item == null || !item.hasItemMeta()) return Optional.empty();
            ItemMeta meta = item.getItemMeta();
            var pdc = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, keyName == null ? DEFAULT_KEY_SEQ : keyName);
            byte[] data = pdc.get(key, PersistentDataType.BYTE_ARRAY);
            if (data == null) return Optional.empty();
            String json = ActionSerializer.decompress(data);
            java.util.List<SerializedAction> list = GSON.fromJson(json, new com.google.gson.reflect.TypeToken<java.util.List<SerializedAction>>(){}.getType());
            if (list == null || list.isEmpty()) return Optional.empty();
            ActionSequence seq = new ActionSequence();
            for (SerializedAction sa : list) {
                ParamList pl = ActionSerializer.serializedParamsToParamList(sa);
                var oa = ActionRegistry.create(sa.type, pl);
                if (oa.isEmpty()) return Optional.empty();
                seq.addAction(oa.get());
            }
            return Optional.of(seq);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Guarda una ActionSequence completa en un único ItemStack (como lista de SerializedAction comprimida).
     * Devuelve true si se escribió correctamente.
     */
    public static boolean saveActionSequenceToItem(ItemStack item, Plugin plugin, String keyName, ActionSequence seq) throws IOException {
        if (seq == null || seq.getActions() == null || seq.getActions().isEmpty()) return false;
        java.util.List<SerializedAction> out = new java.util.ArrayList<>();
        for (Action a : seq.getActions()) {
            Optional<String> idOpt = ActionRegistry.idForClass(a.getClass());
            if (idOpt.isEmpty()) return false;
            String id = idOpt.get();
            ParamList params = a.getParameters();
            SerializedAction sa = ActionSerializer.toSerialized(id, params);
            out.add(sa);
        }
        String json = GSON.toJson(out);
        byte[] compressed = ActionSerializer.compress(json);
        if (compressed.length > 4096) return false;
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, keyName == null ? DEFAULT_KEY_SEQ : keyName);
        pdc.set(key, PersistentDataType.BYTE_ARRAY, compressed);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * Carga una secuencia de acciones desde una lista de ItemStacks.
     * Si algún item no contiene una acción válida, devuelve Optional.empty().
     * @param items
     * @param plugin
     * @param keyName
     * @return ActionSequence con todas las acciones cargadas, o empty si algún item falla.
     */
    public static Optional<ActionSequence> loadActionSequenceFromItems(List<ItemStack> items, Plugin plugin, String keyName) {
        ActionSequence seq = new ActionSequence();
        try {
            for (ItemStack it : items) {
                var oa = loadActionFromItem(it, plugin, keyName);
                if (oa.isEmpty()) return Optional.empty(); // o elegir ignorar items vacíos
                seq.addAction(oa.get());
            }
            return Optional.of(seq);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}

