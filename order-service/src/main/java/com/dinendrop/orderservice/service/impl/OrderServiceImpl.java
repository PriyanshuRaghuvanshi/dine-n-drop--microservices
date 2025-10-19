package com.dinendrop.orderservice.service.impl;

import com.dinendrop.orderservice.client.restaurant.RestaurantClient;
import com.dinendrop.orderservice.client.restaurant.dto.MenuItemDTO;
import com.dinendrop.orderservice.client.restaurant.dto.RestaurantDTO;
import com.dinendrop.orderservice.constants.OrderStatus;
import com.dinendrop.orderservice.constants.PaymentStatus;
import com.dinendrop.orderservice.dto.OrderItemRequestDTO;
import com.dinendrop.orderservice.dto.OrderItemResponseDTO;
import com.dinendrop.orderservice.dto.OrderRequestDTO;
import com.dinendrop.orderservice.dto.OrderResponseDTO;

import com.dinendrop.orderservice.model.Order;
import com.dinendrop.orderservice.model.OrderItem;
import com.dinendrop.orderservice.repository.OrderRepository;
import com.dinendrop.orderservice.service.EventService;
import com.dinendrop.orderservice.service.OrderService;
import com.dinendrop.orderservice.utils.AfterCommitExecutor;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;
    private final EventService eventService;
    private final AfterCommitExecutor afterCommitExecutor;
    private final Executor taskExecutor;



    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO req) {
        validateRequest(req);

        // Run restaurant validation and menu fetch in parallel
        CompletableFuture<RestaurantDTO> restaurantFuture = validateRestaurantAsync(req.getRestaurantId());

        CompletableFuture<Map<String, MenuItemDTO>> menuFuture = getMenuMapAsync(req.getRestaurantId());


        RestaurantDTO restaurant = restaurantFuture.join();
        Map<String, MenuItemDTO> menuMap = menuFuture.join();

//        RestaurantDTO restaurant = validateRestaurant(req.getRestaurantId());
//        Map<String, MenuItemDTO> menuMap = getMenuMap(req.getRestaurantId());

        Order order = buildOrderSkeleton(req);

        attachOrderItems(order, req.getItems(), menuMap);

        //  Best place to calculate total = after attaching items, before saving
        double total = calculateTotal(order);
        order.setOrderTotal(total);
        order.setPaymentStatus(PaymentStatus.INITIATED);
        Order savedOrder = orderRepository.save(order);

        // Ensure events go out only if TX commits; methods themselves are @Async
        afterCommitExecutor.execute(() -> {
            eventService.publishOrderStatusChangedEvent(savedOrder);
            eventService.publishPaymentInitiatedEvent(savedOrder);
        });


        return mapToResponse(savedOrder);
    }

    public Optional<Order> getByIdEntity(String id) {
        return orderRepository.findById(id);
    }

    public OrderResponseDTO getById(String id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public List<OrderResponseDTO> getByUser(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getByRestaurant(String restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        afterCommitExecutor.execute(() -> eventService.publishOrderStatusChangedEvent(order));

        return mapToResponse(order);
    }

    @Transactional
    public boolean cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // business rule: allow cancel only if not PREPARING or later
        if (order.getStatus().ordinal() >= OrderStatus.PREPARING.ordinal()) {
            return false;
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        afterCommitExecutor.execute(() -> eventService.publishOrderStatusChangedEvent(order));
        return true;
    }



    private OrderResponseDTO mapToResponse(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .restaurantId(order.getRestaurantId())
                .deliveryAddress(order.getDeliveryAddress())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .orderTotal(order.getOrderTotal())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream().map(oi -> {
                    OrderItemResponseDTO dto = new OrderItemResponseDTO();
                    dto.setMenuItemId(oi.getMenuItemId());
                    dto.setQuantity(oi.getQuantity());
                    dto.setPrice(oi.getPrice());
                    return dto;
                }).collect(Collectors.toList()))
                .build();
    }

    private void validateRequest(OrderRequestDTO req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("No items provided");
        }
        if (req.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
    }

    @Async("taskExecutor")
    private CompletableFuture<RestaurantDTO> validateRestaurantAsync(String restaurantId) {
        RestaurantDTO restaurant = restaurantClient.getRestaurant(restaurantId);
        if (restaurant == null || !restaurant.isOnline()) {
            throw new IllegalArgumentException("Restaurant unavailable or offline");
        }
        return CompletableFuture.completedFuture(restaurant);
    }


    @Async("taskExecutor")
    private CompletableFuture<Map<String, MenuItemDTO>> getMenuMapAsync(String restaurantId) {
        List<MenuItemDTO> menu = restaurantClient.getRestaurantMenu(restaurantId);
        Map<String, MenuItemDTO> menuMap = menu.stream()
                .collect(Collectors.toMap(MenuItemDTO::getId, m -> m));

        return CompletableFuture.completedFuture(menuMap);
    }

    private Order buildOrderSkeleton(OrderRequestDTO req) {
        LocalDateTime now = LocalDateTime.now();
        return Order.builder()
                .userId(req.getUserId())
                .restaurantId(req.getRestaurantId())
                .deliveryAddress(req.getDeliveryAddress())
                .paymentMethod(req.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .items(new ArrayList<>())
                .build();
    }

    private void attachOrderItems(Order order, List<OrderItemRequestDTO> items, Map<String, MenuItemDTO> menuMap) {
        for (OrderItemRequestDTO item : items) {
            MenuItemDTO menuItem = menuMap.get(item.getMenuItemId());
            if (menuItem == null) {
                throw new IllegalArgumentException("Menu item not found: " + item.getMenuItemId());
            }
            if (Boolean.FALSE.equals(menuItem.getAvailable())) {
                throw new IllegalArgumentException("Menu item unavailable: " + menuItem.getName());
            }

            double priceToUse = menuItem.getPrice();
            OrderItem oi = OrderItem.builder()
                    .menuItemId(menuItem.getId())
                    .quantity(item.getQuantity())
                    .price(priceToUse)
                    .order(order)
                    .build();

            order.getItems().add(oi);
        }
    }

    private double calculateTotal(Order order) {
        return order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

}
