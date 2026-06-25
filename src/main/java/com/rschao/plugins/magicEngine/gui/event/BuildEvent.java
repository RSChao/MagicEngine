package com.rschao.plugins.magicEngine.gui.event;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.persistence.ItemPersistence;
import com.rschao.plugins.magicEngine.gui.CombineGUI;
import com.rschao.plugins.magicEngine.item.ComponentItem;
import com.rschao.plugins.magicEngine.item.GUIItem;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BuildEvent implements Listener {


    @EventHandler
    void onConfirm(InventoryClickEvent ev){
        if(!ev.getView().getTitle().equals(CombineGUI.getTitle())) return;
        if(ev.getSlot() != 17) return;
        // defensive null checks for clicked inventory/item
        if(ev.getClickedInventory() == null) return;
        ItemStack clicked = ev.getClickedInventory().getItem(17);
        if(clicked == null || !clicked.isSimilar(GUIItem.confirmItem())) return;
        if(clicked.isSimilar(GUIItem.confirmItem())){
            ev.setCancelled(true);
            Bukkit.getLogger().info("Confirm clicked");
            //give player the result item
            ActionSequence seq;
            try {
                seq = ItemPersistence.loadActionSequenceFromItem(clicked, Plugin.getInstance(), null).orElse(null);
            } catch (Exception e) {
                return;
            }
            if(seq == null) seq = new ActionSequence();
            //Technique technique = new Technique()
            //close inventory
            ev.getWhoClicked().closeInventory();
        }
    }

}
