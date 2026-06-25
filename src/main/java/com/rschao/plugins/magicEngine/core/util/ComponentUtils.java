package com.rschao.plugins.magicEngine.core.util;

import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionRegistry;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentUtils {

    int maxType = 8;

    public static ParamList getComponentParams(Action action) {
        return action.getParameters();
    }

    public static NamespacedKey getTierModel(int tier, int type){
        return new NamespacedKey("minecraft", type + "_" + tier);
    }

    public static List<String> paramListToLore(ParamList paramList, int type) {
        ChatColor color = getTypeColor(type);

        List<String> lore = new ArrayList<>();
        for (String param : paramList.toStringValues()) {
            lore.add(color + param);
        }

        return lore;

    }

    public static ChatColor getTypeColor(int type) {
        return switch (type) {
            case 1 -> ChatColor.RED;
            case 2 -> ChatColor.GOLD;
            case 3 -> ChatColor.LIGHT_PURPLE;
            case 4 -> ChatColor.DARK_GREEN;
            case 5 -> ChatColor.GREEN;
            case 6 -> ChatColor.DARK_RED;
            case 7 -> ChatColor.GRAY;
            case 8 -> ChatColor.BLUE;
            default -> ChatColor.WHITE;
        };
    }

    public static String getActionId(Action action) {
        return ActionRegistry.idForClass(action.getClass()).orElse("unknown_action");
    }
}
