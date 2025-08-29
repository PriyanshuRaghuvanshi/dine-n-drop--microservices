package com.dinendrop.diningservice.model;

import com.dinendrop.diningservice.util.ReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
public class DiningReservation {

    @Id
    private String id;

    private String restaurantId;

    private LocalDate reservationDate;

    private LocalTime timeSlot;

    private int numberOfPeople;

    private String userId;
    private String username;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;



}
