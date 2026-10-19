package com.example.familyhealth.commom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PasswordHasher 单元测试。
 * <p>
 * 纯 Java 逻辑，不启动 Spring 容器、不连数据库，
 * 所以 `mvn test` 在没有 MySQL 的机器上也能通过（也就不会被 CI 卡住）。
 */
class PasswordHasherTest {

    private final PasswordHasher hasher = new PasswordHasher(false);

    @Test
    @DisplayName("哈希结果不是明文，且带算法前缀")
    void hashIsNotPlaintext() {
        String hash = hasher.hash("123456");
        assertNotEquals("123456", hash);
        assertTrue(hash.startsWith("pbkdf2$"), "应以 pbkdf2$ 开头：" + hash);
        assertTrue(hasher.isHashed(hash));
    }

    @Test
    @DisplayName("正确密码可以通过校验")
    void matchesCorrectPassword() {
        String hash = hasher.hash("S3cret!密码");
        assertTrue(hasher.matches("S3cret!密码", hash));
    }

    @Test
    @DisplayName("错误密码无法通过校验")
    void rejectsWrongPassword() {
        String hash = hasher.hash("123456");
        assertFalse(hasher.matches("123457", hash));
        assertFalse(hasher.matches("", hash));
        assertFalse(hasher.matches(null, hash));
    }

    @Test
    @DisplayName("同一个密码两次哈希结果不同（每次都用新盐）")
    void hashIsSalted() {
        assertNotEquals(hasher.hash("123456"), hasher.hash("123456"));
    }

    @Test
    @DisplayName("默认拒绝明文存量密码（迁移开关关闭时）")
    void rejectsLegacyPlaintextByDefault() {
        assertFalse(hasher.matches("123456", "123456"));
    }

    @Test
    @DisplayName("迁移开关打开时，明文存量密码仍可登录")
    void acceptsLegacyPlaintextWhenEnabled() {
        PasswordHasher legacyHasher = new PasswordHasher(true);
        assertTrue(legacyHasher.matches("123456", "123456"));
        assertFalse(legacyHasher.matches("123456", "654321"));
    }

    @Test
    @DisplayName("库里的值是 null 或空串时不会抛异常")
    void handlesEmptyStoredValue() {
        assertFalse(hasher.matches("123456", null));
        assertFalse(hasher.matches("123456", ""));
    }
}
