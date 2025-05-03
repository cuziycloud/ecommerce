package com.tdtu.DesignPattern.Jeweluxe.service;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}") // email trong properties
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Được gọi bởi Listener
    public boolean sendOrderConfirmationEmail(List<OrderItem> orders) throws Exception {
        if (orders == null || orders.isEmpty()) {
            return false;
        }

        OrderItem firstOrder = orders.get(0);
        if (firstOrder.getOrderAddress() == null) {
            System.err.println("Không có địa chỉ đặt hàng để gửi email xác nhận.");
            return false;
        }
        String customerEmail = firstOrder.getOrderAddress().getEmail();
        String customerName = firstOrder.getOrderAddress().getFirstName();

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<p>Xin chào ").append(customerName).append(",</p>");
        emailContent.append("<p>Cảm ơn bạn đã đặt hàng tại Jeweluxe! Đơn hàng của bạn đang được xử lý.</p>");
        emailContent.append("<p><b>Chi tiết đơn hàng:</b></p>");
        emailContent.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        emailContent.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Đơn giá</th><th>Thành tiền</th></tr>");

        double grandTotal = 0;
        for (OrderItem order : orders) {
            double itemTotal = order.getPrice() * order.getQuantity();
            grandTotal += itemTotal;
            emailContent.append("<tr>")
                    .append("<td>").append(order.getProduct().getTitle()).append("</td>")
                    .append("<td style='text-align:center;'>").append(order.getQuantity()).append("</td>")
                    .append("<td style='text-align:right;'>").append(String.format("%,.0f", order.getPrice())).append("</td>") // Định dạng tiền tệ
                    .append("<td style='text-align:right;'>").append(String.format("%,.0f", itemTotal)).append("</td>")
                    .append("</tr>");
        }
        emailContent.append("<tr><td colspan='3' style='text-align:right; font-weight:bold;'>Tổng cộng:</td><td style='text-align:right; font-weight:bold;'>")
                .append(String.format("%,.0f", grandTotal)).append("</td></tr>");
        emailContent.append("</table>");
        emailContent.append("<p>Chúng tôi sẽ thông báo cho bạn khi đơn hàng được vận chuyển.</p>");
        emailContent.append("<p>Trân trọng,<br>Jeweluxe Team</p>");


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("techstorehtt@gmail.com", "Jeweluxe");
        helper.setTo(customerEmail);
        helper.setSubject("Xác nhận đơn hàng Jeweluxe");
        helper.setText(emailContent.toString(), true);

        mailSender.send(message);
        return true;
    }


    // Được gọi bởi Listener
    public boolean sendGenericEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Jeweluxe Notifications");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}