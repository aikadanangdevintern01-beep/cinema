// src/main/java/hunglcb/example/module5/service/IStaffService.java
package hunglcb.example.module5.service;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.RoleResponseDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IStaffService {

    // CHỈ NHẬN DTO – ĐÃ CÓ SẴN avatarUrl
    StaffResponseDTO createStaff(StaffRequestDTO dto);

    StaffResponseDTO updateStaff(Integer id, StaffRequestDTO dto);

    StaffResponseDTO getStaffById(Integer id);

    void deleteStaff(Integer id);

    Page<StaffResponseDTO> getStaffsPaged(String search, Integer roleId, int page, int size,
            String sortBy, String sortDir);

    List<StaffResponseDTO> getAllStaffs();

    List<RoleResponseDTO> getAllRoles();

    // Dùng để chuyển từ Response → Request khi edit
    StaffRequestDTO toRequestDTO(StaffResponseDTO responseDTO);
}