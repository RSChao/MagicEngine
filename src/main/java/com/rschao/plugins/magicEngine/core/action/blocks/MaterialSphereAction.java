package com.rschao.plugins.magicEngine.core.action.blocks;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.util.BlockUtils;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;

@ActionId(value = "material_sphere", cooldown = 240)
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
                center.getBlock().setType(Material.getMaterial(material));
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
                    block.setType(Material.getMaterial(material));
                    if(duration > 0){
                        Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), () -> {
                            block.setType(mat);
                        }, duration);
                    }
                }
            }
        }

    }

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("material", material),
                new Param("radius", radius),
                new Param("duration", duration)
        );
    }

    public static MaterialSphereAction fromParams(ParamList pl) {
        String material = pl.get("material").map(p -> String.valueOf(p.getValue())).orElse("STONE");
        int radius = Integer.parseInt(pl.get("radius").map(p -> p.getValue().toString()).orElse("0"));
        int duration = Integer.parseInt(pl.get("duration").map(p -> p.getValue().toString()).orElse("0"));
        if (duration > 0) return new MaterialSphereAction(material, radius, duration);
        return new MaterialSphereAction(material, radius);
    }
}
