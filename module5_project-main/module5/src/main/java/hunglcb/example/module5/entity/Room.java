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
@Table(name = "rooms", schema = "cinema_db")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(length = 50, nullable = false)
    private String name;

    private Integer capacity;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "room")
    private List<Seat> seats;

    @OneToMany(mappedBy = "room")
    private List<Showtime> showtimes;

    // getters & setters
}
