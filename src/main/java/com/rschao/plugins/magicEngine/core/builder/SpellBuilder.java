package com.rschao.plugins.magicEngine.core.builder;

import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellBuilder {

    final String group;
    final String name;
    final String id;

    static List<Action> actions = new ArrayList<>();
    static List<String> description = new ArrayList<>();
    static boolean isUltimate = false;
    static TargetSelector targetSelector = null;

    public SpellBuilder(String group, String name, String id) {
        this.group = group.toLowerCase();
        this.name = name;
        this.id = id.toLowerCase();
    }

    public SpellBuilder addAction(Action action) {
        actions.add(action);
        return this;
    }
    public SpellBuilder addActions(Action ... action) {
        Collections.addAll(actions, action);
        return this;
    }
    public SpellBuilder addActions(ActionSequence seq) {
        Collections.addAll(actions, seq.getActions().toArray(new Action[0]));
        return this;
    }

    public SpellBuilder addDescriptionLine(String description) {
        SpellBuilder.description.add(description);
        return this;
    }
    public SpellBuilder addDescriptionLines(String ... description) {
        Collections.addAll(SpellBuilder.description, description);
        return this;
    }

    public SpellBuilder setUltimate(boolean isUltimate) {
        SpellBuilder.isUltimate = isUltimate;
        return this;
    }
    public SpellBuilder setTargets(TargetSelector sel){
        targetSelector = sel;
        return this;
    }

    public Technique build() {

        //check any missing fields
        if(targetSelector == null) {
            throw new IllegalStateException("Target selector must be set for technique " + id);
        }
        if(description == null || description.isEmpty()) {
            description = List.of();
        }
        if(actions == null || actions.isEmpty()) {
            throw new IllegalStateException("At least one action must be added to technique " + id);
        }

        Technique t = new Technique(id, name, new TechniqueMeta(isUltimate, 0, description), targetSelector, (techniqueContext, cancellationToken) -> {
            ActionSequence seq = new ActionSequence();
            seq.addActions(actions.toArray(new Action[0]));
            seq.execute(techniqueContext);
        });
        TechRegistry.registerTechnique(group, t);
        return t;
    }
}
