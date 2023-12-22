package com.joeun.server.security.jwt.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.joeun.server.security.jwt.constants.JwtConstants;
import com.joeun.server.security.jwt.provider.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 생성자
    public JwtRequestFilter( JwtTokenProvider jwtTokenProvider ) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     *  jwt 요청 필터
     *  - request > headers > Authorization (💍JWT)
     *  - JWT 토큰 유효성 검사
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 헤더에서 jwt 토큰을 가져옴
        String header = request.getHeader(JwtConstants.TOKEN_HEADER);
        log.info("authorization : " + header);

        // jwt 토큰이 없으면 다음 필터로 이동
        // Bearer + {jwt} 체크
        if( header == null || header.length() == 0 || !header.startsWith(JwtConstants.TOKEN_PREFIX) ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 💍 JWT
        // Bearer + {jwt} ➡ "Bearer " 제거
        String jwt = header.replace(JwtConstants.TOKEN_PREFIX, "");


        // 토큰 해석
        Authentication authenticaion = jwtTokenProvider.getAuthentication(jwt);

        // 토큰 유효성 검사
        if( jwtTokenProvider.validateToken(jwt) ) {
            log.info("유효한 JWT 토큰입니다.");

            // 로그인
            SecurityContextHolder.getContext().setAuthentication(authenticaion);
        }
        
        // 다음 필터
        filterChain.doFilter(request, response);
    }

    
    
}
