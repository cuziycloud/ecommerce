package com.jeweluxeCode.Jeweluxe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// import java.util.UUID; // Bỏ import UUID

@Entity
@Table(name = "category_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) // Thêm unique=true nếu code là duy nhất
    private String code;

    @Column(nullable = false) // Kiểu TEXT trong SQL thường tốt hơn
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // Thêm FetchType.LAZY
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category; // Kiểu Category không đổi

}

