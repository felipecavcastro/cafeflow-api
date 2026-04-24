package com.cafeflow.repository;

import com.cafeflow.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    // O Spring entende que deve buscar onde o campo 'isAvailable' é true
    List<Station> findByIsAvailableTrue();
}
