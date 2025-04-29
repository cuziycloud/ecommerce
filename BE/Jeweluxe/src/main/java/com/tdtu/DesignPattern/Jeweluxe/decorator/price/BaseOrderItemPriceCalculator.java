package com.tdtu.DesignPattern.Jeweluxe.decorator.price;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import java.text.NumberFormat;
import java.util.Locale;

// concrete Component: Tính giá cơ bản của OrderItem (chưa có tùy chọn).
public class BaseOrderItemPriceCalculator implements OrderItemPriceCalculator {

    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public double calculatePrice(OrderItem item) {
        if (item == null || item.getPrice() == null || item.getQuantity() == null) {
            System.err.println("WARN: OrderItem không hợp lệ để tính giá cơ bản.");
            return 0;
        }
        // Giá gốc = Đơn giá tại thời điểm đặt * sl
        return item.getPrice() * item.getQuantity();
    }

    @Override
    public String getDescription(OrderItem item) {
        if (item == null || item.getPrice() == null || item.getQuantity() == null) {
            return "Giá gốc: N/A";
        }
        double basePrice = item.getPrice() * item.getQuantity();
        String priceFormatted = currencyFormatter.format(item.getPrice());
        String basePriceFormatted = currencyFormatter.format(basePrice);

        return "Giá gốc (" + item.getQuantity() + " x " + priceFormatted + "): " + basePriceFormatted;
    }
}