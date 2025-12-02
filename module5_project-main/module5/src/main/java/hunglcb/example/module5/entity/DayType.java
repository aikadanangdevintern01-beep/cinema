package hunglcb.example.module5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "day_types", schema = "cinema_db")
public class DayType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "surcharge_price", precision = 10, scale = 2)
    private BigDecimal surchargePrice = BigDecimal.ZERO;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "dayType")
    private List<Showtime> showtimes;

}
