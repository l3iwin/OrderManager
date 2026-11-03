package org.example;

import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testSendOrderCompletedEmailDoesNotThrowException() {
        Item item = new Item("Monitor 4K");
        User user = new User("Maria Silva", "maria@example.com");
        Order order = new Order(item, 2, user);

        assertDoesNotThrow(() -> {
            notificationService.sendOrderCompletedEmail(order);
        });
    }
}