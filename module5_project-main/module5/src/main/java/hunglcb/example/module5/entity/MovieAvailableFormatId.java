package hunglcb.example.module5.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class MovieAvailableFormatId implements Serializable {

    private Integer movieId;
    private Integer formatId;

    public MovieAvailableFormatId() {
    }

    public MovieAvailableFormatId(Integer movieId, Integer formatId) {
        this.movieId = movieId;
        this.formatId = formatId;
    }

    // getters, setters, equals, hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieAvailableFormatId)) return false;
        MovieAvailableFormatId that = (MovieAvailableFormatId) o;
        return Objects.equals(movieId, that.movieId)
                && Objects.equals(formatId, that.formatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, formatId);
    }
}
