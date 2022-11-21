package com.polytech.psychological_help.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256("secret".getBytes());
    }
}
