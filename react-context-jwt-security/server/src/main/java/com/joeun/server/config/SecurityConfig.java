package com.joeun.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.joeun.server.security.CustomUserDetailService;
import com.joeun.server.security.jwt.filter.JwtAuthenticationFilter;
import com.joeun.server.security.jwt.filter.JwtRequestFilter;
import com.joeun.server.security.jwt.provider.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig  {

	@Autowired
	private CustomUserDetailService customUserDetailService;

    @Autowired 
    private JwtTokenProvider jwtTokenProvider;

    private AuthenticationManager authenticationManager;

    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		return authenticationManager;
	}

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain...");

        // // 폼 기반 로그인 비활성화
        http.formLogin( login -> login.disable() );

        // // HTTP 기본 인증 비활성화
        http.httpBasic( basic -> basic.disable() );

        // CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
        http.csrf( csrf -> csrf.disable() );

        // 필터 설정
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            ;

        http.authorizeHttpRequests( authorizeRequests ->
                                        authorizeRequests
                                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                            .requestMatchers("/").permitAll()
                                            .requestMatchers("/login").permitAll()
                                            .requestMatchers("/user/**").hasAnyRole("USER" , "ADMIN")
                                            .requestMatchers("/admin/**").hasRole("ADMIN")
                                            .anyRequest().authenticated() )
                                            ;
										
        http.userDetailsService(customUserDetailService);

		return http.build();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


	

	

}