package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class RequestController {
    @GetMapping("/request")
    public String index(Model model) {
        model.addAttribute("activePage", "request");
        return "admin/pages/request/index";
    }

    // Uncomment if you need to create a request
    // @RequestMapping("/create")
    // public String create() {
    //     return "admin/pages/request/create";
    // }

    // Uncomment if you need to update a request
    // @RequestMapping("/update")
    // public String update() {
    //     return "admin/pages/request/update";
    // }
}
