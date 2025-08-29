package com.dinendrop.orderservice.controller;

import com.dinendrop.orderservice.dto.OrderRequestDTO;
import com.dinendrop.orderservice.dto.OrderResponseDTO;
import com.dinendrop.orderservice.dto.UpdateStatusDTO;
import com.dinendrop.orderservice.service.impl.OrderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO req) {
        return ResponseEntity.ok(orderService.createOrder(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable String id) {
        OrderResponseDTO res = orderService.getById(id);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(res);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getByUser(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponseDTO>> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(orderService.getByRestaurant(restaurantId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable String id, @RequestBody UpdateStatusDTO req) {
        System.out.println(req.getStatus());
        return ResponseEntity.ok(orderService.updateStatus(id, req.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancel(@PathVariable String id) {
        boolean cancelled = orderService.cancelOrder(id);
        return cancelled ? ResponseEntity.ok("Order cancelled") : ResponseEntity.badRequest().body("Cannot cancel at current status");
    }


}
