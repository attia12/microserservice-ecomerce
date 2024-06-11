package com.attia12.ecomerce.kafka;

import com.attia12.ecomerce.email.EmailService;
import com.attia12.ecomerce.kafka.order.OrderConfirmation;
import com.attia12.ecomerce.kafka.payment.PaymentConfirmation;
import com.attia12.ecomerce.notification.Notification;
import com.attia12.ecomerce.notification.NotificationRepository;
import com.attia12.ecomerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j

public class NotificationConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from payment topic :: %s",paymentConfirmation));
        repository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)


                        .build()
        );
        //send the email
        var customerName=paymentConfirmation.customerFirstname()+ " " + paymentConfirmation.customerLastname();
          emailService.sendPaymentSuccessEmail(
                  paymentConfirmation.customerEmail(),
                  customerName,
                  paymentConfirmation.amount(),
                  paymentConfirmation.orderReference()

          );



    }
    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from order topic :: %s",orderConfirmation));
        repository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)


                        .build()
        );
        //send the email
        var customerName=orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname();
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
               orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()

        );




    }

}
