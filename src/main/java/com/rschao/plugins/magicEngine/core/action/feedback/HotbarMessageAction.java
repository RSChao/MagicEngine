package com.rschao.plugins.magicEngine.core.action.feedback;

import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import org.checkerframework.checker.units.qual.A;

public class HotbarMessageAction extends InstantAction {

    private final String message;

    public HotbarMessageAction(String message) {
        this.message = message;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        hotbarMessage.sendHotbarMessage(techniqueContext.caster(), message);
    }
}
