package io.github.derec4.sleepanimation;

import net.fabricmc.api.ModInitializer;

public final class SleepAnimation implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfig.load();
    }
}
