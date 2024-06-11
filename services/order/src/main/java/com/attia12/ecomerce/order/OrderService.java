package com.attia12.ecomerce.order;

import com.attia12.ecomerce.customer.CustomerClient;
import com.attia12.ecomerce.exception.BusinessException;
import com.attia12.ecomerce.kafka.OrderConfirmation;
import com.attia12.ecomerce.kafka.OrderProducer;
import com.attia12.ecomerce.orderline.OrderLineRequest;
import com.attia12.ecomerce.orderline.OrderLineService;
import com.attia12.ecomerce.payment.PaymentClient;
import com.attia12.ecomerce.payment.PaymentRequest;
import com.attia12.ecomerce.product.ProductClient;
import com.attia12.ecomerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest request) {
        //check the customer -> openFeign
        var customer=customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No Customer exist with the provided ID "));
        // purchase the product --> product microservice (rest template)
        var purchasedProducts=this.productClient.purchaseProducts(request.products());

        // persist order
        var order=this.repository.save(mapper.toOrder(request));

        // persist orderline

        for (PurchaseRequest purchaseRequest : request.products())
        {
            orderLineService.saveOrderLine(new OrderLineRequest(null,order.getId(),purchaseRequest.productId(),purchaseRequest.quantity()));

        }
        // start payement process
        var payementRequest=new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer

        );
        paymentClient.requestOrderPayment(payementRequest);
        // send the order confirmation to our notification microservice (kafka)
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                request.reference(),request.amount(),request.paymentMethod(),customer,purchasedProducts
        ));
        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return repository.findAll().stream().map(mapper::fromOrder).collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId).map(mapper::fromOrder).orElseThrow(()->new EntityNotFoundException(String.format("No order found with the provided id :: %s",orderId)));
    }
}
