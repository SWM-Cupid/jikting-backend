package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.UnAuthorizedException;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
@Entity
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String phone;
    private String name;
    private String type;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder.Default
    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private MemberProfile memberProfile = new MemberProfile();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MemberCompany> memberCompanies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MemberCertification> memberCertifications = new ArrayList<>();

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public Long getMemberProfileId() {
        return memberProfile.getId();
    }

    public void addMemberProfile() {
        memberProfile = MemberProfile.builder()
                .member(this)
                .build();
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new UnAuthorizedException(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD);
        }
    }
}
