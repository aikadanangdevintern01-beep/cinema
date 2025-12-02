package hunglcb.example.module5.entity;

import hunglcb.example.module5.enums.BookingStatus;
import hunglcb.example.module5.enums.PaymentMethod;
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
@Table(name = "bookings", schema = "cinema_db")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Column(name = "customer_phone", length = 15)
    private String customerPhone;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'CONFIRMED', 'CANCELLED')")
    private BookingStatus status = BookingStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", columnDefinition = "ENUM('CASH', 'VNPAY', 'POINT')")
    private PaymentMethod paymentMethod = PaymentMethod.VNPAY;

    @OneToMany(mappedBy = "booking")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "booking")
    private List<Payment> payments;

    @OneToMany(mappedBy = "booking")
    private List<PointHistory> pointHistories;

    @OneToMany(mappedBy = "booking")
    private List<PromotionCode> promotionCodes;

}
