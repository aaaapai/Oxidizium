package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.Config;
import net.minecraft.util.math.MathHelper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class Cleanup {
    public static void cleanupClasses() {
        if (!Config.getInstance().test() && Config.getInstance().reducedMemoryUsage()) {
            cleanupMathHelper();
        }
    }

    private static void cleanupMathHelper() {
        try {
            removeArray(MathHelper.class, "field_15727", double[].class);
            removeArray(MathHelper.class, "field_15722", double[].class);
            removeArray(MathHelper.class, "field_15725", float[].class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeArray(Class<?> clazz, String intermediaryFieldName, Class<?> arrayType) throws NoSuchFieldException, IllegalAccessException {
        Field field = MappingTranslator.resolveField(clazz, intermediaryFieldName, arrayType);
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
