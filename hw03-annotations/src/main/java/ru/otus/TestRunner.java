package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class TestRunner {
    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        executeTests(TestClass.class);
    }

    private static void executeTests(Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        var beforeMethods = new ArrayList<Method>();
        var afterMethods = new ArrayList<Method>();
        var testMethods = new ArrayList<Method>();

        methodsPartition(clazz, beforeMethods, afterMethods, testMethods);
        var errorMethodsCount = runTest(clazz, testMethods, beforeMethods, afterMethods);

        printStatistics(beforeMethods, afterMethods, testMethods, errorMethodsCount);
    }

    private static void methodsPartition(
            Class<?> clazz,
            ArrayList<Method> beforeMethods,
            ArrayList<Method> afterMethods,
            ArrayList<Method> testMethods) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(Before.class)) {
                    beforeMethods.add(declaredMethod);
                }
                if (declaredAnnotation.annotationType().equals(After.class)) {
                    afterMethods.add(declaredMethod);
                }
                if (declaredAnnotation.annotationType().equals(Test.class)) {
                    testMethods.add(declaredMethod);
                }
            }
        }
    }

    private static long runTest(
            Class<?> clazz,
            ArrayList<Method> testMethods,
            ArrayList<Method> beforeMethods,
            ArrayList<Method> afterMethods)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        var countErrors = 0;

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

        return countErrors;
    }

    private static void printStatistics(
            ArrayList<Method> beforeMethods,
            ArrayList<Method> afterMethods,
            ArrayList<Method> testMethods,
            long errorMethodsCount) {
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
