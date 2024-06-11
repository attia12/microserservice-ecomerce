package com.attia12.ecomerce.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class PaymentMapper {
    public Payment toPayment(PaymentRequest request) {
        return Payment.builder()
                .id(request.id())
                .orderId(request.orderId())

                .paymentMethod(request.paymentMethod())
                .amount(request.amount())



                .build();
    }
}
