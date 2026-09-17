package io.github.derec4.sleepanimation;

import io.github.derec4.sleepanimation.mixin.DimensionTypeAccessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LunarWorldView;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public final class SkyInterpolator {
    private static double displayed = Double.NaN;
    private static long lastNanos;

    private SkyInterpolator() {
    }

    public static float angle(LunarWorldView view, float tickDelta) {
        long actual = view.getLunarTime();
        long now = System.nanoTime();
        if (Double.isNaN(displayed)) {
            displayed = actual;
            lastNanos = now;
            return celestial(view.getDimension(), displayed);
        }
        double dt = (now - lastNanos) / 1_000_000_000.0;
        lastNanos = now;
        if (dt > 0.0 && dt < 0.1) {
            double diff = actual - displayed;
            if (Math.abs(diff) > 4000.0) {
                displayed = actual;
            } else {
                displayed += diff * (1.0 - Math.exp(-14.0 * dt));
            }
        }
        return celestial(view.getDimension(), displayed);
    }

    private static float celestial(DimensionType type, double time) {
        OptionalLong fixed = ((DimensionTypeAccessor) (Object) type).getFixedTime();
        double t = fixed.isPresent() ? fixed.getAsLong() : time;
        double d = MathHelper.fractionalPart(t / 24000.0 - 0.25);
        double e = 0.5 - Math.cos(d * Math.PI) / 2.0;
        return (float) ((d * 2.0 + e) / 3.0);
    }
}
