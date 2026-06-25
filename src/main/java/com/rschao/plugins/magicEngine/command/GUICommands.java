package com.rschao.plugins.magicEngine.command;

import com.rschao.plugins.magicEngine.gui.CombineGUI;
import com.rschao.plugins.magicEngine.item.GUIItem;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class GUICommands {
    public static void register() {
        // Register commands here
        general.register();
        ActionCommand.getCommand().register();
        ActionCommand.giveEmptySpell.register();
    }






    static CommandAPICommand combineGUI = new CommandAPICommand("creator")
            .withPermission("magicengine.command.combineGUI")
            .executes((sender, args) -> {
                // Open the combine GUI for the player
                if (sender instanceof Player player) {
                    player.openInventory(new CombineGUI().createInventory(player));
                } else {
                    sender.sendMessage("This command can only be run by a player.");
                }
            });


    /**
     * comando general
     * debe definirse el ultimo
     */
    static CommandAPICommand general = new CommandAPICommand("spellengine")
            .withSubcommands(combineGUI);
}
