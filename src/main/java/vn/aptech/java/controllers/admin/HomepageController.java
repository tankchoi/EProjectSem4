package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/admin")
@Controller
public class HomepageController {
    @GetMapping("/homepage")
    public String index(Model model) {
        model.addAttribute("activePage", "homepage");
        return "admin/pages/homepage/index";
    }
}
