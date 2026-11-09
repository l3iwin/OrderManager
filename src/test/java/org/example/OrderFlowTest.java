package org.example;

import org.example.dto.OrderRequestDTO;
import org.example.dto.OrderResponseDTO;
import org.example.dto.StockMovementRequestDTO;
import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.StockMovement;
import org.example.entity.User;
import org.example.repository.ItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.OrderRetryService;
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
    private OrderRetryService orderRetryService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testOrderCreationWithExistingStock() {
        Item item = itemRepository.save(new Item("Mouse Gamer"));
        User user = userRepository.save(new User("Sara Pinto", "sara@example.com"));

        StockMovementRequestDTO stockDto = new StockMovementRequestDTO();
        stockDto.setItemId(item.getId());
        stockDto.setQuantity(10);
        stockMovementService.createStockMovement(stockDto);

        OrderResponseDTO response = orderRetryService.createOrderWithRetry(item.getId(), 5, user.getId());

        assertTrue(response.isCompleted(), "A encomenda deve estar concluída de imediato");
        assertEquals(5, response.getQuantityFulfilled());
        assertEquals(100.0, response.getCompletionPercentage());
        assertFalse(response.getAllocations().isEmpty(), "Deve conter registos de alocação de stock");
    }

    @Test
    public void testPartialFulfillmentAndStockMovementAllocation() {
        Item item = itemRepository.save(new Item("Headset Wireless"));
        User user = userRepository.save(new User("Miguel Santos", "miguel@example.com"));

        OrderResponseDTO initialResponse = orderRetryService.createOrderWithRetry(item.getId(), 10, user.getId());
        assertFalse(initialResponse.isCompleted());
        assertEquals(0, initialResponse.getQuantityFulfilled());

        StockMovementRequestDTO stockDto = new StockMovementRequestDTO();
        stockDto.setItemId(item.getId());
        stockDto.setQuantity(4);
        stockMovementService.createStockMovement(stockDto);

        assertEquals(4, initialResponse.getQuantityRequested() - initialResponse.getQuantityFulfilled() + 4); // Lógica de verificação coerente
    }
}