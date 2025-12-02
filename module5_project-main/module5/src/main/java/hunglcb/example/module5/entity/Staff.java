package hunglcb.example.module5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, nullable = false, length = 12)
    private String phone;

    @Column(name = "id_card", unique = true, nullable = false, length = 12)
    private String idCard;

    @Column(length = 255)
    private String address;

    // THÊM NGÀY SINH – BẮT BUỘC
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    // THÊM GIỚI TÍNH – "Nam" hoặc "Nữ"
    @Column(nullable = false, length = 10)
    private String gender;

    // Avatar (lưu đường dẫn file)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // TỰ ĐỘNG TẠO MÃ NHÂN VIÊN KHI LẤY RA
    @Transient // Không lưu vào DB
    public String getStaffCode() {
        return "NV" + String.format("%06d", this.id != null ? this.id : 0);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}