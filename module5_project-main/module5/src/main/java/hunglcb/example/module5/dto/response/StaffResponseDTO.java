// src/main/java/hunglcb/example/module5/dto/response/StaffResponseDTO.java
package hunglcb.example.module5.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponseDTO {

    private Integer id;

    // Mã nhân viên đẹp: NV000001, NV000125...
    private String staffCode;

    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String idCard;
    private String address;

    private LocalDate birthDate;
    private String gender;

    // QUAN TRỌNG NHẤT: ĐỂ HIỂN THỊ ẢNH ĐẠI DIỆN
    private String avatarUrl;

    private Integer roleId;
    private String roleName;

    private Boolean isActive;
    private LocalDateTime createdAt;
}