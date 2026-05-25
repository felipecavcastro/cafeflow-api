package com.cafeflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // E-mail ou nome de login

    @Column(nullable = false)
    private String password; // Senha (que salvaremos criptografada)

    private String role; // "ADMIN" (Dono do Café) ou "USER" (Nômade)
}
