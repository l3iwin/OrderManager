package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StockMovementResponseDTO {
    private Long id;
    private LocalDateTime creationDate;
    private Long itemId;
    private String itemName;
    private int quantity;
    private int quantityAvailable;
    private List<AllocationResponseDTO> allocations;

    public StockMovementResponseDTO(Long id, LocalDateTime creationDate, Long itemId, String itemName,
                                    int quantity, int quantityAvailable, List<AllocationResponseDTO> allocations) {
        this.id = id;
        this.creationDate = creationDate;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
        this.allocations = allocations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public List<AllocationResponseDTO> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<AllocationResponseDTO> allocations) {
        this.allocations = allocations;
    }
}
