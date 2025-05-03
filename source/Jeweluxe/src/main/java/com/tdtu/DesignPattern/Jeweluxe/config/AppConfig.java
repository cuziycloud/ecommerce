package com.tdtu.DesignPattern.Jeweluxe.config;

import com.tdtu.DesignPattern.Jeweluxe.factory.OrderStatusStateCreator;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private OrderStatusStateCreator stateCreator;

    @PostConstruct
    public void initializeStaticFactory() {
        OrderItem.setStateCreator(stateCreator);
    }
}