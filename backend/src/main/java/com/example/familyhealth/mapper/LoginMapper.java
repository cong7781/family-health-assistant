package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    // 和service调用的方法名完全一致
    Member login(Member member);
}