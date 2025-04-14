package com.jeweluxeCode.Jeweluxe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// import java.util.UUID; // Bỏ import UUID
import java.math.BigDecimal; // Nên dùng BigDecimal cho tiền tệ

@Entity
@Table(name="order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    @ManyToOne(fetch = FetchType.EAGER) // EAGER có thể gây N+1 query, cân nhắc LAZY
    @JoinColumn(name = "product_id", nullable = false)
    // @JsonIgnore // Bỏ ignore nếu muốn trả về thông tin product cơ bản
    private Product product; // Kiểu Product không đổi

    // ĐỔI KIỂU DỮ LIỆU CỦA KHÓA NGOẠI TƯỜNG MINH
    @Column(name = "product_variant_id", nullable = true) // Cho phép null nếu order sản phẩm gốc
    private Long productVariantId; // Đổi sang Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Order order; // Kiểu Order không đổi

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 19, scale = 2) // Dùng BigDecimal và precision/scale
    private BigDecimal itemPrice; // Đổi sang BigDecimal

}
