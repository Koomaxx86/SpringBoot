package com.joeun.jwtsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.joeun.jwtsecurity.domain.AuthenticationRequest;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class LoginController {

    /**
     * 👩‍💼➡🔐 JWT 을 생성하는 Login 요청
     * (filter) [POST] - /login
     * body : 
            {
                "username" : "joeun",
                "password" : "123456"
            }
     * @param authReq
     * @return
     */
    @PostMapping("login")
    public void login(@RequestBody AuthenticationRequest authReq) {
        // 사용자로부터 전달받은 인증 정보
        String username = authReq.getUsername();
        String password = authReq.getPassword();

        log.info("username : " + username);
        log.info("password : " + password);
    }


    /**
     * 💍
     * 
     * @param header
     * @return
     */
    @GetMapping("/user/info")
    public void userInfo(@RequestHeader(name="Authorization") String header) {

        log.info("===== header =====");
        log.info("Authorization : " + header);

    }
    
}
