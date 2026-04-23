package com.cafeflow.controller;

import com.cafeflow.model.Station;
import com.cafeflow.repository.StationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationRepository repository;

    public StationController(StationRepository repository) {
        this.repository = repository;
    }

    // Listar todas as mesas (estações de trabalho)
    @GetMapping
    public List<Station> getAll() {
        return repository.findAll();
    }

    // Cadastrar uma nova mesa (Ex: "Mesa da Janela com Tomada")
    @PostMapping
    public Station create(@RequestBody Station station) {
        return repository.save(station);
    }
}
