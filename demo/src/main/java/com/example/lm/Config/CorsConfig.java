package com.example.lm.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 设置允许的源（origin），包括 localhost
        config.setAllowedOrigins(Arrays.asList(
                "http://127.0.0.1:8080",
                "http://120.26.136.194:8080",
                "http://localhost:8080",
                "http://localhost:8088",
                "http://8.130.130.240:8080",
                "http://8.130.130.240:8088",
                "http://localhost:63342"

        ));

        // 设置允许的方法（GET, POST, etc.）
        config.setAllowedMethods(Arrays.asList("*")); // 允许所有HTTP方法

        // 设置允许的头部
        config.setAllowedHeaders(Arrays.asList("*")); // 允许所有头部

        // 允许发送凭证信息（如 cookies）
        config.setAllowCredentials(true);

        // 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 返回一个新的 CorsFilter
        return new CorsFilter(source);
    }
}
