package com.attia12.ecomerce.kafka;

import com.attia12.ecomerce.customer.CustomerResponse;
import com.attia12.ecomerce.order.PaymentMethod;
import com.attia12.ecomerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(

        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
