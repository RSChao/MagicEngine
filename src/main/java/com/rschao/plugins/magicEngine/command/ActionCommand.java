package com.rschao.plugins.magicEngine.command;

import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.tier.ActionTierSelector;
import com.rschao.plugins.magicEngine.core.persistence.ActionRegistry;
import com.rschao.plugins.magicEngine.item.ComponentItem;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ActionCommand {

    public static CommandAPICommand getCommand() {
        return new CommandAPICommand("giveaction")
                .withPermission("magicengine.command.action")
                .withArguments(new StringArgument("actionName").replaceSuggestions(ArgumentSuggestions.strings(ActionRegistry.getRegisteredIds().toArray(new String[0]))), new IntegerArgument("tier", 1, 6), new IntegerArgument("amount").setOptional(true))
                .executesPlayer((sender, args) -> {
                    String actionName = (String) args.get("actionName");
                    int tier = (int) args.get("tier");
                    int amount = args.getOrDefaultUnchecked("amount", 1);
                    Action a = ActionTierSelector.getActionTiered(actionName, tier);
                    if(a == null) {
                        sender.sendMessage(ChatColor.RED + "Invalid parameters.");
                        return;
                    }
                    ItemStack i = ComponentItem.getComponentItem(tier, 1, a);
                    i.setAmount(amount);
                    sender.getInventory().addItem(i);
                })
                .executes((sender, args) -> {
                    sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
                });
    }
    public static CommandAPICommand giveEmptySpell = new CommandAPICommand("giveemptyskill")
            .withPermission("magicengine.command.action")
            .withArguments(new IntegerArgument("amount").setOptional(true))
            .executesPlayer((sender, args) -> {
                int amount = args.getOrDefaultUnchecked("amount", 1);
                ItemStack i = ComponentItem.SpellBookItem(new ActionSequence());
                i.setAmount(amount);
                sender.getInventory().addItem(i);
            })
            .executes((sender, args) -> {
                sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            });
}
