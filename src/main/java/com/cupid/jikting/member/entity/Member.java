package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
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
    private String name;
    private String type;
    private String socialId;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Builder.Default
    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private MemberProfile memberProfile = new MemberProfile();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<MemberCompany> memberCompanies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MemberCertification> memberCertifications = new ArrayList<>();


    public Long getMemberProfileId() {
        return memberProfile.getId();
    }

    public void addMemberProfile(String nickname) {
        memberProfile = MemberProfile.builder()
                .nickname(nickname)
                .gender(gender)
                .member(this)
                .build();
        memberProfile.createDefaultProfileImages();
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new UnAuthorizedException(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD);
        }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void blockCompanies() {
        validateCompanyExists();
        memberCompanies.forEach(MemberCompany::block);
    }

    public boolean isNotCertifiedCompany() {
        return role != Role.CERTIFIED;
    }

    public String getNickname() {
        return memberProfile.getNickname();
    }

    public String getCompany() {
        return memberProfile.getCompany();
    }

    public String getMainImageUrl() {
        return memberProfile.getMainImageUrl();
    }

    public boolean isGuest() {
        return role == Role.GUEST;
    }

    public void updateProfile() {
        role = Role.UNCERTIFIED;
    }

    public void certify(MemberCompany memberCompany) {
        memberCompanies.add(memberCompany);
        role = Role.CERTIFIED;
    }

    private void validateCompanyExists() {
        if (memberCompanies.isEmpty()) {
            throw new BadRequestException(ApplicationError.FORBIDDEN_MEMBER);
        }
    }
}
