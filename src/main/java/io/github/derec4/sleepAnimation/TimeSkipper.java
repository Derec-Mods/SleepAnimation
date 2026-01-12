package io.github.derec4.sleepAnimation;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.world.TimeSkipEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to handle time skipping from night to day.
 * Credits to hraponssi
 */
public class TimeSkipper {

    private final SleepAnimation plugin;
    private final int skipSpeed;
    private final long targetTime;
    private final Set<World> animateWorlds = new HashSet<>();
    private final Map<UUID, CompletableFuture<Void>> animationFutures = new ConcurrentHashMap<>();

    public TimeSkipper(SleepAnimation plugin, int skipSpeed, long targetTime) {
        this.plugin = plugin;
        this.skipSpeed = skipSpeed;
        this.targetTime = targetTime;
    }

    public void startAnimation(World world) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        animationFutures.put(world.getUID(), future);
        animateWorlds.add(world);

        // callback
        future.thenRun(() -> broadcastNightSkipEvent(world));
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, 1L);
    }

    private void tick() {
        // early check, should not have a performance impact
        if (animateWorlds.isEmpty()) {
            return;
        }

        Iterator<World> iterator = animateWorlds.iterator();
        while (iterator.hasNext()) {
            World world = iterator.next();
            long current = world.getTime();
            long distance = (targetTime - current + 24000) % 24000;
            /**
             * math:
             * shortest distance from current time to target time
             * targetTime - current = raw difference (can be negative)
             * add 24000 to handle negative differences
             * modulo by a Minecraft day to get remainder distance forward
             */

            if (distance <= skipSpeed) {
                world.setTime(targetTime);
                iterator.remove();
                CompletableFuture<Void> future = animationFutures.remove(world.getUID());

                if (future != null) {
                    future.complete(null);
                }
            } else {
                world.setTime(world.getTime() + skipSpeed);
            }
        }
    }

    private void broadcastNightSkipEvent(World world) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            TimeSkipEvent event = new TimeSkipEvent(
                    world,
                    TimeSkipEvent.SkipReason.NIGHT_SKIP,
                    0
            );
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    public void clear() {
        animateWorlds.clear();
        animationFutures.values().forEach(future -> future.cancel(false));
        animationFutures.clear();
    }

    public void cancelAnimation(World world) {
        animateWorlds.remove(world);
        CompletableFuture<Void> future = animationFutures.remove(world.getUID());
        if (future != null) {
            future.cancel(false);
        }
    }
}
