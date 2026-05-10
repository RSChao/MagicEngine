package com.rschao.plugins.magicEngine.core.action.buffs;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HealAction extends InstantAction {

    private final double healAmount;
    private final List<Player> players;

    public HealAction(double healAmount) {
        this.healAmount = healAmount;
        this.players = new ArrayList<>();
    }
    public HealAction(double healAmount, List<Player> players) {
        this.healAmount = healAmount;
        this.players = players;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        double health = techniqueContext.caster().getHealth();
        if(players.size() == 0) players.add(techniqueContext.caster());
        for (Player player : players) {
            player.setHealth(Math.min(health + healAmount, player.getAttribute(Attribute.MAX_HEALTH).getBaseValue()));
        }
    }
}
