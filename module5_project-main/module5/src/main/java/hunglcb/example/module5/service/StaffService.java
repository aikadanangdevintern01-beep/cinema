// src/main/java/hunglcb/example/module5/service/StaffService.java
package hunglcb.example.module5.service;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.RoleResponseDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import hunglcb.example.module5.entity.Staff;
import hunglcb.example.module5.entity.Role;
import hunglcb.example.module5.repository.StaffRepository;
import hunglcb.example.module5.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // 🚨 THÊM IMPORT NÀY

import java.io.IOException; // 🚨 THÊM IMPORT NÀY
import java.nio.file.Files; // 🚨 THÊM IMPORT NÀY
import java.nio.file.Path; // 🚨 THÊM IMPORT NÀY
import java.nio.file.Paths; // 🚨 THÊM IMPORT NÀY
import java.nio.file.StandardCopyOption; // 🚨 THÊM IMPORT NÀY
import java.util.List;
import java.util.UUID; // 🚨 THÊM IMPORT NÀY (để tạo tên file duy nhất)

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService implements IStaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // TẠO MỚI NHÂN VIÊN
    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO dto) {
        // Kiểm tra trùng
        if (staffRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        if (staffRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email đã tồn tại");
        if (staffRepository.existsByIdCard(dto.getIdCard()))
            throw new RuntimeException("CMND/CCCD đã tồn tại");

        Role staffRole = roleRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò nhân viên"));

        Staff staff = Staff.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .idCard(dto.getIdCard())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate()) // ĐÃ CÓ NGÀY SINH
                .gender(dto.getGender()) // ĐÃ CÓ GIỚI TÍNH
                .role(staffRole)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        staff = staffRepository.save(staff);
        return toResponseDTO(staff);
    }

    // TẠO MỚI NHÂN VIÊN (CÓ XỬ LÝ FILE)
    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO dto, MultipartFile file) {
        // 1. Kiểm tra trùng (Giữ nguyên logic cũ)
        if (staffRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        if (staffRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email đã tồn tại");
        if (staffRepository.existsByIdCard(dto.getIdCard()))
            throw new RuntimeException("CMND/CCCD đã tồn tại");

        Role staffRole = roleRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò nhân viên"));

        // 2. Xử lý File và lấy đường dẫn
        String avatarPath = handleFileUpload(file);

        Staff staff = Staff.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .idCard(dto.getIdCard())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .role(staffRole)
                .avatarUrl(avatarPath) // 🚨 THÊM ĐƯỜNG DẪN AVATAR
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        staff = staffRepository.save(staff);
        return toResponseDTO(staff);
    }

    @Override
    public StaffResponseDTO updateStaff(Integer id, StaffRequestDTO dto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        // Kiểm tra trùng (trừ chính nó)
        if (staffRepository.existsByUsernameAndIdNot(dto.getUsername(), id))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        if (staffRepository.existsByEmailAndIdNot(dto.getEmail(), id))
            throw new RuntimeException("Email đã tồn tại");
        if (staffRepository.existsByIdCardAndIdNot(dto.getIdCard(), id))
            throw new RuntimeException("CMND/CCCD đã tồn tại");

        staff.setUsername(dto.getUsername());
        staff.setFullName(dto.getFullName());
        staff.setEmail(dto.getEmail());
        staff.setPhone(dto.getPhone());
        staff.setIdCard(dto.getIdCard());
        staff.setAddress(dto.getAddress());
        staff.setBirthDate(dto.getBirthDate()); // CẬP NHẬT NGÀY SINH
        staff.setGender(dto.getGender()); // CẬP NHẬT GIỚI TÍNH

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        staffRepository.save(staff);
        return toResponseDTO(staff);
    }

    // CẬP NHẬT NHÂN VIÊN (CÓ XỬ LÝ FILE)
    @Override
    public StaffResponseDTO updateStaff(Integer id, StaffRequestDTO dto, MultipartFile file) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        // Kiểm tra trùng (trừ chính nó) (Giữ nguyên logic cũ)
        if (staffRepository.existsByUsernameAndIdNot(dto.getUsername(), id))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        if (staffRepository.existsByEmailAndIdNot(dto.getEmail(), id))
            throw new RuntimeException("Email đã tồn tại");
        if (staffRepository.existsByIdCardAndIdNot(dto.getIdCard(), id))
            throw new RuntimeException("CMND/CCCD đã tồn tại");

        // 1. Xử lý File
        if (file != null && !file.isEmpty()) {
            // Lưu file mới và lấy đường dẫn
            String newAvatarPath = handleFileUpload(file);

            // (Optional: Xóa file cũ tại đây nếu cần)

            staff.setAvatarUrl(newAvatarPath); // 🚨 CẬP NHẬT ĐƯỜNG DẪN AVATAR MỚI
        }
        // Nếu file là null hoặc empty, giữ nguyên đường dẫn avatar cũ trong entity
        // staff

        // Cập nhật các trường thông tin khác (Giữ nguyên logic cũ)
        staff.setUsername(dto.getUsername());
        staff.setFullName(dto.getFullName());
        staff.setEmail(dto.getEmail());
        staff.setPhone(dto.getPhone());
        staff.setIdCard(dto.getIdCard());
        staff.setAddress(dto.getAddress());
        staff.setBirthDate(dto.getBirthDate());
        staff.setGender(dto.getGender());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        staffRepository.save(staff);
        return toResponseDTO(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponseDTO getStaffById(Integer id) {
        return staffRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
    }

    @Override
    public void deleteStaff(Integer id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        staff.setIsActive(false);
        staffRepository.save(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffResponseDTO> getStaffsPaged(String search, Integer roleId, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Staff> staffPage = staffRepository.findAllWithSearchAndFilter(search, roleId, pageable);

        return staffPage.map(this::toResponseDTO);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    RoleResponseDTO dto = new RoleResponseDTO();
                    dto.setId(role.getId());
                    dto.setName(role.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // CHUYỂN ENTITY → RESPONSE DTO (có mã NV000001)
    private StaffResponseDTO toResponseDTO(Staff staff) {
        return StaffResponseDTO.builder()
                .id(staff.getId())
                .staffCode("NV" + String.format("%06d", staff.getId()))
                .username(staff.getUsername())
                .fullName(staff.getFullName())
                .email(staff.getEmail())
                .phone(staff.getPhone())
                .idCard(staff.getIdCard())
                .address(staff.getAddress())
                .birthDate(staff.getBirthDate()) // TRẢ VỀ NGÀY SINH
                .gender(staff.getGender()) // TRẢ VỀ GIỚI TÍNH
                .roleId(staff.getRole().getId())
                .roleName(staff.getRole().getName())
                .isActive(staff.isActive())
                .createdAt(staff.getCreatedAt())
                .build();
    }

    @Override
    public StaffRequestDTO toRequestDTO(StaffResponseDTO resp) {
        return StaffRequestDTO.builder()
                .id(resp.getId())
                .username(resp.getUsername())
                .fullName(resp.getFullName())
                .email(resp.getEmail())
                .phone(resp.getPhone())
                .idCard(resp.getIdCard())
                .address(resp.getAddress())
                .birthDate(resp.getBirthDate()) // ĐƯA NGÀY SINH VÀO FORM
                .gender(resp.getGender()) // ĐƯA GIỚI TÍNH VÀO FORM
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDTO> getAllStaffs() {
        return staffRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Xử lý lưu tệp MultipartFile vào thư mục vật lý và trả về đường dẫn tương đối.
     * 
     * @param file Tệp được tải lên từ client.
     * @return Đường dẫn tương đối của tệp đã lưu (ví dụ: /images/avatars/abc.jpg)
     */
    private String handleFileUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null; // Không có tệp để xử lý
        }

        try {
            // 1. Tạo tên file duy nhất (để tránh trùng lặp)
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // 2. Xác định thư mục lưu trữ (Sử dụng đường dẫn tương đối trong thư mục
            // resources/static)
            // **LƯU Ý: Đảm bảo thư mục này tồn tại trong dự án của bạn!**
            Path uploadDir = Paths.get("src/main/resources/static/images/avatars");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);

            // 3. Sao chép nội dung file vào đường dẫn
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Trả về đường dẫn tương đối (để lưu vào DB)
            return "/images/avatars/" + fileName;

        } catch (IOException e) {
            // Log lỗi hoặc xử lý exception
            throw new RuntimeException("Lỗi khi lưu tệp: " + file.getOriginalFilename(), e);
        }
    }
}