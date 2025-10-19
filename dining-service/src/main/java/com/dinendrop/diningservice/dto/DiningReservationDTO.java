package com.dinendrop.diningservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DiningReservationDTO {
    private String restaurantId;
    private LocalDate reservationDate;
    private LocalTime timeSlot;
    private int numberOfPeople;
    private String userId;
    private String username;
}
