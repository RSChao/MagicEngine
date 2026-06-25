package com.rschao.plugins.magicEngine.core.action.internal;

import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;

/**
 * Acción base. Ahora obliga a las subclases concretas a declarar sus parámetros
 * mediante {@link #getParameters()} (ortografía corregida). Para compatibilidad
 * con código antiguo se mantiene {@link #getParamerters()} que delega en la nueva API.
 */
public abstract class Action implements TechniqueAction {

    public void run(TechniqueContext techniqueContext, CancellationToken cancellationToken, Runnable next) {
        execute(techniqueContext, cancellationToken);
        next.run();
    }

    @Override
    public void execute(TechniqueContext techniqueContext, CancellationToken cancellationToken) {

    }

    /**
     * Nueva API: devuelve parámetros (nombre+valor). Es abstracta para forzar
     * la implementación en acciones concretas.
     */
    public abstract ParamList getParameters();

    /**
     * Método legado (con typo) para mantener compatibilidad. Delegará en
     * {@link #getParameters()}.
     */
    @Deprecated
    public ParamList getParamerters() {
        return getParameters();
    }
}
