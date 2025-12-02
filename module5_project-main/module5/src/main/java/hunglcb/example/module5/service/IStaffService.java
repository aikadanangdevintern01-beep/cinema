// src/main/java/hunglcb/example/module5/service/IStaffService.java
package hunglcb.example.module5.service;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.RoleResponseDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile; // 🚨 BƯỚC 1: THÊM IMPORT QUAN TRỌNG NÀY

public interface IStaffService {

    // =========================================================
    // 🚨 BƯỚC 2: THÊM CÁC PHƯƠNG THỨC MỚI (OVERLOAD) XỬ LÝ FILE
    // =========================================================

    // Thêm mới nhân viên (CÓ file upload)
    StaffResponseDTO createStaff(StaffRequestDTO dto, MultipartFile file);

    // Giữ phương thức cũ (DÙNG ĐỂ TẠO MỚI MÀ KHÔNG CẦN FILE, HOẶC CHỈ DÙNG
    // INTERNALLY)
    StaffResponseDTO createStaff(StaffRequestDTO dto);

    // Cập nhật nhân viên (CÓ file upload)
    StaffResponseDTO updateStaff(Integer id, StaffRequestDTO dto, MultipartFile file);

    // Giữ phương thức cũ
    StaffResponseDTO updateStaff(Integer id, StaffRequestDTO dto);

    // =========================================================
    // CÁC PHƯƠNG THỨC KHÁC (GIỮ NGUYÊN)
    // =========================================================

    List<StaffResponseDTO> getAllStaffs();

    List<RoleResponseDTO> getAllRoles();

    Page<StaffResponseDTO> getStaffsPaged(String search, Integer roleId, int page, int size, String sortBy,
            String sortDir);

    StaffResponseDTO getStaffById(Integer id);

    StaffRequestDTO toRequestDTO(StaffResponseDTO responseDTO);

    void deleteStaff(Integer id);
}