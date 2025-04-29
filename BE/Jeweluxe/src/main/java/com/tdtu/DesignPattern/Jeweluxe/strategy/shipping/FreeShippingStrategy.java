package com.tdtu.DesignPattern.Jeweluxe.strategy.shipping;

import org.springframework.stereotype.Component;

// concrete Strategy: free ship
@Component
public class FreeShippingStrategy implements ShippingStrategy {

    @Override
    public ShippingCalculationResult calculate(double subTotal) {
        return new ShippingCalculationResult(0, "Free Shipping");
    }
}