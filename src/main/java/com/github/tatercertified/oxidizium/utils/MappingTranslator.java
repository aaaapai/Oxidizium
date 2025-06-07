package com.github.tatercertified.oxidizium.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MappingTranslator {
    private static final String NAMESPACE = "intermediary";
    private static final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

    // Translate Intermediary field name to runtime and return java.lang.reflect.Field
    public static Field resolveField(Class<?> ownerClass, String intermediaryFieldName, Class<?> fieldType) {
        // Unmap the class name to intermediary
        String intermediaryOwner = unmapClassName(ownerClass);

        // Remap the field name from intermediary to runtime
        String runtimeFieldName = resolver.mapFieldName(
                NAMESPACE,
                intermediaryOwner,
                intermediaryFieldName,
                fieldType.descriptorString()
        );

        try {
            // Look up the field in the actual runtime class
            Field field = ownerClass.getDeclaredField(runtimeFieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find field: " + runtimeFieldName + " in class " + ownerClass.getName(), e);
        }
    }

    public static Method resolveMethod(Class<?> ownerClass, String intermediaryMethodName, Class<?> returnType, Class<?>[] arguments) {
        // Unmap the class name to intermediary
        String intermediaryOwner = unmapClassName(ownerClass);

        // Unmap method arguments
        final var unmap = new String[arguments.length];
        for (int i = 0; i < unmap.length; i++) {
            unmap[i] = unmap(arguments[i]);
        }

        // Remap the method name from intermediary to runtime
        String runtimeMethodName = resolver.mapMethodName(
                NAMESPACE,
                intermediaryOwner,
                intermediaryMethodName,
                '(' + String.join("", unmap) + ')' + unmap(returnType)
        );

        try {
            // Look up the method in the actual runtime class
            Method method = ownerClass.getDeclaredMethod(runtimeMethodName);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find method: " + runtimeMethodName + " in class " + ownerClass.getName(), e);
        }
    }

    /**
     * Unmaps classes
     * @author Ampflower
     * @param clazz Class to upmap
     * @return Unmapped class
     */
    private static String unmapClassName(Class<?> clazz) {
        return resolver.unmapClassName(NAMESPACE, clazz.getName());
    }

    /**
     * Unmaps classes
     * @author Ampflower
     * @param clazz Class to upmap
     * @return Unmapped class signature
     */
    private static String unmap(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return clazz.descriptorString();
        }
        return 'L' + unmapClassName(clazz).replace('.', '/') + ';';
    }
}
