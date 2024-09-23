package com.ordermanager.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ordermanager.api.entity.Item;
import com.ordermanager.api.entity.Order;
import com.ordermanager.api.repository.OrderRepository;
import com.ordermanager.api.utils.LoggingUtil;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class OrderService {

    @Inject
    private OrderRepository orderRepository;
    @Inject
    private StockService stockService;
    @Inject
    private EmailNotificationService emailNotificationService;

    public void createOrder(Order order) {
        order.setCreationDate(LocalDate.now());
        orderRepository.create(order);
        fulfillOrder(order);
    }

    public void fulfillOrdersByItem(Item item){
        List<Order> orderToFulfill = orderRepository.findAllOrderByIsNotCompleteAndItemOrderByCreationDateDesc(item);
        orderToFulfill.forEach(order -> this.fulfillOrder(order));
    }

    public void fulfillOrder(Order order) {
        int availableStock = stockService.getAvailableStockForItem(order.getItem());

        if (availableStock >= order.getQuantity()) {
            order.setComplete(true);
            stockService.reduceStock(order.getItem(), order.getQuantity());
            orderRepository.update(order);
            emailNotificationService.orderComplete(order.getUser());
            LoggingUtil.orderComplete(order);
        }
    }

    public Order getOrder(Long id) {
        Order order = orderRepository.findById(id);
        if(order != null) {
        	return order;
        } else {
        	return new Order();
        }
    }

    public void updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getId());
        if(order != null) {
        	existingOrder.setQuantity(order.getQuantity());
            existingOrder.setItem(order.getItem());
            existingOrder.setUser(order.getUser());
            orderRepository.update(existingOrder);
            fulfillOrder(existingOrder);
        } else {
        	LoggingUtil.logError("Order with ID " + order.getId() + " not found for update.", null);
        }
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id);
        if(order != null) {
        	orderRepository.delete(order);
        } else {
        	LoggingUtil.logError("Order with ID " + id + " not found for deletion.", null);
        }
    }

}

