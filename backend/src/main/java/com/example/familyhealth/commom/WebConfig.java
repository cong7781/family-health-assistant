package com.example.familyhealth.commom;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration inter =  registry.addInterceptor(new TokenInterceptor());
        inter.addPathPatterns("/**");
        inter.excludePathPatterns("/loginweb/login","/loginweb/test");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","OPTIONS","PUT","DELETE")
                .maxAge(3600);
    }
}