package com.github.tatercertified.oxidizium;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Properties;

public record Config(String version, boolean debug) {
    private static Config instance;
    public static void init() {
        final Path config = FabricLoader.getInstance().getConfigDir().resolve("oxidizium.properties");
        final Properties properties = new Properties();
        final String configVerKey = "config-version";
        final String configVer = "1.0";

        if (Files.notExists(config)) {
            try {
                storeConfig(config, configVer, properties);
            } catch (IOException e) {
                Oxidizium.LOGGER.error("Config storing failed", e);
            }
        } else {
            try {
                loadConfig(config, properties);
            } catch (IOException e) {
                Oxidizium.LOGGER.error("Config creation failed", e);
            }

            if (!(Objects.equals(properties.getProperty(configVerKey), configVer))) {
                properties.setProperty(configVerKey, configVer);
                try {
                    storeConfig(config, configVer, properties);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                parse(configVer, properties);
            }
        }
    }

    private static void storeConfig(Path config, String configVer, Properties properties) throws IOException {
        try (OutputStream output = Files.newOutputStream(config, StandardOpenOption.CREATE)) {
            fillDefaults(configVer, properties);
            properties.store(output, null);
        }
        parse(configVer, properties);
    }

    private static void fillDefaults(String configVer, Properties properties) {
        checkProperty("config-version", configVer, properties);
        checkProperty("debug", "false", properties);
    }

    private static void checkProperty(String key, String defaultValue, Properties properties) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, defaultValue);
        }
    }

    private static void loadConfig(Path config, Properties properties) throws IOException {
        try (InputStream input = Files.newInputStream(config)) {
            properties.load(input);
        }
    }

    private static void parse(String configVer, Properties properties) {
        fillDefaults(configVer, properties);
        boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));
        instance = new Config(configVer, debug);
    }

    public static Config getInstance() {
        return instance;
    }
}
