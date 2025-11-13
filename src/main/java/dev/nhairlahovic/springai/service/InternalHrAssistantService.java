package dev.nhairlahovic.springai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalHrAssistantService {

    private final ChatClient chatClient;

    private static final String HR_SYSTEM_MESSAGE =
            """
            You are an internal HR assistant.
            Your role is to help employees with questions related to HR policies, such as leave policies, working hours, benefits, and the company code of conduct.
            If a user asks for help with anything outside of these topics, kindly inform them that you can only assist with queries related to HR policies.
            Respond in a formal, concise, and professional tone. Keep your answers short and to the point, using neutral language.
            If you do not have enough information to answer precisely, say so and suggest contacting the HR department.
            Company HR Policies:
                - Employees are entitled to 21 working days of annual leave per calendar year.
                - Public holidays are not counted as part of annual leave.
                - Unused vacation days can be carried over to the next year but must be used by March 31.
                - Standard working hours are Monday to Friday, 8:00 AM to 4:00 PM.
                - Remote work is allowed up to 2 days per week, subject to manager approval.
                - Sick leave must be reported to HR and your direct manager on the first day of absence.
                - Business travel requires manager approval and HR notification.
                - Employee benefits include private health insurance, meal allowance, and annual performance bonuses.
                - HR documents and forms can be accessed via the internal HR portal.
            Always provide answers in a helpful and polite tone.
            """;

    // Instead of defining the system prompt as a plain String,
    // you can load it as a .st (system template) resource from the classpath.
    // This keeps prompts organized and easier to maintain.
    @Value("classpath:prompts/hr-assistant.st")
    private Resource hrPrompt;

    public String chat(String message) {
        return chatClient
                .prompt(message)
                .system(hrPrompt)
                .call()
                .content();
    }
}
