package com.tdtu.DesignPattern.Jeweluxe.strategy.shipping;

import org.springframework.stereotype.Component;

/**
 * Concrete Strategy: phí vận chuyển tiêu chuẩn theo giá trị đơn hàng.
 * Chỉ xử lý các trường hợp CHƯA đủ điều kiện miễn phí.
 */
@Component
public class StandardShippingStrategy implements ShippingStrategy {

    private static final double THRESHOLD_MEDIUM = 500000;

    private static final double FEE_LOW = 30000;    // Phí cho đơn < 500k
    private static final double FEE_MEDIUM = 15000; // Phí cho đơn >= 500k (chưa free)

    @Override
    public ShippingCalculationResult calculate(double subTotal) {
        double fee;
        String description;

        if (subTotal < THRESHOLD_MEDIUM) {
            fee = FEE_LOW;
            description = "Standard Shipping";
        } else {
            // THRESHOLD_MEDIUM <= subTotal < FREE_SHIPPING_THRESHOLD (1500000 -  đã check vế 2 trong ShippingServiceImpl)
            fee = FEE_MEDIUM;
            description = "Reduced Shipping";
        }
        return new ShippingCalculationResult(fee, description);
    }
}