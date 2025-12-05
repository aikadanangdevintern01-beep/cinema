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
        return "admin/staff/add";
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

            return "admin/staff/edit";

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy nhân viên với ID: " + id);
            return "redirect:/admin/staffs";
        }
    }

    // ==================== LƯU HOẶC CẬP NHẬT – TRUYỀN ĐẦY ĐỦ TẤT CẢ THAM SỐ
    // ====================
    // THÊM MỚI – không có id
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
            Model model,
            RedirectAttributes redirect) {

        // Không cần setId vì thêm mới → id = null

        if (result.hasErrors()) {
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Thêm nhân viên mới");
            return "admin/staff/add";
        }

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String avatarUrl = fileUploadService.uploadAvatar(avatarFile, dto.getUsername());
                dto.setAvatarUrl(avatarUrl);
            }

            staffService.createStaff(dto);
            redirect.addFlashAttribute("success", "Thêm nhân viên thành công!");
            return "redirect:/admin/staffs"; // quay lại danh sách

        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("staff", dto);
            return "admin/staff/add";
        }
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
            Model model,
            RedirectAttributes redirect) {

        dto.setId(id); // 1. Gán ID để Service biết là update

        // 2. Nếu validate form thất bại
        if (result.hasErrors()) {
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");
            return "admin/staff/edit";
        }

        try {
            // 3. Lấy thông tin cũ từ DB để tham chiếu
            StaffResponseDTO oldStaff = staffService.getStaffById(id);

            // 4. XỬ LÝ ẢNH (LOGIC QUAN TRỌNG)
            if (avatarFile != null && !avatarFile.isEmpty()) {
                // TH1: Người dùng CÓ upload ảnh mới

                // Xóa ảnh cũ nếu tồn tại
                if (oldStaff.getAvatarUrl() != null && !oldStaff.getAvatarUrl().isEmpty()) {
                    fileUploadService.deleteOldAvatar(oldStaff.getAvatarUrl());
                }

                // Upload ảnh mới
                String avatarUrl = fileUploadService.uploadAvatar(avatarFile, dto.getUsername());
                dto.setAvatarUrl(avatarUrl);
            } else {
                // TH2: Người dùng KHÔNG upload ảnh mới -> Giữ nguyên ảnh cũ
                // Dòng này rất quan trọng, nếu thiếu -> Mất ảnh
                dto.setAvatarUrl(oldStaff.getAvatarUrl());
            }

            // 5. Gọi Service lưu thông tin
            staffService.updateStaff(id, dto);

            redirect.addFlashAttribute("success", "Cập nhật nhân viên thành công!");
            return "redirect:/admin/staffs";

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug nếu cần
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", true); // Giữ trạng thái edit để form hiển thị đúng
            return "admin/staff/edit";
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