package com.cupid.jikting.member.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemberPersonalities {

    @OneToMany(mappedBy = "memberProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<MemberPersonality> memberPersonalities = new ArrayList<>();

    public void update(List<MemberPersonality> memberPersonalities) {
        this.memberPersonalities.clear();
        this.memberPersonalities.addAll(memberPersonalities);
    }

    public List<String> getKeywords() {
        return memberPersonalities.stream()
                .map(MemberPersonality::getKeyword)
                .collect(Collectors.toList());
    }
}
