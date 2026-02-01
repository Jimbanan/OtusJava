package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.exception.TestException;

public class TestRunnerUtils {

    public static void executeTests(Class<?> clazz) {
        var beforeMethods = methodsPartition(clazz, Before.class);
        var afterMethods = methodsPartition(clazz, After.class);
        var testMethods = methodsPartition(clazz, Test.class);

        var errorMethodsCount = runTest(clazz, testMethods, beforeMethods, afterMethods);

        printStatistics(beforeMethods, afterMethods, testMethods, errorMethodsCount);
    }

    private static List<Method> methodsPartition(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .toList();
    }

    private static long runTest(
            Class<?> clazz, List<Method> testMethods, List<Method> beforeMethods, List<Method> afterMethods) {
        var countErrors = 0;

        try {
            for (Method testMethod : testMethods) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                try {
                    for (Method beforeMethod : beforeMethods) {
                        beforeMethod.setAccessible(true);
                        beforeMethod.invoke(instance);
                    }

                    testMethod.setAccessible(true);
                    testMethod.invoke(instance);
                } catch (Exception e) {
                    countErrors++;
                } finally {
                    for (Method afterMethod : afterMethods) {
                        afterMethod.setAccessible(true);
                        afterMethod.invoke(instance);
                    }
                }
            }
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            throw new TestException(e.getMessage());
        }

        return countErrors;
    }

    private static void printStatistics(
            List<Method> beforeMethods, List<Method> afterMethods, List<Method> testMethods, long errorMethodsCount) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Всего методов с @Before: " + beforeMethods.size());
        System.out.println("Всего методов с @After: " + afterMethods.size());
        System.out.println("Всего методов с @Test: " + testMethods.size());
        System.out.println(
                "Количество успешно выполненных методов с @Test: " + (testMethods.size() - errorMethodsCount));
        System.out.println("Количество ошибок при выполнении методов с @Test: " + errorMethodsCount);
        System.out.println("--------------------------------------------------");
    }
}
