package com.cupid.jikting.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordGenerator extends RandomGenerator {

    private static final String[] CHARACTERS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };
    private static final int PASSWORD_LENGTH = 8;

    public static String generate() {
        return String.join(DELIMITER, IntStream.range(0, PASSWORD_LENGTH)
                .mapToObj(i -> CHARACTERS[(int) (CHARACTERS.length * RANDOM.nextDouble())])
                .toArray(String[]::new));
    }
}
