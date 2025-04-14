package com.jeweluxeCode.Jeweluxe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) // Thêm unique=true nếu code là duy nhất
    private String code;

    @Column(nullable = false) // Kiểu TEXT trong SQL thường tốt hơn cho description dài
    private String description;

    // mappedBy = "category" trong CategoryType không đổi
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Thêm FetchType.LAZY nếu không muốn load types mặc định
    private List<CategoryType> categoryTypes;

}

