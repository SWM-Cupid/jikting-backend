package com.cupid.jikting.meeting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cupid.jikting.meeting.dto.RecommendedTeamResponse;
import com.cupid.jikting.meeting.service.MeetingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/meetings")
public class MeetingController {

	private final MeetingService meetingService;

	@GetMapping("/recommended-teams/{teamId}")
	public ResponseEntity<RecommendedTeamResponse> getRecommendedTeam(@PathVariable Long teamId) {
		return ResponseEntity.ok().body(meetingService.getRecommendedTeam(teamId));
	}

}
