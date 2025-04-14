package com.jeweluxeCode.Jeweluxe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeweluxeCode.Jeweluxe.auth.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
// import java.util.UUID; // Bỏ import UUID
import java.math.BigDecimal; // Nên dùng BigDecimal cho tiền tệ

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // Thêm default
    private Date orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; // Kiểu User không đổi

    @ManyToOne(fetch = FetchType.EAGER) // EAGER có thể gây vấn đề hiệu năng, cân nhắc LAZY
    @JoinColumn(name = "address_id", nullable = false)
    // @JsonIgnore // Xem xét bỏ JsonIgnore nếu cần thông tin address khi trả về Order
    @ToString.Exclude
    private Address address; // Kiểu Address không đổi

    @Column(nullable = false, precision = 19, scale = 2) // Dùng BigDecimal và precision/scale
    private BigDecimal totalAmount; // Đổi sang BigDecimal

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50) // Thêm length giới hạn
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = true)
    private String shipmentTrackingNumber;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedDeliveryDate;

    // mappedBy = "order" trong OrderItem không đổi
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderItem> orderItems; // Sửa tên biến thành số nhiều

    @Column(precision = 10, scale = 2) // Dùng BigDecimal và precision/scale
    private BigDecimal discount; // Đổi sang BigDecimal

    // mappedBy = "order" trong Payment không đổi
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Payment payment;

    @PrePersist
    protected void onPrePersist() {
        if (this.orderDate == null) {
            this.orderDate = new Date();
        }
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.PENDING; // Đặt trạng thái mặc định
        }
    }
}
