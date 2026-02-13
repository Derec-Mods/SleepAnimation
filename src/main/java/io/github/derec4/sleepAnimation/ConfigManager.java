package io.github.derec4.sleepAnimation;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static int skipSpeed;

    public static void loadConfig(SleepAnimation plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        skipSpeed = config.getInt("skip-speed", 50);
    }

    public static int getSkipSpeed() {
        return skipSpeed;
    }
}

