package org.example.service;

import org.example.dto.ItemRequestDTO;
import org.example.dto.ItemResponseDTO;
import org.example.entity.Item;
import org.example.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponseDTO toResponseDTO(Item item) {
        return new ItemResponseDTO(item.getId(), item.getName());
    }

    @Transactional
    public ItemResponseDTO createItem(ItemRequestDTO dto) {
        Item item = new Item(dto.getName());
        Item saved = itemRepository.save(item);
        return toResponseDTO(saved);
    }

    public List<ItemResponseDTO> findAll() {
        return itemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ItemResponseDTO> findById(Long id) {
        return itemRepository.findById(id)
                .map(this::toResponseDTO);
    }

    @Transactional
    public ItemResponseDTO updateItem(Long id, ItemRequestDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com o ID: " + id));

        item.setName(dto.getName());
        Item updated = itemRepository.save(item);
        return toResponseDTO(updated);
    }

    @Transactional
    public boolean deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            return false;
        }
        itemRepository.deleteById(id);
        return true;
    }
}
