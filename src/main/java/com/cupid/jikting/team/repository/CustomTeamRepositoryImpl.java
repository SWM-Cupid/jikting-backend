package com.cupid.jikting.team.repository;

import com.cupid.jikting.team.entity.Team;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cupid.jikting.team.entity.QTeam.team;
import static com.cupid.jikting.team.entity.QTeamMember.teamMember;

@RequiredArgsConstructor
@Repository
public class CustomTeamRepositoryImpl implements CustomTeamRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Team findRecommendingTeamFor(Team recommendingTeam) {
        return queryFactory
                .selectFrom(team)
                .where(
                        team.gender.ne(recommendingTeam.getGender()),
                        team.memberCount.eq(recommendingTeam.getMemberCount())
                )
                .fetchFirst();
    }

    @Override
    public boolean isCompleted(Team checkingTeam) {
        List<Tuple> result = queryFactory
                .select(team, teamMember)
                .from(team, teamMember)
                .join(teamMember.team, team)
                .on(team.id.eq(checkingTeam.getId()))
                .where(team.isDeleted.isFalse())
                .distinct()
                .fetch();
        return result.size() == checkingTeam.getMemberCount();
    }
}
