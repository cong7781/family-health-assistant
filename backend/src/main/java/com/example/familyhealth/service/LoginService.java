package com.example.familyhealth.service;

import com.example.familyhealth.commom.JWTUtil;
import com.example.familyhealth.mapper.LoginMapper;
import com.example.familyhealth.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    LoginMapper loginMapper;


    /**
     * 登录校验
     * @param member 前端传来的账号密码
     * @return 查到用户返回用户对象，无数据返回null
     */
    public Member login(Member member) {
        // 用新变量接收查询结果，不要覆盖入参
        member  = loginMapper.login(member);
        if (member!=null){
            member.setToken(JWTUtil.getToken(member));
        }

        return member;


    }
}