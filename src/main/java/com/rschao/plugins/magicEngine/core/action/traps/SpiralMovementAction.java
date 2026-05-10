package com.rschao.plugins.magicEngine.core.action.traps;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.internal.TimeDependantAction;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpiralMovementAction extends TimeDependantAction {
    private final int radius;
    public SpiralMovementAction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void perform(TechniqueContext techniqueContext, Runnable next) {

        new BukkitRunnable() {
            double spiralRadius = radius;
            double yOffset = 0;
            int tick = 0;
            final Location center = techniqueContext.caster().getLocation().clone();
            final double spiralStep = 0.2; // how much to move up per tick
            final double radiusStep = 0.1; // how much to shrink radius per tick
            final int n = techniqueContext.targets().size();
            final List<LivingEntity> nearbyEntities = techniqueContext.targets().stream().filter(Objects::nonNull).collect(Collectors.toList());
            final World world = techniqueContext.caster().getWorld();
            @Override
            public void run() {
                tick++;
                spiralRadius = Math.max(0, spiralRadius - radiusStep);
                yOffset += spiralStep;

                // Move entities in spiral
                for (int i = 0; i < n; i++) {
                    double angle = 2 * Math.PI * i / n + tick * 0.2;
                    double x = center.getX() + spiralRadius * Math.cos(angle);
                    double z = center.getZ() + spiralRadius * Math.sin(angle);
                    double y = center.getY() + yOffset;
                    Location loc = new Location(world, x, y, z);
                    nearbyEntities.get(i).teleport(loc);
                }

                // Stop when radius is almost zero
                if (spiralRadius <= 0.2) {
                    this.cancel();
                    next.run();
                }
            }
        }.runTaskTimer(Plugin.getInstance(), 10L, 2L); // start after 10 ticks, repeat every 2 ticks
    }
}
