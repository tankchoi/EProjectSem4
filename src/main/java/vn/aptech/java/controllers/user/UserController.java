package vn.aptech.java.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/homepage")
    public String homepage() {
        return "user/pages/homepage";
    }

    @GetMapping("/check_warranty")
    public String checkWarranty() {
        return "user/pages/check_warranty";
    }

    @GetMapping("/schedule_warranty")
    public String scheduleWarranty() {
        return "user/pages/schedule_warranty";
    }

    @GetMapping("/history")
    public String history() {
        return "user/pages/history";
    }

    @GetMapping("/search_parts")
    public String searchParts() {
        return "user/pages/search_parts";
    }

    @GetMapping("/support")
    public String support() {
        return "user/pages/support";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/pages/contact";
    }

    @GetMapping("/information")
    public String information() {
        return "user/pages/information";
    }

    @GetMapping("/login")
    public String login() {
        return "user/pages/login";
    }
}
