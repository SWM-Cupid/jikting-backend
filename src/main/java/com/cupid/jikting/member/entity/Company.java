package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.entity.BaseEntity;
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
@SQLDelete(sql = "UPDATE company SET is_deleted = true WHERE company_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "company_id"))
@Entity
public class Company extends BaseEntity {

    private String name;
    private String email;

    @Builder.Default
    @OneToMany(mappedBy = "company")
    private final List<MemberCompany> memberCompanies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "company")
    private final List<MemberCertification> memberCertifications = new ArrayList<>();
}
