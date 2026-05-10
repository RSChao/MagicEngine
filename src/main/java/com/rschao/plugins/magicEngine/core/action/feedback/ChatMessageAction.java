package com.rschao.plugins.magicEngine.core.action.feedback;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;

public class ChatMessageAction extends InstantAction {

    private final String message;

    public ChatMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(TechniqueContext techniqueContext, CancellationToken cancellationToken) {

    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        techniqueContext.caster().sendMessage(message);
    }
}
