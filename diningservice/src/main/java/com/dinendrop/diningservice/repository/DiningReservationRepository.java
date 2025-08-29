package com.dinendrop.diningservice.repository;

import com.dinendrop.diningservice.model.DiningReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DiningReservationRepository extends JpaRepository<DiningReservation,String> {
    List<DiningReservation> findByUsername(String username);
    List<DiningReservation> findByUserId(String userId);
    List<DiningReservation> findByRestaurantId(String restaurantId);
    Optional<DiningReservation> findByRestaurantIdAndReservationDateAndTimeSlot(
            String restaurantId, LocalDate reservationDate, LocalTime timeSlot);

}
