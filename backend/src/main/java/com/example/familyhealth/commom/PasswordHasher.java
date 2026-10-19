package com.example.familyhealth.commom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码哈希工具（PBKDF2-HMAC-SHA256）。
 * <p>
 * 为什么不用 BCrypt？BCrypt 需要额外引入 {@code spring-security-crypto}，
 * 而 PBKDF2 是 JDK 自带的（{@code javax.crypto}），零依赖即可达到同等强度的
 * 「加盐 + 慢哈希」效果，每用户独立随机盐，天然防彩虹表。
 * 如果后续接入 Spring Security，只要把本类换成 {@code BCryptPasswordEncoder} 即可，
 * 调用方（LoginService / MemberService）不需要改。
 * <p>
 * 存储格式：{@code pbkdf2$迭代次数$盐(Base64)$摘要(Base64)}
 * 自带算法与参数，将来提高迭代次数也能平滑兼容老数据。
 */
@Component
public class PasswordHasher {

    private static final String PREFIX = "pbkdf2";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 120_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private final SecureRandom random = new SecureRandom();

    /**
     * 是否允许「明文存量密码」继续登录（仅用于把老库平滑迁移到哈希存储）。
     * 迁移完成后请设置环境变量 ALLOW_LEGACY_PLAINTEXT=false 关掉。
     */
    private final boolean allowLegacyPlaintext;

    public PasswordHasher(@Value("${auth.allow-legacy-plaintext:false}") boolean allowLegacyPlaintext) {
        this.allowLegacyPlaintext = allowLegacyPlaintext;
    }

    /**
     * 生成可直接入库的哈希串。
     */
    public String hash(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("密码不能为 null");
        }
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        byte[] digest = derive(rawPassword.toCharArray(), salt, ITERATIONS);
        Base64.Encoder encoder = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + encoder.encodeToString(salt) + "$" + encoder.encodeToString(digest);
    }

    /**
     * 校验明文密码与库中存储值是否匹配。
     *
     * @param rawPassword     用户输入的明文
     * @param storedPassword  数据库里的值（新数据是哈希串，老数据可能是明文）
     */
    public boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null || storedPassword.isEmpty()) {
            return false;
        }
        if (!isHashed(storedPassword)) {
            // 存量明文数据：仅在迁移开关打开时允许比对，登录成功后会自动升级为哈希
            return allowLegacyPlaintext && constantTimeEquals(rawPassword, storedPassword);
        }
        String[] parts = storedPassword.split("\\$");
        if (parts.length != 4) {
            return false;
        }
        int iterations;
        try {
            iterations = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        byte[] salt;
        byte[] expected;
        try {
            salt = Base64.getDecoder().decode(parts[2]);
            expected = Base64.getDecoder().decode(parts[3]);
        } catch (IllegalArgumentException e) {
            return false;
        }
        byte[] actual = derive(rawPassword.toCharArray(), salt, iterations);
        return MessageDigest.isEqual(expected, actual);
    }

    /** 判断库里存的是不是已经哈希过的值 */
    public boolean isHashed(String storedPassword) {
        return storedPassword != null && storedPassword.startsWith(PREFIX + "$");
    }

    private byte[] derive(char[] rawPassword, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(rawPassword, salt, iterations, KEY_BITS);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("密码哈希计算失败", e);
        } finally {
            spec.clearPassword();
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.UTF_8),
                b.getBytes(StandardCharsets.UTF_8));
    }
}
