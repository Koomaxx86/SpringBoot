package com.joeun.jwtsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.joeun.jwtsecurity.security.jwt.filter.JwtAuthenticationFilter;
import com.joeun.jwtsecurity.security.jwt.filter.JwtRequestFilter;
import com.joeun.jwtsecurity.security.jwt.provider.JwtTokenProvider;
import com.joeun.jwtsecurity.security.service.CustomAuthenticationManager;
import com.joeun.jwtsecurity.security.service.CustomUserDetailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)       //어노테이션에 prePostEnabled = true를 추가하면 AuthenticationManager를 자동으로 구성합니다.
public class SecurityConfig {

    @Autowired 
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain...");

        // // 폼 기반 로그인 비활성화
        http.formLogin( login -> login.disable() );

        // HTTP 기본 인증 비활성화
        http.httpBasic( basic -> basic.disable() );

        // CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
        http.csrf( csrf -> csrf.disable() );

        http.authenticationManager( new CustomAuthenticationManager() );

        // 필터 설정
        http.addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            ;

        // 세션 비활성화
        http.sessionManagement( management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인가 설정
        http.authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER" , "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                ;

        http.userDetailsService( customUserDetailService );


       
        

        return http.build();
    }



    // 🍃 AuthenticationManager 빈 등록
    // Spring Security 5.7.0 버전부터는 AuthenticationManagerBuilder를 직접 생성하여
    // AuthenticationManager를 생성해야 한다.
    // @Autowired
    // public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.authenticationProvider(customAuthenticatorProvider);
    // }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


	// @Bean
	// public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	// 	return authenticationConfiguration.getAuthenticationManager();
	// }


    
}
