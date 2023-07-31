package com.cupid.jikting.member.oauth2;

import com.cupid.jikting.member.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String username;
    private final Role role;

    private CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                            String nameAttributeKey, String username, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.username = username;
        this.role = role;
    }

    public static CustomOAuth2User of(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                                      String nameAttributeKey, String username, Role role) {
        return new CustomOAuth2User(authorities, attributes, nameAttributeKey, username, role);
    }
}
