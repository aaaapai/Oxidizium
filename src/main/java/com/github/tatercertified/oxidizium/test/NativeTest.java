package com.github.tatercertified.oxidizium.test;

import com.github.tatercertified.oxidizium.Oxidizium;
import com.github.tatercertified.oxidizium.utils.MixinCloner;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class NativeTest {
    private static final float FLOAT_TOLERANCE = 0.0001F;
    private static final double DOUBLE_TOLERANCE = 0.0000001;

    public static void invokeTests() {
        testFramework("com/github/tatercertified/oxidizium/mixin/MathHelperMixin", "Native Math", MathHelper.class, 50, float.class, double.class, int.class, long.class, byte.class);
        testFramework("com/github/tatercertified/oxidizium/mixin/compat/LithiumMathHelperMixin", "Native Math Lithium Compat", MathHelper.class, 50, float.class, double.class, int.class, long.class, byte.class);
    }

    /**
     * Tests parity between a native Mixin and the original Java class
     * @param mixinPath Path to the Mixin; Ex: <b>"com/github/tatercertified/oxidizium/mixin/compat/LithiumMathHelperMixin"</b>
     * @param testName Name of the test
     * @param vanillaClass Vanilla class instance
     * @param runs Number of times to run the test
     * @param returnFilter A filter for return types of tested methods. Null for no filter
     */
    private static void testFramework(String mixinPath, String testName, Class<?> vanillaClass, int runs, @Nullable Class<?>... returnFilter) {
        String mixinName = mixinPath.substring(mixinPath.lastIndexOf('/') + 1);
        String clone = "com/github/tatercertified/oxidizium/Cloned" + mixinName;
        Class<?> mixin;
        try {
            mixin = MixinCloner.cloneStaticMethods(mixinPath, clone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean testsFailed = false;
        Oxidizium.TEST_LOGGER.info("Starting {} Test", testName);
        for (int i = 0; i < runs; i++) {
            for (Method method : mixin.getDeclaredMethods()) {
                try {
                    Method mathHelperMethod = vanillaClass.getMethod(method.getName(), method.getParameterTypes());
                    if (returnFilter == null || Arrays.stream(returnFilter).anyMatch(clazz -> clazz == method.getReturnType())) {
                        try {
                            testsFailed = !invokeAndTest(method, mathHelperMethod);
                        } catch (Exception e) {
                            Oxidizium.TEST_LOGGER.info("\u001B[31m {} ({}) Has Errored \u001B[0m", method.getName(), method.getParameterTypes());
                            Oxidizium.TEST_LOGGER.warn(e.toString());
                            testsFailed = true;
                        }
                    }
                } catch (NoSuchMethodException _) {
                }
            }
            if (testsFailed) {
                break;
            }
        }
        if (testsFailed) {
            Oxidizium.TEST_LOGGER.info("\u001B[31m {} Test Has Failed \u001B[0m", testName);
        } else {
            Oxidizium.TEST_LOGGER.info("{} Test Has Passed", testName);
        }
    }

    private static boolean invokeAndTest(Method nativeMethod, Method javaMethod) throws Exception {
        Class<?>[] parameterTypes = nativeMethod.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getCorrectValue(parameterTypes[i]);
        }

        // Adjust values for parameters named 'min' and 'max'
        adjustMinMaxArguments(nativeMethod, args);

        Object nativeResult = nativeMethod.invoke(null, args);
        Object javaResult = javaMethod.invoke(null, args);

        boolean resultsEqual = areResultsEquivalent(nativeResult, javaResult, nativeMethod.getReturnType());

        return assertTrue(resultsEqual,
                String.format("\u001B[31m %s is invalid: %s != %s \u001B[0m", nativeMethod.getName(), nativeResult, javaResult));
    }

    private static boolean areResultsEquivalent(Object nativeResult, Object javaResult, Class<?> returnType) {
        boolean acceptable;
        boolean exact = true;
        if (returnType.equals(float.class)) {
            float error = Math.abs((float) nativeResult - (float) javaResult);
            acceptable = error <= FLOAT_TOLERANCE;
            exact = nativeResult.equals(javaResult);
        } else if (returnType.equals(double.class)) {
            double error = Math.abs((double) nativeResult - (double) javaResult);
            acceptable = error <= DOUBLE_TOLERANCE;
            exact = nativeResult.equals(javaResult);
        } else {
            acceptable = nativeResult.equals(javaResult);
        }

        if (acceptable && !exact) {
            Oxidizium.TEST_LOGGER.warn("Precision issue detected: {} != {}", nativeResult, javaResult);
        }

        return acceptable;
    }

    private static void adjustMinMaxArguments(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        Integer minIndex = null;
        Integer maxIndex = null;

        for (int i = 0; i < parameters.length; i++) {
            if ("min".equals(parameters[i].getName())) {
                minIndex = i;
            } else if ("max".equals(parameters[i].getName())) {
                maxIndex = i;
            }
        }

        if (minIndex != null && maxIndex != null) {
            if (((Number) args[minIndex]).doubleValue() > ((Number) args[maxIndex]).doubleValue()) {

                Object temp = args[minIndex];
                args[minIndex] = args[maxIndex];
                args[maxIndex] = temp;
            }
        } else if (minIndex != null) {
            // Only min identified, assume last parameter as max
            maxIndex = parameters.length - 1;
            if (((Number) args[minIndex]).doubleValue() > ((Number) args[maxIndex]).doubleValue()) {
                Oxidizium.TEST_LOGGER.info("Assuming last parameter as 'max' and swapping with 'min'");

                Object temp = args[minIndex];
                args[minIndex] = args[maxIndex];
                args[maxIndex] = temp;
            }
        }
    }

    private static Number getCorrectValue(Class<?> type) {
        return switch (type.getName()) {
            case "float" -> ThreadLocalRandom.current().nextFloat(0.0F, (float) (Math.PI * 2.0F));
            case "int" -> ThreadLocalRandom.current().nextInt(0, 1000);
            case "double" -> ThreadLocalRandom.current().nextDouble(0.0, Math.PI * 2.0);
            case "long" -> ThreadLocalRandom.current().nextLong(0, 1000);
            case "byte" -> (byte) ThreadLocalRandom.current().nextInt(0, 128);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    private static boolean assertTrue(boolean bool, String ifFailed) {
        if (!bool) {
            Oxidizium.TEST_LOGGER.error(ifFailed);
        }
        return bool;
    }
}
