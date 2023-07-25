package com.cupid.jikting.member.entity;

import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.InstantMeetingMember;
import com.cupid.jikting.team.entity.TeamMember;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member_profile SET is_deleted = true WHERE id = ?")
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
@Entity
public class MemberProfile extends BaseEntity {

    private String nickname;
    private String birth;
    private int height;
    private String address;
    private String description;
    private String school;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Enumerated(EnumType.STRING)
    private SmokeStatus smokeStatus;

    @Enumerated(EnumType.STRING)
    private DrinkStatus drinkStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<ProfileImage> profileImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<MemberPersonality> memberPersonalities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<MemberHobby> memberHobbies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<InstantMeetingMember> instantMeetingMembers = new ArrayList<>();
}
