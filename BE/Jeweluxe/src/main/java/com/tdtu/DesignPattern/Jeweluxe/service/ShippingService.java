package com.tdtu.DesignPattern.Jeweluxe.service;

import com.tdtu.DesignPattern.Jeweluxe.strategy.shipping.ShippingCalculationResult;

//tính toán phí vận chuyển
public interface ShippingService {
    ShippingCalculationResult calculateShipping(double subTotal);
}