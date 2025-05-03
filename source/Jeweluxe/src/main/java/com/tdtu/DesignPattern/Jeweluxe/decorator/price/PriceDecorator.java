package com.tdtu.DesignPattern.Jeweluxe.decorator.price;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;

/**
 * Abstract Decorator: Lớp cơ sở cho tất cả các decorator tính giá.
 * Chứa tham chiếu đến đối tượng được bọc và ủy quyền các lời gọi.
 */
public abstract class PriceDecorator implements OrderItemPriceCalculator {
    protected OrderItemPriceCalculator decoratedCalculator;

    public PriceDecorator(OrderItemPriceCalculator calculator) {
        if (calculator == null) {
            throw new IllegalArgumentException("Decorated calculator cannot be null");
        }
        this.decoratedCalculator = calculator;
    }

    @Override
    public double calculatePrice(OrderItem item) {
        return decoratedCalculator.calculatePrice(item);
    }

    @Override
    public String getDescription(OrderItem item) {
        return decoratedCalculator.getDescription(item);
    }
}