package hunglcb.example.module5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promotion_campaigns", schema = "cinema_db")
public class PromotionCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer amount;

    @Column(length = 255)
    private String description;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "campaign")
    private List<PromotionCode> promotionCodes;

}
