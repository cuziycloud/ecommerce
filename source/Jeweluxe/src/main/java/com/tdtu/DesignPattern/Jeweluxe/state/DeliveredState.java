package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public class DeliveredState implements OrderStatusState {

    @Override
    public void receiveOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã được giao.");
    }

    @Override
    public void packOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã được giao.");
    }

    @Override
    public void shipOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã được giao.");
    }

    @Override
    public void deliverOrder(OrderItem order) {
        System.out.println("Đơn hàng đã được giao.");
        // throw new IllegalStateException("Đơn hàng đã được giao.");
    }

    @Override
    public void cancelOrder(OrderItem order) {
        throw new IllegalStateException("Không thể hủy đơn hàng đã được giao thành công.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DELIVERED;
    }
}