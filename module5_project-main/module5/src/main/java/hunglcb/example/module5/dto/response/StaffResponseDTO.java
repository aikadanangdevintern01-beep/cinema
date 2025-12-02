// hunglcb/example/module5/dto/response/StaffResponseDTO.java
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
    private String staffCode; // NV000001
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String idCard;
    private String address;
    private LocalDate birthDate; // ĐÃ CÓ
    private String gender; // ĐÃ CÓ
    private Integer roleId;
    private String roleName;
    private Boolean isActive;
    private LocalDateTime createdAt;
}