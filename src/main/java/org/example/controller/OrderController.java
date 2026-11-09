package org.example.controller;

import org.example.dto.OrderRequestDTO;
import org.example.dto.OrderResponseDTO;
import org.example.dto.OrderUpdateDTO;
import org.example.entity.Order;
import org.example.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO createdOrder = orderService.createOrder(dto.getItemId(), dto.getQuantity(), dto.getUserId());
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public List<OrderResponseDTO> listAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(@PathVariable Long id, @Valid @RequestBody OrderUpdateDTO dto) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrder(id, dto);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
