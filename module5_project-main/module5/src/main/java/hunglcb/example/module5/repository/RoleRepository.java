package hunglcb.example.module5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hunglcb.example.module5.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
