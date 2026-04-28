package com.cafeflow.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station station; // Qual mesa foi reservada

    private String nomadName;      // Nome do nômade
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Double prepaidAmount;  // Valor pago para reservar (ex: R$ 20,00)
    private Double consumedAmount = 0.0; // Quanto ele já gastou em café

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    // Este método roda automaticamente antes da reserva ser salva no banco
    @PrePersist
    protected void onCreate() {
        this.startTime = LocalDateTime.now(); // Pega a hora exata do computador
    }

}
