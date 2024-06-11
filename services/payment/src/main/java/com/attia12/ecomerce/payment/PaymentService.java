package com.attia12.ecomerce.payment;

import com.attia12.ecomerce.notification.NotificationProducer;
import com.attia12.ecomerce.notification.PaymentNotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;
    public Integer createPayment(PaymentRequest request) {
        var payment=repository.save(mapper.toPayment(request));
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),request.amount(),request.paymentMethod(),
                request.customer().firstname(),request.customer().lastname(),request.customer().email()
        ));
        return payment.getId();
    }
}
