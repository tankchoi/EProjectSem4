package vn.aptech.java.controllers.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.java.models.Laptop;

@Controller
@RequestMapping("/admin")
public class LaptopController {
    @RequestMapping("/laptop")
    public String index(Model model) {
        model.addAttribute("activePage", "laptop");
        return "admin/pages/laptop/index";
    }
}
