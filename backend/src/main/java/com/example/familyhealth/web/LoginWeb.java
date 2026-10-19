package com.example.familyhealth.web;

import com.example.familyhealth.model.Member;
import com.example.familyhealth.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loginweb")
public class LoginWeb {

    private final LoginService loginService;

    public LoginWeb(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录接口。
     * <p>
     * 改造点：账号或密码错误时返回 HTTP 401 + JSON，而不是 200 加一个 null。
     * 前端 axios 会走到 catch 分支，可以直接区分「密码错」和「网络断了」。
     */
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody Member member) {
        Member loginMember = loginService.login(member);
        if (loginMember == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 401);
            error.put("message", "账号或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        return ResponseEntity.ok(loginMember);
    }

    /**
     * 探活接口，同时支持 GET/POST，方便部署后快速确认服务起来了。
     */
    @RequestMapping(path = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() {
        return "success";
    }
}
