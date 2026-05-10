package com.rschao.plugins.magicEngine.core.action.internal;

import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;

public class Action implements TechniqueAction {

    public void run(TechniqueContext techniqueContext, CancellationToken cancellationToken, Runnable next) {
        execute(techniqueContext, cancellationToken);
        next.run();
    }
    @Override
    public void execute(TechniqueContext techniqueContext, CancellationToken cancellationToken) {

    }
}
