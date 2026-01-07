package io.github.derec4.sleepAnimation;

import org.bukkit.plugin.java.JavaPlugin;

public final class SleepAnimation extends JavaPlugin {

    private TimeSkipper timeSkipper;

    @Override
    public void onEnable() {
        timeSkipper = new TimeSkipper(this, 100, 0);
        timeSkipper.start();
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
