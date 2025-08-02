package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.aptech.java.dtos.admin.UpdatePasswordDTO;
import vn.aptech.java.dtos.admin.UpdateProfileDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.services.UserService;

@Controller
@RequestMapping("/admin/profile")
public class ProfileController {
    @Autowired
    private UserService userService;
    
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("activePage", "profile");
        try{
            User user = userService.getCurrentUser();
            UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
            updateProfileDTO.setFullname(user.getFullname());
            updateProfileDTO.setEmail(user.getEmail());
            updateProfileDTO.setPhone(user.getPhone());
            model.addAttribute("updateProfileDTO", updateProfileDTO);
            UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
            model.addAttribute("updatePasswordDTO", updatePasswordDTO);
            return "admin/pages/profile/index";
        }catch (Exception e) {
            model.addAttribute("error", "Lỗi khi lấy thông tin người dùng: " + e.getMessage());
            return "admin/pages/profile/index";
        }
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("updateProfileDTO") UpdateProfileDTO updateProfileDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                                Model model) {
        model.addAttribute("activePage", "profile");
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateProfileDTO", updateProfileDTO);
            return "admin/pages/profile/index";
        }
        try{
            userService.updateProfile(updateProfileDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("updatePasswordDTO") UpdatePasswordDTO updatePasswordDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                 Model model) {
        model.addAttribute("activePage", "profile");
        if (bindingResult.hasErrors()) {
            User user = userService.getCurrentUser();
            UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
            updateProfileDTO.setFullname(user.getFullname());
            updateProfileDTO.setEmail(user.getEmail());
            updateProfileDTO.setPhone(user.getPhone());
            model.addAttribute("updateProfileDTO", updateProfileDTO);
            model.addAttribute("updatePasswordDTO", updatePasswordDTO);
            return "admin/pages/profile/index";
        }
        try{
            userService.updatePassword(updatePasswordDTO.getNewPassword());
            redirectAttributes.addFlashAttribute("success", "Cập nhật mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
}
