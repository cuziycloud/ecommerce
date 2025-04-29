package com.tdtu.DesignPattern.Jeweluxe.strategy.shipping;

// Strategy Interface: phương thức chung để tính phí vận chuyển
public interface ShippingStrategy {

    ShippingCalculationResult calculate(double subTotal);
}