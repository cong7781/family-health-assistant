package com.example.familyhealth.web;

import com.example.familyhealth.model.Member;
import com.example.familyhealth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loginweb")
public class LoginWeb {

    @Autowired
    private LoginService loginService;

    /**
     * 登录验证接口
     * @RequestBody 将前端JSON自动转为Member实体
     */
    @PostMapping(path = "/login")
    public Member login(@RequestBody Member member){
        System.out.println("访问后端成功了");
        Member loginMember = loginService.login(member);
        // 简化if判断
        return loginMember;
    }

    /**
     * 测试接口，同时支持GET/POST，解决405请求方式不匹配
     */
    @RequestMapping(path = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test(){
        System.out.println("访问test成功");
        return "success";
    }
}