package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.User;
import vn.aptech.java.services.FileUploadService;
import vn.aptech.java.services.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model) {
        // Lấy thông tin user hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Get full user details
        User user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);
        }

        model.addAttribute("activePage", "profile");
        model.addAttribute("username", username);

        return "admin/pages/profile/index";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            RedirectAttributes redirectAttributes) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userService.findByUsername(username);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng!");
                return "redirect:/admin/profile";
            }
            String finalImgUrl = user.getImg_link(); // Keep existing image by default

            // Handle image upload or URL
            if (imageFile != null && !imageFile.isEmpty()) {
                if (fileUploadService.isValidImageFile(imageFile)) {
                    // Delete old image if it was uploaded before
                    if (user.getImg_link() != null && user.getImg_link().startsWith("/images/")) {
                        fileUploadService.deleteFile(user.getImg_link());
                    }

                    finalImgUrl = fileUploadService.saveFile(imageFile, "users");
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "File không hợp lệ. Chỉ chấp nhận các file ảnh (jpg, png, gif, bmp, webp).");
                    return "redirect:/admin/profile";
                }
            } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                if (fileUploadService.isValidImageUrl(imageUrl)) {
                    // Validate and truncate URL if necessary for database compatibility
                    finalImgUrl = fileUploadService.validateAndTruncateUrl(imageUrl);
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "URL ảnh không hợp lệ hoặc quá dài (tối đa 255 ký tự).");
                    return "redirect:/admin/profile";
                }
            }

            // Update user info
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone(phone);
            user.setImg_link(finalImgUrl);

            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật thông tin: " + e.getMessage());
        }

        return "redirect:/admin/profile";
    }
}
