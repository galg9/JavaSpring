package me.prod.orderservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.prod.orderservice.event.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${kafka.topics.order-topic}")
    private String orderTopic;

    public void sendOrder(OrderEvent orderEvent) {
        log.info("Sending order event to Kafka: {}", orderEvent);
        kafkaTemplate.send(orderTopic, orderEvent);
        log.info("Order event sent successfully");
    }
}