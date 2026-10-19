package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {

    /**
     * 分页查询家庭成员。
     *
     * @param offset 偏移量，(page - 1) * size
     * @param size   每页条数
     */
    List<Member> members(@Param("offset") int offset, @Param("size") int size);

    /** 总条数，分页时和 {@link #members} 成对使用 */
    long countMembers();

    void saveMember(Member member);

    void deleteById(Integer id);

    Member getById(Integer id);

    void updateMember(Member member);

    /**
     * 只更新密码字段（登录时把明文存量密码升级为哈希用）。
     */
    int updatePassword(@Param("id") Integer id, @Param("password") String password);
}
