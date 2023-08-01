package com.cupid.jikting.member.entity;

import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
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
@SQLDelete(sql = "UPDATE member_profile SET is_deleted = true WHERE member_profile_id = ?")
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
    @Embedded
    private ProfileImages profileImages = new ProfileImages();

    @Builder.Default
    @Embedded
    private MemberPersonalities memberPersonalities = new MemberPersonalities();

    @Builder.Default
    @Embedded
    private MemberHobbies memberHobbies = new MemberHobbies();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberProfile")
    private List<InstantMeetingMember> instantMeetingMembers = new ArrayList<>();

    public boolean isInTeam() {
        return !teamMembers.isEmpty();
    }

    public Team getTeam() {
        return teamMembers.stream()
                .findAny()
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

    public List<String> getPersonalityKeywords() {
        return memberPersonalities.getKeywords();
    }

    public List<String> getHobbyKeywords() {
        return memberHobbies.getKeywords();
    }

    public List<ProfileImage> getProfileImages() {
        return profileImages.getProfileImages();
    }

    public String getMainImageUrl() {
        return profileImages.getMainImageUrl();
    }

    public void updateProfile(LocalDate birth, int height, Mbti mbti, String address, Gender gender, String college,
                              SmokeStatus smokeStatus, DrinkStatus drinkStatus, String description,
                              List<MemberPersonality> memberPersonalities, List<MemberHobby> memberHobbies, List<ProfileImage> profileImages) {
        this.birth = birth;
        this.height = height;
        this.mbti = mbti;
        this.address = address;
        this.gender = gender;
        this.college = college;
        this.smokeStatus = smokeStatus;
        this.drinkStatus = drinkStatus;
        this.description = description;
        this.memberPersonalities.update(memberPersonalities);
        this.memberHobbies.update(memberHobbies);
        this.profileImages.update(profileImages);
    }

    public void addMemberChattingRoom(MemberChattingRoom memberChattingRoom) {
        memberChattingRooms.add(memberChattingRoom);
    }

    public boolean isSameAs(Long memberProfileId) {
        return id.equals(memberProfileId);
    }

    public Long getTeamId() {
        return getTeam().getId();
    }
}
