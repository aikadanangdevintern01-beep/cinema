// src/main/java/hunglcb/example/module5/controller/StaffController.java
package hunglcb.example.module5.controller;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import hunglcb.example.module5.service.FileUploadService;
import hunglcb.example.module5.service.IStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;
    private final FileUploadService fileUploadService;

    // ==================== DANH SÁCH NHÂN VIÊN – TRUYỀN ĐẦY ĐỦ THAM SỐ
    // ====================
    @GetMapping({ "", "/" })
    public String listStaffs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            Model model) {

        Page<StaffResponseDTO> staffPage = staffService.getStaffsPaged(
                search.isEmpty() ? null : search.trim(),
                null,
                page,
                size,
                sortBy,
                sortDir);

        model.addAttribute("staffPage", staffPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", staffPage.getTotalPages());
        model.addAttribute("totalItems", staffPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");

        return "admin/staff/list";
    }

    // ==================== FORM THÊM MỚI – TRUYỀN ĐẦY ĐỦ ====================
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", new StaffRequestDTO());
        }
        model.addAttribute("isEdit", false);
        model.addAttribute("pageTitle", "Thêm nhân viên mới");
        return "admin/staff/form";
    }

    // ==================== CHỈNH SỬA – TRUYỀN ĐẦY ĐỦ ID ====================
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Integer id, // BẮT BUỘC CÓ TÊN "id"
            Model model,
            RedirectAttributes redirect) {

        try {
            StaffResponseDTO staffResp = staffService.getStaffById(id);

            if (!model.containsAttribute("staff")) {
                model.addAttribute("staff", staffService.toRequestDTO(staffResp));
            }

            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");

            return "admin/staff/form";

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy nhân viên với ID: " + id);
            return "redirect:/admin/staffs";
        }
    }

    // ==================== LƯU HOẶC CẬP NHẬT – TRUYỀN ĐẦY ĐỦ TẤT CẢ THAM SỐ
    // ====================
    @PostMapping({ "/save", "/save/{id}" })
    public String saveOrUpdate(
            @PathVariable(name = "id", required = false) Integer id, // RÕ RÀNG, ĐẦY ĐỦ, KHÔNG THIỂU
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
            Model model) {

        // Gán ID rõ ràng – không thể thiếu
        dto.setId(id);

        // Validation lỗi → trả về form ngay lập tức
        if (result.hasErrors()) {
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", id != null);
            model.addAttribute("pageTitle", id != null ? "Chỉnh sửa nhân viên" : "Thêm nhân viên mới");
            return "admin/staff/form";
        }

        try {
            // XỬ LÝ ẢNH – ĐẦY ĐỦ, KHÔNG THIỂU BƯỚC NÀO
            if (avatarFile != null && !avatarFile.isEmpty()) {
                // Nếu đang sửa → xóa ảnh cũ trước
                if (id != null) {
                    StaffResponseDTO oldStaff = staffService.getStaffById(id);
                    fileUploadService.deleteOldAvatar(oldStaff.getAvatarUrl());
                }

                String avatarUrl = fileUploadService.uploadAvatar(avatarFile, dto.getUsername());
                dto.setAvatarUrl(avatarUrl);
            }

            // Lưu hoặc cập nhật – rõ ràng
            if (id == null) {
                staffService.createStaff(dto);
            } else {
                staffService.updateStaff(id, dto);
            }

            return "redirect:/admin/staffs";

        } catch (Exception e) {
            model.addAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", id != null);
            model.addAttribute("pageTitle", id != null ? "Chỉnh sửa nhân viên" : "Thêm nhân viên mới");
            return "admin/staff/form";
        }
    }

    // ==================== XÓA – TRUYỀN ĐẦY ĐỦ ID ====================
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id, // RÕ RÀNG, KHÔNG THIỂU
            RedirectAttributes redirect) {

        try {
            staffService.deleteStaff(id);
            redirect.addFlashAttribute("success", "Xóa nhân viên thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa nhân viên: " + e.getMessage());
        }
        return "redirect:/admin/staffs";
    }
}