package com.rschao.plugins.magicEngine.core.action.movement;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import net.kryspin.Plugin;
import org.bukkit.util.Vector;

@ActionId(value = "launch", cooldown = 10)
public class LauncAction extends InstantAction {

    public final double speed;
    public final boolean proflight;

    public LauncAction(double speed, boolean proflight) {
        this.speed = speed;
        this.proflight = proflight;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        Vector direction = techniqueContext.caster().getLocation().getDirection().normalize();
        if(proflight && techniqueContext.caster().isGliding()){
            Plugin proflight = Plugin.getPlugin(Plugin.class);
            proflight.enableProFlight(techniqueContext.caster());
        }
        else techniqueContext.caster().setVelocity(direction.multiply(speed));
    }

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("speed", speed),
                new Param("proflight", proflight)
        );
    }

    // Support automatic registration: static factory
    public static LauncAction fromParams(ParamList pl) {
        double speed = Double.parseDouble(pl.get("speed").map(p -> p.getValue().toString()).orElse("0"));
        boolean pf = Boolean.parseBoolean(pl.get("proflight").map(p -> p.getValue().toString()).orElse("false"));
        return new LauncAction(speed, pf);
    }
}
