package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {
    @GetMapping("/admin/homepage")
    public String index() {
        return "admin/pages/homepage/index";
    }
}
