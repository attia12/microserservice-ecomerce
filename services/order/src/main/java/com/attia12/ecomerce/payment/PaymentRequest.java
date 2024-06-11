package com.attia12.ecomerce.payment;

import com.attia12.ecomerce.customer.CustomerResponse;
import com.attia12.ecomerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
