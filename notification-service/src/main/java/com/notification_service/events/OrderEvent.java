package com.notification_service.events;

import com.notification_service.models.OrderStatus;

public record OrderEvent(String orderNumber, int ItemCount,OrderStatus OrderStatus) {
    
}
