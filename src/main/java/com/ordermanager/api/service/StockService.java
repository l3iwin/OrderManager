package com.ordermanager.api.service;

import java.time.LocalDate;
import java.util.List;

import com.ordermanager.api.entity.Item;
import com.ordermanager.api.entity.Stock;
import com.ordermanager.api.repository.StockRepository;
import com.ordermanager.api.utils.LoggingUtil;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class StockService {

    @Inject
    private StockRepository stockRepository;
    @Inject
    private OrderService orderService;
    public void createStock(Stock stock) {
        stock.setCreationDate(LocalDate.now());
        stockRepository.create(stock);
        orderService.fulfillOrdersByItem(stock.getItem());
        LoggingUtil.stockMovement(stock);
    }

    public Stock getStock(Long id) {
        Stock stock = stockRepository.findById(id);
        if(stock != null) {
        	return stock;
        } else {
        	return new Stock();
        }
    }

    public void updateStock(Stock stock) {
        Stock existingStock = stockRepository.findById(stock.getId());
        if(existingStock != null) {
        	existingStock.setQuantity(stock.getQuantity());
            stockRepository.update(existingStock);
            orderService.fulfillOrdersByItem(stock.getItem());
            LoggingUtil.stockMovement(stock);
        } else {
        	LoggingUtil.logError("Stock with ID " + stock.getId() + " not found for update.", null);
        }
    }

    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id);
        if(stock != null) {
        	stockRepository.delete(stock);
            LoggingUtil.stockMovement(stock);
        } else {
        	LoggingUtil.logError("Stock with ID " + id + " not found for deletion.", null);
        }
    }

    public int getAvailableStockForItem(Item item){
        return stockRepository.findByItem(item).getQuantity();
    }

    public void reduceStock(Item item, int quantity){
        Stock stock = stockRepository.findByItem(item);
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.update(stock);
        LoggingUtil.stockMovement(stock);
    }

}

