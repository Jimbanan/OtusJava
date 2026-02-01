package ru.otus;

import ru.otus.util.TestRunnerUtils;

public class TestExecutor {
    public static void main(String[] args) {
        TestRunnerUtils.executeTests(TestClass.class);
    }
}
