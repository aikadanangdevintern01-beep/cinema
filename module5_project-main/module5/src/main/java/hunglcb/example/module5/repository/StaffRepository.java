// src/main/java/hunglcb/example/module5/repository/StaffRepository.java
package hunglcb.example.module5.repository;

import hunglcb.example.module5.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

        // Các method cũ giữ nguyên
        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        boolean existsByUsernameAndIdNot(String username, Integer id);

        boolean existsByEmailAndIdNot(String email, Integer id);

        @Query("SELECT s FROM Staff s WHERE " +
                        "(:search IS NULL OR LOWER(s.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "s.idCard LIKE CONCAT('%', :search, '%')) " + // ĐÃ THÊM DÒNG NÀY
                        "AND (:roleId IS NULL OR s.role.id = :roleId) " +
                        "AND s.isActive = true")
        Page<Staff> findAllWithSearchAndFilter(
                        @Param("search") String search,
                        @Param("roleId") Integer roleId,
                        Pageable pageable);

        boolean existsByIdCard(String idCard);

        boolean existsByIdCardAndIdNot(String idCard, Integer id);
}