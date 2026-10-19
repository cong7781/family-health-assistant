package com.example.familyhealth.commom;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.familyhealth.model.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类。
 * <p>
 * 改造点：原来签名密钥是写死在代码里的字符串常量，任何人拿到仓库都能伪造 Token。
 * 现在改为由 {@code jwt.secret} 注入（生产环境通过环境变量 JWT_SECRET 提供），
 * 并且启动时校验长度，避免部署时漏配、却悄悄用一个弱密钥跑起来。
 */
@Component
public class JWTUtil {

    /** Token 有效期：1 小时 */
    private static final long EXPIRE_MILLIS = 3600L * 1000L;

    /** HMAC-SHA256 建议密钥至少 32 字节，这里只做长度兜底校验 */
    private static final int MIN_SECRET_LENGTH = 32;

    private final String secret;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.trim().length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException(
                    "jwt.secret 未配置或长度不足 " + MIN_SECRET_LENGTH
                            + " 位，请通过环境变量 JWT_SECRET 设置一个足够长的随机串");
        }
        this.secret = secret;
    }

    /**
     * 根据用户 id、账号生成 Token。
     */
    public String getToken(Member member) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_MILLIS);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header)
                .withClaim("id", member.getId())
                .withClaim("account", member.getAccount())
                .withExpiresAt(expireDate)
                .sign(algorithm());
    }

    /**
     * 校验 Token 是否合法（签名正确且未过期）。
     */
    public boolean verify(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            JWT.require(algorithm()).build().verify(token);
            return true;
        } catch (Exception e) {
            // 签名错误 / 已过期 / 格式非法，统一按无效处理
            return false;
        }
    }

    /**
     * 解析 Token 的 payload，调用前请先用 {@link #verify(String)} 校验。
     */
    public DecodedJWT getTokenInfo(String token) {
        JWTVerifier verifier = JWT.require(algorithm()).build();
        return verifier.verify(token);
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }
}
