package com.ordermanager.api.utils;

import java.util.logging.Logger;

import com.ordermanager.api.entity.Order;
import com.ordermanager.api.entity.Stock;
import com.ordermanager.api.entity.User;

public class LoggingUtil {
    private static final Logger logger = Logger.getLogger("LoggingUtil");

    public static void orderComplete(Order order) {
        logger.info("Order " + order.getId() + " completed.");
    }

    public static void stockMovement(Stock stock) {
        logger.info("Stock movement for item " + stock.getItem().getName() + " with quantity " + stock.getQuantity());
    }

    public static void orderCompleteUser(User user) {
        logger.info("Order has completed. Email sent to " + user.getEmail());
    }

    public static void logError(String message, Exception e) {
        logger.severe(e.getCause().toString());
    }
}
