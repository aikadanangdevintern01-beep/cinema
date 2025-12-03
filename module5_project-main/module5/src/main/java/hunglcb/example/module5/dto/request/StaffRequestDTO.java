// hunglcb/example/module5/dto/request/StaffRequestDTO.java
package hunglcb.example.module5.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRequestDTO {

    private Integer id;

    @NotBlank(message = "Tài khoản không được để trống")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String password; // chỉ dùng khi tạo mới
    private String confirmPassword; // chỉ dùng khi tạo mới

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số CMND/CCCD không được để trống")
    @Pattern(regexp = "\\d{9,12}", message = "CMND/CCCD phải chứa 9-12 chữ số")
    private String idCard;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0[0-9]{9}", message = "Số điện thoại phải có 10 số, bắt đầu bằng 0")
    private String phone;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDate; // ĐÃ CÓ NGÀY SINH

    @NotNull(message = "không được để trống")
    private String address;

    @NotBlank(message = "Vui lòng chọn giới tính")
    private String gender; // "Nam" hoặc "Nữ"

    private String avatarUrl;

    private MultipartFile avatarFile;
}