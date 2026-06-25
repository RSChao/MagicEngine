package com.rschao.plugins.magicEngine.gui;

import com.rschao.plugins.magicEngine.item.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CombineGUI {

    final static String title = "Spell creator";

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, title);
        for(int i = 0; i < 27; i++) {
            if(i != 11 && i != 15) {
                inventory.setItem(i, GUIItem.fillerItem());
            }
            else inventory.setItem(i, new ItemStack(Material.AIR));
        }
        inventory.setItem(17, GUIItem.confirmItem());
        return inventory;
    }
    public Inventory createInventory(Player p) {
        Inventory inventory = Bukkit.createInventory(p, 27, title);
        for(int i = 0; i < 27; i++) {
            if(i != 11 && i != 15) {
                inventory.setItem(i, GUIItem.fillerItem());
            }
            else inventory.setItem(i, new ItemStack(Material.AIR));
        }
        inventory.setItem(17, GUIItem.confirmItem());
        return inventory;
    }

    public static String getTitle() {
        return title;
    }


}
