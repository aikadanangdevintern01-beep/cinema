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

import java.time.LocalDateTime;
import java.util.List;
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
                .password(passwordEncoder.encode(dto.getPassword())) // BẮT BUỘC có password khi tạo mới
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .idCard(dto.getIdCard())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .avatarUrl(dto.getAvatarUrl()) // LẤY ĐƯỜNG DẪN ẢNH ĐÃ XỬ LÝ TỪ CONTROLLER
                .role(staffRole)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        staff = staffRepository.save(staff);
        return toResponseDTO(staff);
    }

    // CẬP NHẬT NHÂN VIÊN – ĐÃ FIX HOÀN HẢO
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

        // CẬP NHẬT THÔNG TIN
        staff.setUsername(dto.getUsername());
        staff.setFullName(dto.getFullName());
        staff.setEmail(dto.getEmail());
        staff.setPhone(dto.getPhone());
        staff.setIdCard(dto.getIdCard());
        staff.setAddress(dto.getAddress());
        staff.setBirthDate(dto.getBirthDate());
        staff.setGender(dto.getGender());

        // CẬP NHẬT ẢNH ĐẠI DIỆN NẾU CÓ MỚI
        if (dto.getAvatarUrl() != null && !dto.getAvatarUrl().trim().isEmpty()) {
            staff.setAvatarUrl(dto.getAvatarUrl());
        }

        // CẬP NHẬT MẬT KHẨU NẾU NGƯỜI DÙNG NHẬP MỚI
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        staff = staffRepository.save(staff);
        return toResponseDTO(staff);
    }

    // CHUYỂN ENTITY → RESPONSE DTO – ĐÃ BỔ SUNG avatarUrl
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
                .birthDate(staff.getBirthDate())
                .gender(staff.getGender())
                .avatarUrl(staff.getAvatarUrl()) // ĐÃ THÊM – BẮT BUỘC!
                .roleId(staff.getRole().getId())
                .roleName(staff.getRole().getName())
                .isActive(staff.isActive())
                .createdAt(staff.getCreatedAt())
                .build();
    }

    // toRequestDTO – ĐÃ BỔ SUNG avatarUrl
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
                .birthDate(resp.getBirthDate())
                .gender(resp.getGender())
                .avatarUrl(resp.getAvatarUrl()) // ĐÃ THÊM – ĐỂ GIỮ ẢNH KHI SỬA
                .build();
    }

    // CÁC METHOD KHÁC GIỮ NGUYÊN (đã hoàn hảo)
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

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDTO> getAllStaffs() {
        return staffRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }
}