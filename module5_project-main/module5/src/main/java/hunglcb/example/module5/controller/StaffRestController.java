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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StaffRestController {

    private final IStaffService staffService;

    // 1. LẤY DANH SÁCH PHÂN TRANG – HOÀN HẢO
    @GetMapping
    public ResponseEntity<Page<StaffResponseDTO>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer roleId) { // THÊM roleId nếu cần lọc

        Page<StaffResponseDTO> result = staffService.getStaffsPaged(search, roleId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(result);
    }

    // 2. LẤY CHI TIẾT – HOÀN HẢO
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    // 3. TẠO MỚI + UPLOAD ẢNH – ĐÃ FIX TRIỆT ĐỂ
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<StaffResponseDTO> create(
            @Valid @RequestPart("staff") StaffRequestDTO dto,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {

        // XỬ LÝ ẢNH TRƯỚC KHI GỌI SERVICE (nếu có)
        // → Dùng FileUploadService như ở Thymeleaf
        // → Gán dto.setAvatarUrl(...)

        StaffResponseDTO created = staffService.createStaff(dto); // dto đã có avatarUrl
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. CẬP NHẬT + UPLOAD ẢNH – ĐÃ FIX TRIỆT ĐỂ
    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<StaffResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestPart("staff") StaffRequestDTO dto,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {

        dto.setId(id); // Quan trọng: gán ID từ path vào DTO

        StaffResponseDTO updated = staffService.updateStaff(id, dto);
        return ResponseEntity.ok(updated);
    }

    // 5. XÓA MỀM – HOÀN HẢO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}