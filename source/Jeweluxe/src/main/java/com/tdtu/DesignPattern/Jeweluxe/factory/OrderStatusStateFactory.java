package com.tdtu.DesignPattern.Jeweluxe.factory;

import com.tdtu.DesignPattern.Jeweluxe.state.CancelledState;
import com.tdtu.DesignPattern.Jeweluxe.state.DeliveredState;
import com.tdtu.DesignPattern.Jeweluxe.state.InProgressState;
import com.tdtu.DesignPattern.Jeweluxe.state.OrderReceivedState;
import com.tdtu.DesignPattern.Jeweluxe.state.OrderStatusState; 
import com.tdtu.DesignPattern.Jeweluxe.state.ProductPackedState;
import com.tdtu.DesignPattern.Jeweluxe.state.ShippedState;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

// Factory Class: Chịu trách nhiệm tạo các instance OrderStatusState
public class OrderStatusStateFactory {

    //Tạo đối tượng State dựa trên Enum OrderStatus/ Đây là điểm tạo đối tượng tập trung
    public static OrderStatusState createState(OrderStatus statusEnum) {
        if (statusEnum == null) {
            return null;
        }

        switch (statusEnum) {
            case IN_PROGRESS:
                return new InProgressState();
            case ORDER_RECEIVED:
                return new OrderReceivedState();
            case PRODUCT_PACKED:
                return new ProductPackedState();
            case OUT_FOR_DELIVERY: 
                return new ShippedState();
            case DELIVERED:
                return new DeliveredState();
            case CANCELLED:
                return new CancelledState();
            default:
                throw new IllegalArgumentException("Trạng thái chưa được hỗ trợ: " + statusEnum);
        }
    }
}