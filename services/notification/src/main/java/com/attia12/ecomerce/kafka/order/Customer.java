package com.attia12.ecomerce.kafka.order;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email)  {
}
