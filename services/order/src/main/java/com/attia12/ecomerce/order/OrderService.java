package com.attia12.ecomerce.order;

import com.attia12.ecomerce.customer.CustomerClient;
import com.attia12.ecomerce.exception.BusinessException;
import com.attia12.ecomerce.orderline.OrderLineRequest;
import com.attia12.ecomerce.orderline.OrderLineService;
import com.attia12.ecomerce.product.ProductClient;
import com.attia12.ecomerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    public Integer createOrder(OrderRequest request) {
        //check the customer -> openFeign
        var customer=customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No Customer exist with the provided ID "));
        // purchase the product --> product microservice (rest template)
        this.productClient.purchaseProducts(request.products());

        // persist order
        var order=this.repository.save(mapper.toOrder(request));

        // persist orderline

        for (PurchaseRequest purchaseRequest : request.products())
        {
            orderLineService.saveOrderLine(new OrderLineRequest(null,order.getId(),purchaseRequest.productId(),purchaseRequest.quantity()));

        }
        // start payement process
        // send the order confirmation to our notification microservice kafka
        return null;
    }
}
