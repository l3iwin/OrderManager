package org.example;

import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.StockMovement;
import org.example.entity.User;
import org.example.repository.ItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.OrderService;
import org.example.service.StockMovementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderFlowTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testOrderCreationWithExistingStock() {
        Item item = itemRepository.save(new Item("Smartphone"));
        User user = userRepository.save(new User("Alice Smith", "alice@example.com"));

        StockMovement movement = stockMovementService.createStockMovement(item.getId(), 10);
        assertEquals(10, movement.getQuantityAvailable());

        Order order = orderService.createOrder(item.getId(), 4, user.getId());

        assertTrue(order.isCompleted());
        assertEquals(4, order.getQuantityFulfilled());
        assertEquals(100.0, order.getCompletionPercentage());
    }

    @Test
    public void testPartialFulfillmentAndStockMovementAllocation() {
        Item item = itemRepository.save(new Item("Headphones"));
        User user = userRepository.save(new User("Bob Jones", "bob@example.com"));

        Order order = orderService.createOrder(item.getId(), 10, user.getId());
        assertFalse(order.isCompleted());
        assertEquals(0, order.getQuantityFulfilled());

        stockMovementService.createStockMovement(item.getId(), 6);

        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertFalse(updatedOrder.isCompleted());
        assertEquals(6, updatedOrder.getQuantityFulfilled());
        assertEquals(60.0, updatedOrder.getCompletionPercentage());

        stockMovementService.createStockMovement(item.getId(), 4);

        Order finalOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertTrue(finalOrder.isCompleted());
        assertEquals(10, finalOrder.getQuantityFulfilled());
        assertEquals(100.0, finalOrder.getCompletionPercentage());
    }
}