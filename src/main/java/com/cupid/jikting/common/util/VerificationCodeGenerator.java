package com.cupid.jikting.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCodeGenerator extends RandomGenerator {

    private static final int VERIFICATION_CODE_RANGE = 10;

    public static String generate(int verificationCodeLength) {
        return String.join(DELIMITER, IntStream.range(0, verificationCodeLength)
                .mapToObj(n -> RANDOM.nextInt(VERIFICATION_CODE_RANGE))
                .map(String::valueOf)
                .toArray(CharSequence[]::new));
    }
}
