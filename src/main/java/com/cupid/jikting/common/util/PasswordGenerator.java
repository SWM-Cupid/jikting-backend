package com.cupid.jikting.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordGenerator {

    private static final char[] CHAT_SET = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static final Random RANDOM = new Random();
    private static final String DELIMITER = "";
    private static final int PASSWORD_LENGTH = 8;

    public static String generate() {
        return String.join(DELIMITER, IntStream.range(0, PASSWORD_LENGTH)
                .mapToObj(i -> CHAT_SET[(int) (CHAT_SET.length * RANDOM.nextDouble())])
                .toArray(CharSequence[]::new));
    }
}
