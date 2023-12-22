package com.joeun.server.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("com.joeun.jwt")       // com.joeun.jwt 경로 하위 속성들을 지정
public class JwtProps {
    
    // com.joeun.jwt.secretKey로 지정된 프로퍼티 값을 주입받는 필드
    // ✅ com.joeun.jwt.secret-key ➡ secretKey : {인코딩된 시크릿 키}
    private String secretKey;
    

}