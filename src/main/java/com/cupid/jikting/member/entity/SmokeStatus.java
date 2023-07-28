package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SmokeStatus {

    NONSMOKING("비흡연"),
    SMOKING("흡연");

    private final String message;

    public static SmokeStatus find(String message) {
        return Arrays.stream(values())
                .filter(value -> value.message.equals(message))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ApplicationError.INVALID_GENDER));
    }
}
