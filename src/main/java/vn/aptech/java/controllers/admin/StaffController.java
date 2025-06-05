package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class StaffController {
    @GetMapping("/staff")
    public String index(Model model) {
        model.addAttribute("activePage", "staff");
        return "admin/pages/staff/index";
    }

    // Uncomment if you need to create a staff member
    // @RequestMapping("/create")
    // public String create() {
    //     return "admin/pages/staff/create";
    // }

    // Uncomment if you need to update a staff member
    // @RequestMapping("/update")
    // public String update() {
    //     return "admin/pages/staff/update";
    // }
}
