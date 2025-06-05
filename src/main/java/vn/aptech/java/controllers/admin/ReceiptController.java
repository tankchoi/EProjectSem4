package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReceiptController {
    @GetMapping("/receipt")
    public String index(Model model) {
        model.addAttribute("activePage", "receipt");
        return "admin/pages/receipt/index";
    }

    // Uncomment if you need to create a receipt
    // @RequestMapping("/create")
    // public String create() {
    //     return "admin/pages/receipt/create";
    // }

    // Uncomment if you need to update a receipt
    // @RequestMapping("/update")
    // public String update() {
    //     return "admin/pages/receipt/update";
    // }
}
