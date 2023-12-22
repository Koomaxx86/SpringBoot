package com.joeun.jwtsecurity.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.joeun.jwtsecurity.domain.Users;

public class CustomUser extends User {

    private Users user;

    // 생성자
    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    // 생성자
    public CustomUser(Users user) {
        super( user.getUserId()
        , user.getUserPw()
        , user.getAuthList()
        .stream()
        .map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()))
        ;
        this.user = user;
    }
    
    // 생성자
    public CustomUser(Users user, Collection<? extends GrantedAuthority> authorities) {
        super( user.getUserId(), user.getUserPw(), authorities);

        this.user = user;
    }

    public int getNo() {
        return user.getNo();
    }

    public String getUserId() {
        return user.getUserId();
    }

}
