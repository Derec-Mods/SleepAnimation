package io.github.derec4.sleepanimation.mixin;

import io.github.derec4.sleepanimation.SkyInterpolator;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Inject(method = "setTimeOfDay", at = @At("HEAD"))
    private void sleepanimation$capture(long time, CallbackInfo ci) {
        SkyInterpolator.capture(((ClientWorld) (Object) this).getTimeOfDay());
    }

    @Inject(method = "setTimeOfDay", at = @At("TAIL"))
    private void sleepanimation$follow(long time, CallbackInfo ci) {
        SkyInterpolator.setTarget(((ClientWorld) (Object) this).getTimeOfDay());
    }
}
