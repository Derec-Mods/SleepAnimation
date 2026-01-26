package io.github.derec4.sleepAnimation;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when the night-skip animation completes for a world.
 * Minimal event exposing the world whose animation finished.
 */
public class NightSkipCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final World world;

    public NightSkipCompleteEvent(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

