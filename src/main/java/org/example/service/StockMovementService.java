package org.example.service;

import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.OrderAllocation;
import org.example.entity.StockMovement;
import org.example.repository.ItemRepository;
import org.example.repository.OrderAllocationRepository;
import org.example.repository.OrderRepository;
import org.example.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderAllocationRepository allocationRepository;
    private final NotificationService notificationService;

    @Autowired
    public StockMovementService(StockMovementRepository stockMovementRepository, ItemRepository itemRepository,
                                OrderRepository orderRepository, OrderAllocationRepository allocationRepository,
                                NotificationService notificationService) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.allocationRepository = allocationRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public StockMovement createStockMovement(Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser superior a zero");
        }

        StockMovement movement = new StockMovement(item, quantity);
        movement = stockMovementRepository.save(movement);

        tryToAllocateStockToPendingOrders(movement);
        return stockMovementRepository.save(movement);
    }

    @Transactional
    public void tryToAllocateStockToPendingOrders(StockMovement movement) {
        List<Order> pendingOrders = orderRepository.findPendingByItemIdWithLock(movement.getItem().getId());

        for (Order order : pendingOrders) {
            if (movement.getQuantityAvailable() <= 0) break;

            int needed = order.getQuantityRequested() - order.getQuantityFulfilled();
            int availableInStock = movement.getQuantityAvailable();

            int toAllocate = Math.min(needed, availableInStock);

            movement.setQuantityAvailable(availableInStock - toAllocate);
            order.setQuantityFulfilled(order.getQuantityFulfilled() + toAllocate);

            OrderAllocation allocation = new OrderAllocation(order, movement, toAllocate);
            allocationRepository.save(allocation);
            orderRepository.save(order);

            if (order.getQuantityFulfilled() >= order.getQuantityRequested()) {
                order.setCompleted(true);
                orderRepository.save(order);
                notificationService.sendOrderCompletedEmail(order);
            }
        }
    }

    public List<StockMovement> findAll() {
        return stockMovementRepository.findAll();
    }

    public StockMovement findById(Long id) {
        return stockMovementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movimento não encontrado"));
    }
}
