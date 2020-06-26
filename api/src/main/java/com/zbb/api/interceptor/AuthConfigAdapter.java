package com.zbb.api.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author sunflower
 * @className AuthConfigAdapter
 * @description 生效权限拦截器
 * @date 2020/6/25
 */

@Configuration
public class AuthConfigAdapter implements WebMvcConfigurer {
    @Bean
    public AuthInterceptor getLoginInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.jpeg")
                .excludePathPatterns("/**/*.icon")
                .excludePathPatterns("/**/*.gif")
                .excludePathPatterns("/**/*.svg")
                .excludePathPatterns("/**/*.woff")
                .excludePathPatterns("/**/*.font")
                .excludePathPatterns("/**/*.ttf")
                .excludePathPatterns("/**/*.woff2")
                .excludePathPatterns("/**/*.mp3")
                .excludePathPatterns("/**/*.mp4");

    }
}
