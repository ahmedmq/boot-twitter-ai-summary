package com.ahmedmq.boot.twitter.ai.summary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class BootTwitterAiSummaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootTwitterAiSummaryApplication.class, args);
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests(auth -> {
                    auth.anyRequest().permitAll();
                }).build();
    }

}
