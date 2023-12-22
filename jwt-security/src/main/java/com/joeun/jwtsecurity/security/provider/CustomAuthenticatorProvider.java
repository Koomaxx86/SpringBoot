package com.joeun.jwtsecurity.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.joeun.jwtsecurity.security.domain.CustomUser;
import com.joeun.jwtsecurity.security.service.CustomUserDetailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticatorProvider implements AuthenticationProvider  {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // 🍃 비밀번호 암호화 빈 등록
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();                     // username
        String password = (String) authentication.getCredentials();     // password

        CustomUser customUser = (CustomUser) customUserDetailService.loadUserByUsername(username);

        String userPw = customUser.getPassword();

        if(!passwordEncoder.matches(password, userPw)) {
            log.info("비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }


        if(passwordEncoder.matches(password, userPw)) {
            log.info("인증되었습니다!");
        }

        return new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
