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
@Table(name = "roles", schema = "cinema_db")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @OneToMany(mappedBy = "role")
    private List<Staff> staffs;

}
