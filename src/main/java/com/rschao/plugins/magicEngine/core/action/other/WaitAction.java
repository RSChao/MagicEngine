package com.rschao.plugins.magicEngine.core.action.other;

import com.rschao.plugins.magicEngine.Plugin;
import com.rschao.plugins.magicEngine.core.action.internal.TimeDependantAction;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;

public class WaitAction extends TimeDependantAction {

    private final long duration;

    public WaitAction(long duration) {
        this.duration = duration;
    }

    @Override
    protected void perform(TechniqueContext ctx, Runnable next) {
        Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), next, duration);
    }
}
