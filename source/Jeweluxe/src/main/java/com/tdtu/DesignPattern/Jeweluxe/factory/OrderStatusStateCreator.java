package com.tdtu.DesignPattern.Jeweluxe.factory;
import com.tdtu.DesignPattern.Jeweluxe.state.OrderStatusState;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

public interface OrderStatusStateCreator {
    OrderStatusState createStatusState(OrderStatus status);
}