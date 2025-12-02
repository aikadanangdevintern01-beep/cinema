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
@Table(name = "showtimes", schema = "cinema_db")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "day_type_id", nullable = false)
    private DayType dayType;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "movie_available_formats_movie_id", referencedColumnName = "movie_id"),
            @JoinColumn(name = "movie_available_formats_format_id", referencedColumnName = "format_id")
    })
    private MovieAvailableFormat movieAvailableFormat;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "base_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @Column(name = "copied_day_surcharge", precision = 10, scale = 2)
    private BigDecimal copiedDaySurcharge = BigDecimal.ZERO;

    @Column(name = "copied_format_surcharge", precision = 10, scale = 2)
    private BigDecimal copiedFormatSurcharge = BigDecimal.ZERO;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "showtime")
    private List<Ticket> tickets;

}

