package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.java.dtos.admin.CreateStaffDTO;
import vn.aptech.java.dtos.admin.UpdateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.services.StaffService;

import java.util.List;

@Controller
@RequestMapping("/admin/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String listStaffs(@RequestParam(required = false) String phone, Model model) {
        List<User> staffs = (phone != null && !phone.isEmpty())
                ? staffService.searchByPhone(phone)
                : staffService.getAllStaff();
        model.addAttribute("staffs", staffs);
        return "admin/pages/staff/index";
    }

    @GetMapping("/{id}")
    public String staffDetail(@PathVariable Long id, Model model) {
        User staff = staffService.getStaffById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
        model.addAttribute("staff", staff);
        return "admin/pages/staff/view";

    }

    @PostMapping("/create")
    public String createStaff(@ModelAttribute CreateStaffDTO dto) {
        staffService.createStaff(dto);
        return "redirect:/admin/staffs";
    }

    @PostMapping("/update")
    public String updateStaff(@ModelAttribute UpdateStaffDTO dto) {
        staffService.updateStaff(dto);
        return "redirect:/admin/staffs";
    }

    @PostMapping("/{id}/ban")
    public String banStaff(@PathVariable Long id) {
        staffService.banStaff(id);
        return "redirect:/admin/staffs";
    }
}