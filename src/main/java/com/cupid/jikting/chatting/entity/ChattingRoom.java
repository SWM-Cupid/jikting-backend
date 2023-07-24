package com.cupid.jikting.chatting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
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

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private List<Chatting> chattings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    public void createChatting(Chatting chatting) {
        chattings.add(chatting);
        Member sender = chatting.getMember();
        MemberChattingRoom memberChattingRoom = MemberChattingRoom.of(sender, this);
        memberChattingRooms.add(memberChattingRoom);
        sender.addMemberChattingRoom(memberChattingRoom);
    }
}
