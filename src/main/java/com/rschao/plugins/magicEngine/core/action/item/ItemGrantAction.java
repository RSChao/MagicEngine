package com.rschao.plugins.magicEngine.core.action.item;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@ActionId(value = "item_grant", cooldown = 120)
public class ItemGrantAction extends InstantAction {

    private final ItemStack itemStack;
    private final int amount;
    private final boolean overStack;

    public ItemGrantAction(ItemStack itemStack, int amount, boolean overStack) {
        this.itemStack = itemStack;
        this.amount = amount;
        this.overStack = overStack;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        ItemStack stackToGive = itemStack.clone();
        stackToGive.setAmount(amount);
        if(!overStack){
            techniqueContext.caster().getInventory().addItem(stackToGive);
        }
        else{
            Player player = techniqueContext.caster();
            ItemStack[] inventoryContents = player.getInventory().getContents();
            int freeSlot = -1;
            for (int i = 0; i < inventoryContents.length; i++) {
                if (inventoryContents[i] == null || inventoryContents[i].getType() == Material.AIR) {
                    freeSlot = i;
                    break;
                }
            }
            if (freeSlot == -1) {
                hotbarMessage.sendHotbarMessage(player, ChatColor.RED + "You have no free inventory slots for a Free Pearl!");
                return;
            }
            player.getInventory().setItem(freeSlot, stackToGive);
        }

    }

    @Override
    public ParamList getParameters() {
            return ParamList.of(
                    new Param("itemStack", itemStack),
                    new Param("amount", amount),
                    new Param("overStack", overStack)
            );
    }

    public static ItemGrantAction fromParams(ParamList pl) {
        ItemStack itemStack = (ItemStack) pl.get("itemStack").map(p -> p.getValue()).orElse(null);
        int amount = Integer.parseInt(pl.get("amount").map(p -> p.getValue().toString()).orElse("1"));
        boolean overStack = Boolean.parseBoolean(pl.get("overStack").map(p -> p.getValue().toString()).orElse("false"));
        return new ItemGrantAction(itemStack, amount, overStack);
    }
}
