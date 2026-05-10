package com.rschao.plugins.magicEngine.core.action.internal;

import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;

public abstract class TimeDependantAction extends Action {



    public void run(TechniqueContext techniqueContext, CancellationToken cancellationToken, Runnable next) {
        perform(techniqueContext, next);
    }
    @Override
    public void execute(TechniqueContext ctx, CancellationToken cancellationToken) {

    }

    protected abstract void perform(
            TechniqueContext ctx,
            Runnable next
    );
}
