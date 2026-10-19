package com.example.familyhealth.commom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理。
 * <p>
 * 改造点：原来直接返回裸数字 500，前端拿到一个没有结构的响应无法处理，
 * 而且异常堆栈只打到控制台。现在统一返回 {code, message} 结构，
 * 详细信息只写日志，不回传给客户端（避免把内部实现细节泄露出去）。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(IllegalArgumentException e) {
        log.warn("请求参数不合法：{}", e.getMessage());
        return body(400, e.getMessage() == null ? "请求参数不合法" : e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception e) {
        log.error("服务器内部错误", e);
        return body(500, "服务器内部错误，请稍后重试");
    }

    private Map<String, Object> body(int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        return result;
    }
}
