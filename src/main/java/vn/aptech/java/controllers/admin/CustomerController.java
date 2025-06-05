package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CustomerController {
    @GetMapping("/customer")
    public String index() {
        return "admin/pages/customer/index";
    }

}
