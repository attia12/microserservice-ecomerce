package com.attia12.ecomerce.email;

import com.attia12.ecomerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Async
    public void sendPaymentSuccessEmail(
            String destinationEamil,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@attia12.com");
        final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();
        Map<String,Object> variables=new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference",orderReference);
        Context context=new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());
        try {
            String htmlTemplate=templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destinationEamil);
            mailSender.send(mimeMessage);
            log.info(String.format("Info -Email successfully send to %s with template %s",destinationEamil,templateName));

        }catch (MessagingException e)
        {
            log.warn("Warning -Cannot send email to {}",destinationEamil);

        }

    }

    @Async
    public void sendOrderConfirmationEmail(
            String destinationEamil,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@attia12.com");
        final String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();
        Map<String,Object> variables=new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("totalAmount",amount);
        variables.put("orderReference",orderReference);
        variables.put("products",products);
        Context context=new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());
        try {
            String htmlTemplate=templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destinationEamil);
            mailSender.send(mimeMessage);
            log.info(String.format("Info -Email successfully send to %s with template %s",destinationEamil,templateName));

        }catch (MessagingException e)
        {
            log.warn("Warning -Cannot send email to {}",destinationEamil);

        }

    }
}
