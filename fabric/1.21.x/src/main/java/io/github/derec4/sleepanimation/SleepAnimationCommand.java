package io.github.derec4.sleepanimation;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class SleepAnimationCommand {
    private SleepAnimationCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(root("sleepanimation"));
        dispatcher.register(root("sa"));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> root(String name) {
        return CommandManager.literal(name)
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("reload").executes(ctx -> {
                    ModConfig.load();
                    ctx.getSource().sendFeedback(
                            () -> Text.literal("SleepAnimation configuration reloaded.").formatted(Formatting.GREEN),
                            true
                    );
                    return 1;
                }));
    }
}
