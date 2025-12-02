// src/main/java/hunglcb/example/module5/controller/StaffController.java
package hunglcb.example.module5.controller;

import hunglcb.example.module5.dto.request.StaffRequestDTO;
import hunglcb.example.module5.dto.response.StaffResponseDTO;
import hunglcb.example.module5.service.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;

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

    // FORM THÊM MỚI
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", new StaffRequestDTO());
        }
        model.addAttribute("isEdit", false);
        return "admin/staff/form";
    }

    // FORM SỬA – ĐÃ XỬ LÝ RÕ RÀNG LỖI + FLASH ATTRIBUTE
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirect) {

        try {
            StaffResponseDTO staffResp = staffService.getStaffById(id);
            if (!model.containsAttribute("staff")) {
                model.addAttribute("staff", staffService.toRequestDTO(staffResp));
            }
            model.addAttribute("isEdit", true);
            return "admin/staff/form";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy nhân viên với ID: " + id);
            return "redirect:/admin/staffs";
        }
    }

    // THÊM MỚI NHÂN VIÊN
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.staff", result);
            redirect.addFlashAttribute("staff", dto);
            return "redirect:/admin/staffs/create";
        }

        staffService.createStaff(dto);
        redirect.addFlashAttribute("success", "Thêm nhân viên thành công!");
        return "redirect:/admin/staffs";
    }

    // CẬP NHẬT NHÂN VIÊN
    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("staff") StaffRequestDTO dto,
            BindingResult result,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.staff", result);
            redirect.addFlashAttribute("staff", dto);
            return "redirect:/admin/staffs/edit/" + id;
        }

        staffService.updateStaff(id, dto);
        redirect.addFlashAttribute("success", "Cập nhật nhân viên thành công!");
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