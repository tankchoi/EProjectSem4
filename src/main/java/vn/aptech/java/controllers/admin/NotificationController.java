package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class NotificationController {

    @GetMapping("/notification")
    public String notification(Model model) {
        model.addAttribute("activePage", "notification");

        return "admin/pages/notification/index";
    }
}
