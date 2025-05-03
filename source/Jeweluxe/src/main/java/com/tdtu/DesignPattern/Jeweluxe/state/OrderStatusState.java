package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public interface OrderStatusState {

    void receiveOrder(OrderItem order); // Khi đơn hàng được xác nhận/nhận
    void packOrder(OrderItem order);    // Khi đơn hàng được đóng gói
    void shipOrder(OrderItem order);    // Khi đơn hàng được gửi đi
    void deliverOrder(OrderItem order); // Khi đơn hàng được giao thành công
    void cancelOrder(OrderItem order);  // Khi đơn hàng bị hủy

    OrderStatus getStatus();
}