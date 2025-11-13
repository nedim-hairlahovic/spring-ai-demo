package dev.nhairlahovic.springai.service;

import dev.nhairlahovic.springai.advisor.TokenUsageAdvisor;
import dev.nhairlahovic.springai.tools.HelpDeskTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
public class ToolsChatService {

    private final ChatClient chatClient;
    private final HelpDeskTools helpDeskTools;

    @Value("classpath:/prompts/helpDeskSystemPrompt.st")
    private Resource systemPrompt;

    public ToolsChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, HelpDeskTools helpDeskTools) {
        this.helpDeskTools = helpDeskTools;
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new TokenUsageAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient.prompt()
                .system(systemPrompt)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, sessionId))
                .user(message)
                .tools(helpDeskTools)
                .toolContext(Map.of("username", "some-user"))
                .call()
                .content();
    }
}
