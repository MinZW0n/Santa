package com.example.santa.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                        request -> request
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/", HttpMethod.POST.name())).permitAll()
//                                .anyRequest().authenticated())
                                .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}
