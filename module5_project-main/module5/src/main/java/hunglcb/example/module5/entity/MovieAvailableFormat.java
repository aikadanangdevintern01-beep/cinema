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
@Table(name = "movie_available_formats", schema = "cinema_db")
public class MovieAvailableFormat {

    @EmbeddedId
    private MovieAvailableFormatId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @MapsId("formatId")
    @JoinColumn(name = "format_id", nullable = false)
    private MovieFormat format;

    @OneToMany(mappedBy = "movieAvailableFormat")
    private List<Showtime> showtimes;

    // getters & setters
}
