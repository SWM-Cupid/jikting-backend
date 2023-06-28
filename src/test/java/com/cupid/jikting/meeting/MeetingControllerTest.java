package com.cupid.jikting.meeting;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.meeting.controller.MeetingController;
import com.cupid.jikting.meeting.dto.HobbyResponse;
import com.cupid.jikting.meeting.dto.ImageResponse;
import com.cupid.jikting.meeting.dto.MemberResponse;
import com.cupid.jikting.meeting.dto.PersonalityResponse;
import com.cupid.jikting.meeting.dto.RecommendedTeamResponse;
import com.cupid.jikting.meeting.service.MeetingService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@WebMvcTest(MeetingController.class)
public class MeetingControllerTest extends ApiDocument {

	private final String CONTEXT_PATH = "/api/v1";

	private RecommendedTeamResponse recommendedTeamResponse;

	@MockBean
	private MeetingService meetingService;

	@BeforeEach
	void setUp() {
		List<HobbyResponse> hobbyResponseList = IntStream.rangeClosed(1, 3)
			.mapToObj(n -> HobbyResponse.builder()
				.hobby("취미" + n)
				.build())
			.collect(Collectors.toList());
		List<PersonalityResponse> personalityResponseList = IntStream.rangeClosed(1, 3)
			.mapToObj(n -> PersonalityResponse.builder()
				.personality("성격" + n)
				.build())
			.collect(Collectors.toList());
		List<ImageResponse> imageResponseList = LongStream.rangeClosed(1, 3)
			.mapToObj(n -> ImageResponse.builder()
				.isMain(true)
				.memberId(n)
				.url("http://사진"+n+"-url")
				.build())
			.collect(Collectors.toList());
		List<MemberResponse> memberResponseList = IntStream.rangeClosed(1, 3)
				.mapToObj(n -> MemberResponse.builder()
					.age(1)
					.company("line")
					.description("description")
					.drinkStatus("안마심")
					.height(180)
					.hobbies(hobbyResponseList)
					.images(imageResponseList)
					.build())
					.collect(Collectors.toList());
		recommendedTeamResponse = RecommendedTeamResponse.builder()
			.teamId(1L)
			.members(memberResponseList)
			.personalities(personalityResponseList)
			.build();
	}

	@Test
	void 추천팀_조회_성공() throws Exception {
	    //given
		willReturn(recommendedTeamResponse).given(meetingService).getRecommendedTeam(anyLong());
	    //when
		ResultActions resultActions = mockMvc.perform(get(CONTEXT_PATH + "/meetings/recommended-teams/1")
			.contextPath(CONTEXT_PATH));
	    //then
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(toJson(recommendedTeamResponse)))
			.andDo(print())
			.andDo(toDocument("get-recommended-team-success"));
	}
}
