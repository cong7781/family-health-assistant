package com.example.familyhealth.web;

import com.example.familyhealth.service.AIChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能医生 AI 对话接口。
 */
@RestController
@RequestMapping("/aichat")
public class AIChatWeb {

    private static final Logger log = LoggerFactory.getLogger(AIChatWeb.class);

    /** 一次请求最多带多少条历史消息（防止把整段对话无限拼上去烧 token） */
    private static final int MAX_MESSAGES = 40;
    /** 单条消息最大字符数 */
    private static final int MAX_CONTENT_LENGTH = 4000;

    private final AIChatService aiChatService;

    public AIChatWeb(AIChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * 请求格式：
     * <pre>
     * {
     *   "messages": [
     *     {"role": "user", "content": "我最近头疼"},
     *     {"role": "assistant", "content": "头疼可能由多种原因引起..."}
     *   ]
     * }
     * </pre>
     * 响应格式：{"code": 200, "content": "..."}
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();

        Object rawMessages = request.get("messages");
        if (!(rawMessages instanceof List) || ((List<?>) rawMessages).isEmpty()) {
            return badRequest(result, "消息列表不能为空");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> raw = (List<Map<String, Object>>) rawMessages;
        if (raw.size() > MAX_MESSAGES) {
            return badRequest(result, "单次对话最多 " + MAX_MESSAGES + " 条消息，请新开一轮对话");
        }

        List<Map<String, String>> messages = new ArrayList<>(raw.size());
        for (Map<String, Object> item : raw) {
            Object role = item.get("role");
            Object content = item.get("content");
            if (role == null || content == null) {
                return badRequest(result, "消息格式不合法：每条消息都需要 role 和 content");
            }
            String contentText = String.valueOf(content);
            if (contentText.length() > MAX_CONTENT_LENGTH) {
                return badRequest(result, "单条消息不能超过 " + MAX_CONTENT_LENGTH + " 字");
            }
            Map<String, String> message = new HashMap<>(2);
            message.put("role", String.valueOf(role));
            message.put("content", contentText);
            messages.add(message);
        }

        String aiResponse = aiChatService.chat(messages);

        result.put("code", 200);
        result.put("content", aiResponse);
        return ResponseEntity.ok(result);
    }

    /** 健康检查接口，确认 AI 服务配置是否就绪 */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "智能医生服务运行正常");
        result.put("aiConfigured", aiChatService.isConfigured());
        return result;
    }

    private ResponseEntity<Map<String, Object>> badRequest(Map<String, Object> result, String message) {
        log.debug("AI 对话请求不合法：{}", message);
        result.put("code", 400);
        result.put("content", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
