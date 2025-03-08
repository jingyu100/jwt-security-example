package com.jwt.domain.member;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserPrincipal extends User {

    private static final String PASSWORD_ERASED_VALUE = "[PASSWORD_ERASED]";
    private final String email;

    public UserPrincipal(String email, String username,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, PASSWORD_ERASED_VALUE, authorities);
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "email='" + email + '\'' +
                '}';
    }
}
