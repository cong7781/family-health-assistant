package com.example.familyhealth.service;

import com.example.familyhealth.commom.PageResult;
import com.example.familyhealth.commom.PasswordHasher;
import com.example.familyhealth.mapper.MemberMapper;
import com.example.familyhealth.model.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    /** 新增成员时的初始密码，落库前会被哈希 */
    private static final String DEFAULT_PASSWORD = "123456";

    private final MemberMapper memberMapper;
    private final PasswordHasher passwordHasher;

    public MemberService(MemberMapper memberMapper, PasswordHasher passwordHasher) {
        this.memberMapper = memberMapper;
        this.passwordHasher = passwordHasher;
    }

    /**
     * 分页查询成员列表（密码字段一律不返回）。
     */
    public PageResult<Member> members(Integer pageParam, Integer sizeParam) {
        int page = PageResult.safePage(pageParam);
        int size = PageResult.safeSize(sizeParam);

        long total = memberMapper.countMembers();
        List<Member> records = memberMapper.members((page - 1) * size, size);
        records.forEach(member -> member.setPassword(null));

        return new PageResult<>(total, page, size, records);
    }

    /**
     * 新增成员：密码统一用默认值并哈希后入库。
     */
    public void saveMember(Member member) {
        if (member == null || isBlank(member.getAccount())) {
            throw new IllegalArgumentException("账号不能为空");
        }
        if (isBlank(member.getName())) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        member.setAccount(member.getAccount().trim());
        member.setPassword(passwordHasher.hash(DEFAULT_PASSWORD));
        memberMapper.saveMember(member);
    }

    public void deleteById(Integer id) {
        memberMapper.deleteById(id);
    }

    /**
     * 编辑回显用，同样不返回密码。
     */
    public Member getById(Integer id) {
        Member member = memberMapper.getById(id);
        if (member != null) {
            member.setPassword(null);
        }
        return member;
    }

    /**
     * 更新资料。SQL 里本来就不更新 password 字段，这里再保险一次。
     */
    public void updateMember(Member member) {
        if (member == null || member.getId() == null) {
            throw new IllegalArgumentException("成员 id 不能为空");
        }
        member.setPassword(null);
        memberMapper.updateMember(member);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
