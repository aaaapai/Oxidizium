package com.github.tatercertified.oxidizium.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public final class MappingTranslator {
    private static final String NAMESPACE = "intermediary";
    private static final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
    private static final boolean IS_DEV = !resolver.getCurrentRuntimeNamespace().equals(NAMESPACE);


    public static String remapFieldName(String remappedOwner, String intermediaryField, String fieldDesc) {
        if (IS_DEV) {
            String resolved = resolver.mapFieldName(
                    NAMESPACE,
                    remappedOwner,
                    intermediaryField,
                    fieldDesc
            );

            FieldStripper.STRIPPER_LOGGER.info("Remapping {} to {}", intermediaryField, resolved);
            return resolved;
        } else {
            return intermediaryField;
        }
    }

    /**
     * Unmaps classes
     * @author Ampflower
     * @param intermediaryClass dot path to class
     * @return Unmapped class
     */
    public static String remapClassName(String intermediaryClass) {
        if (IS_DEV) {
            String resolved = resolver.mapClassName(NAMESPACE, intermediaryClass);
            FieldStripper.STRIPPER_LOGGER.info("Remapping {} to {}", intermediaryClass, resolved);
            return resolved;
        } else {
            return intermediaryClass;
        }
    }
}
