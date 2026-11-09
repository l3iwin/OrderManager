package org.example.dto;

import javax.validation.constraints.Min;

public class OrderUpdateDTO {
    private int quantityRequested;

    public OrderUpdateDTO(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityRequested() { return quantityRequested; }
    public void setQuantityRequested(int quantityRequested) { this.quantityRequested = quantityRequested; }
}
