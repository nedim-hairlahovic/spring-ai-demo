package dev.nhairlahovic.springai.service;

import dev.nhairlahovic.springai.advisor.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
public class ChatMemoryService {

    private final ChatClient chatClient;

    public ChatMemoryService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new TokenUsageAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient
                .prompt(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, sessionId))
                .call()
                .content();
    }
}
