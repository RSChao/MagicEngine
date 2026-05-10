package com.rschao.plugins.magicEngine.core.action;

import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.internal.TimeDependantAction;
import com.rschao.plugins.techniqueAPI.tech.cancel.CancellationToken;
import com.rschao.plugins.techniqueAPI.tech.cancel.SimpleCancellationToken;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionSequence {

    private final List<Action> actions  = new ArrayList<>();

    public ActionSequence addAction(Action action) {
        actions.add(action);
        return this;
    }

    public ActionSequence addActions(Action ... actionsToAdd) {
        actions.addAll(Arrays.asList(actionsToAdd));
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void execute(TechniqueContext ctx) {

        CancellationToken cancellationToken = new SimpleCancellationToken();
        executeAction(ctx, cancellationToken, 0);
    }

    private void executeAction(
            TechniqueContext ctx,
            CancellationToken cancellationToken,
            int index
    ) {

        if (index >= actions.size()) {
            return;
        }
        if(cancellationToken.isCancelled()) {
            return;
        }
        Action action = actions.get(index);
        if( action instanceof TimeDependantAction timeDependantAction) {
            timeDependantAction.run(ctx, cancellationToken, ()  -> executeAction(ctx, cancellationToken,index + 1));
        } else {
            action.run(ctx, cancellationToken, () -> executeAction(ctx, cancellationToken, index + 1));
        }
    }
}
