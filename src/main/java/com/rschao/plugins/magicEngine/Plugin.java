package com.rschao.plugins.magicEngine;

import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.buffs.HealAction;
import com.rschao.plugins.magicEngine.core.action.feedback.ChatMessageAction;
import com.rschao.plugins.magicEngine.core.action.feedback.HotbarMessageAction;
import com.rschao.plugins.magicEngine.core.action.movement.LauncAction;
import com.rschao.plugins.magicEngine.core.action.traps.SpiralMovementAction;
import com.rschao.plugins.magicEngine.core.builder.SpellBuilder;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        ActionSequence seq = new ActionSequence();
        seq.addActions(new SpiralMovementAction(3), new LauncAction(10, true), new HealAction(10), new HotbarMessageAction("You have been healed!"));
        Technique technique = new Technique("test_engine", "Test", new TechniqueMeta(false, 0, List.of()), TargetSelectors.radial(10), ((techniqueContext, cancellationToken) -> {
            seq.execute(techniqueContext);
        }));
        TechRegistry.registerTechnique("engine_test", technique);

        Technique technique2 = (new SpellBuilder("engine_test", "Test Spell", "test_spell"))
                .addActions(new SpiralMovementAction(3), new LauncAction(3, false), new ChatMessageAction("You have casted a spell!"))
                .addDescriptionLine("This is a test spell.")
                .setUltimate(true)
                .setTargets(TargetSelectors.self())
                .build();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Plugin getInstance() {
        return JavaPlugin.getPlugin(Plugin.class);
    }
}
