package org.example.service;

import org.example.dto.OrderResponseDTO;
import org.example.entity.Order;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class OrderRetryService {
    private final OrderService orderService;

    public OrderRetryService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Retryable(
            value = { CannotAcquireLockException.class, PessimisticLockException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 2)
    )
    public OrderResponseDTO createOrderWithRetry(Long itemId, int quantity, Long userId) {
        return orderService.createOrder(itemId, quantity, userId);
    }
}
