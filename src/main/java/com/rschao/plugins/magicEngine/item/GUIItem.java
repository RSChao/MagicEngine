package com.rschao.plugins.magicEngine.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GUIItem {

    public static ItemStack fillerItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.GRAY_STAINED_GLASS_PANE);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey("magicengine", "filler"), org.bukkit.persistence.PersistentDataType.STRING, "filler");
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack backItem() {
        ItemStack item = new ItemStack(Material.RED_WOOL);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey("magicengine", "back"), org.bukkit.persistence.PersistentDataType.STRING, "back");
        meta.setDisplayName("Back");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack confirmItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.GREEN_WOOL);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey("magicengine", "confirm"), org.bukkit.persistence.PersistentDataType.STRING, "confirm");
        meta.setDisplayName("Confirm");
        item.setItemMeta(meta);
        return item;
    }
}