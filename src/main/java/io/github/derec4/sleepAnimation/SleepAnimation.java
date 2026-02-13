package io.github.derec4.sleepAnimation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SleepAnimation extends JavaPlugin {

    private TimeSkipper timeSkipper;

    @Override
    public void onEnable() {
        ConfigManager.loadConfig(this);
        timeSkipper = new TimeSkipper(this, ConfigManager.getSkipSpeed(), 0);
        timeSkipper.start();
        getServer().getPluginManager().registerEvents(new SleepListener(this), this);

        Bukkit.getLogger().info("");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  |_______|                             " +
                "  ");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  | Derex |     Sleep Animation v" + getDescription().getVersion());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  |_______|     Running on " + Bukkit.getName() + " - " + Bukkit.getVersion());
        Bukkit.getLogger().info("");
    }

    @Override
    public void onDisable() {
        if (timeSkipper != null) {
            timeSkipper.clear();
        }
    }

    public TimeSkipper getTimeSkipper() {
        return timeSkipper;
    }
}
