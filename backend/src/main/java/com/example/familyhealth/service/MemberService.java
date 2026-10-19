package com.example.familyhealth.service;
import com.example.familyhealth.mapper.MemberMapper;
import  com.example.familyhealth.model.Member;
import  org.springframework.beans.factory.annotation.Autowired;
import  org.springframework.stereotype.Service;
import  java.util.ArrayList;
@Service
public class MemberService {
    @Autowired
    MemberMapper memberMapper;
    public  ArrayList<Member>members(){
        return  memberMapper.members();
    }
    public void  saveMember(Member member){
        member.setPassword("123456");
        memberMapper.saveMember(member);

    }
    public void deleteById(Integer id){
        memberMapper.deleteById(id);
    }
    public Member getById(Integer id){
        return memberMapper.getById(id);
    }

    // 修改数据
    public void updateMember(Member member){
        memberMapper.updateMember(member);
    }
}
