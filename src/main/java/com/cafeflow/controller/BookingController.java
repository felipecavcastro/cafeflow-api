package com.cafeflow.controller;

import com.cafeflow.model.*;
import com.cafeflow.repository.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepo;
    private final StationRepository stationRepo;

    public BookingController(BookingRepository bookingRepo, StationRepository stationRepo) {
        this.bookingRepo = bookingRepo;
        this.stationRepo = stationRepo;
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        // 1. Buscamos a mesa REAAL do banco de dados
        Station station = stationRepo.findById(booking.getStation().getId())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada!"));

        // 2. Verificamos se está disponível
        if (!station.isAvailable()) {
            throw new RuntimeException("Esta mesa já está ocupada!");
        }

        // 3. Marcamos como ocupada e SALVAMOS a mesa primeiro
        station.setAvailable(false);
        stationRepo.save(station);

        // 4. Vinculamos a mesa salva ao agendamento
        booking.setStation(station);
        booking.setStatus(BookingStatus.ACTIVE);

        return bookingRepo.save(booking);
    }

    // 2. Lançar Consumo (O garçom adiciona o valor do café/pão de queijo)
    @PatchMapping("/{id}/consume")
    public Booking addConsumption(@PathVariable Long id, @RequestParam Double amount) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada!"));

        // Lógica do Crédito: O que ele consome é somado ao total consumido
        booking.setConsumedAmount(booking.getConsumedAmount() + amount);

        return bookingRepo.save(booking);
    }
    // 3. Fechar a conta (Agora LIBERANDO a mesa)
    @PostMapping("/{id}/checkout")
    public String checkout(@PathVariable Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada!"));

        // Libera a mesa vinculada a esta reserva
        Station station = booking.getStation();
        if (station != null) {
            station.setAvailable(true);
            stationRepo.save(station);
        }

        double saldo = booking.getPrepaidAmount() - booking.getConsumedAmount();
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepo.save(booking);

        if (saldo >= 0) {
            return "Check-out realizado! O cliente ainda tinha R$ " + String.format("%.2f", saldo) + " de crédito não utilizado. Mesa liberada.";
        } else {
            double extra = Math.abs(saldo);
            return "Check-out realizado! O cliente deve pagar R$ " + String.format("%.2f", extra) + " adicionais. Mesa liberada.";
        }
    }
}