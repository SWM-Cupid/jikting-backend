package com.cupid.jikting.team.repository;

import com.cupid.jikting.team.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.cupid.jikting.team.entity.QTeam.team;

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
}
