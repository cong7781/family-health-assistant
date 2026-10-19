package com.example.familyhealth.commom;

import com.example.familyhealth.mapper.MemberMapper;
import com.example.familyhealth.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 首次启动初始化演示账号。
 * <p>
 * 好处有两个：
 * 1. 仓库里不需要出现任何明文密码或写死的哈希，clone 下来执行完 schema.sql 就能登录；
 * 2. 顺便验证了「新增成员 = 密码哈希入库」这条链路是通的。
 * <p>
 * 表里已经有数据时什么都不做，所以重复启动是安全的。
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String DEMO_ACCOUNT = "demo";
    private static final String DEMO_PASSWORD = "123456";

    private final MemberMapper memberMapper;
    private final PasswordHasher passwordHasher;

    @Value("${app.demo-account.enabled:true}")
    private boolean enabled;

    public DataInitializer(MemberMapper memberMapper, PasswordHasher passwordHasher) {
        this.memberMapper = memberMapper;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }
        if (memberMapper.countMembers() > 0) {
            return;
        }

        Member demo = new Member();
        demo.setAccount(DEMO_ACCOUNT);
        demo.setPassword(passwordHasher.hash(DEMO_PASSWORD));
        demo.setName("演示用户");
        demo.setGender("女");
        demo.setPhone("13800000000");
        demo.setBloodType("A");
        demo.setIllnessHistory("无");
        demo.setAllergy("无");
        memberMapper.saveMember(demo);

        log.info("member 表为空，已自动创建演示账号：{} / {}（请登录后及时修改密码）", DEMO_ACCOUNT, DEMO_PASSWORD);
    }
}
