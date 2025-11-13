package dev.nhairlahovic.springai.service;

import dev.nhairlahovic.springai.repository.TicketRepository;
import dev.nhairlahovic.springai.entity.Ticket;
import dev.nhairlahovic.springai.model.TicketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

    private final TicketRepository ticketRepository;

    public Ticket createTicket(TicketRequest ticketInput, String username) {
        var ticket = Ticket.builder()
                .issue(ticketInput.issue())
                .username(username)
                .status(Ticket.TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUsername(String username) {
        return ticketRepository.findByUsername(username);
    }
}
