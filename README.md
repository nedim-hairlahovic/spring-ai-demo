# Spring AI Demo

This project showcases the capabilities of Spring AI (v1.0.0), a Spring-based framework that simplifies integration with large language models (LLMs) such as OpenAI GPT, enabling AI-powered features in Java applications.

The demo includes examples of:
- **Simple chat interactions with OpenAI** — basic prompt-response communication using ChatClient.
- **Streaming responses** — demonstrates real-time token streaming from the model for a more interactive user experience.
- **Structured output** — shows how to map model responses directly into typed Java objects for safer and predictable AI responses.
- **Conversations with in-memory chat memory** — maintains context across turns within a single chat session.
- **Retrieval-Augmented Generation (RAG)** — retrieves relevant context from local HR policy documents using an in-memory vector store.
- **Tool integration** (Help Desk example) — allows the chat to perform actions such as creating or retrieving help desk tickets based on user requests.

Each feature is exposed as a simple REST API that can be tested through a minimal Angular frontend (located in the /frontend folder).

## Basic Chat Example

This example, implemented in the `BasicChatService` class, shows the simplest way to interact with an OpenAI model using Spring AI.
The ChatClient is configured separately in the `ChatConfig` class, where default model options and advisors are defined.

`ChatClient` is the main entry point for communicating with a language model. It handles sending prompts and receiving responses through a simple fluent API.

`ChatOptions` defines configuration for the chat model — such as which model to use (`gpt-4.1-mini` in this case), temperature, or max tokens.
These options can also be set in `application.properties` or `application.yml`, allowing you to change models or tuning parameters without modifying code.

`TokenUsageAdvisor` is a custom advisor that logs token usage details for each model call, helping track and analyze cost or performance.
In Spring AI, advisors act as interceptors that can extend or modify the behavior of AI interactions — for example, by logging usage, adding context, handling errors, or post-processing model responses.

## Prompt Template Example

This example, implemented in the `InternalHrAssistantService` class, demonstrates how to guide the model’s behavior using system prompts and prompt templating.

In Spring AI, each message in a chat interaction has a role, which defines its purpose in the conversation:
- `system` – sets the model’s behavior, context, and tone. It defines how the AI should respond.
- `user` – represents the user’s input or question.
- `assistant` – represents the model’s previous replies (used in chat memory or multi-turn conversations).

Together, these roles form the conversation context that the model uses to generate coherent and consistent responses.

### Prompt Templating

Instead of hardcoding the system message as a long string, prompts can be externalized into files — such as `.st` (System Template) files — and then loaded from the classpath:
```
@Value("classpath:prompts/prompt-template.st")
private Resource some;
```

This approach allows you to:
- Keep prompts organized and maintainable.
- Reuse them across multiple services or environments.
- Dynamically inject variables into the prompt using placeholders (e.g. `{employeeName}`, `{policyType}`).

A `.st` file (prompt template) can contain static text or placeholders like:
```
You are an HR assistant. Help answer questions about {topic} in a professional tone.
```
At runtime, these placeholders can be filled in programmatically like this:
```java
String response = chatClient
    .prompt()
    .user(promptTemplateSpec -> promptTemplateSpec
        .text(promptTemplate)          // your template text with placeholders
        .param("topic", "Some topic value") // fill the {topic} placeholder
    )
    .call()
    .content();
```

An example of this can be found in the PromptTemplateService class.

## Streaming Responses

You can receive streaming responses from the chat client like this:

```java
public Flux<String> streamChat(String message) {
    return chatClient
            .prompt(message)
            .stream()
            .content();
}
```

This example can be found in the `BasicChatService` class.

## Structured Output

The structured output feature allows the chat client to return responses as strongly-typed objects instead of plain text, making it easier to work with in your code.

Example usage:

```java
chatClient
        .prompt(message)                  // user prompt
        .system(systemMessage)            // optional system message
        .call()                           // execute the prompt
        .entity(new ParameterizedTypeReference<MyResponseDto>() {}); // map response to list of DTO's
```

This example can be found in the `StructuredOutputService` class.

## Memory Chat

The memory chat feature allows the chat client to maintain context across multiple messages, enabling more natural, stateful conversations. 
This is useful for scenarios like customer support, personal assistants, or any multi-turn dialogue where the AI should “remember” previous interactions.

A simple in-memory example can be found in the `ChatMemoryServic` class, including;

- `ChatMemory` manages conversation message history. In this example, it's simple in-memory, meaning it will be lost if the application restarts.
- `ChatMemoryRepository` is used for storing conversation messages. By default, only `InMemoryChatMemoryRepository` is provided, which keeps messages in memory.
- `MessageChatMemoryAdvisor` uses the memory to provide context for follow-up messages.
- `CONVERSATION_ID` identifies the conversation, ensuring messages are linked to the correct session.

## Retrieval-Augmented Generation (RAG)

RAG allows the AI to answer questions using external documents by retrieving relevant information first, then generating a response based on that context. 
This is useful for building assistants that can answer domain-specific queries (e.g., HR policies, product manuals).

Example service using RAG:

```java
@Service
@RequiredArgsConstructor
public class RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/hrPolicySystemTemplate.st")
    private Resource hrSystemTemplate;

    public String chat(String message) {
        // Search for similar documents in the vector store
        var searchRequest = SearchRequest.builder()
                .query(message)
                .topK(3)
                .similarityThreshold(0.5)
                .build();
        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
        String similarContext = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        // Use retrieved context in the system prompt
        return chatClient.prompt(message)
                .system(promptSystemSpec ->
                        promptSystemSpec.text(hrSystemTemplate)
                                        .param("documents", similarContext))
                .call()
                .content();
    }
}
```

How it works:
- `vectorStore.similaritySearch(searchRequest)` retrieves the most relevant documents to the user query.
- The retrieved documents are passed into a system prompt template (hrSystemTemplate) for context-aware generation.
- This enables the AI to provide more accurate and domain-specific answers.

For this example, a simple in-memory vector store is used (`VectorStoreConfig` class), but Spring AI supports other implementations such as Quandtra, PGVector, or other vector databases.

## Tools Integration

Spring AI supports tooling, allowing the chat client to invoke external services or APIs as part of a conversation. 
This is useful for creating multi-step workflows, like help desk operations, directly from chat.

Tools are simple Java methods annotated with `@Tool` and can accept parameters and a ToolContext.

Key Points:
- `@Tool` methods can be automatically invoked by the AI when relevant.
- `ToolContext` allows passing dynamic information (e.g., current username, session data).
- Tools can return objects or direct text, depending on the use case.
- Tools enables the chat client to perform real-world actions like creating tickets, checking status, or interacting with other APIs.

This example is implemented in the `ToolsChatService` and `HelpDeskTools` classes.

