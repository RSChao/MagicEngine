package com.rschao.plugins.magicEngine.core.action.projectile;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.internal.TimeDependantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@ActionId(value = "arrow_barrage", cooldown = 60)
public class ArrowBarrageAction extends TimeDependantAction {

    private final int durationTicks;
    private final double arrowsPerSecond;
    Plugin plugin = Plugin.getInstance();

    public ArrowBarrageAction(int durationTicks, double arrowsPerSecond) {
        this.durationTicks = durationTicks;
        this.arrowsPerSecond = arrowsPerSecond;
    }

    @Override
    protected void perform(TechniqueContext ctx, Runnable next) {
        Player player = ctx.caster();
        double arrowsPerTick = arrowsPerSecond * 20.0;
        World world = player.getWorld();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            new BukkitRunnable() {
                int ticks = 0;
                double arrowAccumulator = 0.0;

                @Override
                public void run() {
                    for(LivingEntity e : ctx.targets()){

                        if (ticks++ >= durationTicks || !player.isOnline()) {
                            this.cancel();
                            next.run();
                            return;
                        }
                        Location playerLoc = player.getEyeLocation();
                        arrowAccumulator += arrowsPerTick;
                        int arrowsThisTick = (int) arrowAccumulator;
                        arrowAccumulator -= arrowsThisTick;
                        float yaw = playerLoc.getYaw();
                        for (int i = 0; i < arrowsThisTick; i++) {
                            double phi = Math.random() * 2 * Math.PI;
                            double radius = 12 + Math.random() * 10;
                            double heightAbove = 8 + Math.random() * 4;
                            double x = radius * Math.cos(phi);
                            double y = heightAbove;
                            double z = radius * Math.sin(phi);
                            double yawRad = Math.toRadians(-yaw);
                            double rotatedX = x * Math.cos(yawRad) - z * Math.sin(yawRad);
                            double rotatedZ = x * Math.sin(yawRad) + z * Math.cos(yawRad);
                            Location spawnLoc = playerLoc.clone().add(rotatedX, y, rotatedZ);
                            Vector direction = e.getLocation().add(0, 1, 0).toVector().subtract(spawnLoc.toVector()).normalize();
                            Arrow arrow = world.spawnArrow(spawnLoc, direction, 2.5f, 0.1f);
                            arrow.setShooter(player);
                            arrow.setCritical(true);
                            arrow.setDamage(15);
                            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                            arrow.setGravity(false);
                        }
                    }

                }
            }.runTaskTimer(plugin, 0L, 1L);
        }, 100L);
    }


    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("duration", durationTicks),
                new Param("arrowsPerSecond", arrowsPerSecond)
        );
    }

    public static ArrowBarrageAction fromParams(ParamList pl) {
        int duration = Integer.parseInt(pl.get("duration").map(p -> p.getValue().toString()).orElse("100"));
        double aps = Double.parseDouble(pl.get("arrowsPerSecond").map(p -> p.getValue().toString()).orElse("5"));
        return new ArrowBarrageAction(duration, aps);
    }
}
