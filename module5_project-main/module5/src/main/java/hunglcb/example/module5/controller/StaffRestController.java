// src/main/java/hunglcb/example/module5/controller/StaffRestController.java
package hunglcb.example.module5.controller;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import hunglcb.example.module5.service.IStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StaffRestController {

    private final IStaffService staffService;

    /**
     * LẤY DANH SÁCH NHÂN VIÊN CÓ PHÂN TRANG + TÌM KIẾM + SẮP XẾP
     * Ví dụ: GET
     * /api/staffs?page=0&size=10&sortBy=fullName&sortDir=asc&search=nguyen
     */
    @GetMapping
    public ResponseEntity<Page<StaffResponseDTO>> getAllPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "search", required = false) String search) {

        Page<StaffResponseDTO> result = staffService.getStaffsPaged(
                search, // từ khóa tìm kiếm (tên, email, CMND)
                null, // roleId = null → không lọc theo role
                page, // trang hiện tại
                size, // số bản ghi trên 1 trang
                sortBy, // cột để sắp xếp
                sortDir // asc hoặc desc
        );

        return ResponseEntity.ok(result);
    }

    /**
     * LẤY CHI TIẾT 1 NHÂN VIÊN THEO ID
     * Ví dụ: GET /api/staffs/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getById(@PathVariable("id") Integer id) {
        StaffResponseDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    /**
     * TẠO MỚI NHÂN VIÊN
     * POST /api/staffs + JSON body
     */
    @PostMapping
    public ResponseEntity<StaffResponseDTO> create(@Valid @RequestBody StaffRequestDTO dto) {
        StaffResponseDTO created = staffService.createStaff(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
                .body(created);
    }

    /**
     * CẬP NHẬT NHÂN VIÊN
     * PUT /api/staffs/5 + JSON body
     */
    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> update(
            @PathVariable("id") Integer id,
            @Valid @RequestBody StaffRequestDTO dto) {

        StaffResponseDTO updated = staffService.updateStaff(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * XÓA MỀM NHÂN VIÊN (is_active = false)
     * DELETE /api/staffs/5
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}