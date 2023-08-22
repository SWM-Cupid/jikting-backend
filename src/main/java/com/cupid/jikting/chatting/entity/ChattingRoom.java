package com.cupid.jikting.chatting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.team.entity.Team;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chatting_room SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@AttributeOverride(name = "id", column = @Column(name = "chatting_room_id"))
@Entity
public class ChattingRoom extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    public String getOppositeTeamName(Team team) {
        return meeting.getOppositeTeamName(team);
    }

    public String getOppositeTeamDescription(Team team) {
        return meeting.getOppositeTeamDescription(team);
    }

    public List<String> getOppositeTeamKeywords(Team team) {
        return meeting.getOppositeTeamKeywords(team);
    }

    public List<String> getOppositeTeamImageUrls(Team team) {
        return meeting.getOppositeTeamImageUrls(team);
    }

    public void addMemberChattingRoom(MemberChattingRoom memberChattingRoom) {
        memberChattingRooms.add(memberChattingRoom);
    }

    public void confirmMeeting(Long meetingId, LocalDateTime schedule, String place) {
        meeting.confirm(meetingId, schedule, place);
    }

    public List<MemberProfile> getMemberProfiles() {
        return meeting.getMemberProfiles();
    }
}
