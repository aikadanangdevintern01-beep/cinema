package hunglcb.example.module5.entity;

import hunglcb.example.module5.enums.AuthProvider;
import hunglcb.example.module5.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", schema = "cinema_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "membership_code", length = 20, unique = true)
    private String membershipCode;

    @Column(length = 50, unique = true)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('MALE', 'FEMALE', 'OTHER')")
    private Gender gender;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 255)
    private String address;

    private LocalDate birthDate;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @Column (nullable = false)
    private Boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", length = 20)
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @Column(name = "provider_id", length = 100)
    private String providerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<PointHistory> pointHistories;

    @OneToMany(mappedBy = "usedByUser")
    private List<PromotionCode> usedPromotionCodes;
}
