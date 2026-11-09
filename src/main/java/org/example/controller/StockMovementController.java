package org.example.controller;

import org.example.dto.StockMovementRequestDTO;
import org.example.dto.StockMovementResponseDTO;
import org.example.dto.StockMovementUpdateDTO;
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
    public ResponseEntity<StockMovementResponseDTO> create(@Valid @RequestBody StockMovementRequestDTO dto) {
        StockMovementResponseDTO created = stockMovementService.createStockMovement(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<StockMovementResponseDTO> listAll() {
        return stockMovementService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> getById(@PathVariable Long id) {
        return stockMovementService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> update(@PathVariable Long id, @Valid @RequestBody StockMovementUpdateDTO dto) {
        try {
            StockMovementResponseDTO updated = stockMovementService.updateStockMovement(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = stockMovementService.deleteStockMovement(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
