package com.jeweluxeCode.Jeweluxe.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

// import java.util.UUID; // Bỏ import UUID

@Entity
// Đổi tên bảng thành "roles" hoặc "authorities" cho rõ ràng hơn (tùy chọn)
// Hoặc giữ "AUTH_AUTHORITY" nếu bạn đã tạo bảng với tên này
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    // Đổi tên cột thành "name" hoặc "authority" - phổ biến hơn cho role/authority
    // Giữ lại "roleCode" nếu bạn muốn
    @Column(nullable = false, unique = true, length = 50) // Thêm unique và length
    private String roleCode; // Ví dụ: "ROLE_USER", "ROLE_ADMIN"

    // Có thể bỏ cột này nếu roleCode đã đủ rõ ràng, hoặc giữ lại
    @Column(nullable = true) // Description có thể không bắt buộc
    private String roleDescription;

    @Override
    public String getAuthority() {
        // Trả về roleCode, đây là cách Spring Security nhận diện quyền
        return this.roleCode;
    }
}
