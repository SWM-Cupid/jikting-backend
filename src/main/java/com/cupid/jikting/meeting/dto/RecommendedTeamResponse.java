package com.cupid.jikting.meeting.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedTeamResponse {

	private Long teamId;
	private List<MemberResponse> members;
	private List<PersonalityResponse> personalities;
}
