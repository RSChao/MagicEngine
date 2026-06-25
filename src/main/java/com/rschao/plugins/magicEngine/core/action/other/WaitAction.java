package com.rschao.plugins.magicEngine.core.action.other;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.magicEngine.core.action.internal.TimeDependantAction;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;

@ActionId(value = "wait", cooldown = 0)
public class WaitAction extends TimeDependantAction {

    private final long duration;

    public WaitAction(long duration) {
        this.duration = duration;
    }

    @Override
    protected void perform(TechniqueContext ctx, Runnable next) {
        Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), next, duration);
    }

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("duration", duration)
        );
    }

    public static WaitAction fromParams(ParamList pl) {
        long duration = 0L;
        var opt = pl.get("duration");
        if (opt.isPresent()) {
            Object v = opt.get().getValue();
            if (v instanceof Number) duration = ((Number) v).longValue(); else duration = Long.parseLong(String.valueOf(v));
        }
        return new WaitAction(duration);
    }
}
