package io.github.derec4.sleepAnimation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.world.TimeSkipEvent;

/**
 * Class to handle time skipping from night to day.
 * Credits to hraponssi
 */
public class TimeSkipper {
    
    private final SleepAnimation plugin;
    private final int skipSpeed;
    private final long targetTime;
    private final Set<World> animateWorlds = new HashSet<>();

    public TimeSkipper(SleepAnimation plugin, int skipSpeed, long targetTime) {
        this.plugin = plugin;
        this.skipSpeed = skipSpeed;
        this.targetTime = targetTime;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, 1L);
    }

    private void tick() {
        // early check, should not have a performance impact
        if (animateWorlds.isEmpty()) return;

        Iterator<World> iterator = animateWorlds.iterator();
        while (iterator.hasNext()) {
            World world = iterator.next();
            long current = world.getTime();
            long distance = (targetTime - current + 24000) % 24000;

            if (distance <= skipSpeed) {
                world.setTime(targetTime);
                iterator.remove();
                TimeSkipEvent event = new TimeSkipEvent(world, TimeSkipEvent.SkipReason.NIGHT_SKIP, targetTime - current);
                Bukkit.getPluginManager().callEvent(event);
            } else {
                world.setTime(world.getTime() + skipSpeed);
            }
        }
    }

    public void startAnimation(World world) {
        animateWorlds.add(world);
    }

    public boolean isAnimating(World world) {
        return animateWorlds.contains(world);
    }

    public void clear() {
        animateWorlds.clear();
    }
}
