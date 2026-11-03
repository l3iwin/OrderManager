package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "order_allocations")
public class OrderAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stock_movement_id")
    private StockMovement stockMovement;

    @Column(nullable = false)
    private int quantityAllocated;

    public OrderAllocation() {}

    public OrderAllocation(Order order, StockMovement stockMovement, int quantityAllocated) {
        this.order = order;
        this.stockMovement = stockMovement;
        this.quantityAllocated = quantityAllocated;
    }

    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public StockMovement getStockMovement() { return stockMovement; }
    public int getQuantityAllocated() { return quantityAllocated; }
}
