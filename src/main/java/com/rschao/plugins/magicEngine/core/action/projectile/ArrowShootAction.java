package com.rschao.plugins.magicEngine.core.action.projectile;

import com.rschao.Plugin;
import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@ActionId(value = "arrow_shoot", cooldown = 30)
public class ArrowShootAction extends InstantAction {

    private final double speed;
    private final double amount;
    private final double accuracy;

    public ArrowShootAction(double speed, double amount, double accuracy) {
        this.speed = speed;
        this.amount = amount;
        this.accuracy = accuracy;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        Player player = techniqueContext.caster();
        for (int i = 0; i < amount; i++) {

            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                Vector direction = player.getEyeLocation().getDirection().normalize();
                Vector randomOffset = new Vector(
                        (Math.random() - 0.5) * accuracy,
                        (Math.random() - 0.5) * accuracy,
                        (Math.random() - 0.5) * accuracy
                );
                Vector finalDirection = direction.add(randomOffset).multiply(speed); // Adjust speed multiplier as needed
                Arrow arrow = player.launchProjectile(Arrow.class, finalDirection);
                arrow.setDamage(10.0); // Set base damage to 50
                arrow.setGravity(false);
                arrow.setVelocity(finalDirection);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED); // Prevent pickup
            }, i * 2L); // Slight delay between each arrow
        }
    }

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("speed", speed),
                new Param("amount", amount),
                new Param("accuracy", accuracy)
        );
    }

    public static ArrowShootAction fromParams(ParamList pl) {
        double speed = Double.parseDouble(pl.get("speed").map(p -> p.getValue().toString()).orElse("1.0"));
        double amount = Double.parseDouble(pl.get("amount").map(p -> p.getValue().toString()).orElse("1"));
        double accuracy = Double.parseDouble(pl.get("accuracy").map(p -> p.getValue().toString()).orElse("0"));
        return new ArrowShootAction(speed, amount, accuracy);
    }
}
