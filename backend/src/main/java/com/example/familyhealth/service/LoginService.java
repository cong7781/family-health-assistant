package com.example.familyhealth.service;

import com.example.familyhealth.commom.JWTUtil;
import com.example.familyhealth.commom.PasswordHasher;
import com.example.familyhealth.mapper.LoginMapper;
import com.example.familyhealth.mapper.MemberMapper;
import com.example.familyhealth.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 登录校验。
 * <p>
 * 改造点（三件事）：
 * 1. 密码不再由 SQL 明文比对，改为「按账号查出来 → 哈希校验」；
 * 2. 校验通过后把 password 置空，绝不把密码哈希回传给前端；
 * 3. 老库里的明文密码可以自动升级成哈希（迁移用，可用开关关掉）。
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final LoginMapper loginMapper;
    private final MemberMapper memberMapper;
    private final PasswordHasher passwordHasher;
    private final JWTUtil jwtUtil;

    public LoginService(LoginMapper loginMapper,
                        MemberMapper memberMapper,
                        PasswordHasher passwordHasher,
                        JWTUtil jwtUtil) {
        this.loginMapper = loginMapper;
        this.memberMapper = memberMapper;
        this.passwordHasher = passwordHasher;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 登录校验。
     *
     * @param input 前端传来的账号、密码
     * @return 校验通过返回用户信息（含 token，密码已置空）；失败返回 null
     */
    public Member login(Member input) {
        if (input == null || isBlank(input.getAccount()) || isBlank(input.getPassword())) {
            return null;
        }

        Member stored = loginMapper.findByAccount(input.getAccount().trim());
        if (stored == null) {
            log.debug("登录失败：账号不存在 account={}", input.getAccount());
            return null;
        }

        if (!passwordHasher.matches(input.getPassword(), stored.getPassword())) {
            log.debug("登录失败：密码不匹配 account={}", input.getAccount());
            return null;
        }

        // 存量明文密码：这次登录成功后立即升级为哈希，下次就走正常流程了
        if (!passwordHasher.isHashed(stored.getPassword())) {
            String upgraded = passwordHasher.hash(input.getPassword());
            memberMapper.updatePassword(stored.getId(), upgraded);
            log.info("检测到明文密码账号 id={}，已自动升级为哈希存储", stored.getId());
        }

        // 无论成功与否，密码都不应该出现在响应里
        stored.setPassword(null);
        stored.setToken(jwtUtil.getToken(stored));
        return stored;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
