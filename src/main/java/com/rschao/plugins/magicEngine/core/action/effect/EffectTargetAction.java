package com.rschao.plugins.magicEngine.core.action.effect;

import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;

public class EffectTargetAction implements TechniqueAction {

    private final String effectName;

    private final int maxRadius;
    private final int minRadius;

    private final PotionEffectType potionEffectType;

    public EffectTargetAction(String effectName, int maxRadius, int minRadius) {
        this.effectName = effectName;
        this.maxRadius = maxRadius;
        this.minRadius = minRadius;
        this.potionEffectType = PotionEffectType.getByName(effectName.toUpperCase());
    }
    int duration;
    int amplifier;
    @Override
    public void execute(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        if(potionEffectType.getCategory().equals(PotionEffectTypeCategory.HARMFUL)){
            amplifier = 2;

        }
        else {
            amplifier = 1;
        }
        duration = 20*5;
        for (LivingEntity target : techniqueContext.targets()){
            if(target.getLocation().distanceSquared(target.getLocation()) > maxRadius*maxRadius) continue;
            if(target.getLocation().distanceSquared(target.getLocation()) < minRadius*minRadius) continue;
            target.addPotionEffect(potionEffectType.createEffect(duration, amplifier));
        }
    }
}
