package dev.nhairlahovic.springai.service;

import dev.nhairlahovic.springai.model.SportTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StructuredOutputService {

    private final ChatClient chatClient;

    private final String systemMessage = """
    You are a data extraction assistant specialized in sports.
    Only answer and return data for questions that ask about sport teams participating in competitions or leagues.
    If the question is not related to sport teams or competitions, do not answer or return an empty result.
    Always provide structured, factual information about relevant teams only.
    """;

    public List<SportTeam> chat(String message) {
        return chatClient
                .prompt(message)
                .system(systemMessage)
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }
}
