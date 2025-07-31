package vn.aptech.java.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    @GetMapping("/homepage")
    public String homepage() {
        return "user/pages/homepage";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/pages/contact";
    }
}
