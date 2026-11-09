package org.example.service;

import org.example.entity.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final StockMovementRepository stockMovementRepository;
    private final OrderAllocationRepository allocationRepository;
    private final NotificationService notificationService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository,
                        UserRepository userRepository, StockMovementRepository stockMovementRepository,
                        OrderAllocationRepository allocationRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.allocationRepository = allocationRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Order createOrder(Long itemId, int quantity, Long userId) {
        Item item = itemRepository.findByIdWithLock(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser superior a zero");
        }

        Order order = new Order(item, quantity, user);
        order = orderRepository.save(order);

        fulfillOrderWithAvailableStock(order);
        return orderRepository.save(order);
    }

    @Transactional
    public void fulfillOrderWithAvailableStock(Order order) {
        List<StockMovement> movements = stockMovementRepository.findAvailableByItemIdWithLock(order.getItem().getId());

        for (StockMovement movement : movements) {
            if (order.isCompleted()) break;

            int needed = order.getQuantityRequested() - order.getQuantityFulfilled();
            int availableInStock = movement.getQuantityAvailable();

            if (availableInStock > 0 && needed > 0) {
                int toAllocate = Math.min(needed, availableInStock);

                movement.setQuantityAvailable(availableInStock - toAllocate);
                order.setQuantityFulfilled(order.getQuantityFulfilled() + toAllocate);

                OrderAllocation allocation = new OrderAllocation(order, movement, toAllocate);
                allocationRepository.save(allocation);
                stockMovementRepository.save(movement);

                if (order.getQuantityFulfilled() >= order.getQuantityRequested()) {
                    order.setCompleted(true);
                    notificationService.sendOrderCompletedEmail(order);
                }
            }
        }
    }

    public List<Order> findAll() { return orderRepository.findAll(); }
    public Order findById(Long id) { return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada")); }

    @Transactional
    public void deleteOrder(Long id) { orderRepository.deleteById(id); }
}
