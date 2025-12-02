package hunglcb.example.module5.entity;

import hunglcb.example.module5.enums.PromotionCodeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promotion_codes", schema = "cinema_db")
public class PromotionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private PromotionCampaign campaign;

    @Column(name = "code_value", length = 50, nullable = false, unique = true)
    private String codeValue;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('AVAILABLE', 'USED', 'LOCKED')")
    private PromotionCodeStatus status = PromotionCodeStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "used_by_user_id")
    private User usedByUser;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "bookings_id")
    private Booking booking;

}

