package com.tdtu.DesignPattern.Jeweluxe.decorator.price;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import java.text.NumberFormat;
import java.util.Locale;

// phí bảo hiểm vận chuyển.
public class InsuranceDecorator extends PriceDecorator {
    private final double insuranceRate;
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public InsuranceDecorator(OrderItemPriceCalculator calculator, double rate) {
        super(calculator);
        this.insuranceRate = rate;
    }

    public InsuranceDecorator(OrderItemPriceCalculator calculator) {
        this(calculator, 0.01); // mặc định 1% (lazada )
    }

    @Override
    public double calculatePrice(OrderItem item) {
        double priceBeforeInsurance = super.calculatePrice(item);
        double insuranceFee = priceBeforeInsurance * insuranceRate;
        return priceBeforeInsurance + insuranceFee;
    }

    @Override
    public String getDescription(OrderItem item) {
        double priceBeforeInsurance = super.calculatePrice(item);
        double insuranceFee = priceBeforeInsurance * insuranceRate;
        return super.getDescription(item) + " + Phí bảo hiểm (" + currencyFormatter.format(insuranceFee) + ")";
    }
}