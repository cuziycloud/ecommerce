package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public class ProductPackedState implements OrderStatusState {

    @Override
    public void receiveOrder(OrderItem order) {
        throw new IllegalStateException("Đơn hàng đã được đóng gói.");
    }

    @Override
    public void packOrder(OrderItem order) {
        System.out.println("Đơn hàng đã được đóng gói."); 
        // throw new IllegalStateException("Đơn hàng đã được đóng gói.");
    }

    @Override
    public void shipOrder(OrderItem order) {
        // Hợp lệ: chuyển sang trạng thái Shipped (hoặc Out for Delivery)
        System.out.println("Đơn hàng " + order.getId() + " đang được gửi đi.");
        order.changeState(new ShippedState()); 
    }

    @Override
    public void deliverOrder(OrderItem order) {
        throw new IllegalStateException("Không thể giao đơn hàng chưa được gửi đi.");
    }

    @Override
    public void cancelOrder(OrderItem order) {
        // Hợp lệ: chuyển sang trạng thái Cancelled
        System.out.println("Đơn hàng " + order.getId() + " đã bị hủy.");
        order.changeState(new CancelledState());
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PRODUCT_PACKED;
    }
}