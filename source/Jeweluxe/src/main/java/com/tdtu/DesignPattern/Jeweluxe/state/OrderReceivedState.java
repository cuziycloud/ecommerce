package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public class OrderReceivedState implements OrderStatusState {

    @Override
    public void receiveOrder(OrderItem order) {
        System.out.println("Đơn hàng đã được nhận rồi.");
        //throw new IllegalStateException("Đơn hàng đã được nhận rồi.");
    }

    @Override
    public void packOrder(OrderItem order) {
        // Hợp lệ: chuyển sang trạng thái ProductPacked
        System.out.println("Đơn hàng " + order.getId() + " đang được đóng gói.");
        order.changeState(new ProductPackedState()); 
    }

    @Override
    public void shipOrder(OrderItem order) {
        throw new IllegalStateException("Không thể gửi đơn hàng chưa được đóng gói.");
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
        return OrderStatus.ORDER_RECEIVED;
    }
}