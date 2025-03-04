package com.ismael.openstreamify.model;

import java.security.SecureRandom;

public class VerificationCodeGenerator {
    private static final int CHARACTERS[] = {0,1,2,3,4,5,6,7,8,9};
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS[(random.nextInt(CHARACTERS.length))]);
        }
        return code.toString();
    }
}
