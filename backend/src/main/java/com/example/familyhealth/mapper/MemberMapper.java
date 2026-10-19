package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

public interface MemberMapper {
    ArrayList<Member> members();

    void saveMember(Member member);
    void deleteById(Integer id);
    Member getById(Integer id);
    void updateMember(Member member);
}
