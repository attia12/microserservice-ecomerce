package com.attia12.ecomerce.orderline;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class OrderLineService {
    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;
    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        var order=mapper.toOrderLine(orderLineRequest);
        return repository.save(order).getId();
    }
}
