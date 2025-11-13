package dev.nhairlahovic.springai.tools;

import dev.nhairlahovic.springai.model.TicketRequest;
import dev.nhairlahovic.springai.entity.Ticket;
import dev.nhairlahovic.springai.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpDeskTools {

    private final HelpDeskTicketService service;

    @Tool(name = "createTicket", description = "Create the Support Ticket", returnDirect = true)
    String createTicket(@ToolParam(description = "Details to create a Support ticket")
                        TicketRequest ticketRequest, ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        log.info("Creating support ticket for user: {} with details: {}", username, ticketRequest);
        var savedTicket = service.createTicket(ticketRequest,username);
        log.info("Ticket created successfully. Ticket ID: {}, Username: {}", savedTicket.getId(), savedTicket.getUsername());
        return "Ticket #" + savedTicket.getId() + " created successfully for user " + savedTicket.getUsername();
    }

    @Tool(description = "Fetch the status of the tickets based on a given username")
    List<Ticket> getTicketStatus(ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        log.info("Fetching tickets for user: {}", username);
        var tickets =  service.getTicketsByUsername(username);
        log.info("Found {} tickets for user: {}", tickets.size(), username);
        return tickets;
    }
}
