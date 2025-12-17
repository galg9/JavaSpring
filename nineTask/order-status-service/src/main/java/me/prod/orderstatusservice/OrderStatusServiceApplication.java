package me.prod.orderstatusservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrderStatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderStatusServiceApplication.class, args);
    }
}