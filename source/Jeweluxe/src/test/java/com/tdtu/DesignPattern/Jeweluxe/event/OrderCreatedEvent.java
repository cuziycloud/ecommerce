package com.tdtu.DesignPattern.Jeweluxe.event;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import org.springframework.context.ApplicationEvent;
import java.util.Collections;
import java.util.List;

public class OrderCreatedEvent extends ApplicationEvent {

    private final List<OrderItem> savedOrders;

    public OrderCreatedEvent(Object source, List<OrderItem> savedOrders) {
        super(source);
        this.savedOrders = (savedOrders != null) ? List.copyOf(savedOrders) : Collections.emptyList();
    }

    // Phương thức để Listener lấy dữ liệu
    public List<OrderItem> getSavedOrders() {
        return savedOrders;
    }
}