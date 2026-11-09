package org.example;

import org.example.dto.OrderResponseDTO;
import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.repository.*;
import org.example.service.NotificationService;
import org.example.service.OrderRetryService;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Autowired
    private OrderRetryService orderRetryService;

    @Test
    public void testCreateOrderSuccessfully() {
        Item item = itemRepository.save(new Item("Teclado Mecânico"));
        User user = userRepository.save(new User("Carlos Lima", "carlos@example.com"));

        OrderResponseDTO response = orderRetryService.createOrderWithRetry(item.getId(), 3, user.getId());

        assertNotNull(response);
        assertEquals(3, response.getQuantityRequested());
        assertEquals(item.getId(), response.getItemId());
        assertEquals("Teclado Mecânico", response.getItemName());
    }
}