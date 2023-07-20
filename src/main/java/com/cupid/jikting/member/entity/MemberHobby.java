package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.entity.Hobby;
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
@SQLDelete(sql = "UPDATE member_hobby SET is_deleted = true WHERE member_hobby_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "member_hobby_id"))
@Entity
public class MemberHobby extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hobby_id")
    private Hobby hobby;
}
