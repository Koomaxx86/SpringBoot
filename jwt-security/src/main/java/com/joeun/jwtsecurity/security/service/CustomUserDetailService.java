package com.joeun.jwtsecurity.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joeun.jwtsecurity.domain.Users;
import com.joeun.jwtsecurity.mapper.UserMapper;
import com.joeun.jwtsecurity.security.domain.CustomUser;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // MyBatis를 사용하여 데이터베이스에서 사용자 세부 정보를 가져옵니다.
       Users user = userMapper.login(username);

       if (user == null) {
           throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
       }

       // 🟢🟡🔴 CustomUser (➡User) 사용
       CustomUser customUser = new CustomUser(user);
       return customUser;

    }
    
}
