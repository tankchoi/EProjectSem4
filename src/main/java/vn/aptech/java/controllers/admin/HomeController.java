package vn.aptech.java.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.aptech.java.services.LaptopService;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;
import vn.aptech.java.services.RequestService;
import vn.aptech.java.services.UserService;
import vn.aptech.java.models.*;

@Controller
public class HomeController {
    @Autowired
    private LaptopService laptopService;

    @Autowired
    private PartService partService;

    @Autowired
    private PartTypeService partTypeService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin/homepage")
    public String homepage(Model model) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalLaptops", laptopService.count());
        stats.put("totalParts", partService.count());
        stats.put("totalPartTypes", partTypeService.count());
        stats.put("totalRequests", requestService.count());
        stats.put("pendingRequests", requestService.countByStatus(Request.Status.PENDING));
        stats.put("completedRequests", requestService.countByStatus(Request.Status.COMPLETED));
        stats.put("totalCustomers", userService.countByRole(User.Role.CUSTOMER));
        stats.put("totalStaff", userService.countByRole(User.Role.STAFF));

        model.addAttribute("stats", stats);

        return "admin/pages/homepage/index";
    }
}
