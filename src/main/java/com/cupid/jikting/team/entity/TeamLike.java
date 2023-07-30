package com.cupid.jikting.team.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE team_like SET is_deleted = true WHERE team_like_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "team_Like_id"))
@Entity
public class TeamLike extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private AcceptStatus acceptStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_team_id")
    private Team receivedTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_team_id")
    private Team sentTeam;

    public void reject() {
        acceptStatus = AcceptStatus.REJECT;
    }
}
