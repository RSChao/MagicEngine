package com.rschao.plugins.magicEngine.core.action.feedback;

import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;

@ActionId(value = "chat_message", cooldown = 0)
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

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("message", message)
        );
    }

    public static ChatMessageAction fromParams(ParamList pl) {
        String message = pl.get("message").map(p -> String.valueOf(p.getValue())).orElse("");
        return new ChatMessageAction(message);
    }
}
