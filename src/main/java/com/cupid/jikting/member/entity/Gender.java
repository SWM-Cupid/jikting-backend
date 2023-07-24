package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남"),
    FEMALE("여");

    private final String key;

    public static Gender find(String key) {
        return Arrays.stream(values())
                .filter(value -> value.key.equals(key))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ApplicationError.INVALID_GENDER));
    }
}
