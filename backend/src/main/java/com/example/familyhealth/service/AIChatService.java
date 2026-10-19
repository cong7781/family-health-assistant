package com.example.familyhealth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 智能医生 AI 对话服务
 * 对接 DeepSeek / OpenAI 兼容的大模型 API
 */
@Service
public class AIChatService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIChatService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 与AI大模型对话
     * @param messages 历史消息列表，每条消息包含 role(user/assistant) 和 content
     * @return AI回复内容
     */
    public String chat(List<Map<String, String>> messages) {
        try {
            // 构建请求体
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);

            ArrayNode messagesArray = objectMapper.createArrayNode();

            // 添加系统提示词
            ObjectNode systemMsg = objectMapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", buildSystemPrompt());
            messagesArray.add(systemMsg);

            // 添加历史对话
            for (Map<String, String> msg : messages) {
                ObjectNode msgNode = objectMapper.createObjectNode();
                msgNode.put("role", msg.get("role"));
                msgNode.put("content", msg.get("content"));
                messagesArray.add(msgNode);
            }

            requestBody.set("messages", messagesArray);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class);

            // 解析响应
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode choices = root.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode message = choices.get(0).get("message");
                if (message != null) {
                    String content = message.get("content").asText();
                    return content != null ? content : "抱歉，我暂时无法回答这个问题。";
                }
            }
            return "抱歉，AI服务返回了空响应，请稍后再试。";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI服务调用失败：" + e.getMessage();
        }
    }

    /**
     * 构建系统提示词——定义智能医生的角色和行为
     */
    private String buildSystemPrompt() {
        return "你是一位专业、友好的「智慧医生」健康助手。你的职责是：\n" +
                "1. 用通俗易懂的语言解答用户的健康相关问题\n" +
                "2. 根据用户描述的症状，提供初步的健康建议和生活方式指导\n" +
                "3. 提醒用户定期监测体温、血压、血糖、血氧、体重等健康指标\n" +
                "4. 对于严重症状，务必提醒用户及时就医\n\n" +
                "注意事项：\n" +
                "- 你并非真正的医生，不能替代专业医疗诊断\n" +
                "- 回答要简洁、有条理，使用中文\n" +
                "- 保持亲切、耐心的语气\n" +
                "- 如果用户的问题超出健康范围，可以友好地引导回健康话题";
    }
}
