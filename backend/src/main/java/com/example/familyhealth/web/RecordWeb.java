package com.example.familyhealth.web;

import com.example.familyhealth.commom.PageResult;
import com.example.familyhealth.model.Record;
import com.example.familyhealth.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class RecordWeb {

    private final RecordService recordService;

    public RecordWeb(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * 当前登录用户的健康记录（分页）。
     * GET /health/all?page=1&size=10
     */
    @GetMapping("/all")
    public PageResult<Record> listAll(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        return recordService.recordList(loginMemberId, page, size);
    }

    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody Record record, HttpServletRequest request) {
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        record.setMemberid(loginMemberId);
        recordService.recordSaveRecord(record);
        return ok("success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, HttpServletRequest request) {
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        Record record = recordService.recordGetById(id, loginMemberId);
        if (record == null) {
            return notFound(id);
        }
        return ResponseEntity.ok(record);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Record record, HttpServletRequest request) {
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        record.setMemberid(loginMemberId);
        int affected = recordService.recordUpdateRecord(record, loginMemberId);
        if (affected == 0) {
            return notFound(record.getId());
        }
        return ResponseEntity.ok(ok("success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer loginMemberId = (Integer) request.getAttribute("loginMemberId");
        int affected = recordService.recordDeleteById(id, loginMemberId);
        if (affected == 0) {
            return notFound(id);
        }
        return ResponseEntity.ok(ok("success"));
    }

    /**
     * id 不存在，或者存在但不属于当前登录用户——两种情况都返回 404。
     * 刻意不区分：告诉调用方「这条记录不是你的」，等于泄露了它存在。
     */
    private ResponseEntity<Map<String, Object>> notFound(Integer id) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", 404);
        body.put("message", "记录不存在或无权访问：" + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    private Map<String, Object> ok(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", message);
        return result;
    }
}
