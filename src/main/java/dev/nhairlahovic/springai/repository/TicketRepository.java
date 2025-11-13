package dev.nhairlahovic.springai.repository;

import dev.nhairlahovic.springai.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUsername(String username);
}
