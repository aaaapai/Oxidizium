package com.github.tatercertified.oxidizium.utils;

import net.minecraft.util.math.MathHelper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class Cleanup {
    public static void cleanupClasses() {
        cleanupMathHelper();
    }

    private static void cleanupMathHelper() {
        try {
            removeArray(MathHelper.class, "ARCSINE_TABLE");
            removeArray(MathHelper.class, "COSINE_OF_ARCSINE_TABLE");
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

        // Set to a new empty array
        unsafe.putObject(staticFieldBase, staticFieldOffset, new double[0]);
    }
}
