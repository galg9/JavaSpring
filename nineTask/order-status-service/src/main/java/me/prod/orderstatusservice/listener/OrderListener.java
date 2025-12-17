package me.prod.orderstatusservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.prod.orderstatusservice.event.OrderEvent;
import me.prod.orderstatusservice.event.OrderStatusEvent;
import me.prod.orderstatusservice.producer.OrderStatusProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderListener {

    private final OrderStatusProducer orderStatusProducer;

    @KafkaListener(
            topics = "${kafka.topics.order-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(@Payload OrderEvent orderEvent) {
        log.info("Received order event: {}", orderEvent);

        OrderStatusEvent orderStatusEvent = new OrderStatusEvent("CREATED", Instant.now());
        orderStatusProducer.sendOrderStatus(orderStatusEvent);

        log.info("Order status event sent for order: {}", orderEvent);
    }
}