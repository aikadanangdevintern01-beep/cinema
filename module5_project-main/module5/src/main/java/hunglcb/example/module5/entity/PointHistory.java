package hunglcb.example.module5.entity;

import hunglcb.example.module5.enums.PointTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "point_history", schema = "cinema_db")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "points_amount", nullable = false)
    private Integer pointsAmount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('EARN', 'REDEEM')")
    private PointTransactionType type;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // getters & setters
}
