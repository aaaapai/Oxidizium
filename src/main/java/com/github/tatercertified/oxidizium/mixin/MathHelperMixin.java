package com.github.tatercertified.oxidizium.mixin;

import com.github.tatercertified.rust.lib_h;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(MathHelper.class)
public class MathHelperMixin {

    // Remove useless things
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;"))
    private static <T> Object dontMake(T object, Consumer<? super T> initializer) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/lang/Math;asin(D)D"), cancellable = true)
    private static void cancelArrayFill(CallbackInfo ci) {
        ci.cancel();
    }
    // TODO Remove ArcSineTable and CosineOfArcSineTable

    // Implement Rust
    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float sin(float value) {
        return lib_h.sin_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float cos(float value) {
        return lib_h.cos_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float sqrt(float value) {
        return lib_h.sqrt_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int floor(float value) {
        return lib_h.floor_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int floor(double value) {
        return lib_h.floor_double(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static long lfloor(double value) {
        return lib_h.floor_long(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float abs(float value) {
        return lib_h.abs_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int abs(int value) {
        return lib_h.abs_int(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int ceil(float value) {
        return lib_h.ceil_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int ceil(double value) {
        return lib_h.ceil_double(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int clamp(int value, int min, int max) {
        return lib_h.clamp_int(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static long clamp(long value, long min, long max) {
        return lib_h.clamp_long(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float clamp(float value, float min, float max) {
        return lib_h.clamp_float(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double clamp(double value, double min, double max) {
        return lib_h.clamp_double(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double clampedLerp(double start, double end, double delta) {
        return lib_h.clamp_lerp_double(start, end, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float clampedLerp(float start, float end, float delta) {
        return lib_h.clamp_lerp_float(start, end, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double absMax(double a, double b) {
        return lib_h.abs_max(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int floorDiv(int dividend, int divisor) {
        return lib_h.floor_div(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static boolean approximatelyEquals(float a, float b) {
        return lib_h.approximately_equals_float(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static boolean approximatelyEquals(double a, double b) {
        return lib_h.approximately_equals_double(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int floorMod(int dividend, int divisor) {
        return lib_h.floor_mod_int(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float floorMod(float dividend, float divisor) {
        return lib_h.floor_mod_float(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double floorMod(double dividend, double divisor) {
        return lib_h.floor_mod_double(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static boolean isMultipleOf(int a, int b) {
        return lib_h.is_multiple_of(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static byte packDegrees(float degrees) {
        return lib_h.pack_degrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float unpackDegrees(byte packedDegrees) {
        return lib_h.unpack_degrees(packedDegrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int wrapDegrees(int degrees) {
        return lib_h.wrap_degrees_int(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float wrapDegrees(long degrees) {
        return lib_h.wrap_degrees_long(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float wrapDegrees(float degrees) {
        return lib_h.wrap_degrees_float(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double wrapDegrees(double degrees) {
        return lib_h.wrap_degrees_double(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float subtractAngles(float start, float end) {
        return lib_h.subtract_angles(start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float angleBetween(float first, float second) {
        return lib_h.angle_between(first, second);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float clampAngle(float value, float mean, float delta) {
        return lib_h.clamp_angle(value, mean, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float stepTowards(float from, float to, float step) {
        return lib_h.step_towards(from, to, step);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float stepUnwrappedAngleTowards(float from, float to, float step) {
        return lib_h.step_unwrapped_angle_towards(from, to, step);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int smallestEncompassingPowerOfTwo(int value) {
        return lib_h.smallest_encompassing_power_of_two(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static boolean isPowerOfTwo(int value) {
        return lib_h.is_power_of_two(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int ceilLog2(int value) {
        return lib_h.ceil_log_2(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int floorLog2(int value) {
        return lib_h.floor_log_2(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float fractionalPart(float value) {
        return lib_h.fractional_part_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double fractionalPart(double value) {
        return lib_h.fractional_part_double(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Deprecated
    @Overwrite
    public static long hashCode(int x, int y, int z) {
        return lib_h.hash_code(x, y, z);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double getLerpProgress(double value, double start, double end) {
        return lib_h.get_lerp_progress_double(value, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float getLerpProgress(float value, float start, float end) {
        return lib_h.get_lerp_progress_float(value, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double atan2(double y, double x) {
        return lib_h.atan_2(y, x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float inverseSqrt(float x) {
        return lib_h.inverse_sqrt_float(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double inverseSqrt(double x) {
        return lib_h.inverse_sqrt_double(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float fastInverseCbrt(float x) {
        return lib_h.fast_inverse_cbrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int hsvToRgb(float hue, float saturation, float value) {
        return lib_h.hsv_to_rgb(hue, saturation, value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int hsvToArgb(float hue, float saturation, float value, int alpha) {
        return lib_h.hsv_to_argb(hue, saturation, value, alpha);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int idealHash(int value) {
        return lib_h.ideal_hash(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int lerp(float delta, int start, int end) {
        return lib_h.lerp_int(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int lerpPositive(float delta, int start, int end) {
        return lib_h.lerp_positive(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float lerp(float delta, float start, float end) {
        return lib_h.lerp_float(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double lerp(double delta, double start, double end) {
        return lib_h.lerp_double(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double lerp2(double deltaX, double deltaY, double x0y0, double x1y0, double x0y1, double x1y1) {
        return lib_h.lerp_2(deltaX, deltaY, x0y0, x1y0, x0y1, x1y1);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double lerp3(
            double deltaX,
            double deltaY,
            double deltaZ,
            double x0y0z0,
            double x1y0z0,
            double x0y1z0,
            double x1y1z0,
            double x0y0z1,
            double x1y0z1,
            double x0y1z1,
            double x1y1z1
    ) {
        return lib_h.lerp_3(deltaX, deltaY, deltaZ, x0y0z0, x1y0z0, x0y1z0, x1y1z0, x0y0z1, x1y0z1, x0y1z1, x1y1z1);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float catmullRom(float delta, float p0, float p1, float p2, float p3) {
        return lib_h.catmull_rom(delta, p0, p1, p2, p3);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double perlinFade(double value) {
        return lib_h.perlin_fade(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double perlinFadeDerivative(double value) {
        return lib_h.perlin_fade_derivative(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int sign(double value) {
        return lib_h.sign(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float lerpAngleDegrees(float delta, float start, float end) {
        return lib_h.lerp_angle_degrees_float(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double lerpAngleDegrees(double delta, double start, double end) {
        return lib_h.lerp_angle_degrees_double(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float lerpAngleRadians(float delta, float start, float end) {
        return lib_h.lerp_angle_radians(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float wrap(float value, float maxDeviation) {
        return lib_h.wrap(value, maxDeviation);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float square(float n) {
        return lib_h.sqrt_float(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double square(double n) {
        return lib_h.square_double(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int square(int n) {
        return lib_h.square_int(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static long square(long n) {
        return lib_h.square_long(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double clampedMap(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return lib_h.clamped_map_double(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float clampedMap(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return lib_h.clamped_map_float(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double map(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return lib_h.map_double(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float map(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return lib_h.map_float(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int roundUpToMultiple(int value, int divisor) {
        return lib_h.round_up_to_multiple(value, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int ceilDiv(int a, int b) {
        return lib_h.ceil_div(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double squaredHypot(double a, double b) {
        return lib_h.squared_hypot(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double hypot(double a, double b) {
        return lib_h.hypot_double(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float hypot(float a, float b) {
        return lib_h.hypot_float(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double squaredMagnitude(double a, double b, double c) {
        return lib_h.squared_magnitude(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static double magnitude(double a, double b, double c) {
        return lib_h.magnitude_double(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float magnitude(float a, float b, float c) {
        return lib_h.magnitude_float(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static int roundDownToMultiple(double a, int b) {
        return lib_h.round_down_to_multiple(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Overwrite
    public static float easeInOutSine(float value) {
        return lib_h.ease_in_out_sine(value);
    }
}
