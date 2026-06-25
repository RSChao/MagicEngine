package com.rschao.plugins.magicEngine.gui.event;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.persistence.ItemPersistence;
import com.rschao.plugins.magicEngine.gui.CombineGUI;
import com.rschao.plugins.magicEngine.item.ComponentItem;
import com.rschao.plugins.magicEngine.item.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CombineEvent implements Listener {

    Map<Player, ItemStack> resultMap = new HashMap<>();

    @EventHandler
    void onInventoryModify(InventoryClickEvent ev){
        Bukkit.getLogger().info("Inventory modified");
        if(!ev.getView().getTitle().equals(CombineGUI.getTitle())) return;
        //get items in slot 11 and 15
        if(ev.getView().getTopInventory().getItem(11) == null || ev.getView().getTopInventory().getItem(15) == null) return;
        //get items as ItemStack
        ItemStack item1 = ev.getView().getTopInventory().getItem(11);
        ItemStack item2 = ev.getView().getTopInventory().getItem(15);
        // load action from item1 and item2
        ActionSequence seq;
        Action action;
        try {
            // If the spell book has no saved sequence, treat it as an empty sequence so we can add to it
            seq = ItemPersistence.loadActionSequenceFromItem(item1, Plugin.getInstance(), null).orElse(new ActionSequence());
            action = ItemPersistence.loadActionFromItem(item2, Plugin.getInstance(), null).orElse(null);
        } catch (Exception e) {
            return;
        }
        if (action == null) return;
        seq.addAction(action);
        Bukkit.getLogger().info("Sequence length: " + seq.getActions().size());
        //save action to item1
        try {
            ItemPersistence.saveActionSequenceToItem(item1, Plugin.getInstance(), null, seq);
        } catch (Exception e) {
            return;
        }

        resultMap.put((Player) ev.getWhoClicked(), ComponentItem.SpellBookItem(seq));

    }
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
            ItemStack result = resultMap.get((Player) ev.getWhoClicked());
            ActionSequence seq;
            if (result != null) {
                try {
                    seq = ItemPersistence.loadActionSequenceFromItem(result, Plugin.getInstance(), null).orElse(null);
                } catch (Exception e) {
                    return;
                }
            } else {
                // If there was no cached result, try to read the book from the GUI slot 11 or fallback to empty sequence
                ItemStack book = ev.getView().getTopInventory().getItem(11);
                try {
                    seq = ItemPersistence.loadActionSequenceFromItem(book, Plugin.getInstance(), null).orElse(new ActionSequence());
                } catch (Exception e) {
                    seq = new ActionSequence();
                }
            }
            if(seq == null) seq = new ActionSequence();
            result = ComponentItem.SpellBookItem(seq);
            Item resultItem = ev.getWhoClicked().getWorld().dropItemNaturally(ev.getWhoClicked().getLocation(), result);
            resultItem.setPickupDelay(0);
            resultMap.remove((Player) ev.getWhoClicked());
            //close inventory
            ev.getWhoClicked().closeInventory();
        }
    }

}
