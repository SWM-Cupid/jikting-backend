package com.cupid.jikting.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmokeStatus {

    NONSMOKING("비흡연"),
    SMOKING("흡연");

    private final String value;
}
