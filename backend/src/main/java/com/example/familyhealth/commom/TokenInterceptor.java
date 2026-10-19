package com.example.familyhealth.commom;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS预检请求，解决跨域CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return true;
        }

        String token = request.getHeader("token");
        System.out.println("token 认证拦截器:" + token);
        // 校验token合法性
        if (JWTUtil.verify(token)) {
            // 新增：从token中解析登录用户id，存入request域，给Controller取用
            Integer loginMemberId = JWT.decode(token).getClaim("id").asInt();
            request.setAttribute("loginMemberId", loginMemberId);
            return true;
        } else {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(401));
            return false;
        }
    }
}