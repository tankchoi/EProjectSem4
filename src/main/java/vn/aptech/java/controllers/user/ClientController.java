package vn.aptech.java.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.aptech.java.models.*;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.*;
import java.util.List;
import org.springframework.ui.Model;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class ClientController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PartService partService;

    @GetMapping("/homepage")
    public String homepage() {
        return "user/pages/homepage";
    }

    @GetMapping("/custom-login")
    public String login() {
        return "user/pages/login";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "user/pages/login";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.CUSTOMER);
        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);

        model.addAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        return "user/pages/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login/index";
    }

    @GetMapping("/information")
    public String information() {
        return "user/pages/information";
    }

    @GetMapping("/history")
    public String history() {
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

    @GetMapping("/contact")
    public String contact() {
        return "user/pages/contact";
    }
}
