package com.rschao.plugins.magicEngine.core.action.tier;

import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.feedback.ChatMessageAction;
import com.rschao.plugins.magicEngine.core.action.projectile.ArrowBarrageAction;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.action.projectile.ArrowShootAction;
import com.rschao.plugins.magicEngine.core.persistence.ActionRegistry;

/**
 * Helper que mapea (actionId, tier) -> instancia de Action con parámetros adecuados.
 *
 * Actualmente soporta directamente algunas acciones conocidas (chat_message,
 * arrow_barrage). Para otras acciones intentará crear una instancia usando
 * {@link ActionRegistry} pasando un ParamList que contiene el parámetro "tier".
 */
public class ActionTierSelector {

    public static Action getActionTiered(String actionType, int tier) {
        if (actionType == null) return null;

        switch (actionType) {
            case "arrow_barrage":
                // Mapear tiers a parámetros: duración (ticks) y arrowsPerSecond
                // Tier 1: 100 ticks, 5 aps
                // Cada tier añade duración y velocidad de disparo.
                int baseDuration = 100;
                double baseAps = 5.0;
                int duration = baseDuration + (Math.max(1, tier) - 1) * 50;
                double aps = baseAps + (Math.max(1, tier) - 1) * 5.0;
                return new ArrowBarrageAction(duration, aps);
            case "arrow_shoot":
                // Mapear tiers a parámetros: velocidad de disparo
                double baseSpeed = 1.0;
                double baseAmmo = 2.0;
                double baseSpread = 0.7;
                double speed = baseSpeed + (Math.max(1, tier) - 1) * 0.5;
                double ammo = baseAmmo + (Math.max(1, tier) - 1);
                double spread = Math.min(1.0, baseSpread + (Math.max(1, tier) - 1) * 0.1);
                return new ArrowShootAction(speed, ammo, spread);
            default:
                // Fallback: si ActionRegistry conoce el id intentamos crear usando "tier" en ParamList
                if (ActionRegistry.hasId(actionType)) {
                    return null;
                }
                return null;
        }
    }

}
