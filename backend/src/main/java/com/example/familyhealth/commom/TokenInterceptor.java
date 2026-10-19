package com.example.familyhealth.commom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录态拦截器：校验请求头中的 token，并把解析出的用户 id 放进 request 域。
 * <p>
 * 改造点：不再依赖静态工具类，JWTUtil 由 WebConfig 注入，密钥来源统一；
 * 同时把原来的 System.out.println 换成日志。
 */
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    private final JWTUtil jwtUtil;

    public TokenInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 预检请求，否则浏览器跨域时会先收到 401
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return true;
        }

        String token = request.getHeader("token");
        if (jwtUtil.verify(token)) {
            Integer loginMemberId = jwtUtil.getTokenInfo(token).getClaim("id").asInt();
            request.setAttribute("loginMemberId", loginMemberId);
            return true;
        }

        log.debug("token 校验失败：method={} uri={} token={}", request.getMethod(), request.getRequestURI(), token);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"登录已过期，请重新登录\"}");
        return false;
    }
}
