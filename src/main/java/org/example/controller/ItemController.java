package org.example.controller;

import org.example.dto.ItemRequestDTO;
import org.example.dto.ItemResponseDTO;
import org.example.entity.Item;
import org.example.repository.ItemRepository;
import org.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO created = itemService.createItem(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<ItemResponseDTO> listAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable Long id) {
        return itemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ItemRequestDTO dto) {
        try {
            ItemResponseDTO updated = itemService.updateItem(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = itemService.deleteItem(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
