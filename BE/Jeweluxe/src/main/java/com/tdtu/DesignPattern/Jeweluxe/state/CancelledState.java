package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public class CancelledState implements OrderStatusState {

    @Override
    public void receiveOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã bị hủy.");
    }

    @Override
    public void packOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã bị hủy.");
    }

    @Override
    public void shipOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã bị hủy.");
    }

    @Override
    public void deliverOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã bị hủy.");
    }

    @Override
    public void cancelOrder(OrderItem order) {
        System.out.println("Đơn hàng đã bị hủy."); 
        // throw new IllegalStateException("Đơn hàng đã bị hủy rồi.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CANCELLED; 
    }
}