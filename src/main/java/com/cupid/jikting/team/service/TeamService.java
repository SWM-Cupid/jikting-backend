package com.cupid.jikting.team.service;

import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.dto.TeamUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    public TeamRegisterResponse register(TeamRegisterRequest teamRegisterRequest) {
        return null;
    }

    public void attend(Long teamId) {
    }

    public TeamResponse get(Long teamId) {
        return null;
    }

    public void update(Long teamId, TeamUpdateRequest teamUpdateRequest) {
    }

    public void delete(Long teamId) {
    }

    public void deleteMember(Long teamId, Long memberId) {
    }
}
