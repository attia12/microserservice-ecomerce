package com.attia12.ecomerce.orderline;

public record OrderLineRequest(Integer id, Integer orderId, Integer productId, double quantity) {
}
