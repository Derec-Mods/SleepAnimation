package io.github.derec4.sleepanimation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModConfig {
    public static int skipSpeed = 50;
    public static boolean instantWakeup;

    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("sleepanimation.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private ModConfig() {
    }

    public static void load() {
        Data data = new Data();
        try {
            if (Files.exists(PATH)) {
                try (BufferedReader reader = Files.newBufferedReader(PATH)) {
                    Data parsed = GSON.fromJson(reader, Data.class);
                    if (parsed != null) {
                        data = parsed;
                    }
                }
            } else {
                Files.createDirectories(PATH.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(PATH)) {
                    GSON.toJson(data, writer);
                }
            }
        } catch (IOException ignored) {
        }
        skipSpeed = data.skipSpeed == null ? 50 : Math.max(1, data.skipSpeed);
        instantWakeup = data.instantWakeup;
    }

    private static final class Data {
        @SerializedName("skip-speed")
        Integer skipSpeed = 50;
        @SerializedName("instant-wakeup")
        boolean instantWakeup;
    }
}
