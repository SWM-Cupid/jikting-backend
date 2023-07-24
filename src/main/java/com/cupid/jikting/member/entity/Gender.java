package com.cupid.jikting.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남"),
    FEMALE("여");

    private final String key;
}
