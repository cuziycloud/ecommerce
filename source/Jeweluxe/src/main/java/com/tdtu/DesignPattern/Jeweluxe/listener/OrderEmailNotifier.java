package com.tdtu.DesignPattern.Jeweluxe.listener;

import com.tdtu.DesignPattern.Jeweluxe.event.OrderCreatedEvent;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // Đánh dấu là Spring Bean để được quét và quản lý (singleton)
public class OrderEmailNotifier {

    private final EmailService emailService; // Inject dịch vụ gửi email

    @Autowired
    public OrderEmailNotifier(EmailService emailService) {
        this.emailService = emailService;
    }


    @EventListener // Tự động được gọi khi OrderCreatedEvent được publish (Đăng ký nghe sự kiện OrderCreatedEvent)
    public void handleOrderCreated(OrderCreatedEvent event) {
        List<OrderItem> orders = event.getSavedOrders(); //Lấy dữ liệu từ "bản tin"

        if (orders != null && !orders.isEmpty()) {
            try {
                emailService.sendOrderConfirmationEmail(orders);
            } catch (Exception e) {
                System.err.println("LỖI: Không thể gửi email xác nhận đơn hàng. Chi tiết lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Listener nhận được OrderCreatedEvent nhưng không có OrderItem nào, bỏ qua gửi email.");
        }
    }
}