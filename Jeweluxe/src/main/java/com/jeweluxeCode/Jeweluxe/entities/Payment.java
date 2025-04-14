package com.jeweluxeCode.Jeweluxe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
// import java.util.UUID; // Bỏ import UUID
import java.math.BigDecimal; // Nên dùng BigDecimal

@Entity
@Table(name="payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true) // Thêm unique=true cho OneToOne FK
    @JsonIgnore
    @ToString.Exclude
    private Order order; // Kiểu Order không đổi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // Thêm default
    private Date paymentDate;

    @Column(nullable = false, precision = 19, scale = 2) // Dùng BigDecimal
    private BigDecimal amount; // Đổi sang BigDecimal

    @Column(nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50) // Thêm length
    private PaymentStatus paymentStatus;

    @PrePersist
    protected void onPrePersist() {
        if (this.paymentDate == null) {
            this.paymentDate = new Date();
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING; // Đặt trạng thái mặc định
        }
    }
}
