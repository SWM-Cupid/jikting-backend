package com.cupid.jikting.common.util;

import java.util.Random;
import java.util.stream.IntStream;

public class PasswordUtil {

    public static final char[] CHAT_SET = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, 8)
                .mapToObj(i -> {
                    double randomValue = random.nextDouble();
                    int index = (int) (CHAT_SET.length * randomValue);
                    return CHAT_SET[index];
                })
                .forEach(password::append);
        return password.toString();
    }
}
