package org.example.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderRequestDTO {
    private Long itemId;
    private int quantity;
    private Long userId;

    public OrderRequestDTO() {}

    public OrderRequestDTO(Long itemId, int quantity, Long userId) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
