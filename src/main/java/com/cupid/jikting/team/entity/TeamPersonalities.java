package com.cupid.jikting.team.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class TeamPersonalities {

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<TeamPersonality> teamPersonalities = new ArrayList<>();

    public void update(List<TeamPersonality> teamPersonalities) {
        this.teamPersonalities.clear();
        this.teamPersonalities.addAll(teamPersonalities);
    }

    public List<String> getKeywords() {
        return teamPersonalities.stream()
                .map(TeamPersonality::getKeyword)
                .collect(Collectors.toList());
    }

    public List<TeamPersonality> getTeamPersonalities() {
        return Collections.unmodifiableList(teamPersonalities);
    }
}
