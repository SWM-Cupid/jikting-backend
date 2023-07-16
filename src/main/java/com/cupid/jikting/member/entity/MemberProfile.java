package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.InstantMeetingMember;
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
    private Gender gender;
    private int height;
    private MBTI mbti;
    private String address;
    private SmokeStatus smokeStatus;
    private DrinkStatus drinkStatus;
    private String description;
    private String school;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private final List<ProfileImage> profileImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private final List<MemberPersonality> memberPersonalities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private final List<MemberHobby> memberHobbies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private final List<InstantMeetingMember> instantMeetingMembers = new ArrayList<>();
}
