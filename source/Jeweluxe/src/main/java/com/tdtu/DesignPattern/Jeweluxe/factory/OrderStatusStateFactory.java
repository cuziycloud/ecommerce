package com.tdtu.DesignPattern.Jeweluxe.factory;

import com.tdtu.DesignPattern.Jeweluxe.state.*;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;
import org.springframework.stereotype.Component; 

@Component 
public class OrderStatusStateFactory implements OrderStatusStateCreator {

    @Override
    public OrderStatusState createStatusState(OrderStatus statusEnum) {
        if (statusEnum == null) {
            throw new IllegalArgumentException("OrderStatus enum ko được null để tạo State.");
        }
        switch (statusEnum) {
            case IN_PROGRESS: return new InProgressState();
            case ORDER_RECEIVED: return new OrderReceivedState();
            case PRODUCT_PACKED: return new ProductPackedState();
            case OUT_FOR_DELIVERY: return new ShippedState();
            case DELIVERED: return new DeliveredState();
            case CANCELLED: return new CancelledState();
            default:
                throw new IllegalArgumentException("Trạng thái chưa được hỗ trợ: " + statusEnum);
        }
    }
}