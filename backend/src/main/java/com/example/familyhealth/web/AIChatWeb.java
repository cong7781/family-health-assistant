package com.example.familyhealth.web;

import com.example.familyhealth.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能医生 AI 对话接口
 */
@RestController
@RequestMapping("/aichat")
public class AIChatWeb {

    @Autowired
    private AIChatService aiChatService;

    /**
     * AI对话接口
     * 前端发送历史消息列表，后端调用大模型并返回回复
     *
     * 请求格式：
     * {
     *   "messages": [
     *     {"role": "user", "content": "我最近头疼"},
     *     {"role": "assistant", "content": "头疼可能由多种原因引起..."},
     *     {"role": "user", "content": "有什么缓解方法吗？"}
     *   ]
     * }
     *
     * 响应格式：
     * {
     *   "code": 200,
     *   "content": "针对头疼，您可以尝试以下方法..."
     * }
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();

        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> messages = (List<Map<String, String>>) request.get("messages");

            if (messages == null || messages.isEmpty()) {
                result.put("code", 400);
                result.put("content", "消息列表不能为空");
                return result;
            }

            // 调用AI服务
            String aiResponse = aiChatService.chat(messages);

            result.put("code", 200);
            result.put("content", aiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("content", "服务器内部错误：" + e.getMessage());
        }

        return result;
    }

    /**
     * 健康检查接口，测试AI服务是否可用
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "智能医生服务运行正常");
        return result;
    }
}
