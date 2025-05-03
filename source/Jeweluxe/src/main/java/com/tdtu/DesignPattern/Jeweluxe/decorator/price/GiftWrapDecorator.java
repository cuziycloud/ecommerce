package com.tdtu.DesignPattern.Jeweluxe.decorator.price;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import java.text.NumberFormat;
import java.util.Locale;

// phí gói quà
public class GiftWrapDecorator extends PriceDecorator {
    private final double giftWrapFee;
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public GiftWrapDecorator(OrderItemPriceCalculator calculator, double fee) {
        super(calculator);
        this.giftWrapFee = fee;
    }

    public GiftWrapDecorator(OrderItemPriceCalculator calculator) {
        this(calculator, 50000); // mặc định 50,000 VNĐ
    }

    @Override
    public double calculatePrice(OrderItem item) {
        return super.calculatePrice(item) + giftWrapFee;
    }

    @Override
    public String getDescription(OrderItem item) {
        return super.getDescription(item) + " + Phí gói quà (" + currencyFormatter.format(giftWrapFee) + ")";
    }
}