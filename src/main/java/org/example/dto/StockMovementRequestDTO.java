package org.example.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StockMovementRequestDTO {
    @NotNull()
    private Long itemId;

    @Min(value = 1)
    private int quantity;

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
