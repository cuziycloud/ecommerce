package com.tdtu.DesignPattern.Jeweluxe.state;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public class InProgressState implements OrderStatusState {

    @Override
    public void receiveOrder(OrderItem order) {
        // hợp lệ: chuyển sang trạng thái OrderReceived
        System.out.println("Đơn hàng " + order.getId() + " đã được nhận.");
        order.changeState(new OrderReceivedState());
    }

    @Override
    public void packOrder(OrderItem order) {
        throw new IllegalStateException("Không thể đóng gói đơn hàng đang ở trạng thái 'In Progress'.");
    }

    @Override
    public void shipOrder(OrderItem order) {
        throw new IllegalStateException("Không thể gửi đơn hàng đang ở trạng thái 'In Progress'.");
    }

    @Override
    public void deliverOrder(OrderItem order) {
        throw new IllegalStateException("Không thể giao đơn hàng đang ở trạng thái 'In Progress'.");
    }

    @Override
    public void cancelOrder(OrderItem order) {
        // Hợp lệ: chuyển sang trạng thái Cancelled
        System.out.println("Đơn hàng " + order.getId() + " đã bị hủy.");
        order.changeState(new CancelledState());
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.IN_PROGRESS;
    }
}