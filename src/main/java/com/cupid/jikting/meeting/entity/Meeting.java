package com.cupid.jikting.meeting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.team.entity.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE meeting SET is_deleted = true WHERE meeting_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "meeting_id"))
@Entity
public class Meeting extends BaseEntity {

    private LocalDateTime schedule;
    private String place;
    private LocalDateTime confirmAt;

    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_team_id")
    private Team recommendedTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acceptiong_team_id")
    private Team acceptingTeam;
}
