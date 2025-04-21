package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public class ShippedState implements OrderStatusState {

    @Override
    public void receiveOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã được gửi đi rồi.");
    }

    @Override
    public void packOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã được gửi đi rồi.");
    }

    @Override
    public void shipOrder(OrderItem order) {
        System.out.println("Đơn hàng đã được gửi đi rồi."); 
        // throw
    }

    @Override
    public void deliverOrder(OrderItem order) {
        // Hợp lệ: Chuyển sang Delivered
        System.out.println("Đơn hàng " + order.getId() + " đã được giao.");
        order.changeState(new DeliveredState());
    }

    @Override
    public void cancelOrder(OrderItem order) {
        throw new IllegalStateException("Không thể hủy đơn hàng đã được gửi đi.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.OUT_FOR_DELIVERY; 
    }
}