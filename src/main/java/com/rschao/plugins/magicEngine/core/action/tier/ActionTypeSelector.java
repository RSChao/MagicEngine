package com.rschao.plugins.magicEngine.core.action.tier;

public class ActionTypeSelector {

    public static int getTypeTier(String actionType) {
        if (actionType.startsWith("instant:")) {
            return 1;
        } else if (actionType.startsWith("timed:")) {
            return 2;
        } else if (actionType.startsWith("continuous:")) {
            return 3;
        } else {
            throw new IllegalArgumentException("Unknown action type: " + actionType);
        }
    }
}
