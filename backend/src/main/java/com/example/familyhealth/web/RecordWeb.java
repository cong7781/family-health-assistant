package com.example.familyhealth.web;

import com.example.familyhealth.model.Record;
import com.example.familyhealth.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/health")
public class RecordWeb {
    @Resource
    private RecordService recordService;

    // 前端GET /health/all 查询全部记录
    @GetMapping("/all")
    public List<Record> listAll(HttpServletRequest request){
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        return recordService.recordList(loginMemberId);
    }

    // 前端POST /health/save 新增记录
    @PostMapping("/save")
    public String save(@RequestBody Record record, HttpServletRequest request){
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        record.setMemberid(loginMemberId);
        recordService.recordSaveRecord(record);
        return "success";
    }

    // 前端GET /health/{id} 单条查询
    @GetMapping("/{id}")
    public Record getById(@PathVariable Integer id, HttpServletRequest request){
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        return recordService.recordGetById(id, loginMemberId);
    }

    // 前端POST /health/update 修改记录
    @PostMapping("/update")
    public String update(@RequestBody Record record, HttpServletRequest request){
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        record.setMemberid(loginMemberId);
        recordService.recordUpdateRecord(record, loginMemberId);
        return "success";
    }

    // 前端DELETE /health/{id} 删除记录
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id, HttpServletRequest request){
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        recordService.recordDeleteById(id, loginMemberId);
        return "success";
    }
}