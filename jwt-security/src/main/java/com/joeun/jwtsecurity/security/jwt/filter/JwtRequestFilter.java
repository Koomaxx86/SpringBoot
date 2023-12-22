package com.joeun.jwtsecurity.security.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.joeun.jwtsecurity.security.jwt.constants.SecurityConstants;
import com.joeun.jwtsecurity.security.jwt.provider.JwtTokenProvider;

public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Autowired 
    private JwtTokenProvider jwtTokenProvider;

    // JwtTokenProvider 주입 받는 생성자
    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP 헤더에서 토큰을 가져옴
        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);

        //✅ Bearer + {jwt} 체크
        // 헤더가 없거나 형식이 올바르지 않으면 다음 필터로 진행
        if (header == null || header.length() == 0 || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 토큰을 사용하여 Authentication 객체 생성
        Authentication authentication = jwtTokenProvider.getAuthentication(header);

        // SecurityContextHolder에 Authentication 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
