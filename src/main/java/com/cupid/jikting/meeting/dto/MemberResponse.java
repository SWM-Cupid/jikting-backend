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
public class MemberResponse {

	private String username;
	private List<ImageResponse> images;
	private int age;
	private String mbti;
	private String residence;
	private String company;
	private boolean isSmoke;
	private String drinkStatus;
	private int height;
	private String description;
	private List<PersonalityResponse> personalities;
	private List<HobbyResponse> hobbies;
	private String school;
}
