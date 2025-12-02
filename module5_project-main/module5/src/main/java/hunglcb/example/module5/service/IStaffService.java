// src/main/java/hunglcb/example/module5/service/IStaffService.java
package hunglcb.example.module5.service;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.RoleResponseDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import java.util.List;

import org.springframework.data.domain.Page;

public interface IStaffService {
    StaffResponseDTO createStaff(StaffRequestDTO dto);

    StaffResponseDTO updateStaff(Integer id, StaffRequestDTO dto);

    List<StaffResponseDTO> getAllStaffs();

    List<RoleResponseDTO> getAllRoles();

    // IStaffService.java
    Page<StaffResponseDTO> getStaffsPaged(String search, Integer roleId, int page, int size, String sortBy,
            String sortDir);

    StaffResponseDTO getStaffById(Integer id);

    StaffRequestDTO toRequestDTO(StaffResponseDTO responseDTO);

    void deleteStaff(Integer id);
}