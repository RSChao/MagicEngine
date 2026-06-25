package com.rschao.plugins.magicEngine.core.action.buffs;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ActionId(value = "heal", cooldown = 120)
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

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("healAmount", healAmount),
                new Param("players", players)
        );
    }

    public static HealAction fromParams(ParamList pl) {
        double amount = Double.parseDouble(pl.get("healAmount").map(p -> p.getValue().toString()).orElse("0"));
        List<Player> players = new ArrayList<>();
        var opt = pl.get("players");
        if (opt.isPresent()) {
            Object val = opt.get().getValue();
            if (val instanceof List) {
                for (Object e : (List<?>) val) {
                    try {
                        UUID id = e instanceof UUID ? (UUID) e : UUID.fromString(String.valueOf(e));
                        Player p = Bukkit.getPlayer(id);
                        if (p != null) players.add(p);
                    } catch (Exception ignore) {}
                }
            }
        }
        return new HealAction(amount, players);
    }
}
