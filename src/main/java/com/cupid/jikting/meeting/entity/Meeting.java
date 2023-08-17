package com.cupid.jikting.meeting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.WrongAccessException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.team.entity.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE meeting SET is_deleted = true WHERE meeting_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "meeting_id"))
@Entity
public class Meeting extends BaseEntity {

    private static final String CHATTING_ROOM_NAME_DELIMITER = " - ";

    private LocalDateTime schedule;
    private String place;

    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_team_id")
    private Team recommendedTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acceptiong_team_id")
    private Team acceptingTeam;

    public String getOppositeTeamName(Team team) {
        if (recommendedTeam.equals(team)) {
            return acceptingTeam.getName();
        }
        return recommendedTeam.getName();
    }

    public String getOppositeTeamDescription(Team team) {
        if (recommendedTeam.equals(team)) {
            return acceptingTeam.getDescription();
        }
        return recommendedTeam.getDescription();
    }

    public List<String> getOppositeTeamKeywords(Team team) {
        if (recommendedTeam.equals(team)) {
            return acceptingTeam.getPersonalities();
        }
        return recommendedTeam.getPersonalities();
    }

    public List<MemberProfile> getMemberProfiles() {
        List<MemberProfile> memberProfiles = new ArrayList<>(recommendedTeam.getMemberProfiles());
        memberProfiles.addAll(acceptingTeam.getMemberProfiles());
        return memberProfiles;
    }

    public void confirm(Long meetingId, LocalDateTime schedule, String place) {
        validate(meetingId);
        this.schedule = schedule;
        this.place = place;
        confirmStatus = ConfirmStatus.DECIDED;
    }

    private void validate(Long meetingId) {
        if (!id.equals(meetingId)) {
            throw new WrongAccessException(ApplicationError.WRONG_ACCESS);
        }
    }
}
