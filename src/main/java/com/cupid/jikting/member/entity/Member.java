package com.cupid.jikting.member.entity;

import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
@Entity
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String phone;
    private String name;
    private String type;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private final List<MemberCompany> memberCompanies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private final List<MemberCertification> memberCertifications = new ArrayList<>();

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void addMemberChattingRoom(MemberChattingRoom memberChattingRoom) {
        memberChattingRooms.add(memberChattingRoom);
    }
}
