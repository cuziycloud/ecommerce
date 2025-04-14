package com.jeweluxeCode.Jeweluxe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeweluxeCode.Jeweluxe.auth.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    private String name;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude // Giữ lại nếu cần tránh vòng lặp toString
    private User user; // Kiểu User không đổi, JPA sẽ tự xử lý join qua Long ID

}
