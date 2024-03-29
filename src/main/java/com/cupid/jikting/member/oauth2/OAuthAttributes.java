package com.cupid.jikting.member.oauth2;

import com.cupid.jikting.member.entity.Gender;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.entity.Role;
import com.cupid.jikting.member.entity.SocialType;
import com.cupid.jikting.member.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.cupid.jikting.member.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.cupid.jikting.member.oauth2.userinfo.OAuth2UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oauth2UserInfo;

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .username(UUID.randomUUID().toString())
                .gender(Gender.valueOf(oauth2UserInfo.getGender()))
                .role(Role.GUEST)
                .build();
    }

    public String getOauth2UserInfoId() {
        return oauth2UserInfo.getId();
    }
}
