package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PartTypeController {
    @RequestMapping("/part-type")
    public String index(Model model) {
        model.addAttribute("activePage", "part-type");
        return "admin/pages/part_type/index";
    }

    @RequestMapping("part_type/create")
    public String create() {
        return "admin/pages/part_type/create";
    }
//
//    @RequestMapping("/update")
//    public String update() {
//        return "admin/pages/part_type/update";
//    }
}
