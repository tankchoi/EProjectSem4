package vn.aptech.java.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/client")
@Controller
public class ClientHomepageController {

    @GetMapping("/homepage")
    public String index(Model model) {
        model.addAttribute("activePage", "homepage");
        return "client/pages/homepage/index";
    }
}
