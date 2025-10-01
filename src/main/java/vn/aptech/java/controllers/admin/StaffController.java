package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @GetMapping
    public String listStaffs(
            Model model) {
        List<User> staffs = staffService.getAllStaff();
        model.addAttribute("activePage", "staff");
        model.addAttribute("staffList", staffs);
        return "admin/pages/staff/index";
    }

    @GetMapping("/search")
    public String searchStaffs(@RequestParam("search") String search, Model model) {
        List<User> staffs = staffService.searchByNameEmailPhone(search);
        model.addAttribute("activePage", "staff");
        model.addAttribute("staffList", staffs);
        model.addAttribute("search", search);
        return "admin/pages/staff/index";

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
        try {
            model.addAttribute("activePage", "staff");
            model.addAttribute("createStaffDTO", new CreateStaffDTO());

            // Debug logging
            System.out.println("Create staff form loaded successfully");

            return "admin/pages/staff/create";
        } catch (Exception e) {
            System.err.println("Error loading create staff form: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi tải form tạo nhân viên: " + e.getMessage());
            model.addAttribute("activePage", "staff");
            return "admin/pages/staff/index";
        }
    }

    @PostMapping("/create")
    public String createStaff(@Valid @ModelAttribute("createStaffDTO") CreateStaffDTO createStaffDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        logger.info("Create staff POST request received");
        logger.debug("DTO: {}", createStaffDTO);
        logger.debug("Has errors: {}", bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors found:");
            bindingResult.getAllErrors().forEach(error ->
                    logger.warn("- {}", error.getDefaultMessage())
            );

            model.addAttribute("activePage", "staff");
            model.addAttribute("error", "Vui lòng kiểm tra lại thông tin nhập vào");
            return "admin/pages/staff/create";
        }

        try {
            staffService.createStaff(createStaffDTO);
            logger.info("Staff created successfully, username={}", createStaffDTO.getUsername());

            redirectAttributes.addFlashAttribute("success",
                    "Tạo nhân viên thành công! Tên đăng nhập: " + createStaffDTO.getUsername());
            return "redirect:/admin/staff";

        } catch (Exception e) {
            logger.error("Error creating staff", e);

            model.addAttribute("activePage", "staff");
            model.addAttribute("error", "Có lỗi xảy ra khi tạo nhân viên: " + e.getMessage());
            return "admin/pages/staff/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editStaffForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Loading edit form for staff ID: " + id);

            Optional<User> staffOpt = staffService.getStaffById(id);
            if (staffOpt.isPresent()) {
                User staff = staffOpt.get();
                System.out.println("Staff found: " + staff.getUsername());

                UpdateStaffDTO updateStaffDTO;
                try {
                    updateStaffDTO = staffService.getUpdateStaffDTO(id);
                    System.out.println("UpdateStaffDTO created via service");
                } catch (Exception e) {
                    System.err.println("Service method failed, creating DTO manually: " + e.getMessage());
                    updateStaffDTO = new UpdateStaffDTO();
                    updateStaffDTO.setId(staff.getId());
                    updateStaffDTO.setFullname(staff.getFullname());
                    updateStaffDTO.setEmail(staff.getEmail());
                    updateStaffDTO.setPhone(staff.getPhone());
                    updateStaffDTO.setStatus(staff.getStatus());
                }

                System.out.println("UpdateStaffDTO prepared successfully");

                model.addAttribute("activePage", "staff");
                model.addAttribute("staff", staff);
                model.addAttribute("updateStaffDTO", updateStaffDTO);

                return "admin/pages/staff/edit";
            } else {
                System.err.println("Staff not found with ID: " + id);
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên!");
                return "redirect:/admin/staff";
            }
        } catch (Exception e) {
            logger.error("Error edit staff", e);

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

        logger.info("Update staff POST request for ID: {}", id);
        logger.debug("UpdateStaffDTO: {}", updateStaffDTO);
        logger.debug("Has errors: {}", bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors found for staff ID {}:", id);
            bindingResult.getAllErrors().forEach(error ->
                    logger.warn("- {}", error.getDefaultMessage())
            );

            try {
                Optional<User> staffOpt = staffService.getStaffById(id);
                if (staffOpt.isPresent()) {
                    model.addAttribute("activePage", "staff");
                    model.addAttribute("staff", staffOpt.get());
                    model.addAttribute("error", "Vui lòng kiểm tra lại thông tin nhập vào");
                    return "admin/pages/staff/edit";
                }
            } catch (Exception e) {
                logger.error("Error loading staff for validation error display (ID={}): {}", id, e.getMessage(), e);
            }

            redirectAttributes.addFlashAttribute("error", "Có lỗi trong dữ liệu nhập!");
            return "redirect:/admin/staff/" + id + "/edit";
        }

        try {
            updateStaffDTO.setId(id);
            staffService.updateStaff(updateStaffDTO);
            logger.info("Staff updated successfully, ID={}", id);

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin nhân viên thành công!");
            return "redirect:/admin/staff/" + id;
        } catch (Exception e) {
            logger.error("Error updating staff, ID={}", id, e);
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

    @PostMapping("/{id}/reset-password")
    public String resetPassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User staff = staffService.resetPassword(id);
            redirectAttributes.addFlashAttribute("success",
                    "Mật khẩu mới đã được gửi đến email: " + staff.getEmail());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi reset mật khẩu: " + e.getMessage());
        }
        return "redirect:/admin/staff/" + id + "/edit";
    }
}