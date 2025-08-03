package vn.aptech.java.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;

import vn.aptech.java.dtos.client.RegisterDTO;
import vn.aptech.java.dtos.client.UpdateInfoDTO;
import vn.aptech.java.models.*;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.UserService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/custom-login")
    public String login() {
        return "user/pages/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "user/pages/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "user/pages/register";
        }

        try {
            userService.createAccount(registerDTO);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().contains("Tên đăng nhập")) {
                bindingResult.rejectValue("username", "error.username", ex.getMessage());
            } else if (ex.getMessage().contains("Email")) {
                bindingResult.rejectValue("email", "error.email", ex.getMessage());
            } else {
                model.addAttribute("error", ex.getMessage());
            }
            return "user/pages/register";
        }

        return "user/pages/login";
    }

    @GetMapping("/information")
    public String information(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username).orElse(null);
            model.addAttribute("user", user);

            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setUsername(user.getUsername());
            dto.setFullname(user.getFullname());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            model.addAttribute("updateInfoDTO", dto);
        }
        return "user/pages/information";
    }

    @PostMapping("/information")
    public String updateInformation(
            @Valid @ModelAttribute("updateInfoDTO") UpdateInfoDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return "user/pages/information";
        }

        Long userId = userDetails.getUser().getId();

        try {
            userService.updateInformation(userId, dto);
        } catch (IllegalArgumentException ex) {
            String message = ex.getMessage();
            if (message.contains("Email")) {
                bindingResult.rejectValue("email", null, message);
            } else if (message.contains("Mật khẩu")) {
                bindingResult.rejectValue("confirmPassword", null, message);
            } else if (message.contains("Người dùng")) {
                model.addAttribute("error", message);
            } else {
                model.addAttribute("error", "Đã xảy ra lỗi: " + message);
            }
            
            return "user/pages/information";
        }

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/information";
    }
}
