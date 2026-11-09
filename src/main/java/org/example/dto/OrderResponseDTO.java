package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;


public class OrderResponseDTO {
    private Long id;
    private LocalDateTime creationDate;
    private Long itemId;
    private String itemName;
    private Long userId;
    private String userName;
    private int quantityRequested;
    private int quantityFulfilled;
    private boolean completed;
    private double completionPercentage;
    private List<AllocationResponseDTO> allocations;

    public OrderResponseDTO(Long id, LocalDateTime creationDate, Long itemId, String itemName,
                            Long userId, String userName, int quantityRequested, int quantityFulfilled,
                            boolean completed, double completionPercentage, List<AllocationResponseDTO> allocations) {
        this.id = id;
        this.creationDate = creationDate;
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
        this.userName = userName;
        this.quantityRequested = quantityRequested;
        this.quantityFulfilled = quantityFulfilled;
        this.completed = completed;
        this.completionPercentage = completionPercentage;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityFulfilled() {
        return quantityFulfilled;
    }

    public void setQuantityFulfilled(int quantityFulfilled) {
        this.quantityFulfilled = quantityFulfilled;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<AllocationResponseDTO> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<AllocationResponseDTO> allocations) {
        this.allocations = allocations;
    }
}