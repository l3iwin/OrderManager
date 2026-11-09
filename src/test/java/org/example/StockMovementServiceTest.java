package org.example;

import org.example.dto.StockMovementRequestDTO;
import org.example.dto.StockMovementResponseDTO;
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
        // 1. Preparar dados: criar e persistir um Item base
        Item item = itemRepository.save(new Item("MacBook Pro M3"));

        // 2. Construir o Request DTO para a entrada de stock
        StockMovementRequestDTO requestDTO = new StockMovementRequestDTO();
        requestDTO.setItemId(item.getId());
        requestDTO.setQuantity(50);

        // 3. Executar a ação através do serviço (que processa e devolve o Response DTO)
        StockMovementResponseDTO responseDTO = stockMovementService.createStockMovement(requestDTO);

        // 4. Validação rigorosa baseada no Response DTO
        assertNotNull(responseDTO, "O DTO de resposta não deve ser nulo");
        assertNotNull(responseDTO.getId(), "O ID gerado do movimento de stock não deve ser nulo");
        assertEquals(item.getId(), responseDTO.getItemId(), "O ID do item deve coincidir");
        assertEquals("MacBook Pro M3", responseDTO.getItemName(), "O nome do item deve coincidir");
        assertEquals(50, responseDTO.getQuantity(), "A quantidade total deve ser 50");
        assertEquals(50, responseDTO.getQuantityAvailable(), "A quantidade disponível inicial deve ser 50");
        assertTrue(responseDTO.getAllocations().isEmpty(), "Como não existem encomendas pendentes, as alocações devem estar vazias");
    }

    @Test
    public void testCreateStockMovementItemNotFound() {
        StockMovementRequestDTO dto = new StockMovementRequestDTO();
        dto.setItemId(9999L); // ID que não existe
        dto.setQuantity(10);

        assertThrows(IllegalArgumentException.class, () -> {
            stockMovementService.createStockMovement(dto);
        }, "Deve lançar exceção quando o item não é encontrado");
    }
}