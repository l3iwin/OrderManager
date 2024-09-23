package com.ordermanager.api.service;

import com.ordermanager.api.entity.Item;
import com.ordermanager.api.repository.ItemRepository;
import com.ordermanager.api.utils.LoggingUtil;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ItemService {

    @Inject
    private ItemRepository itemRepository;
    @Inject
    private StockService stockService;
    @Inject
    private EmailNotificationService emailNotificationService;

    public void createItem(Item item) {
        itemRepository.create(item);
    }

    public Item getItem(Long id) {
        Item item = itemRepository.findById(id);
        if(item != null) {
        	return item;
        } else {
        	return new Item();
        }
    }

    public void updateItem(Item item) {
        Item existingItem = itemRepository.findById(item.getId());
        if(existingItem != null) {
        	existingItem.setName(item.getName());
            itemRepository.update(existingItem);
        } else {
        	LoggingUtil.logError("Item with ID " + item.getId() + " not found for update.", null);
        }
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id);
        if(item != null) {
        	itemRepository.delete(item);
        } else {
        	LoggingUtil.logError("Item with ID " + id + " not found for deletion.", null);
        }
    }

}

