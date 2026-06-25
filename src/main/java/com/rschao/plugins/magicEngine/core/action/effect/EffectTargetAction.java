package com.rschao.plugins.magicEngine.core.action.effect;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
@ActionId(value = "effect_give", cooldown = 30)
public class EffectTargetAction extends InstantAction {

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
    public ParamList getParameters() {
        return ParamList.of(new Param("effectName", effectName),
                new Param("maxRadius", maxRadius),
                new Param("minRadius", minRadius));
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
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
