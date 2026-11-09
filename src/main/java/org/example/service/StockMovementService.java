package org.example.service;

import org.example.dto.AllocationResponseDTO;
import org.example.dto.StockMovementRequestDTO;
import org.example.dto.StockMovementResponseDTO;
import org.example.dto.StockMovementUpdateDTO;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderAllocationRepository allocationRepository;
    private final NotificationService notificationService;

    public StockMovementService(StockMovementRepository stockMovementRepository, ItemRepository itemRepository,
                                OrderRepository orderRepository, OrderAllocationRepository allocationRepository,
                                NotificationService notificationService) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.allocationRepository = allocationRepository;
        this.notificationService = notificationService;
    }

    public StockMovementResponseDTO toResponseDTO(StockMovement movement) {
        List<AllocationResponseDTO> allocationDTOs = movement.getAllocations().stream()
                .map(a -> new AllocationResponseDTO(a.getOrder().getId(), a.getQuantityAllocated()))
                .collect(Collectors.toList());

        return new StockMovementResponseDTO(
                movement.getId(),
                movement.getCreationDate(),
                movement.getItem().getId(),
                movement.getItem().getName(),
                movement.getQuantity(),
                movement.getQuantityAvailable(),
                allocationDTOs
        );
    }

    @Transactional
    public StockMovementResponseDTO createStockMovement(StockMovementRequestDTO dto) {
        // 1. Bloqueia o Item em exclusivo (Pessimistic Lock no Agregador) para prevenir race conditions
        Item item = itemRepository.findByIdWithLock(dto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com o ID: " + dto.getItemId()));

        StockMovement movement = new StockMovement(item, dto.getQuantity());
        movement = stockMovementRepository.save(movement);

        // 2. Tenta satisfazer encomendas pendentes com o novo stock
        tryToAllocateStockToPendingOrders(movement);
        movement = stockMovementRepository.save(movement);

        return toResponseDTO(movement);
    }

    private void tryToAllocateStockToPendingOrders(StockMovement movement) {
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
                notificationService.triggerEmailAfterCommit(order);
            }
        }
    }

    public List<StockMovementResponseDTO> findAll() {
        return stockMovementRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<StockMovementResponseDTO> findById(Long id) {
        return stockMovementRepository.findById(id)
                .map(this::toResponseDTO);
    }

    @Transactional
    public StockMovementResponseDTO updateStockMovement(Long id, StockMovementUpdateDTO dto) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimento de stock não encontrado com o ID: " + id));

        int alreadyAllocated = movement.getQuantity() - movement.getQuantityAvailable();

        if (dto.getQuantity() < alreadyAllocated) {
            throw new IllegalArgumentException("A nova quantidade não pode ser inferior ao stock que já foi alocado (" + alreadyAllocated + ").");
        }

        int diff = dto.getQuantity() - movement.getQuantity();
        movement.setQuantity(dto.getQuantity());
        movement.setQuantityAvailable(movement.getQuantityAvailable() + diff);

        movement = stockMovementRepository.save(movement);
        return toResponseDTO(movement);
    }

    @Transactional
    public boolean deleteStockMovement(Long id) {
        if (!stockMovementRepository.existsById(id)) {
            return false;
        }
        stockMovementRepository.deleteById(id);
        return true;
    }
}
