package com.cupid.jikting.chatting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.team.entity.Team;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chatting SET deleted = true WHERE id=?")
@AttributeOverride(name = "id", column = @Column(name = "chatting_room_id"))
@Entity
public class ChattingRoom extends BaseEntity implements Serializable {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private List<Chatting> chattings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    public void createChatting(Chatting chatting) {
        chattings.add(chatting);
        MemberProfile sender = chatting.getMemberProfile();
        MemberChattingRoom memberChattingRoom = MemberChattingRoom.of(sender, this);
        memberChattingRooms.add(memberChattingRoom);
        sender.addMemberChattingRoom(memberChattingRoom);
    }

    public String getOppositeTeamName(Team team) {
        return meeting.getOppositeTeamName(team);
    }

    public void confirmMeeting(Long meetingId, LocalDateTime schedule, String place) {
        meeting.confirm(meetingId, schedule, place);
    }
}
