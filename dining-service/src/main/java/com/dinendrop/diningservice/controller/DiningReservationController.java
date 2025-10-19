package com.dinendrop.diningservice.controller;



import com.dinendrop.diningservice.dto.DiningReservationDTO;
import com.dinendrop.diningservice.exceptions.ResourceNotFoundException;
import com.dinendrop.diningservice.model.DiningReservation;
import com.dinendrop.diningservice.service.DiningReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dining")
@RequiredArgsConstructor
public class DiningReservationController {

    private final DiningReservationService reservationService;

    @PostMapping("/book")
    public ResponseEntity<DiningReservation> bookDining(@RequestHeader("Authorization") String authHeader ,@RequestBody DiningReservationDTO dto) {
            DiningReservation reservation = reservationService.bookReservation(authHeader,dto);
            return ResponseEntity.ok(reservation);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DiningReservation> getById(@PathVariable String id) {
        return reservationService.getById(id)
                .map(ResponseEntity::ok)
              //  .orElse(ResponseEntity.notFound().build());
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DiningReservation>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reservationService.getByUserId(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<DiningReservation>> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(reservationService.getByRestaurantId(restaurantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancel(@PathVariable String id) {
        boolean cancelled = reservationService.cancelReservation(id);
        if (!cancelled) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }
        return ResponseEntity.ok("Reservation cancelled");
    }
}
