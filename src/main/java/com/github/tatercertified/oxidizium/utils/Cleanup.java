package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.Config;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
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
            removeField(MathHelper.class, "field_15727", double[].class);
            removeField(MathHelper.class, "field_15722", double[].class);
            removeField(MathHelper.class, "field_15725", float[].class);
            removeField(MathHelper.class, "field_15723", int[].class);
            removeField(MathHelper.class, "field_29852", long.class);
            removeField(MathHelper.class, "field_29853", long.class);
            removeField(MathHelper.class, "field_29854", long.class);
            removeField(MathHelper.class, "field_29855", long.class);
            removeField(MathHelper.class, "field_29844", float.class);
            removeField(MathHelper.class, "field_29845", float.class);
            removeField(MathHelper.class, "field_29846", float.class);
            removeField(MathHelper.class, "field_29847", float.class);
            removeField(MathHelper.class, "field_29848", float.class);
            removeField(MathHelper.class, "field_29849", float.class);
            removeField(MathHelper.class, "field_29856", float.class);
            removeField(MathHelper.class, "field_46243", Vector3f.class);
            removeField(MathHelper.class, "field_46244", Vector3f.class);
            removeField(MathHelper.class, "field_29857", double.class);
            removeField(MathHelper.class, "field_29858", int.class);
            removeField(MathHelper.class, "field_29859", int.class);
            removeField(MathHelper.class, "field_15728", double.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeField(Class<?> clazz, String intermediaryFieldName, Class<?> fieldType) throws NoSuchFieldException, IllegalAccessException {
        Field field = MappingTranslator.resolveField(clazz, intermediaryFieldName, fieldType);
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
