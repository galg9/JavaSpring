package me.prod.orderstatusservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.prod.orderstatusservice.event.OrderStatusEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusProducer {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    @Value("${kafka.topics.order-status-topic}")
    private String orderStatusTopic;

    public void sendOrderStatus(OrderStatusEvent orderStatusEvent) {
        log.info("Sending order status event to Kafka: {}", orderStatusEvent);
        kafkaTemplate.send(orderStatusTopic, orderStatusEvent);
        log.info("Order status event sent successfully");
    }
}