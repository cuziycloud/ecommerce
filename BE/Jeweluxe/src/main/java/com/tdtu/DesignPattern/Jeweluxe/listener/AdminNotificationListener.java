package com.tdtu.DesignPattern.Jeweluxe.listener;

import com.tdtu.DesignPattern.Jeweluxe.event.OrderCreatedEvent;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.service.EmailService; 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // đọc giá trị từ properties
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.time.format.DateTimeFormatter; 

@Component
public class AdminNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(AdminNotificationListener.class);
    private final EmailService emailService;

    @Value("${admin.notification.email}")
    private String adminEmail;

    @Autowired
    public AdminNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleOrderCreatedForAdmin(OrderCreatedEvent event) {
        log.info("Admin Listener nhận được OrderCreatedEvent!");
        List<OrderItem> orders = event.getSavedOrders();

        if (orders != null && !orders.isEmpty()) {
            if (adminEmail == null || adminEmail.isBlank()) {
                log.warn("Email admin chưa được cấu hình trong application.properties (admin.notification.email). Bỏ qua gửi thông báo.");
                return;
            }

            String subject = "[Jeweluxe] Thông báo Đơn hàng Mới - ID Đơn hàng (Ví dụ): " + (orders.get(0).getOrderId() != null ? orders.get(0).getOrderId() : "N/A");

            StringBuilder body = new StringBuilder();
            OrderItem firstItem = orders.get(0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String orderDate = firstItem.getOrderDate().format(dateFormatter); // Sử dụng LocalDate trực tiếp

            body.append("<html><body style='font-family: Arial, sans-serif; line-height: 1.6;'>");
            body.append("<h2 style='color: #0056b3;'>Thông báo Đơn hàng Mới tại Jeweluxe</h2>");
            body.append("<p>Hệ thống ghi nhận một đơn hàng mới vừa được tạo vào ngày: <strong>").append(orderDate).append("</strong>.</p>"); // Chỉ hiển thị ngày

            // Thông tin Khách hàng
            body.append("<h3>Thông tin Khách hàng</h3>");
            if (firstItem.getUser() != null) {
                body.append("<ul>");
                body.append("<li>ID Khách hàng: ").append(firstItem.getUser().getId()).append("</li>");
                body.append("<li>Tên Khách hàng: ").append(firstItem.getUser().getName()).append("</li>");
                body.append("<li>Email: ").append(firstItem.getUser().getEmail()).append("</li>");
                body.append("<li>Số điện thoại: ").append(firstItem.getUser().getMobileNumber() != null ? firstItem.getUser().getMobileNumber() : "N/A").append("</li>");
                body.append("</ul>");
            } else if (firstItem.getOrderAddress() != null) {
                body.append("<ul>");
                body.append("<li>Tên (Giao hàng): ").append(firstItem.getOrderAddress().getFirstName()).append(" ").append(firstItem.getOrderAddress().getLastName()).append("</li>");
                body.append("<li>Email (Giao hàng): ").append(firstItem.getOrderAddress().getEmail()).append("</li>");
                body.append("<li>Số điện thoại (Giao hàng): ").append(firstItem.getOrderAddress().getMobileNo()).append("</li>");
                body.append("</ul>");
            } else {
                body.append("<p><i>(Không có thông tin khách hàng hoặc địa chỉ giao hàng)</i></p>");
            }

            // Thông tin Giao hàng
            if (firstItem.getOrderAddress() != null) {
                body.append("<h3>Thông tin Giao hàng</h3>");
                body.append("<p>");
                body.append(firstItem.getOrderAddress().getFirstName()).append(" ").append(firstItem.getOrderAddress().getLastName()).append("<br>");
                body.append(firstItem.getOrderAddress().getAddress()).append("<br>");
                body.append(firstItem.getOrderAddress().getCity()).append(", ").append(firstItem.getOrderAddress().getState()).append(" ").append(firstItem.getOrderAddress().getPincode()).append("<br>");
                body.append("Điện thoại: ").append(firstItem.getOrderAddress().getMobileNo()).append("<br>");
                body.append("Email: ").append(firstItem.getOrderAddress().getEmail());
                body.append("</p>");
            }


            // Chi tiết Đơn hàng
            body.append("<h3>Chi tiết Đơn hàng</h3>");
            body.append("<table border='1' style='border-collapse: collapse; width: 100%; border-color: #ccc;'>");
            body.append("<thead style='background-color: #f2f2f2;'>");
            body.append(
                    "<tr>" +
                        "<th style='padding: 8px; text-align: left;'>Mã SP</th>" +
                        "<th style='padding: 8px; text-align: left;'>Tên Sản phẩm</th>" +
                        "<th style='padding: 8px; text-align: center;'>Số lượng</th>" +
                        "<th style='padding: 8px; text-align: right;'>Đơn giá</th>" +
                        "<th style='padding: 8px; text-align: right;'>Thành tiền</th>" +
                    "</tr>"
                    );
            body.append("</thead><tbody>");

            double grandTotal = 0;
            for (OrderItem item : orders) {
                double itemTotal = item.getPrice() * item.getQuantity();
                grandTotal += itemTotal;
                body.append("<tr>");
                body.append("<td style='padding: 8px;'>").append(item.getProduct() != null ? item.getProduct().getId() : "N/A").append("</td>");
                body.append("<td style='padding: 8px;'>").append(item.getProduct() != null ? item.getProduct().getTitle() : "(Sản phẩm không xác định)").append("</td>");
                body.append("<td style='padding: 8px; text-align: center;'>").append(item.getQuantity()).append("</td>");
                body.append("<td style='padding: 8px; text-align: right;'>").append(String.format("%,.0f đ", item.getPrice())).append("</td>");
                body.append("<td style='padding: 8px; text-align: right;'>").append(String.format("%,.0f đ", itemTotal)).append("</td>");
                body.append("</tr>");
            }
            body.append("</tbody><tfoot>");
            body.append("<tr>")
                .append("<td colspan='4' style='padding: 8px; text-align: right; font-weight: bold;'>Tổng giá trị đơn hàng:</td>")
                .append("<td style='padding: 8px; text-align: right; font-weight: bold;'>")
                .append(String.format("%,.0f đ", grandTotal))
                .append("</td>")
                .append("</tr>");
            body.append("</tfoot></table>");

            // Thông tin thanh toán
            body.append("<h3>Thông tin Thanh toán</h3>");
            body.append("<p>Phương thức thanh toán: <strong>").append(firstItem.getPaymentType() != null ? firstItem.getPaymentType() : "N/A").append("</strong></p>");

            body.append("<p style='margin-top: 20px;'>Vui lòng truy cập hệ thống quản trị để xem chi tiết đầy đủ và xử lý đơn hàng.</p>");
            // body.append("<p><a href='URL_TO_ADMIN_ORDER_PAGE' style='...'>Xem chi tiết đơn hàng</a></p>");

            body.append("<p>Trân trọng,<br>Hệ thống Jeweluxe</p>");
            body.append("</body></html>");


            // Gửi email =  EmailService
            try {
                boolean sent = emailService.sendGenericEmail(adminEmail, subject, body.toString());
                if (sent) {
                    log.info("Đã gửi thông báo đơn hàng mới đến admin: {}", adminEmail);
                } else {
                    log.error("Gửi thông báo đơn hàng mới đến admin thất bại.");
                }

            } catch (Exception e) {
                log.error("Lỗi khi gửi email thông báo đơn hàng mới cho admin: {}", adminEmail, e);
            }
        } else {
            log.info("Admin Listener nhận OrderCreatedEvent rỗng, bỏ qua.");
        }
    }
}