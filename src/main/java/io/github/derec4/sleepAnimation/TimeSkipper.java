package io.github.derec4.sleepAnimation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.World;

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
                world.setStorm(false);
                world.setThundering(false);
                iterator.remove();
            } else {
                world.setTime(world.getTime() + skipSpeed);
            }
        }
    }

    public boolean startAnimation(World world) {
        return animateWorlds.add(world);
    }

    public void clear() {
        animateWorlds.clear();
    }
}
