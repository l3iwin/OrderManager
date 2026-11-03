package org.example.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderRequestDTO {
    @NotNull()
    private Long itemId;

    @Min(value = 1)
    private int quantity;

    @NotNull()
    private Long userId;

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
