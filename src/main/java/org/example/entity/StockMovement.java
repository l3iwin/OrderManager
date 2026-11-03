package org.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_movements")
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int quantityAvailable;

    @OneToMany(mappedBy = "stockMovement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderAllocation> allocations = new ArrayList<>();

    @Version
    private Long version;

    public StockMovement() {
        this.creationDate = LocalDateTime.now();
    }

    public StockMovement(Item item, int quantity) {
        this.creationDate = LocalDateTime.now();
        this.item = item;
        this.quantity = quantity;
        this.quantityAvailable = quantity;
    }

    public Long getId() { return id; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
    public int getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(int quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    public List<OrderAllocation> getAllocations() { return allocations; }
}
