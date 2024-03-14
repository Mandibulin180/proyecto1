package com.notification_service.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.notification_service.events.OrderEvent;
import com.notification_service.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventsListener {

    // Este crea el topic
    @KafkaListener(topics = "orders-topic")
    public void handleOrdersNotifications(String message) {
        var orderEvent = JsonUtils.fromJson(message, OrderEvent.class);

        //Send email to customer, send SMS to customer, etc.
        //Notify another service...
        log.info("Order {} event received for order : {} with {} items", orderEvent.OrderStatus(), orderEvent.orderNumber(), orderEvent.ItemCount());
    }

}
