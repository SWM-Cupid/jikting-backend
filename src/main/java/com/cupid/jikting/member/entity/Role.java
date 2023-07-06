package com.cupid.jikting.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST"), UNCERTIFIED("ROLE_UNCERTIFIED"), CERTIFIED("ROLE_CERTIFIED");

    private final String key;
}
