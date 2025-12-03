// src/main/java/hunglcb/example/module5/dto/request/StaffRequestDTO.java
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
    @Size(min = 4, max = 50, message = "Tài khoản phải từ 4-50 ký tự")
    private String username;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không quá 100 ký tự")
    private String fullName;

    // Chỉ dùng khi tạo mới → không validate ở đây
    private String password;
    private String confirmPassword;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số CMND/CCCD không được để trống")
    @Pattern(regexp = "\\d{9,12}", message = "CMND/CCCD phải chứa 9-12 chữ số")
    private String idCard;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0[0-9]{9}", message = "Số điện thoại phải bắt đầu bằng 0 và có 10 số")
    private String phone;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDate;

    private String address; // Không bắt buộc

    @NotBlank(message = "Vui lòng chọn giới tính")
    private String gender; // "Nam" hoặc "Nữ"

    // QUAN TRỌNG: Chỉ lưu đường dẫn ảnh, không bắt buộc
    private String avatarUrl; // Có thể null (khi thêm mới hoặc không đổi ảnh)

    // QUAN TRỌNG: File upload – KHÔNG BAO GIỜ ĐƯỢC ĐÁNH DẤU @NotNull
    // Vì khi sửa mà không chọn ảnh mới → file = null → lỗi validation!
    private MultipartFile avatarFile; // nullable, chỉ có khi người dùng chọn ảnh mới
}