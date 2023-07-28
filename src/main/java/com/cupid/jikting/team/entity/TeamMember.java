package com.cupid.jikting.team.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE team_member SET is_deleted = true WHERE team_member_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "team_member_id"))
@Entity
public class TeamMember extends BaseEntity {

    private boolean isLeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    public static TeamMember of(boolean isLeader, Team team, MemberProfile memberProfile) {
        TeamMember teamMember = new TeamMember(isLeader, team, memberProfile);
        team.addMemberProfile(teamMember);
        memberProfile.addTeam(teamMember);
        return teamMember;
    }

    public List<TeamLike> getReceivedTeamLikes() {
        return team.getReceivedTeamLikes();
    }
}
