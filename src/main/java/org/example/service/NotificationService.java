package org.example.service;

import org.example.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOrderCompletedEmail(Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getUser().getEmail());
            message.setSubject("Encomenda Concluída #" + order.getId());
            message.setText("Olá " + order.getUser().getName() + ",\n\nA sua encomenda para o item '" +
                    order.getItem().getName() + "' (Qtd: " + order.getQuantityRequested() +
                    ") foi totalmente satisfeita.\n\nObrigado!");

            mailSender.send(message);
            logger.info("[EMAIL SENT] Encomenda #{} concluída com sucesso para {}", order.getId(), order.getUser().getEmail());
        } catch (Exception e) {
            logger.error("[EMAIL ERROR] Falha ao enviar email para a encomenda #{}: {}", order.getId(), e.getMessage());
        }
    }
}
