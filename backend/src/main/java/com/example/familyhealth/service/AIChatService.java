package com.example.familyhealth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 智能医生 AI 对话服务，对接 DeepSeek 的 OpenAI 兼容接口。
 * <p>
 * 改造点：
 * 1. API Key 从环境变量注入，没配也不影响应用启动，只在真正调用时给出明确提示；
 * 2. RestTemplate 加了连接/读取超时——原来的写法一旦上游卡住，线程会一直挂着；
 * 3. 异常只记日志，不再把上游报错原文抛给前端。
 */
@Service
public class AIChatService {

    private static final Logger log = LoggerFactory.getLogger(AIChatService.class);

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int READ_TIMEOUT_MS = 60_000;
    private static final double TEMPERATURE = 0.7;
    private static final int MAX_TOKENS = 2000;

    private final String apiUrl;
    private final String apiKey;
    private final String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIChatService(@Value("${ai.api.url}") String apiUrl,
                         @Value("${ai.api.key}") String apiKey,
                         @Value("${ai.api.model}") String model) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.model = model;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        factory.setReadTimeout(READ_TIMEOUT_MS);
        this.restTemplate = new RestTemplate(factory);
    }

    /** 是否配置了 API Key */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * 与 AI 大模型对话。
     *
     * @param messages 历史消息列表，每条含 role(user/assistant) 与 content
     * @return AI 回复内容；失败时返回给用户看的中文提示
     */
    public String chat(List<Map<String, String>> messages) {
        if (!isConfigured()) {
            log.warn("ai.api.key 未配置，无法调用大模型。请设置环境变量 AI_API_KEY 后重启后端。");
            return "AI 服务尚未配置：请在启动后端前设置环境变量 AI_API_KEY（DeepSeek 的 API Key）。";
        }

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);

            ArrayNode messagesArray = objectMapper.createArrayNode();

            ObjectNode systemMsg = objectMapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", buildSystemPrompt());
            messagesArray.add(systemMsg);

            for (Map<String, String> msg : messages) {
                ObjectNode msgNode = objectMapper.createObjectNode();
                msgNode.put("role", msg.get("role"));
                msgNode.put("content", msg.get("content"));
                messagesArray.add(msgNode);
            }

            requestBody.set("messages", messagesArray);
            requestBody.put("temperature", TEMPERATURE);
            requestBody.put("max_tokens", MAX_TOKENS);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<String> entity =
                    new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode choices = root.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                String content = choices.get(0).path("message").path("content").asText(null);
                if (content != null && !content.isBlank()) {
                    return content;
                }
            }
            log.warn("大模型返回了空内容，response={}", response.getBody());
            return "抱歉，AI 服务返回了空响应，请稍后再试。";

        } catch (Exception e) {
            log.error("调用大模型失败", e);
            return "AI 服务暂时不可用，请稍后再试。";
        }
    }

    /**
     * 构建系统提示词——定义「智慧医生」的角色、职责与安全边界。
     */
    private String buildSystemPrompt() {
        return "你是一位专业、友好的「智慧医生」健康助手。你的职责是：\n"
                + "1. 用通俗易懂的语言解答用户的健康相关问题\n"
                + "2. 根据用户描述的症状，提供初步的健康建议和生活方式指导\n"
                + "3. 提醒用户定期监测体温、血压、血糖、血氧、体重等健康指标\n"
                + "4. 对于严重症状，务必提醒用户及时就医\n\n"
                + "注意事项：\n"
                + "- 你并非真正的医生，不能替代专业医疗诊断\n"
                + "- 回答要简洁、有条理，使用中文\n"
                + "- 保持亲切、耐心的语气\n"
                + "- 如果用户的问题超出健康范围，可以友好地引导回健康话题";
    }
}
