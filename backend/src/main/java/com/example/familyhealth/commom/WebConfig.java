package com.example.familyhealth.commom;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 层配置：注册登录拦截器 + 跨域规则。
 * <p>
 * 改造点：拦截器改为从容器里拿 JWTUtil；把 Spring Boot 内置的 /error 也排除掉，
 * 否则异常转发到 /error 时会被拦成 401，真正的错误信息就被吞了。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JWTUtil jwtUtil;

    public WebConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new TokenInterceptor(jwtUtil));
        registration.addPathPatterns("/**");
        registration.excludePathPatterns(
                "/loginweb/login",
                "/loginweb/test",
                "/error"
        );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
