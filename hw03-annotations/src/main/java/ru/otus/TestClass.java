package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class TestClass {

    @Test
    public void method1() {
        System.out.println("Запущен method1 с аннотацией @Test");
    }

    @Before
    public void method2() {
        System.out.println("\nЗапущен method2 с аннотацией @Before");
    }

    @Test
    public void method3() {
        System.out.println("Запущен method3 с аннотацией @Test");
    }

    @After
    public void method4() {
        System.out.println("Запущен method4 с аннотацией @After");
    }

    @Test
    public void method5() {
        System.out.println("Запущен method5 с аннотацией @Test и выбросом исключения");
        throw new RuntimeException();
    }

    @Test
    public void method6() {
        System.out.println("Запущен method6 с аннотацией @Test");
    }

    @After
    public void method7() {
        System.out.println("Запущен method7 с аннотацией @After");
    }
}
