package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 登录相关查询。
 * <p>
 * 改造点：原来直接在 SQL 里比对 {@code password = #{password}}，
 * 一旦密码改成哈希存储就永远查不到。现在只按账号查，密码校验交给
 * {@link com.example.familyhealth.commom.PasswordHasher} 在 Java 层做。
 */
@Mapper
public interface LoginMapper {

    /**
     * 按账号查询用户（含密码哈希），查不到返回 null。
     */
    Member findByAccount(@Param("account") String account);
}
