package com.joeun.jwtsecurity.security.jwt.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.joeun.jwtsecurity.security.domain.CustomUser;
import com.joeun.jwtsecurity.security.jwt.constants.SecurityConstants;
import com.joeun.jwtsecurity.security.jwt.provider.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;


    // 생성자
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;

        // 필터 URL 경로 설정
        // /login
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }


    // TODO: 어떤 메소드?
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("username : " + username);
        log.info("password : " + password);

        Authentication authToken = new UsernamePasswordAuthenticationToken(username, password);

        log.info("authToken : Authentication  - " + authToken);

        log.info("isAuthenticated : " + authToken.isAuthenticated());
        
        log.info("authenticationManager : " + authenticationManager);


        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException, ServletException {


        CustomUser user = ((CustomUser) authentication.getPrincipal());
        int userNo = user.getNo();
        String userId = user.getUserId();

        List<String> roles = user.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

        // 🔐 JWT
        String token = jwtTokenProvider.createToken(userNo, userId, roles);

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        
    }



    

    



    
    
}
