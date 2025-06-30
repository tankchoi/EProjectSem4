package vn.aptech.java.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // Admin Login
    @GetMapping("/admin/login")
    public String adminLogin(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }

        return "admin/pages/login/index";
    }

    // Client Login
    @GetMapping("/client/login")
    public String clientLogin(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }

        return "client/pages/login/index";
    }

    // Redirect dựa trên role sau khi login thành công
    @GetMapping("/")
    public String home() {
        return "redirect:/client/homepage"; // Default redirect to client
    }

    // Backwards compatibility
    @GetMapping("/custom-login")
    public String legacyLogin() {
        return "redirect:/admin/login";
    }
}
