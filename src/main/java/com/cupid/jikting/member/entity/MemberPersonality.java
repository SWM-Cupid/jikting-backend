package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.entity.Personality;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member_personality SET is_deleted = true WHERE member_personality_id = ?")
@Where(clause = "is_deleted = false")
@AttributeOverride(name = "id", column = @Column(name = "member_personality_id"))
@Entity
public class MemberPersonality extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personality_id")
    private Personality personality;

    public String getKeyword() {
        return personality.getKeyword();
    }
}
