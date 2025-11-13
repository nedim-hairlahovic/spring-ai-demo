package dev.nhairlahovic.springai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String issue;

    private TicketStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime eta;

    public enum TicketStatus {
        OPEN, IN_PROGRESS, CLOSED
    }
}
