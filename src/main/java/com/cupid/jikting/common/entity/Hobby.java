package com.cupid.jikting.common.entity;

import com.cupid.jikting.member.entity.MemberHobby;
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
@SQLDelete(sql = "UPDATE hobby SET is_deleted = true WHERE hobby_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "hobby_id"))
@Entity
public class Hobby extends BaseEntity {

    private String keyword;

    @Builder.Default
    @OneToMany(mappedBy = "hobby")
    private List<MemberHobby> memberHobbies = new ArrayList<>();
}
