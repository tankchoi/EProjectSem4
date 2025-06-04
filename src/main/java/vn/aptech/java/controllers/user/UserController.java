package vn.aptech.java.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/user/homepage")
    public String index() {
        return "user/pages/check_warranty";
    }
}
