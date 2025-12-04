// src/main/java/hunglcb/example/module5/controller/StaffRestController.java
package hunglcb.example.module5.controller;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import hunglcb.example.module5.service.FileUploadService;
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
    private final FileUploadService fileUploadService; // BẮT BUỘC THÊM – ĐỂ XỬ LÝ ẢNH

    // 1. LẤY DANH SÁCH PHÂN TRANG – TRUYỀN ĐẦY ĐỦ THAM SỐ
    @GetMapping
    public ResponseEntity<Page<StaffResponseDTO>> getAllPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "roleId", required = false) Integer roleId) {

        Page<StaffResponseDTO> result = staffService.getStaffsPaged(
                search.isEmpty() ? null : search.trim(),
                roleId,
                page,
                size,
                sortBy,
                sortDir);
        return ResponseEntity.ok(result);
    }

    // 2. LẤY CHI TIẾT – TRUYỀN ĐẦY ĐỦ ID
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getById(
            @PathVariable(name = "id") Integer id) { // RÕ RÀNG, ĐẦY ĐỦ
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    // 3. TẠO MỚI + UPLOAD ẢNH – ĐẦY ĐỦ, HOÀN HẢO 100%
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<StaffResponseDTO> create(
            @Valid @RequestPart(name = "staff") StaffRequestDTO dto,
            @RequestPart(name = "avatarFile", required = false) MultipartFile avatarFile) {

        // XỬ LÝ ẢNH MỚI – BẮT BUỘC CÓ
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileUploadService.uploadAvatar(avatarFile, dto.getUsername());
            dto.setAvatarUrl(avatarUrl);
        }

        StaffResponseDTO created = staffService.createStaff(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. CẬP NHẬT + UPLOAD ẢNH + XÓA ẢNH CŨ – HOÀN HẢO NHẤT 2025
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<StaffResponseDTO> update(
            @PathVariable(name = "id") Integer id, // RÕ RÀNG, ĐẦY ĐỦ
            @Valid @RequestPart(name = "staff") StaffRequestDTO dto,
            @RequestPart(name = "avatarFile", required = false) MultipartFile avatarFile) {

        dto.setId(id); // Gán ID rõ ràng

        // XỬ LÝ ẢNH MỚI + XÓA ẢNH CŨ
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Lấy ảnh cũ để xóa
            StaffResponseDTO oldStaff = staffService.getStaffById(id);
            fileUploadService.deleteOldAvatar(oldStaff.getAvatarUrl());

            // Upload ảnh mới
            String avatarUrl = fileUploadService.uploadAvatar(avatarFile, dto.getUsername());
            dto.setAvatarUrl(avatarUrl);
        }
        // Không có file mới → giữ nguyên avatarUrl cũ

        StaffResponseDTO updated = staffService.updateStaff(id, dto);
        return ResponseEntity.ok(updated);
    }

    // 5. XÓA MỀM – TRUYỀN ĐẦY ĐỦ ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") Integer id) { // RÕ RÀNG, KHÔNG THIẾU
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}