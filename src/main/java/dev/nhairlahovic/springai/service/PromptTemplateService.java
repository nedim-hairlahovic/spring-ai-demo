package dev.nhairlahovic.springai.service;

import dev.nhairlahovic.springai.advisor.TokenUsageAdvisor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptTemplateService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/customerEmailTemplate.st")
    Resource userPromptTemplate;

    public String chat(String customerName, String customerMessage) {
        return chatClient
                .prompt()
                .system("""
                        You are a professional customer service assistant which helps drafting email
                        responses to improve the productivity of the customer support team
                        """)
                .advisors(new TokenUsageAdvisor())
                .user(promptTemplateSpec ->
                        promptTemplateSpec.text(userPromptTemplate)
                                .param("customerName", customerName)
                                .param("customerMessage", customerMessage))
                .call().content();
    }
}
