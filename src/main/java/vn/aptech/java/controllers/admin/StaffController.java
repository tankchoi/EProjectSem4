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
import vn.aptech.java.dtos.CreateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.services.StaffService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/staff")
    public String index(Model model,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> staffPage;

            if (search != null && !search.trim().isEmpty()) {
                staffPage = staffService.searchStaffPage(search, pageable);
                model.addAttribute("search", search);
            } else {
                staffPage = staffService.getStaffPage(pageable);
            }

            model.addAttribute("staffList", staffPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", staffPage.getTotalPages());
            model.addAttribute("totalElements", staffPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("activePage", "staff");

        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách nhân viên: " + e.getMessage());
            model.addAttribute("staffList", List.of());
        }

        return "admin/pages/staff/index";
    }

    @GetMapping("/staff/create")
    public String createForm(Model model) {
        model.addAttribute("createStaffDTO", new CreateStaffDTO());
        model.addAttribute("activePage", "staff");
        return "admin/pages/staff/create";
    }

    @PostMapping("/staff/create")
    public String create(@Valid @ModelAttribute("createStaffDTO") CreateStaffDTO createStaffDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/create";
        }

        try {
            User newStaff = staffService.createStaff(createStaffDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Tạo nhân viên mới thành công! ID: " + newStaff.getId());
            return "redirect:/admin/staff";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/create";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tạo nhân viên: " + e.getMessage());
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/create";
        }
    }

    @GetMapping("/staff/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> staffOpt = staffService.getStaffById(id);
            if (staffOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nhân viên không tồn tại");
                return "redirect:/admin/staff";
            }

            model.addAttribute("staff", staffOpt.get());
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/staff";
        }
    }

    @GetMapping("/staff/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> staffOpt = staffService.getStaffById(id);
            if (staffOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nhân viên không tồn tại");
                return "redirect:/admin/staff";
            }

            User staff = staffOpt.get();
            CreateStaffDTO updateStaffDTO = new CreateStaffDTO();
            updateStaffDTO.setUsername(staff.getUsername());
            updateStaffDTO.setFullname(staff.getFullname());
            updateStaffDTO.setEmail(staff.getEmail());
            updateStaffDTO.setPhone(staff.getPhone());
            updateStaffDTO.setRole(staff.getRole());
            updateStaffDTO.setStatus(staff.getStatus());

            model.addAttribute("updateStaffDTO", updateStaffDTO);
            model.addAttribute("staff", staff);
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/staff";
        }
    }

    @PostMapping("/staff/{id}/edit")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("updateStaffDTO") CreateStaffDTO updateStaffDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            try {
                Optional<User> staffOpt = staffService.getStaffById(id);
                if (staffOpt.isPresent()) {
                    model.addAttribute("staff", staffOpt.get());
                }
            } catch (Exception e) {
                // Handle silently
            }
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/edit";
        }

        try {
            User updatedStaff = staffService.updateStaff(id, updateStaffDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Cập nhật thông tin nhân viên thành công! ID: " + updatedStaff.getId());
            return "redirect:/admin/staff/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            try {
                Optional<User> staffOpt = staffService.getStaffById(id);
                if (staffOpt.isPresent()) {
                    model.addAttribute("staff", staffOpt.get());
                }
            } catch (Exception ex) {
                // Handle silently
            }
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/edit";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật nhân viên: " + e.getMessage());
            try {
                Optional<User> staffOpt = staffService.getStaffById(id);
                if (staffOpt.isPresent()) {
                    model.addAttribute("staff", staffOpt.get());
                }
            } catch (Exception ex) {
                // Handle silently
            }
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/edit";
        }
    }

    @PostMapping("/staff/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            staffService.deleteStaff(id);
            redirectAttributes.addFlashAttribute("success", "Khóa tài khoản nhân viên thành công!");
            return "redirect:/admin/staff";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/staff";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi khóa tài khoản: " + e.getMessage());
            return "redirect:/admin/staff";
        }
    }

    @PostMapping("/staff/{id}/restore")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            staffService.restoreStaff(id);
            redirectAttributes.addFlashAttribute("success", "Khôi phục tài khoản nhân viên thành công!");
            return "redirect:/admin/staff";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/staff";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi khôi phục tài khoản: " + e.getMessage());
            return "redirect:/admin/staff";
        }
    }
}
