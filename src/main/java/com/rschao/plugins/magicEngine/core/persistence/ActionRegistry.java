package com.rschao.plugins.magicEngine.core.persistence;

import com.rschao.plugins.magicEngine.core.action.internal.Action;
import com.rschao.plugins.magicEngine.core.action.parameters.ParamList;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Registry que permite crear Actions a partir de su id y ParamList.
 * También mantiene mapeo inverso class->id para serializar. Ofrece
 * escaneo dinámico de clases anotadas con {@link ActionId}.
 */
public class ActionRegistry {

    private static final Map<String, Function<ParamList, Action>> factories = new HashMap<>();
    private static final Map<Class<? extends Action>, String> classToId = new HashMap<>();

    public static void register(String id, Class<? extends Action> clazz, Function<ParamList, Action> factory) {
        factories.put(id, factory);
        classToId.put(clazz, id);
    }

    public static Optional<Action> create(String id, ParamList params) {
        Function<ParamList, Action> f = factories.get(id);
        if (f == null) return Optional.empty();
        return Optional.ofNullable(f.apply(params));
    }

    public static Optional<String> idForClass(Class<? extends Action> clazz) {
        return Optional.ofNullable(classToId.get(clazz));
    }

    public static boolean hasId(String id) { return factories.containsKey(id); }

    /** Escanea el classpath buscando clases anotadas con @ActionId dentro del package dado. */
    public static void scanAndRegister(String basePackage) {
        try (ScanResult scanResult = new ClassGraph().acceptPackages(basePackage).enableClassInfo().enableAnnotationInfo().scan()) {
            for (var ci : scanResult.getClassesWithAnnotation(ActionId.class.getName())) {
                try {
                    Class<?> cls = ci.loadClass();
                    if (!Action.class.isAssignableFrom(cls)) continue;
                    @SuppressWarnings("unchecked")
                    Class<? extends Action> actionClass = (Class<? extends Action>) cls;
                    ActionId aid = actionClass.getAnnotation(ActionId.class);
                    if (aid == null) continue;
                    String id = aid.value();

                    // Build factory: prefer static fromParams(ParamList), then constructor(ParamList), then no-arg constructor
                    Function<ParamList, Action> factory = getParamListActionFunction(actionClass);

                    if (factory != null) register(id, actionClass, factory);
                } catch (Throwable t) {
                    // ignore per-class failures
                }
            }
        }
    }

    private static @Nullable Function<ParamList, Action> getParamListActionFunction(Class<? extends Action> actionClass) {
        Function<ParamList, Action> factory = null;
        try {
            Method m = actionClass.getDeclaredMethod("fromParams", ParamList.class);
            if (java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
                factory = pl -> {
                    try { return (Action) m.invoke(null, pl); } catch (Exception e) { return null; }
                };
            }
        } catch (NoSuchMethodException ignored) {}

        if (factory == null) {
            try {
                Constructor<? extends Action> c = actionClass.getConstructor(ParamList.class);
                factory = pl -> {
                    try { return c.newInstance(pl); } catch (Exception e) { return null; }
                };
            } catch (NoSuchMethodException ignored) {}
        }

        if (factory == null) {
            try {
                Constructor<? extends Action> c0 = actionClass.getConstructor();
                factory = pl -> {
                    try { return c0.newInstance(); } catch (Exception e) { return null; }
                };
            } catch (NoSuchMethodException ignored) {}
        }
        return factory;
    }

    public static List<String> getRegisteredIds() {
        return List.copyOf(factories.keySet());
    }
}

