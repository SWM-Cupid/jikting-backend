package com.cupid.jikting.member.entity;

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
@SQLDelete(sql = "UPDATE profile_image SET is_deleted = true WHERE profile_image_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "profile_image_id"))
@Entity
public class ProfileImage extends BaseEntity {

    private String url;

    @Enumerated(EnumType.STRING)
    private Sequence sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    public Long getMemberProfileId() {
        return memberProfile.getId();
    }

    public boolean isMain() {
        return sequence.equals(Sequence.MAIN);
    }
}
