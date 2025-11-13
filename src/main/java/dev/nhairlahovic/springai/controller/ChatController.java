package dev.nhairlahovic.springai.controller;

import dev.nhairlahovic.springai.model.SportTeam;
import dev.nhairlahovic.springai.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final BasicChatService basicChatService;
    private final InternalHrAssistantService internalHrAssistantService;
    private final StructuredOutputService structuredOutputService;
    private final PromptTemplateService promptTemplateService;
    private final ChatMemoryService chatMemoryService;
    private final RagChatService ragChatService;
    private final ToolsChatService toolsChatService;

    @GetMapping("/basic")
    public String chat(@RequestParam("message") String message) {
        return basicChatService.chat(message);
    }

    @GetMapping("/hr-internal")
    public String hrInternal(@RequestParam("message") String message) {
        return internalHrAssistantService.chat(message);
    }

    @GetMapping("/stream")
    public Flux<String> streamChat(@RequestParam("message") String message) {
        return basicChatService.streamChat(message);
    }

    @GetMapping("/structured-output")
    public List<SportTeam> sportTeamChat(@RequestParam("message") String message) {
        return structuredOutputService.chat(message);
    }

    @GetMapping("/prompt-template")
    public String promptTemplateChat(@RequestParam("name") String name,
                                     @RequestParam("message") String message) {
        return promptTemplateService.chat(name, message);
    }

    @GetMapping("/memory")
    public String memoryChat(@RequestParam("message") String message,
                             @RequestHeader("X-Session-Id") String sessionId) {
        return chatMemoryService.chat(message, sessionId);
    }

    @GetMapping("/rag")
    public String ragChat(@RequestParam("message") String message) {
        return ragChatService.chat(message);
    }

    @GetMapping("/tools")
    public String toolsChat(@RequestParam("message") String message,
                             @RequestHeader("X-Session-Id") String sessionId) {
        return toolsChatService.chat(message, sessionId);
    }
}
