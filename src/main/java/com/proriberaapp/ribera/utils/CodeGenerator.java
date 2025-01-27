package com.proriberaapp.ribera.utils;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGenerator {
    private static final int CODE_LENGTH = 8;
    private static final Random RANDOM = new SecureRandom();

    public static String generateCode() {
        int randomNumber = RANDOM.nextInt(100000000);

        return String.format("%08d", randomNumber);
    }

    // Alternative using StringBuilder
    public static String generateCodeAlt() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }

}
