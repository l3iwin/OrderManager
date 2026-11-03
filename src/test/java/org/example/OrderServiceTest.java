package org.example;

import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.repository.*;
import org.example.service.NotificationService;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StockMovementRepository stockMovementRepository;
    @Mock
    private OrderAllocationRepository allocationRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrderSuccessfully() {
        Item item = new Item("Laptop");
        User user = new User("John Doe", "john@example.com");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(1L, 5, 1L);

        assertNotNull(order);
        assertEquals(5, order.getQuantityRequested());
        assertFalse(order.isCompleted());
        verify(orderRepository, atLeastOnce()).save(any(Order.class));
    }
}