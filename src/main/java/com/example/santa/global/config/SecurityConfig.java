package com.example.santa.global.config;

import com.example.santa.global.security.jwt.JwtAuthenticationFilter;
import com.example.santa.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    public final String[] allowedUrls = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/",
            "/error",
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(allowedUrls).permitAll()
                                .requestMatchers("/api/oauth2/**").permitAll()
                                .requestMatchers("/api/users/signup").permitAll()
                                .requestMatchers("/api/users/sign-in").permitAll()
                                .requestMatchers("/api/users/duplicate/email").permitAll()
                                .requestMatchers("/api/users/duplicate/nickname").permitAll()
                                .requestMatchers("/api/users/send-email").permitAll()
                                .requestMatchers("/api/users/verify-email").permitAll()
                                .requestMatchers("/api/categories").permitAll()
                                .requestMatchers("/api/meetings").permitAll()
                                .requestMatchers("/api/meetings/{meetingId}").permitAll()
                                .requestMatchers("/api/meetings/tag-search").permitAll()
                                .requestMatchers("/api/meetings/category-search").permitAll()
                                .requestMatchers("/api/meetings/participants").permitAll()
                                .requestMatchers("/api/mountains").permitAll()
                                .requestMatchers("/api/mountains/{id}").permitAll()
                                .requestMatchers("/api/ranks").permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
//                .oauth2Login(oauth2 -> oauth2
//                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/**"))
//                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
//                )
                // REST api -> basic, csrf 사용 x
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                // stateless
                .sessionManagement(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
