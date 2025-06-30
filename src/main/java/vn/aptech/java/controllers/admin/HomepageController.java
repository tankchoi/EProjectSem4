package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.java.dtos.DashboardStatsDTO;
import vn.aptech.java.services.HomepageService;

@RequestMapping("/admin")
@Controller
public class HomepageController {

    @Autowired
    private HomepageService homepageService;

    @GetMapping("/homepage")
    public String index(Model model) {
        DashboardStatsDTO stats = homepageService.getDashboardStats();

        model.addAttribute("activePage", "homepage");
        model.addAttribute("stats", stats);

        return "admin/pages/homepage/index";
    }
}
