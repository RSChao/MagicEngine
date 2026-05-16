package com.rschao.plugins.magicEngine.core.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public final class BlockUtils {

    public static Set<Block> sphereAround(Location location, int radius) {
        Set<Block> sphere = new HashSet<Block>();
        Block center = location.getBlock();
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y, z);
                    double dist = center.getLocation().distance(b.getLocation());
                    if(dist <= radius && dist > (radius - 2)) {
                        sphere.add(b);
                    }
                }
            }
        }
        return sphere;
    }
}
