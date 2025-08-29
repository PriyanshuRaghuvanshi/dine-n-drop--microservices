package com.dinendrop.orderservice.dto;

import com.dinendrop.orderservice.constants.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private String userId;
    private String restaurantId;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private List<OrderItemRequestDTO> items;
}
