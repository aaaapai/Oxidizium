package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.Config;
import net.minecraft.util.math.MathHelper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class Cleanup {
    public static void cleanupClasses() {
        if (!Config.getInstance().debug() && Config.getInstance().reducedMemoryUsage()) {
            cleanupMathHelper();
        }
    }

    private static void cleanupMathHelper() {
        try {
            removeArray(MathHelper.class, "ARCSINE_TABLE");
            removeArray(MathHelper.class, "COSINE_OF_ARCSINE_TABLE");
            removeArray(MathHelper.class, "SINE_TABLE");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeArray(Class clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Get Unsafe instance
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        // Static fields require the base as null
        Object staticFieldBase = unsafe.staticFieldBase(field);
        long staticFieldOffset = unsafe.staticFieldOffset(field);

        // Set to a null
        unsafe.putObject(staticFieldBase, staticFieldOffset, null);
    }
}
