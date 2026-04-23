package com.cafeflow.model;

public enum BookingStatus {
    PENDING,   // Reservou mas não chegou
    ACTIVE,    // Está no café trabalhando
    COMPLETED, // Já foi embora
    CANCELLED  // Não apareceu (No-show)
}
