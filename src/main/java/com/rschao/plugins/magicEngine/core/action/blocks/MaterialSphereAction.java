package com.rschao.plugins.magicEngine.core.action.blocks;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.util.BlockUtils;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;

public class MaterialSphereAction extends InstantAction {

     private final String material;
     private final int radius;
     private final int duration;

    public MaterialSphereAction(String material, int radius, int duration) {
        this.material = material;
        this.radius = radius;
        this.duration = duration;
    }

    public MaterialSphereAction(String material, int radius) {
        this.material = material;
        this.radius = radius;
        this.duration = 0;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        if(radius <= 0 ){
            Location center = techniqueContext.caster().getLocation().clone().add(0,-2,0);
            Material mat = center.getBlock().getType();
            if(!mat.isSolid()){
                center.getBlock().setType(org.bukkit.Material.getMaterial(material));
                if(duration > 0){
                    Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), () -> {
                        center.getBlock().setType(mat);
                    }, duration);
                }
            }
        }
        else{
            Location center = techniqueContext.caster().getLocation().clone().add(0,0,0);
            Set<Block> sphere = BlockUtils.sphereAround(center, radius);
            for(Block block : sphere){
                Material mat = block.getType();
                if(!mat.isSolid()){
                    block.setType(org.bukkit.Material.getMaterial(material));
                    if(duration > 0){
                        Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), () -> {
                            block.setType(mat);
                        }, duration);
                    }
                }
            }
        }

    }
}
