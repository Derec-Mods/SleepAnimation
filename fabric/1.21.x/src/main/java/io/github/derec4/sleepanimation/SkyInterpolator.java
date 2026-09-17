package io.github.derec4.sleepanimation;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LunarWorldView;

public final class SkyInterpolator {
    private static double from = Double.NaN;
    private static double to = Double.NaN;

    private SkyInterpolator() {
    }

    public static void capture(long currentTime) {
        if (Double.isNaN(from)) {
            from = currentTime;
            to = currentTime;
        }
    }

    public static void setTarget(long time) {
        from = Double.isNaN(to) ? time : to;
        to = time;
        if (Math.abs(to - from) > 3000.0) {
            from = to;
        }
    }

    public static float angle(LunarWorldView view, float tickDelta) {
        if (Double.isNaN(from)) {
            return view.getDimension().getSkyAngle(view.getLunarTime());
        }
        double time = MathHelper.lerp(
                (double) smooth(MathHelper.clamp(tickDelta, 0.0F, 1.0F)),
                from,
                to
        );
        return view.getDimension().getSkyAngle(Math.round(time));
    }

    private static float smooth(float t) {
        return t * t * (3.0F - 2.0F * t);
    }
}
