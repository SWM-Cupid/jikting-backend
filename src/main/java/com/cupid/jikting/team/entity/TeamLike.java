package com.cupid.jikting.team.entity;

import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.ConfirmStatus;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE team_like SET is_deleted = true WHERE team_like_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "team_Like_id"))
@Entity
public class TeamLike extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private AcceptStatus acceptStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_team_id")
    private Team receivedTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_team_id")
    private Team sentTeam;

    public ChattingRoom accept() {
        acceptStatus = AcceptStatus.ACCEPT;
        Meeting meeting = Meeting.builder()
                .acceptingTeam(receivedTeam)
                .recommendedTeam(sentTeam)
                .confirmStatus(ConfirmStatus.UNDECIDED)
                .build();
        return ChattingRoom.builder()
                .meeting(meeting)
                .build();
    }

    public List<MemberProfile> getReceivedMemberProfiles() {
        return receivedTeam.getMemberProfiles();
    }

    public List<MemberProfile> getSentMemberProfiles() {
        return sentTeam.getMemberProfiles();
    }
}
