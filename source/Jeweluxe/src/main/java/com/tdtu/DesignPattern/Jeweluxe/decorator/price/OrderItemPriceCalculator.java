package com.tdtu.DesignPattern.Jeweluxe.decorator.price;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;

//component Interface: tính giá OrderItem.
public interface OrderItemPriceCalculator {

    double calculatePrice(OrderItem item);

    String getDescription(OrderItem item);
}