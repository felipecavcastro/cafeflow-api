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

    // 1. CORRIGIDO: Relacionamento limpo com a sua entidade User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 2. CORRIGIDO: Adicionado o mapeamento do banco que faltava para a Mesa!
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private String nomadName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Double prepaidAmount;
    private Double consumedAmount = 0.0;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @PrePersist
    protected void onCreate() {
        this.startTime = LocalDateTime.now();
    }
}
