package com.tdtu.DesignPattern.Jeweluxe.event;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.List;

// Đóng gói thông tin về trạng thái saveOrder r lan tin cho các Listener
public class OrderCreatedEvent extends ApplicationEvent {

    private final List<OrderItem> savedOrders;

    public OrderCreatedEvent(Object source, List<OrderItem> savedOrders) {
        super(source); //OrderServiceImpl
        this.savedOrders = (savedOrders != null) ? List.copyOf(savedOrders) : Collections.emptyList();
    }

    public List<OrderItem> getSavedOrders() { // Cách listener lấy dữ liệu
        return savedOrders;
    }     
}