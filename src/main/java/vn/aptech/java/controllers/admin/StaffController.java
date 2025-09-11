package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.admin.CreateStaffDTO;
import vn.aptech.java.dtos.admin.UpdateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.services.StaffService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String listStaffs(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> staffPage;

            if (search != null && !search.trim().isEmpty()) {
                staffPage = staffService.searchStaffPaginated(search.trim(), pageable);
            } else {
                staffPage = staffService.getAllStaffPaginated(pageable);
            }

            model.addAttribute("activePage", "staff");
            model.addAttribute("staffList", staffPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", staffPage.getTotalPages());
            model.addAttribute("totalElements", staffPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("search", search);

            return "admin/pages/staff/index";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách nhân viên: " + e.getMessage());
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/index";
        }
    }

    @GetMapping("/{id}")
    public String staffDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> staffOpt = staffService.getStaffById(id);
            if (staffOpt.isPresent()) {
                model.addAttribute("activePage", "staff");
                model.addAttribute("staff", staffOpt.get());
                return "admin/pages/staff/view";
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên!");
                return "redirect:/admin/staff";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi truy xuất thông tin nhân viên: " + e.getMessage());
            return "redirect:/admin/staff";
        }
    }

    @GetMapping("/create")
    public String createStaffForm(Model model) {
        model.addAttribute("activePage", "staff");
        model.addAttribute("createStaffDTO", new CreateStaffDTO());
        return "admin/pages/staff/create";
    }

    @PostMapping("/create")
    public String createStaff(@Valid @ModelAttribute("createStaffDTO") CreateStaffDTO createStaffDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/create";
        }

        try {
            staffService.createStaff(createStaffDTO);
            redirectAttributes.addFlashAttribute("success", "Tạo nhân viên thành công!");
            return "redirect:/admin/staff";
        } catch (Exception e) {
            model.addAttribute("activePage", "staff");
            model.addAttribute("error", "Có lỗi xảy ra khi tạo nhân viên: " + e.getMessage());
            return "admin/pages/staff/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editStaffForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> staffOpt = staffService.getStaffById(id);
            if (staffOpt.isPresent()) {
                UpdateStaffDTO updateStaffDTO = staffService.getUpdateStaffDTO(id);

                model.addAttribute("activePage", "staff");
                model.addAttribute("staff", staffOpt.get());
                model.addAttribute("updateStaffDTO", updateStaffDTO);
                return "admin/pages/staff/edit";
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên!");
                return "redirect:/admin/staff";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi truy xuất thông tin nhân viên: " + e.getMessage());
            return "redirect:/admin/staff";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateStaff(@PathVariable Long id,
            @Valid @ModelAttribute("updateStaffDTO") UpdateStaffDTO updateStaffDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            try {
                Optional<User> staffOpt = staffService.getStaffById(id);
                if (staffOpt.isPresent()) {
                    model.addAttribute("activePage", "staff");
                    model.addAttribute("staff", staffOpt.get());
                    return "admin/pages/staff/edit";
                }
            } catch (Exception e) {
                // Handle exception
            }
            redirectAttributes.addFlashAttribute("error", "Có lỗi trong dữ liệu nhập!");
            return "redirect:/admin/staff/" + id + "/edit";
        }

        try {
            updateStaffDTO.setId(id);
            staffService.updateStaff(updateStaffDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin nhân viên thành công!");
            return "redirect:/admin/staff/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi cập nhật thông tin nhân viên: " + e.getMessage());
            return "redirect:/admin/staff/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteStaff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            staffService.banStaff(id);
            redirectAttributes.addFlashAttribute("success", "Khóa tài khoản nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi khóa tài khoản: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @PostMapping("/{id}/restore")
    public String restoreStaff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            staffService.restoreStaff(id);
            redirectAttributes.addFlashAttribute("success", "Khôi phục tài khoản nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi khôi phục tài khoản: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }
}