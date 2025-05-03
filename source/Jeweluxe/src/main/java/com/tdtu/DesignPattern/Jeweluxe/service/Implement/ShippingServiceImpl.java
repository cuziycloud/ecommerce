package com.tdtu.DesignPattern.Jeweluxe.service.Implement;

import com.tdtu.DesignPattern.Jeweluxe.service.ShippingService;
import com.tdtu.DesignPattern.Jeweluxe.strategy.shipping.FreeShippingStrategy;
import com.tdtu.DesignPattern.Jeweluxe.strategy.shipping.ShippingCalculationResult;
import com.tdtu.DesignPattern.Jeweluxe.strategy.shipping.ShippingStrategy;
import com.tdtu.DesignPattern.Jeweluxe.strategy.shipping.StandardShippingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingStrategy standardShippingStrategy;
    private final ShippingStrategy freeShippingStrategy;
    private static final double FREE_SHIPPING_THRESHOLD = 1500000; //free ship

    @Autowired
    public ShippingServiceImpl(StandardShippingStrategy standardShippingStrategy,
                               FreeShippingStrategy freeShippingStrategy) {
        this.standardShippingStrategy = standardShippingStrategy;
        this.freeShippingStrategy = freeShippingStrategy;
    }

    @Override
    public ShippingCalculationResult calculateShipping(double subTotal) {
        if (subTotal <= 0) {
            return new ShippingCalculationResult(0, "N/A");
        }

        ShippingStrategy selectedStrategy;

        if (subTotal >= FREE_SHIPPING_THRESHOLD) {
            selectedStrategy = this.freeShippingStrategy;
            //System.out.println("Selected Strategy: Free Shipping");
        } else {
            selectedStrategy = this.standardShippingStrategy;
            //System.out.println("Selected Strategy: Standard Shipping");
        }


        return selectedStrategy.calculate(subTotal);
    }
}