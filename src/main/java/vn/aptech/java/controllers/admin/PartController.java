package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PartController {
    @GetMapping("/part")
    public String index(Model model) {
        model.addAttribute("activePage", "part");
        return "admin/pages/part/index";
    }
    @GetMapping("/create")
    public String create() {
        return "admin/pages/part/create";
    }
//    @RequestMapping("/update")
//    public String update() {
//        return "admin/pages/part/update";
//    }

}
