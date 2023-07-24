package com.cupid.jikting.chatting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chatting SET deleted = true WHERE id=?")
@AttributeOverride(name = "id", column = @Column(name = "member_chatting_room_id"))
@Entity
public class MemberChattingRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    public static MemberChattingRoom of(Member member, ChattingRoom chattingRoom) {
        return new MemberChattingRoom(member, chattingRoom);
    }
}
