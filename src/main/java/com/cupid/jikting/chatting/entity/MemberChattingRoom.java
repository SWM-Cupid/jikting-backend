package com.cupid.jikting.chatting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member_chatting_room SET is_deleted = true WHERE id=?")
@AttributeOverride(name = "id", column = @Column(name = "member_chatting_room_id"))
@Entity
public class MemberChattingRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    public static MemberChattingRoom of(MemberProfile memberProfile, ChattingRoom chattingRoom) {
        MemberChattingRoom memberChattingRoom = new MemberChattingRoom(memberProfile, chattingRoom);
        memberProfile.addMemberChattingRoom(memberChattingRoom);
        chattingRoom.addMemberChattingRoom(memberChattingRoom);
        return memberChattingRoom;
    }
}
