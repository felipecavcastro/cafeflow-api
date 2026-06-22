package com.cafeflow.controller;

import com.cafeflow.model.*;
import com.cafeflow.repository.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepo;
    private final StationRepository stationRepo;
    private final UserRepository userRepo;

    public BookingController(BookingRepository bookingRepo, StationRepository stationRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.stationRepo = stationRepo;
        this.userRepo = userRepo;
    }


    @PostMapping
    public Booking create(@RequestBody Booking booking, @AuthenticationPrincipal String username) {
        // REGRA DE NEGÓCIO: Valor mínimo de R$ 10,00
        if (booking.getPrepaidAmount() < 10.0) {
            throw new RuntimeException("O valor mínimo para reserva é R$ 10,00.");
        }

        // Buscamos a mesa real do banco
        Station station = stationRepo.findById(booking.getStation().getId())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada!"));

        // Verificamos se está disponível
        if (!station.isAvailable()) {
            throw new RuntimeException("Esta mesa já está ocupada!");
        }

        // CAPTURA AUTOMÁTICA: Busca o usuário que está logado no token
        com.cafeflow.model.User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));

        // Marcamos a mesa como ocupada
        station.setAvailable(false);
        stationRepo.save(station);

        // Vincula a mesa, o usuário e o status ativo à reserva
        booking.setStation(station);
        booking.setUser(currentUser); // Vincula o usuário real aqui!
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

    // Listar todas as reservas para conferir nomes, créditos e horários
    @GetMapping
    public List<Booking> getAll() {
        return bookingRepo.findAll();
    }

    @GetMapping("/revenue")
    public String getRevenue() {
        Double total = bookingRepo.calculateTotalRevenue();
        if (total == null) total = 0.0;
        return "Faturamento total das reservas concluídas: R$ " + String.format("%.2f", total);
    }

    // NOVO ENDPOINT DE HISTÓRICO PROTEGIDO E FILTRADO POR USUÁRIO:
    @GetMapping("/history")
    public List<Booking> getHistory(@AuthenticationPrincipal String username) {
        return bookingRepo.findByUserUsername(username);
    }
}
