package com.cupid.jikting.team.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRegisterResponse {

    private String invitationUrl;

    public static TeamRegisterResponse from(String invitationUrl) {
        return new TeamRegisterResponse(invitationUrl);
    }
}
