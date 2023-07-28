package com.cupid.jikting.team.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.recommend.entity.Recommend;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE team SET is_deleted = true WHERE team_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "team_id"))
@Entity
public class Team extends BaseEntity {

    private String name;
    private String description;
    private int memberCount;

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<TeamPersonality> teamPersonalities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receivedTeam")
    private List<TeamLike> receivedTeamLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sentTeam")
    private List<TeamLike> sentTeamLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "from")
    private List<Recommend> recommendsFrom = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "to")
    private List<Recommend> recommendsTo = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recommendedTeam")
    private List<Meeting> recommendedMeetings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "acceptingTeam")
    private List<Meeting> acceptingMeetings = new ArrayList<>();

    public void addMemberProfile(TeamMember teamMember) {
        teamMembers.add(teamMember);
    }

    public void addTeamPersonalities(List<Personality> personalities) {
        personalities.stream()
                .map(personality -> TeamPersonality.builder()
                        .team(this)
                        .personality(personality)
                        .build())
                .forEach(teamPersonalities::add);
    }

    public List<MemberProfile> getMemberProfiles() {
        return teamMembers.stream()
                .map(TeamMember::getMemberProfile)
                .collect(Collectors.toList());
    }
}
