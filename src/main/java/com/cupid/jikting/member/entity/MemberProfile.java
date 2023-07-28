package com.cupid.jikting.member.entity;

import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.error.WrongAccessException;
import com.cupid.jikting.meeting.entity.InstantMeetingMember;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member_profile SET is_deleted = true WHERE id = ?")
@AttributeOverride(name = "id", column = @Column(name = "member_profile_id"))
@Entity
public class MemberProfile extends BaseEntity {

    private String nickname;
    private LocalDate birth;
    private int height;
    private String address;
    private String description;
    private String college;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

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
    @OneToMany(mappedBy = "memberProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<InstantMeetingMember> instantMeetingMembers = new ArrayList<>();

    public Team getTeam() {
        return teamMembers.stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM))
                .getTeam();
    }

    public List<Recommend> getRecommends() {
        return getTeam().getRecommendsFrom();
    }

    public int getAge() {
        return LocalDate.now().getYear() - birth.getYear() + 1;
    }

    public String getCompany() {
        return member.getMemberCompanies()
                .stream()
                .findFirst()
                .orElseThrow(() -> new WrongAccessException(ApplicationError.FORBIDDEN_MEMBER))
                .getCompany()
                .getName();
    }

    public void addTeam(TeamMember teamMember) {
        teamMembers.add(teamMember);
    }

    public void addMemberChattingRoom(MemberChattingRoom memberChattingRoom) {
        memberChattingRooms.add(memberChattingRoom);
    }

    public String getMainImageUrl() {
        return profileImages.stream()
                .filter(ProfileImage::isMain)
                .map(ProfileImage::getUrl)
                .findAny()
                .orElseThrow(() -> new NotFoundException(ApplicationError.NOT_EXIST_REGISTERED_IMAGES));
    }
}
