package com.rschao.plugins.magicEngine.core.action.internal;

import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;

public abstract class InstantAction extends Action {



    public void run(TechniqueContext techniqueContext, CancellationToken cancellationToken, Runnable next) {
        executeInstant(techniqueContext, cancellationToken);

        next.run();
    }

    abstract protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken);
}
