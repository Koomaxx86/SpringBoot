package com.joeun.server.security.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joeun.server.dto.CustomUser;
import com.joeun.server.dto.Users;
import com.joeun.server.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        log.info("login - loadUserByUsername : " + username);

        Users user = userMapper.login(username);
        
        if( user == null ) {
            log.info("사용자 없음... (일치하는 아이디가 없음)");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
        }

        log.info("user : ");
        log.info(user.toString());

        // Users -> CustomUser
        CustomUser customUser = new CustomUser(user);

        log.info("customUser : ");
        log.info(customUser.toString());

        return customUser;
    }

    
}
