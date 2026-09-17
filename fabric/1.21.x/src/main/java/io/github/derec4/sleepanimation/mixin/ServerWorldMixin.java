package io.github.derec4.sleepanimation.mixin;

import io.github.derec4.sleepanimation.ModConfig;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Unique
    private boolean sleepanimation$active;

    @Shadow
    @Final
    private SleepManager sleepManager;

    @Shadow
    private void wakeSleepingPlayers() {
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void sleepanimation$tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        if (!world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            return;
        }
        int percentage = world.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        boolean enoughInBed = this.sleepManager.canSkipNight(percentage);
        if (this.sleepanimation$active) {
            if (!ModConfig.instantWakeup && !enoughInBed) {
                this.sleepanimation$active = false;
                return;
            }
            this.sleepanimation$advance(world);
            return;
        }
        if (enoughInBed) {
            this.sleepanimation$active = true;
            if (world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE) && world.isRaining()) {
                world.resetWeather();
            }
            if (ModConfig.instantWakeup) {
                this.wakeSleepingPlayers();
            }
            this.sleepanimation$advance(world);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/SleepManager;canResetTime(ILjava/util/List;)Z"))
    private boolean sleepanimation$blockVanillaSkip(SleepManager manager, int percentage, List<ServerPlayerEntity> players) {
        return !this.sleepanimation$active && manager.canResetTime(percentage, players);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
    private void sleepanimation$blockInstantJump(ServerWorld world, long time) {
    }

    @Inject(method = "wakeSleepingPlayers", at = @At("HEAD"), cancellable = true)
    private void sleepanimation$stayInBed(CallbackInfo ci) {
        if (this.sleepanimation$active && !ModConfig.instantWakeup) {
            ci.cancel();
        }
    }

    @Unique
    private void sleepanimation$advance(ServerWorld world) {
        long time = world.getTimeOfDay();
        long distance = (24000L - time % 24000L) % 24000L;
        int speed = ModConfig.skipSpeed;
        if (distance <= speed) {
            world.setTimeOfDay(time + distance);
            this.sleepanimation$active = false;
            if (!ModConfig.instantWakeup) {
                this.wakeSleepingPlayers();
            }
        } else {
            world.setTimeOfDay(time + speed);
        }
        this.sleepanimation$sync(world);
    }

    @Unique
    private void sleepanimation$sync(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        int size = players.size();
        if (size == 0) {
            return;
        }
        WorldTimeUpdateS2CPacket packet = new WorldTimeUpdateS2CPacket(
                world.getTime(),
                world.getTimeOfDay(),
                world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)
        );
        for (int i = 0; i < size; i++) {
            players.get(i).networkHandler.sendPacket(packet);
        }
    }
}
