package com.dinendrop.diningservice.service;

import com.dinendrop.diningservice.dto.DiningReservationDTO;
import com.dinendrop.diningservice.dto.RestaurantDTO;
import com.dinendrop.diningservice.model.DiningReservation;
import com.dinendrop.diningservice.repository.DiningReservationRepository;
import com.dinendrop.diningservice.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DiningReservationService {

    private final DiningReservationRepository reservationRepository;
    private final WebClient.Builder webClientBuilder;

    public DiningReservation bookReservation(String token, DiningReservationDTO dto) {
        // Fetch restaurant info

        RestaurantDTO restaurant = new RestaurantDTO();
        try {
            restaurant = webClientBuilder.build()
                    .get()
                    .uri("lb://api-gateway/restaurants/{id}", dto.getRestaurantId())
                    .header("Authorization" , token)
                    .retrieve()
                    .bodyToMono(RestaurantDTO.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace(); // full cause
        }


        // Validate restaurant existence
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found");
        }

        // Check if restaurant is online & allows dining
        if (!restaurant.isOnline() || !restaurant.isDiningEnabled()) {
            throw new RuntimeException("Restaurant is not available for dining reservations");
        }

        // Validate reservation time slot
        LocalTime slot = dto.getTimeSlot();
        if (slot.isBefore(restaurant.getOpenAt()) || slot.isAfter(restaurant.getCloseAt())) {
            throw new RuntimeException("Selected time is outside restaurant's operating hours");
        }

        // Check for conflicting reservation
        Optional<DiningReservation> conflict = reservationRepository
                .findByRestaurantIdAndReservationDateAndTimeSlot(
                        dto.getRestaurantId(), dto.getReservationDate(), slot
                );

        if (conflict.isPresent()) {
            throw new RuntimeException("This time slot is already booked for the restaurant");
        }

        //payment service upcoming

        // Save reservation
        DiningReservation reservation = new DiningReservation(
                UUID.randomUUID().toString(),
                dto.getRestaurantId(),
                LocalDate.now(),
                slot,
                dto.getNumberOfPeople(),
                dto.getUserId(),
                dto.getUsername(),
                LocalDateTime.now(),
                ReservationStatus.CONFIRMED
        );

        return reservationRepository.save(reservation);
    }

    public Optional<DiningReservation> getById(String id) {
        return reservationRepository.findById(id);
    }

    public List<DiningReservation> getByUserId(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<DiningReservation> getByRestaurantId(String restId) {
        return reservationRepository.findByRestaurantId(restId);
    }

    public boolean cancelReservation(String id) {
        Optional<DiningReservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            DiningReservation reservation = reservationOpt.get();

            // Set status to CANCELLED
            reservation.setStatus(ReservationStatus.CANCELLED);

            // Save updated reservation
            reservationRepository.save(reservation);
            return true;
        }
        return false;
    }

}
