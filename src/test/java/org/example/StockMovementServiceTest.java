package org.example;

import org.example.entity.Item;
import org.example.entity.StockMovement;
import org.example.repository.ItemRepository;
import org.example.repository.OrderAllocationRepository;
import org.example.repository.OrderRepository;
import org.example.repository.StockMovementRepository;
import org.example.service.NotificationService;
import org.example.service.StockMovementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockMovementServiceTest {

    @Mock
    private StockMovementRepository stockMovementRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderAllocationRepository allocationRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private StockMovementService stockMovementService;

    @Test
    public void testCreateStockMovementSuccessfully() {
        Item item = new Item("Teclado Mecânico");
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(stockMovementRepository.save(any(StockMovement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.findPendingByItemId(1L)).thenReturn(Collections.emptyList());

        StockMovement movement = stockMovementService.createStockMovement(1L, 20);

        assertNotNull(movement);
        assertEquals(20, movement.getQuantity());
        assertEquals(20, movement.getQuantityAvailable());
        assertEquals(item, movement.getItem());

        verify(itemRepository, times(1)).findById(1L);
        verify(stockMovementRepository, atLeastOnce()).save(any(StockMovement.class));
    }

    @Test
    public void testCreateStockMovementItemNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockMovementService.createStockMovement(99L, 10);
        });

        assertEquals("Item não encontrado", exception.getMessage());
        verify(stockMovementRepository, never()).save(any());
    }

    @Test
    public void testCreateStockMovementInvalidQuantity() {
        Item item = new Item("Mouse");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockMovementService.createStockMovement(1L, 0);
        });

        assertEquals("A quantidade deve ser superior a zero", exception.getMessage());
    }
}