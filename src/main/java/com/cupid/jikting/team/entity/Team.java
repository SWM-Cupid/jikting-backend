package com.cupid.jikting.team.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.meeting.entity.Meeting;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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
    private final List<TeamPersonality> teamPersonalities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private final List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receivedTeam")
    private final List<TeamLike> receivedTeamLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sentTeam")
    private final List<TeamLike> sentTeamLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recommendedTeam")
    private final List<Meeting> recommendedMeetings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "acceptingTeam")
    private final List<Meeting> acceptingMeetings = new ArrayList<>();
}
