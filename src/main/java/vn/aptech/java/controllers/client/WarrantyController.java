package vn.aptech.java.controllers.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.aptech.java.services.PartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import vn.aptech.java.models.*;
import vn.aptech.java.repositories.*;

@Controller
public class WarrantyController {
    @Autowired
    private PartService partService;

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/history")
    public String viewHistory(Model model,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long customerId = customUserDetails.getUser().getId(); // lấy ID từ session
        List<Request> history = requestRepository.getHistoryByCustomerId(customerId);
        model.addAttribute("historyList", history);
        return "user/pages/history";
    }

    @GetMapping("/check-warranty")
    public String checkWarranty() {
        return "user/pages/check_warranty";
    }

    @GetMapping("/schedule-warranty")
    public String scheduleWarranty() {
        return "user/pages/schedule_warranty";
    }

    @GetMapping("/search-parts")
    public String searchParts(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Part> parts = partService.searchByName(keyword);
        model.addAttribute("parts", parts);
        model.addAttribute("keyword", keyword);
        return "user/pages/search_parts";
    }

}
