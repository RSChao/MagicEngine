package com.rschao.plugins.magicEngine.item;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.persistence.ActionRegistry;
import com.rschao.plugins.magicEngine.core.persistence.ItemPersistence;
import com.rschao.plugins.magicEngine.core.util.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ComponentItem {

    /** Crea un ItemStack representando la action y la guarda en PDC. */
    public static ItemStack getComponentItem(int tier, int type, Action action) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Magic Component (" + ComponentUtils.getActionId(action) + ")");
            meta.setLore(ComponentUtils.paramListToLore(action.getParameters(), type));
            meta.setItemModel(ComponentUtils.getTierModel(tier, type));
            item.setItemMeta(meta);
        }
        try {
            ItemPersistence.saveActionToItem(item, Plugin.getInstance(), null, action);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save action to item: " + e.getMessage());
        }
        return item;
    }

    public static ItemStack SpellBookItem(ActionSequence sequence) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Spell Book");
            List<String> lore = new ArrayList<>();
            lore.add("Contains a sequence of actions.");
            Bukkit.getLogger().info("Sequence length: " + sequence.getActions().size());
            for(Action action : sequence.getActions()) {
                lore.add(ActionRegistry.idForClass(action.getClass()).orElse("unknown_action"));
            }
            meta.setLore(lore);
            // set the meta before saving PDC/data so the persistence helper can augment the existing meta
            item.setItemMeta(meta);
            try {
                ItemPersistence.saveActionSequenceToItem(item, Plugin.getInstance(), null, sequence);
            } catch (IOException e) {
                return new ItemStack(Material.AIR); // Return an empty item if saving fails
            }
        }
        return item;
    }

    public static ItemStack targetSelector(String type, int number){
        List<String> selector = List.of("self", "closest", "radial");
        if(!selector.contains(type)) return null;
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        switch (type){
            case "self":
                lore.add("self");
                break;
            case "closest":
                lore.add("closest");
                lore.add(String.valueOf(number));
                break;
            case "radial":
                lore.add("radial");
                lore.add(String.valueOf(number));
                break;
        }
        meta.setItemModel(NamespacedKey.minecraft("rune_target_sel"));
        item.setItemMeta(meta);
        return item;
    }


}
