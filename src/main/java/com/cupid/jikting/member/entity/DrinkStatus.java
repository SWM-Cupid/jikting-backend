package com.cupid.jikting.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkStatus {

    NEVER("전혀 마시지 않음"),
    RARELY("거의 마시지 않음"),
    OFTEN("가끔 마심"),
    USUALLY("자주 마심");

    private final String value;
}
