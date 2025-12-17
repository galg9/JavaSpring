package me.prod.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.prod.orderservice.event.OrderEvent;
import me.prod.orderservice.model.Order;
import me.prod.orderservice.producer.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderProducer orderProducer;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        log.info("Received order request: {}", order);

        OrderEvent orderEvent = new OrderEvent(order.getProduct(), order.getQuantity());
        orderProducer.sendOrder(orderEvent);

        return ResponseEntity.ok("Order received and sent to processing");
    }
}