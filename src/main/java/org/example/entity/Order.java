package org.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantityRequested;

    @Column(nullable = false)
    private int quantityFulfilled;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean completed;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderAllocation> allocations = new ArrayList<>();

    @Version
    private Long version;

    public Order() {
        this.creationDate = LocalDateTime.now();
        this.completed = false;
        this.quantityFulfilled = 0;
    }

    public Order(Item item, int quantityRequested, User user) {
        this();
        this.item = item;
        this.quantityRequested = quantityRequested;
        this.user = user;
    }

    public double getCompletionPercentage() {
        if (quantityRequested == 0) return 100.0;
        return ((double) quantityFulfilled / quantityRequested) * 100.0;
    }

    public Long getId() { return id; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Item getItem() { return item; }
    public int getQuantityRequested() { return quantityRequested; }
    public int getQuantityFulfilled() { return quantityFulfilled; }
    public void setQuantityFulfilled(int quantityFulfilled) { this.quantityFulfilled = quantityFulfilled; }
    public User getUser() { return user; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public List<OrderAllocation> getAllocations() { return allocations; }
}
