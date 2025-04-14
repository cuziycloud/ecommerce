package com.jeweluxeCode.Jeweluxe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// import java.util.UUID; // Bỏ import UUID

@Entity
// Đổi tên bảng nếu cần khớp với SQL CREATE TABLE (product_resources)
@Table(name = "product_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT") // Nên dùng TEXT cho URL dài
    private String url;

    @Column(nullable = false)
    private Boolean isPrimary; // Boolean (ánh xạ sang bit(1) hoặc tinyint(1))

    @Column(nullable = false, length=50) // Thêm length giới hạn
    private String type; // Ví dụ: THUMBNAIL, GALLERY, VIDEO

    @ManyToOne(fetch = FetchType.LAZY) // Đổi thành LAZY
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product; // Kiểu Product không đổi

}
