package org.example.service;

import org.example.dto.AllocationResponseDTO;
import org.example.dto.OrderResponseDTO;
import org.example.dto.OrderUpdateDTO;
import org.example.entity.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final StockMovementRepository stockMovementRepository;
    private final OrderAllocationRepository allocationRepository;
    private final NotificationService notificationService;

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

    public OrderResponseDTO toResponseDTO(Order order) {
        List<AllocationResponseDTO> allocationDTOs = order.getAllocations().stream()
                .map(a -> new AllocationResponseDTO(a.getStockMovement().getId(), a.getQuantityAllocated()))
                .collect(Collectors.toList());

        double percentage = order.getQuantityRequested() > 0
                ? ((double) order.getQuantityFulfilled() / order.getQuantityRequested()) * 100.0
                : 0.0;

        return new OrderResponseDTO(
                order.getId(),
                order.getCreationDate(),
                order.getItem().getId(),
                order.getItem().getName(),
                order.getUser().getId(),
                order.getUser().getName(),
                order.getQuantityRequested(),
                order.getQuantityFulfilled(),
                order.isCompleted(),
                percentage,
                allocationDTOs
        );
    }

    @Transactional
    public OrderResponseDTO createOrder(Long itemId, int quantity, Long userId) {
        // 1. Bloqueia o Item em exclusivo (Pessimistic Lock no Agregador)
        Item item = itemRepository.findByIdWithLock(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com o ID: " + itemId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado com o ID: " + userId));

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser superior a zero");
        }

        Order order = new Order(item, quantity, user);
        order = orderRepository.save(order);

        fulfillOrderWithAvailableStock(order);
        order = orderRepository.save(order);

        return toResponseDTO(order);
    }

    private void fulfillOrderWithAvailableStock(Order order) {
        List<StockMovement> availableMovements = stockMovementRepository.findAvailableByItemIdWithLock(order.getItem().getId());

        for (StockMovement movement : availableMovements) {
            if (order.getQuantityFulfilled() >= order.getQuantityRequested()) break;

            int needed = order.getQuantityRequested() - order.getQuantityFulfilled();
            int availableInStock = movement.getQuantityAvailable();

            int toAllocate = Math.min(needed, availableInStock);

            movement.setQuantityAvailable(availableInStock - toAllocate);
            order.setQuantityFulfilled(order.getQuantityFulfilled() + toAllocate);

            OrderAllocation allocation = new OrderAllocation(order, movement, toAllocate);
            allocationRepository.save(allocation);
            stockMovementRepository.save(movement);

            if (order.getQuantityFulfilled() >= order.getQuantityRequested()) {
                order.setCompleted(true);
                orderRepository.save(order);
                notificationService.triggerEmailAfterCommit(order);
            }
        }
    }

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponseDTO> findById(Long id) {
        return orderRepository.findById(id).map(this::toResponseDTO);
    }

    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderUpdateDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada com o ID: " + id));

        if (order.isCompleted() || order.getQuantityFulfilled() > 0) {
            throw new IllegalStateException("Não é possível alterar uma encomenda que já possui alocações ou está concluída.");
        }

        order.setQuantityRequested(dto.getQuantityRequested());
        order = orderRepository.save(order);

        fulfillOrderWithAvailableStock(order);
        order = orderRepository.save(order);

        return toResponseDTO(order);
    }

    @Transactional
    public boolean deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }
}
