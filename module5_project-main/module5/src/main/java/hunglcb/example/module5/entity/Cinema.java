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
@Table(name = "cinemas", schema = "cinema_db")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 20)
    private String hotline;

    @Lob
    private String description;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "cinema")
    private List<Room> rooms;

    // getters & setters
}
