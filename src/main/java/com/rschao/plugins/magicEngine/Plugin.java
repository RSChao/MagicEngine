package com.rschao.plugins.magicEngine;

import com.rschao.plugins.magicEngine.command.GUICommands;
import com.rschao.plugins.magicEngine.core.action.ActionSequence;
import com.rschao.plugins.magicEngine.core.action.buffs.HealAction;
import com.rschao.plugins.magicEngine.core.action.feedback.ChatMessageAction;
import com.rschao.plugins.magicEngine.core.action.feedback.HotbarMessageAction;
import com.rschao.plugins.magicEngine.core.action.movement.LauncAction;
import com.rschao.plugins.magicEngine.core.action.traps.SpiralMovementAction;
import com.rschao.plugins.magicEngine.core.builder.SpellBuilder;
import com.rschao.plugins.magicEngine.core.persistence.ActionRegistry;
import com.rschao.plugins.magicEngine.core.persistence.TechniquePersistence;
import com.rschao.plugins.magicEngine.gui.event.CombineEvent;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Dynamic registration: scan package for classes annotated with @ActionId and register factories
        ActionRegistry.scanAndRegister("com.rschao.plugins.magicEngine.core.action");
        // attempt to auto-register common target selectors exposed by TargetSelectors


        // load persisted techniques (if any) and register them
        registerCommands();
        registerEvents();
        TechniquePersistence.initialize();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        TechniquePersistence.serializeTechniques();
    }

    void registerCommands() {
        GUICommands.register();
    }
    void registerEvents() {
        getServer().getPluginManager().registerEvents(new CombineEvent(), this);
    }

    public static Plugin getInstance() {
        return JavaPlugin.getPlugin(Plugin.class);
    }
}
