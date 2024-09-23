package com.ordermanager.api.service;

import com.ordermanager.api.entity.User;
import com.ordermanager.api.utils.LoggingUtil;

public class EmailNotificationService {
    public void orderComplete(User user) {
        System.out.println("Sending order completion email to " + user.getEmail());
        LoggingUtil.orderCompleteUser(user);
    }
}
