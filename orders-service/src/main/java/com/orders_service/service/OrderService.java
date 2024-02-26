package com.orders_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.orders_service.dto.BaseResponse;
import com.orders_service.dto.OrderItemsRequest;
import com.orders_service.dto.OrderItemsResponse;
import com.orders_service.dto.OrderRequest;
import com.orders_service.dto.OrderResponse;
import com.orders_service.models.Order;
import com.orders_service.models.OrderItems;
import com.orders_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        // TODO check for inventory

        BaseResponse result = this.webClientBuilder.build()
            .post()
            .uri("lb://inventory-service/api/inventory/in-stock")
            .bodyValue(orderRequest.getOrderItems())
            .retrieve()
            .bodyToMono(BaseResponse.class)
            .block(); 

        if(result != null && !result.hasErrors()){
            Order order = new Order();
            order.setOrdernumber(UUID.randomUUID().toString());
            order.setOrderItems(orderRequest.getOrderItems().stream()
            .map(orderItemRequest -> mapOrderItemRequestToOrderItem(orderItemRequest,order)).toList());
            this.orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Some of the products are not in stock");
        }
    }

    public List<OrderResponse> getAllOrders(){
        List<Order> orders = this.orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    private OrderItems mapOrderItemRequestToOrderItem(OrderItemsRequest orderItemRequest, Order order) {
        return  OrderItems.builder()
                .id(orderItemRequest.getId())
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrdernumber()
                , order.getOrderItems().stream().map(this::mapToOrderItemRequest).toList());
    }

    private OrderItemsResponse mapToOrderItemRequest(OrderItems orderItems){
        return new OrderItemsResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }
    
}
