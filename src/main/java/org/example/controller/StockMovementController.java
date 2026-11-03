package org.example.controller;

import org.example.dto.StockMovementRequestDTO;
import org.example.entity.StockMovement;
import org.example.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    public ResponseEntity<StockMovement> create(@Valid @RequestBody StockMovementRequestDTO dto) {
        StockMovement movement = stockMovementService.createStockMovement(dto.getItemId(), dto.getQuantity());
        return ResponseEntity.ok(movement);
    }

    @GetMapping
    public List<StockMovement> listAll() {
        return stockMovementService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(stockMovementService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
