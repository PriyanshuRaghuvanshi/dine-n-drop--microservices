package com.dinendrop.orderservice.dto;

import com.dinendrop.orderservice.constants.OrderStatus;
import lombok.Data;

@Data
public class UpdateStatusDTO {
    private OrderStatus status;
}
