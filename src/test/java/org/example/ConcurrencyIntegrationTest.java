package org.example;

import org.example.dto.OrderRequestDTO;
import org.example.dto.OrderResponseDTO;
import org.example.dto.StockMovementRequestDTO;
import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.repository.ItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.OrderRetryService;
import org.example.service.OrderService;
import org.example.service.StockMovementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrencyIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("order_manager_test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.datasource.hikari.connection-init-sql", () -> "SET lock_timeout = '5s'");
    }

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRetryService orderRetryService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testConcurrentOrderAndStockCreationOnRealPostgres() throws InterruptedException {
        Item item = itemRepository.save(new Item("Laptop Gamer Testcontainers"));
        User user = userRepository.save(new User("Ana Silva", "ana.silva@example.com"));

        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        executorService.submit(() -> {
            try {
                latch.await();

                orderRetryService.createOrderWithRetry(item.getId(), 10, user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() -> {
            try {
                latch.await();

                StockMovementRequestDTO stockDto = new StockMovementRequestDTO();
                stockDto.setItemId(item.getId());
                stockDto.setQuantity(10);

                stockMovementService.createStockMovement(stockDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        latch.countDown();
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(15, TimeUnit.SECONDS));

        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size(), "Deve existir exatamente 1 encomenda");
        assertTrue(orders.get(0).isCompleted(), "A encomenda deve estar concluída");
        assertEquals(10, orders.get(0).getQuantityFulfilled(), "A quantidade cumprida deve ser exatamente 10");
    }
}
