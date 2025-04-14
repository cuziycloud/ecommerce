package com.jeweluxeCode.Jeweluxe.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeweluxeCode.Jeweluxe.entities.Address;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đổi chiến lược
    private Long id; // Đổi kiểu dữ liệu sang Long

    private String firstName;

    private String lastName;

    @JsonIgnore // Giữ lại để không trả về password trong API response
    @Column(nullable = false) // Mật khẩu thường là bắt buộc
    private String password;

    @Temporal(TemporalType.TIMESTAMP) // Thêm @Temporal
    @Column(nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // Thêm default và không cho cập nhật
    private Date createdOn; // Giữ tên hoặc đổi thành createdAt

    @Temporal(TemporalType.TIMESTAMP) // Thêm @Temporal
    @Column(nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP") // Thêm default và tự cập nhật
    private Date updatedOn; // Giữ tên hoặc đổi thành updatedAt

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true) // Số điện thoại cũng nên là duy nhất (tùy yêu cầu)
    private String phoneNumber;

    @Column(length = 50) // Giới hạn độ dài provider (GOOGLE, FACEBOOK, LOCAL,...)
    private String provider; // Ví dụ: "LOCAL", "GOOGLE"

    @Column(length = 64) // Giới hạn độ dài mã xác thực
    private String verificationCode;

    @Column(nullable = false) // Thêm nullable = false
    private boolean enabled = false; // Giữ nguyên hoặc đặt mặc định là true nếu muốn user active ngay

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER) // Bỏ CascadeType.ALL, chỉ dùng PERSIST, MERGE cho Role/Authority
    @JoinTable(
            name = "user_roles", // Đổi tên bảng trung gian
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // Trỏ tới cột id của User
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") // Trỏ tới cột id của Authority/Role
    )
    private List<Authority> authorities; // Đảm bảo Authority cũng dùng Long id

    // mappedBy = "user" trong Address không đổi
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Thêm orphanRemoval=true để xóa Address khi xóa User
    @ToString.Exclude
    private List<Address> addresses; // Đổi tên thành addresses cho rõ ràng


    @PrePersist
    protected void onPrePersist() {
        Date now = new Date();
        if (this.createdOn == null) {
            this.createdOn = now;
        }
        if (this.updatedOn == null) {
            this.updatedOn = now;
        }
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.updatedOn = new Date();
    }


    // --- Implementations của UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // Thường trả về email hoặc một username duy nhất khác
        return this.email;
    }

    // Các phương thức khác của UserDetails (thường cần để Spring Security hoạt động đầy đủ)
    // Bạn có thể trả về true nếu không quản lý các trạng thái này phức tạp

    @Override
    public boolean isAccountNonExpired() {
        return true; // Tài khoản không bao giờ hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Tài khoản không bao giờ bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Thông tin đăng nhập (mật khẩu) không bao giờ hết hạn
    }

    @Override
    public boolean isEnabled() {
        return this.enabled; // Trả về trạng thái kích hoạt của user
    }
}
