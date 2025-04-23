package com.tdtu.DesignPattern.Jeweluxe.listener;

import com.tdtu.DesignPattern.Jeweluxe.event.OrderCreatedEvent;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.service.EmailService; // <<< IMPORT EmailService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener; // <<< IMPORT @EventListener
import org.springframework.stereotype.Component; // <<< IMPORT @Component
// import org.springframework.scheduling.annotation.Async; // Import nếu muốn chạy bất đồng bộ

import java.util.List;

@Component // Đánh dấu là Spring Bean để được quét và quản lý
public class OrderEmailNotifier {

    private final EmailService emailService; // Inject dịch vụ gửi email

    @Autowired
    public OrderEmailNotifier(EmailService emailService) {
        this.emailService = emailService;
    }

    // Phương thức này sẽ tự động được gọi khi OrderCreatedEvent được publish
    @EventListener
    // @Async // Bỏ comment nếu muốn gửi email bất đồng bộ (cần cấu hình @EnableAsync)
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Listener nhận được OrderCreatedEvent!");
        List<OrderItem> orders = event.getSavedOrders();

        if (orders != null && !orders.isEmpty()) {
            System.out.println("Chuẩn bị gửi email xác nhận cho " + orders.size() + " mục đơn hàng.");
            try {
                // Gọi phương thức gửi email từ EmailService
                // Giả sử EmailService có phương thức này
                emailService.sendOrderConfirmationEmail(orders);
                System.out.println("Email xác nhận đơn hàng đã được gửi (hoặc đưa vào hàng đợi).");
            } catch (Exception e) {
                // Quan trọng: Log lỗi chi tiết nhưng không để lỗi gửi mail làm sập ứng dụng
                System.err.println("LỖI NGHIÊM TRỌNG: Không thể gửi email xác nhận đơn hàng. Chi tiết lỗi: " + e.getMessage());
                e.printStackTrace(); // Nên dùng logger chuẩn trong thực tế (ví dụ: SLF4j)
            }
        } else {
            System.out.println("Listener nhận được OrderCreatedEvent nhưng không có OrderItem nào, bỏ qua gửi email.");
        }
    }
}