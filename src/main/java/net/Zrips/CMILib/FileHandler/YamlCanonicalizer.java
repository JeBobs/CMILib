package net.Zrips.CMILib.FileHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import com.google.common.io.Files;

public class YamlCanonicalizer {

    public static String canonicalize(YamlConfiguration configuration) {
        Map<String, Object> sortedValues = canonicalizeMap(configuration.getValues(false));
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        options.setIndent(configuration.options().indent());
        Yaml yaml = new Yaml(options);
        return yaml.dump(sortedValues);
    }

    public static void save(YamlConfiguration configuration, File file) throws IOException {
        Files.createParentDirs(file);
        String data = canonicalize(configuration);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    private static Map<String, Object> canonicalizeMap(Map<?, ?> original) {
        Map<String, Object> target = new TreeMap<>();
        for (Map.Entry<?, ?> entry : original.entrySet()) {
            target.put(String.valueOf(entry.getKey()), canonicalizeValue(entry.getValue()));
        }
        return target;
    }

    private static Object canonicalizeValue(Object value) {
        if (value instanceof ConfigurationSection) {
            return canonicalizeMap(((ConfigurationSection) value).getValues(false));
        }
        if (value instanceof Map) {
            return canonicalizeMap((Map<?, ?>) value);
        }
        if (value instanceof Iterable) {
            List<Object> canonicalized = new ArrayList<>();
            for (Object item : (Iterable<?>) value) {
                canonicalized.add(canonicalizeValue(item));
            }
            return canonicalized;
        }
        return value;
    }
}
