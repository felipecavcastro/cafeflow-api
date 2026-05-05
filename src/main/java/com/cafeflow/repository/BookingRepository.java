package com.cafeflow.repository;

import com.cafeflow.model.Booking;
import com.cafeflow.model.BookingStatus; // Importação necessária
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT SUM(b.prepaidAmount) FROM Booking b WHERE b.status = 'COMPLETED'")
    Double calculateTotalRevenue();

    List<Booking> findByStatus(BookingStatus status);
}
