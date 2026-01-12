package io.github.derec4.sleepAnimation;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class SleepListener implements Listener {

    private final SleepAnimation plugin;

    public SleepListener(SleepAnimation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            event.setCancelled(true);
//            Bukkit.getLogger().info("Playing night skip animation");
            plugin.getTimeSkipper().startAnimation(event.getWorld());
        }
    }
}
