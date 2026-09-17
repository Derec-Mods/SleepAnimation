package io.github.derec4.sleepanimation.mixin;

import io.github.derec4.sleepanimation.SkyInterpolator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.LunarWorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LunarWorldView.class)
public interface LunarWorldViewMixin {
    @Inject(method = "getSkyAngle", at = @At("HEAD"), cancellable = true)
    private void sleepanimation$smooth(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (!((Object) this instanceof ClientWorld)) {
            return;
        }
        cir.setReturnValue(SkyInterpolator.angle((LunarWorldView) (Object) this, tickDelta));
    }
}
