package com.cupid.jikting.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCodeGenerator {

    private static final String DELIMITER = "";
    private static final int VERIFICATION_CODE_RANGE = 9;

    public static String generate(int verificationCodeLength) {
        return String.join(DELIMITER, IntStream.range(0, verificationCodeLength)
                .mapToObj(n -> getSecureRandom().nextInt(VERIFICATION_CODE_RANGE))
                .map(String::valueOf)
                .toArray(CharSequence[]::new));
    }

    private static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
