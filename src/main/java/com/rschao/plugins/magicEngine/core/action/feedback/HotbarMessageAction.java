package com.rschao.plugins.magicEngine.core.action.feedback;

import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.parameters.Param;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import com.rschao.plugins.magicEngine.core.persistence.ActionId;
import com.rschao.plugins.magicEngine.core.action.internal.InstantAction;
import com.rschao.plugins.techniqueAPI.tech.TechniqueAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;

@ActionId(value = "hotbar_message", cooldown = 0)
public class HotbarMessageAction extends InstantAction {

    private final String message;

    public HotbarMessageAction(String message) {
        this.message = message;
    }

    @Override
    protected void executeInstant(TechniqueContext techniqueContext, CancellationToken cancellationToken) {
        hotbarMessage.sendHotbarMessage(techniqueContext.caster(), message);
    }

    @Override
    public ParamList getParameters() {
        return ParamList.of(
                new Param("message", message)
        );
    }

    public static HotbarMessageAction fromParams(ParamList pl) {
        String message = pl.get("message").map(p -> String.valueOf(p.getValue())).orElse("");
        return new HotbarMessageAction(message);
    }
}
