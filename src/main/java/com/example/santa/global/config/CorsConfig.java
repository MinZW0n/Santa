package com.example.santa.global.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");

        config.addAllowedOriginPattern("http://localhost" ); // 수정된 부분
        config.addAllowedOriginPattern("http://localhost:5173" );
        config.addAllowedOriginPattern("http://localhost:5173/" );
        config.addAllowedOriginPattern("https://d1xcphd0q4kb63.cloudfront.net/" );
        config.addAllowedOriginPattern("https://d1xcphd0q4kb63.cloudfront.net" );


        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}