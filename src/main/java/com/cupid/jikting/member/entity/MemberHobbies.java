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
public class MemberHobbies {

    @OneToMany(mappedBy = "memberProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MemberHobby> memberHobbies = new ArrayList<>();

    public void update(List<MemberHobby> memberHobbies) {
        this.memberHobbies.clear();
        this.memberHobbies.addAll(memberHobbies);
    }

    public List<String> getKeywords() {
        return memberHobbies.stream()
                .map(MemberHobby::getKeyword)
                .collect(Collectors.toList());
    }
}
