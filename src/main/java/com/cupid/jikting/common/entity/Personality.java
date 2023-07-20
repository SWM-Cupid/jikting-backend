package com.cupid.jikting.common.entity;

import com.cupid.jikting.member.entity.MemberPersonality;
import com.cupid.jikting.team.entity.TeamPersonality;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE personality SET is_deleted = true WHERE personality_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "personality_id"))
@Entity
public class Personality extends BaseEntity {

    private String keyword;

    @Builder.Default
    @OneToMany(mappedBy = "personality")
    private final List<MemberPersonality> memberPersonalities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "personality")
    private final List<TeamPersonality> teamPersonalities = new ArrayList<>();
}
