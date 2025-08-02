package vn.aptech.java.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;

import vn.aptech.java.dtos.client.RegisterDTO;
import vn.aptech.java.dtos.client.UpdateInfoDTO;
import vn.aptech.java.models.*;
import vn.aptech.java.repositories.UserRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "user/pages/register";
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);

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
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return "user/pages/information";
        }

        Long userId = userDetails.getUser().getId();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy người dùng!");
            return "user/pages/information";
        }

        if (!dto.getNewPassword().isEmpty() || !dto.getConfirmPassword().isEmpty()) {
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                model.addAttribute("error", "Mật khẩu mới và xác nhận không khớp!");
                return "user/pages/information";
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        user.setUsername(dto.getUsername());
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        userRepository.save(user);

        model.addAttribute("success", "Cập nhật thông tin thành công!");
        return "user/pages/information";
    }

}
