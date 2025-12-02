package hunglcb.example.module5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "seats", schema = "cinema_db",
        uniqueConstraints = @UniqueConstraint(
                name = "UK_room_row_seat",
                columnNames = {"room_id", "row_name", "seat_number"}))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    @Column(name = "row_name", length = 2, nullable = false)
    private String rowName;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;

    // getters & setters
}
