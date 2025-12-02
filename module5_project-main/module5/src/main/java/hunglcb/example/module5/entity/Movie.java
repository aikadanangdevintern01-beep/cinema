package hunglcb.example.module5.entity;

import hunglcb.example.module5.enums.MovieStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "movies", schema = "cinema_db")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(length = 100)
    private String director;

    @Column(length = 255)
    private String actor;

    @Column(length = 100)
    private String brand;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @Column(name = "trailer_url", length = 500)
    private String trailerUrl;

    @Column(length = 50)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('COMING_SOON', 'NOW_SHOWING', 'STOPPED')")
    private MovieStatus status = MovieStatus.COMING_SOON;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            schema = "cinema_db",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @OneToMany(mappedBy = "movie")
    private List<MovieAvailableFormat> availableFormats;

    // getters & setters
}
