package com.cupid.jikting.recommend.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamPersonality;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE recommend SET is_deleted = true WHERE recommend_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "recommend_id"))
@Entity
public class Recommend extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_team_id")
    private Team from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_team_id")
    private Team to;

    public String getFromTeamName() {
        return from.getName();
    }

    public String getFromTeamDescription() {
        return from.getDescription();
    }

    public List<TeamPersonality> getFromTeamPersonalities() {
        return from.getTeamPersonalities();
    }
}
