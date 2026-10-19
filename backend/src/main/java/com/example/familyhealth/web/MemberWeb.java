package com.example.familyhealth.web;
import com.example.familyhealth.model.Member;
import com.example.familyhealth.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/memberweb")
@CrossOrigin("*")
public class MemberWeb {
    @Autowired
    MemberService memberService;
    @RequestMapping(path = "/members")
    public ArrayList<Member>members(){
        ArrayList<Member>members = memberService.members();
        return  members;

    }
    @RequestMapping(path =  "/saveMember")
    public  String saveMember(@RequestBody Member member){
        memberService.saveMember(member);

        return "保存成功";
    }
    @DeleteMapping("/members/{id}")
    public String deleteMember(@PathVariable Integer id){
        memberService.deleteById(id);
        return "删除成功";
    }
    // 1. 根据ID查询单条数据（编辑回显）
    @GetMapping("/member/{id}")
    public Member getOne(@PathVariable Integer id){
        return memberService.getById(id);
    }

    // 2. 修改更新接口
    @PutMapping("/updateMember")
    public String update(@RequestBody Member member){
        memberService.updateMember(member);
        return "修改成功";
    }
}
