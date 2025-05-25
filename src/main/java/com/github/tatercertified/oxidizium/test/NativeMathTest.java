package com.github.tatercertified.oxidizium.test;

import com.github.tatercertified.oxidizium.Oxidizium;
import com.github.tatercertified.oxidizium.mixin.MathHelperMixin;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ThreadLocalRandom;

public class NativeMathTest {
    public static boolean testsFailed;
    private static final float FLOAT_TOLERANCE = 0.0001F;
    private static final double DOUBLE_TOLERANCE = 0.0000001;

    public static void testNativeMath() {
        Class<?> mathHelperMixin = MathHelperMixin.class;
        Class<?> mathHelperClass = MathHelper.class;

        for (Method method : mathHelperMixin.getDeclaredMethods()) {
            try {
                Method mathHelperMethod = mathHelperClass.getMethod(method.getName(), method.getParameterTypes());
                if (method.getReturnType().equals(float.class)
                        || method.getReturnType().equals(double.class)
                        || method.getReturnType().equals(int.class)
                        || method.getReturnType().equals(long.class)
                        || method.getReturnType().equals(byte.class)) {
                    Oxidizium.TEST_LOGGER.info("Testing {} ({})}", method.getName(), method.getParameterTypes());
                    try {
                        invokeAndTest(method, mathHelperMethod);
                    } catch (Exception e) {
                        Oxidizium.TEST_LOGGER.error("Error Thrown");
                        Oxidizium.TEST_LOGGER.warn(e.toString());
                        testsFailed = true;
                    }
                }
            } catch (NoSuchMethodException e) {
                Oxidizium.TEST_LOGGER.warn("Method {} does not exist in MathHelper", method.getName());
            }
        }
    }

    private static void invokeAndTest(Method nativeMethod, Method javaMethod) throws Exception {
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

        assertTrue(resultsEqual,
                String.format("%s is invalid: %s != %s", nativeMethod.getName(), nativeResult, javaResult));
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

    private static void assertTrue(boolean bool, String ifFailed) {
        if (!bool) {
            testsFailed = true;
            Oxidizium.TEST_LOGGER.error(ifFailed);
        }
    }
}
