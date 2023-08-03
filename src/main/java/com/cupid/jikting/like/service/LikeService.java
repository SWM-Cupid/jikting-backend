package com.cupid.jikting.like.service;

import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.team.entity.TeamLike;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.repository.TeamLikeRepository;
import com.cupid.jikting.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamLikeRepository teamLikeRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Transactional(readOnly = true)
    public List<LikeResponse> getAllReceivedLike(Long memberProfileId) {
        return getTeamMemberById(memberProfileId)
                .getReceivedTeamLikes()
                .stream()
                .map(LikeResponse::fromReceivedTeamLike)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LikeResponse> getAllSentLike(Long memberProfileId) {
        return getTeamMemberById(memberProfileId)
                .getSentTeamLikes()
                .stream()
                .map(LikeResponse::fromSentTeamLike)
                .collect(Collectors.toList());
    }

    public void acceptLike(Long likeId) {
        TeamLike teamLike = getTeamLikeById(likeId);
        ChattingRoom chattingRoom = teamLike.accept();
        teamLike.getReceivedTeamMemberProfiles()
                .forEach(memberProfile -> MemberChattingRoom.of(memberProfile, chattingRoom));
        teamLike.getSentTeamMemberProfiles()
                .forEach(memberProfile -> MemberChattingRoom.of(memberProfile, chattingRoom));
        teamLikeRepository.save(teamLike);
        chattingRoomRepository.save(chattingRoom);
    }

    public void rejectLike(Long likeId) {
    }

    public TeamDetailResponse getTeamDetail(Long likeId) {
        return null;
    }

    private TeamMember getTeamMemberById(Long memberProfileId) {
        return teamMemberRepository.getTeamMemberByMemberProfileId(memberProfileId)
                .orElseThrow(() -> new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM));
    }

    private TeamLike getTeamLikeById(Long likeId) {
        return teamLikeRepository.findById(likeId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.LIKE_NOT_FOUND));
    }
}
