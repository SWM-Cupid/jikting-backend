package com.cupid.jikting.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailRequestRecipient {

    private String address;
    private String name;
    private String type;

    public static MailRequestRecipient of(String name, String email, String type) {
        return new MailRequestRecipient(email, name, type);
    }
}
