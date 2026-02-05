package net.Zrips.CMILib.FileHandler;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

public class YamlCanonicalizerTest {

    @Test
    public void configSaveIsStable() throws Exception {
        File folder = Files.createTempDirectory("config-stable").toFile();
        File configFile = new File(folder, "config.yml");
        ConfigReader reader = new ConfigReader(configFile);
        reader.addComment("values", "Header");
        reader.set("values.beta", "second");
        reader.set("values.alpha", "first");
        reader.set("root", true);
        reader.save();
        byte[] initial = Files.readAllBytes(configFile.toPath());
        reader.save();
        byte[] repeat = Files.readAllBytes(configFile.toPath());
        assertArrayEquals(initial, repeat);
        String output = new String(initial, StandardCharsets.UTF_8);
        assertTrue(output.indexOf("alpha") < output.indexOf("beta"));
    }

    @Test
    public void skinCacheSaveIsStable() throws Exception {
        File tempFile = Files.createTempFile("skins", ".yml").toFile();
        YamlConfiguration configuration = new YamlConfiguration();
        String firstId = "b0000000-0000-0000-0000-000000000001";
        String secondId = "a0000000-0000-0000-0000-000000000002";
        configuration.set(firstId + ".name", "First");
        configuration.set(firstId + ".signature", "sig1");
        configuration.set(firstId + ".skin", "skin1");
        configuration.set(firstId + ".lastUpdate", 5L);
        configuration.set(secondId + ".name", "Second");
        configuration.set(secondId + ".signature", "sig2");
        configuration.set(secondId + ".skin", "skin2");
        configuration.set(secondId + ".lastUpdate", 10L);
        YamlCanonicalizer.save(configuration, tempFile);
        byte[] initial = Files.readAllBytes(tempFile.toPath());
        YamlCanonicalizer.save(configuration, tempFile);
        byte[] repeat = Files.readAllBytes(tempFile.toPath());
        assertArrayEquals(initial, repeat);
        String output = new String(initial, StandardCharsets.UTF_8);
        assertTrue(output.indexOf("a0000000") < output.indexOf("b0000000"));
    }
}
