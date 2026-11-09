package org.example.dto;

public class AllocationResponseDTO {
    private Long stockMovementId;
    private int quantityAllocated;

    public AllocationResponseDTO(Long stockMovementId, int quantityAllocated) {
        this.stockMovementId = stockMovementId;
        this.quantityAllocated = quantityAllocated;
    }

    public Long getStockMovementId() { return stockMovementId; }
    public int getQuantityAllocated() { return quantityAllocated; }
}
