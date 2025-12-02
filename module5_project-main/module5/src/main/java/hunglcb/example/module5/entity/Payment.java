package hunglcb.example.module5.entity;

import hunglcb.example.module5.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments", schema = "cinema_db")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "transaction_code", length = 100)
    private String transactionCode;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "points_used")
    private Integer pointsUsed = 0;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'SUCCESS', 'FAILED')")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Lob
    @Column(name = "payment_url")
    private String paymentUrl;

    // getters & setters
}
