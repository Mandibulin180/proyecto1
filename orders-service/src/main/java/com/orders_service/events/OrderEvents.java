package com.orders_service.events;

import com.orders_service.models.OrderStatus;

public record OrderEvents(String orderNumber, int itemsCount, OrderStatus OrderStatus) {    
}
