package com.cafeflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // Ex: "Mesa da Janela 01"
    private boolean hasOutlet; // Se tem tomada (Ouro para o nômade!)
    private Integer capacity;  // Quantidade de pessoas
    private boolean isAvailable = true; // Se está livre agora
}
