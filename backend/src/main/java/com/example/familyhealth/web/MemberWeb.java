package com.example.familyhealth.web;

import com.example.familyhealth.commom.PageResult;
import com.example.familyhealth.model.Member;
import com.example.familyhealth.service.MemberService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memberweb")
public class MemberWeb {

    private final MemberService memberService;

    public MemberWeb(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 成员列表（分页）。
     * GET /memberweb/members?page=1&size=10
     */
    @GetMapping("/members")
    public PageResult<Member> members(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return memberService.members(page, size);
    }

    @PostMapping("/saveMember")
    public Map<String, Object> saveMember(@RequestBody Member member) {
        memberService.saveMember(member);
        return ok("保存成功");
    }

    @DeleteMapping("/members/{id}")
    public Map<String, Object> deleteMember(@PathVariable Integer id) {
        memberService.deleteById(id);
        return ok("删除成功");
    }

    /** 编辑回显 */
    @GetMapping("/member/{id}")
    public Member getOne(@PathVariable Integer id) {
        return memberService.getById(id);
    }

    @PutMapping("/updateMember")
    public Map<String, Object> update(@RequestBody Member member) {
        memberService.updateMember(member);
        return ok("修改成功");
    }

    private Map<String, Object> ok(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", message);
        return result;
    }
}
