package com.cupid.jikting.member.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailRequest {

    private String title;
    private String senderAddress;
    private String senderName;
    private String body;
    private List<MailRequestRecipient> recipients;
}
