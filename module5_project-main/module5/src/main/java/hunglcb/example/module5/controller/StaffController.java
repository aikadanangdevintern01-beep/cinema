// src/main/java/hunglcb/example/module5/controller/StaffController.java
package hunglcb.example.module5.controller;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import hunglcb.example.module5.service.FileUploadService;
import hunglcb.example.module5.service.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 🚨 THÊM DÒNG NÀY
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;
    private final FileUploadService fileUploadService;

    // DANH SÁCH + TÌM KIẾM + PHÂN TRANG – ĐÃ RÕ RÀNG 100%
    @GetMapping({ "", "/" })
    public String listStaffs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        Page<StaffResponseDTO> staffPage = staffService.getStaffsPaged(
                search, // từ khóa tìm kiếm
                null, // roleId = null (không lọc theo role)
                page, // trang hiện tại
                size, // số bản ghi/trang
                sortBy, // cột sắp xếp
                sortDir // asc/desc
        );

        // Truyền rõ ràng từng tham số để Thymeleaf dùng phân trang + tìm kiếm
        model.addAttribute("staffPage", staffPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "admin/staff/list";
    }

    // 1. FORM THÊM MỚI – HOÀN HẢO
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Nếu có lỗi từ flash attribute (validation fail) → giữ lại dữ liệu cũ
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", new StaffRequestDTO());
        }
        model.addAttribute("isEdit", false);
        model.addAttribute("pageTitle", "Thêm nhân viên mới");
        return "admin/staff/form";
    }

    // 2. FORM SỬA – HOÀN HẢO, AN TOÀN, KHÔNG LỖI
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        try {
            // Lấy dữ liệu nhân viên
            StaffResponseDTO staffResp = staffService.getStaffById(id);

            // Nếu có flash attribute từ lỗi validation → ưu tiên dùng nó (giữ dữ liệu người
            // dùng nhập)
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

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            Model model) { // KHÔNG DÙNG RedirectAttributes khi có lỗi!

        // CÓ LỖI → TRẢ VỀ TRỰC TIẾP FORM
        if (result.hasErrors()) {
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", dto.getId() != null);
            return "admin/staff/form";
        }

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String avatarUrl = fileUploadService.saveAvatar(avatarFile, dto.getUsername());
                dto.setAvatarUrl(avatarUrl);
            }

            if (dto.getId() == null) {
                staffService.createStaff(dto);
            } else {
                staffService.updateStaff(dto.getId(), dto);
            }

            return "redirect:/admin/staffs"; // CHỈ REDIRECT KHI THÀNH CÔNG

        } catch (Exception e) {
            model.addAttribute("error", "Lưu thất bại: " + e.getMessage());
            model.addAttribute("staff", dto);
            model.addAttribute("isEdit", dto.getId() != null);
            return "admin/staff/form";
        }
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            RedirectAttributes redirect) {

        // 1. Đảm bảo ID từ URL được gán vào DTO (rất quan trọng!)
        dto.setId(id);

        // 2. Kiểm tra lỗi validation
        if (result.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.staff", result);
            redirect.addFlashAttribute("staff", dto);
            return "redirect:/admin/staffs/edit/" + id;
        }

        try {
            // 3. XỬ LÝ ẢNH ĐẠI DIỆN (nếu người dùng upload ảnh mới)
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String avatarUrl = fileUploadService.saveAvatar(avatarFile, dto.getUsername());
                dto.setAvatarUrl(avatarUrl); // Gán đường dẫn mới vào DTO
            }
            // Nếu không upload → giữ nguyên avatarUrl cũ (đã có trong DTO từ form)

            // 4. Gọi Service – CHỈ TRUYỀN DTO (đã có avatarUrl)
            staffService.updateStaff(id, dto);

            redirect.addFlashAttribute("success", "Cập nhật nhân viên thành công!");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
            redirect.addFlashAttribute("staff", dto);
            return "redirect:/admin/staffs/edit/" + id;
        }

        return "redirect:/admin/staffs";
    }

    // XÓA MỀM NHÂN VIÊN
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id,
            RedirectAttributes redirect) {

        try {
            staffService.deleteStaff(id);
            redirect.addFlashAttribute("success", "Xóa nhân viên thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa nhân viên này!");
        }
        return "redirect:/admin/staffs";
    }
}