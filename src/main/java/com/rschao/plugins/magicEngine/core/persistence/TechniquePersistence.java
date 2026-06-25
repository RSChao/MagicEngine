package com.rschao.plugins.magicEngine.core.persistence;

import com.rschao.plugins.magicEngine.Plugin;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.rschao.plugins.techniqueAPI.tech.Technique;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Persiste variables bajo la sección "techniques.x.y" en un archivo YAML dentro del folder del plugin.
 * Provee utilidades para leer, escribir y borrar variables. Los métodos trabajan con tipos comunes
 * (Integer, Double, Boolean, String, Long, Float) y también intentan castear valores genéricos.
 */
public class TechniquePersistence {

    private static Map<String, Map<String, Technique>> techniques = new HashMap<>();

    public static final File TECHNIQUE_FILE = new File(Plugin.getInstance().getDataFolder(), "techniques.yml");

    private static void ensureFileExists() {
        try {
            if (!TECHNIQUE_FILE.getParentFile().exists()) {
                TECHNIQUE_FILE.getParentFile().mkdirs();
            }
            if (!TECHNIQUE_FILE.exists()) {
                TECHNIQUE_FILE.createNewFile();
                // create an empty yaml so bukkit's loader has something
                YamlConfiguration cfg = new YamlConfiguration();
                cfg.save(TECHNIQUE_FILE);
            }
        } catch (IOException e) {
            Plugin.getInstance().getLogger().log(Level.SEVERE, "Failed to ensure techniques.yml exists", e);
        }
    }

    /**
     * Lee todas las variables almacenadas bajo la sección "techniques" y las devuelve en un mapa
     * estructurado como Map<x, Map<y, value>>. Se asume que todos los valores encontrados son del mismo tipo
     * indicado por {@code clazz}. Además, mantiene los nombres de x e y como Strings.
     * @return mapa anidado vacío si no existe la sección o si ocurre un error
     */
    public static Map<String, Map<String, Technique>> getAllVariables() {
        ensureFileExists();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(TECHNIQUE_FILE);
        ConfigurationSection techniques = cfg.getConfigurationSection("techniques");
        if (techniques == null) return Collections.emptyMap();

        Map<String, Map<String, Technique>> result = new HashMap<>();

        for (String x : techniques.getKeys(false)) {
            ConfigurationSection xs = techniques.getConfigurationSection(x);
            if (xs == null) continue;
            Map<String, Technique> inner = new HashMap<>();
            for (String y : xs.getKeys(false)) {
                Object raw = xs.get(y);
                if (raw == null) continue;
                // Assuming Technique can be instantiated or converted from the raw value
                Technique value = (Technique) raw; // This cast may need to be adjusted based on how Technique is stored
                inner.put(y, value);
            }
            if (!inner.isEmpty()) result.put(x, inner);
        }

        return result;
    }

    /**
     * Guarda una variable en la ruta "techniques.x.y".
     * @param x nombre de la primera sección
     * @param y nombre de la segunda sección
     * @param value valor a guardar (debe ser serializable a YAML)
     */
    public static void saveVariable(String x, String y, Object value) {
        if (x == null || y == null) throw new IllegalArgumentException("x and y must not be null");
        ensureFileExists();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(TECHNIQUE_FILE);
        String path = "techniques." + x + "." + y;
        cfg.set(path, value);
        try {
            cfg.save(TECHNIQUE_FILE);
        } catch (IOException e) {
            Plugin.getInstance().getLogger().log(Level.SEVERE, "Failed to save variable to techniques.yml", e);
        }
    }

    /**
     * Elimina la variable en "techniques.x.y". Si la entrada no existe, no hace nada.
     */
    public static void deleteVariable(String x, String y) {
        if (x == null || y == null) throw new IllegalArgumentException("x and y must not be null");
        ensureFileExists();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(TECHNIQUE_FILE);
        String path = "techniques." + x + "." + y;
        cfg.set(path, null);
        try {
            cfg.save(TECHNIQUE_FILE);
        } catch (IOException e) {
            Plugin.getInstance().getLogger().log(Level.SEVERE, "Failed to delete variable from techniques.yml", e);
        }
    }

    /**
     * Guarda todos los valores del mapa proporcionado. No guarda el mapa crudo: itera y llama a
     * {@link #saveVariable(String, String, Object)} para cada entrada.
     * @param map mapa estructurado como Map<x, Map<y, value>>
     */
    public static void saveAllVariables(Map<String, Map<String, Technique>> map) {
        if (map == null) return;
        for (Map.Entry<String, Map<String, Technique>> e : map.entrySet()) {
            String x = e.getKey();
            Map<String, Technique> inner = e.getValue();
            if (inner == null) continue;
            for (Map.Entry<String, Technique> e2 : inner.entrySet()) {
                saveVariable(x, e2.getKey(), e2.getValue());
            }
        }
    }

    public static Technique getTechnique(String x, String y) {
        ensureFileExists();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(TECHNIQUE_FILE);
        String path = "techniques." + x + "." + y;
        Object raw = cfg.get(path);
        if (raw == null) return null;
        return (Technique) raw; // Adjust this cast based on how Technique is stored
    }

    public static void initialize() {
        ensureFileExists();
        techniques = getAllVariables();
    }

    public static void serializeTechniques() {
        saveAllVariables(techniques);
    }
}



