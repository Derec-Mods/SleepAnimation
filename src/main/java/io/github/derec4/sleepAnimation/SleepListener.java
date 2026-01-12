package io.github.derec4.sleepAnimation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class SleepListener implements Listener {

    private final SleepAnimation plugin;

    public SleepListener(SleepAnimation plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP && !event.isCancelled()) {
            // Check if this world is already animating (to avoid handling our own fired event)
            if (!plugin.getTimeSkipper().isAnimating(event.getWorld())) {
                event.setCancelled(true);
                plugin.getTimeSkipper().startAnimation(event.getWorld());
            }
        }
    }
}
