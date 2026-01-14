package ru.otus;

import static com.google.common.base.Objects.equal;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloOtus {

    private static final Logger logger = Logger.getLogger(HelloOtus.class.getName());

    public static void main(String[] args) {
        String str1 = "Hello Otus";
        String str2 = null;
        String logMessage = "str1 = " + str1 + ", str2 = " + str2 + ", equalsResult = " + equal(str1, str2);
        logger.log(Level.INFO, logMessage);
    }
}
