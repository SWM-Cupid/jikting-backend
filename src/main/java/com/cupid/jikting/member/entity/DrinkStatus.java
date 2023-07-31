package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DrinkStatus {

    NEVER("전혀 마시지 않음"),
    RARELY("거의 마시지 않음"),
    OFTEN("가끔 마심"),
    USUALLY("자주 마심");

    private final String message;

    public static DrinkStatus find(String message) {
        return Arrays.stream(values())
                .filter(value -> value.message.equals(message))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ApplicationError.INVALID_DRINK_STATUS));
    }
}
