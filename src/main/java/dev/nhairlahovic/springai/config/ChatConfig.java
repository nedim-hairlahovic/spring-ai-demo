package dev.nhairlahovic.springai.config;

import dev.nhairlahovic.springai.advisor.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    // ChatOptions define model behavior (e.g., model name, temperature, etc.).
    // These options can also be configured via application.properties, e.g.:
    // spring.ai.openai.chat.options.model=gpt-4.1-mini
    // spring.ai.openai.chat.options.temperature=0.8
    private final ChatOptions chatOptions = ChatOptions.builder()
            .model("gpt-4.1-mini")
            .build();

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultAdvisors(new TokenUsageAdvisor())
                .build();
    }
}
