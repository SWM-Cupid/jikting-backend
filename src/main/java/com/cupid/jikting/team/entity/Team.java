package com.cupid.jikting.team.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.Gender;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.recommend.entity.Recommend;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE team SET is_deleted = true WHERE team_id = ?")
@Where(clause = "is_deleted = false")
@AttributeOverride(name = "id", column = @Column(name = "team_id"))
@Entity
public class Team extends BaseEntity {

    private String name;
    private String description;
    private int memberCount;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @Embedded
    private TeamPersonalities teamPersonalities = new TeamPersonalities();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receivedTeam")
    private List<TeamLike> receivedTeamLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sentTeam")
    private List<TeamLike> sentTeamLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "to")
    private List<Recommend> recommends = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recommendedTeam")
    private List<Meeting> recommendedMeetings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "acceptingTeam")
    private List<Meeting> acceptingMeetings = new ArrayList<>();

    public void addMemberProfile(TeamMember teamMember) {
        teamMembers.add(teamMember);
    }

    public void addTeamPersonalities(List<TeamPersonality> teamPersonalities) {
        this.teamPersonalities.update(teamPersonalities);
    }

    public List<MemberProfile> getMemberProfiles() {
        return teamMembers.stream()
                .map(TeamMember::getMemberProfile)
                .collect(Collectors.toList());
    }

    public List<TeamPersonality> getTeamPersonalities() {
        return teamPersonalities.getTeamPersonalities();
    }

    public List<String> getPersonalities() {
        return teamPersonalities.getKeywords();
    }

    public List<String> getMainImageUrls() {
        return getMemberProfiles().stream()
                .map(MemberProfile::getMainImageUrl)
                .collect(Collectors.toList());
    }

    public void update(String description, List<TeamPersonality> teamPersonalities) {
        this.description = description;
        this.teamPersonalities.update(teamPersonalities);
    }
}
