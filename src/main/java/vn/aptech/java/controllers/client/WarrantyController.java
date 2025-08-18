package vn.aptech.java.controllers.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.aptech.java.services.CustomerLaptopService;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.RequestService;
import vn.aptech.java.utils.ImgUploadUtil;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import vn.aptech.java.dtos.client.WarrantyRequestDTO;
import vn.aptech.java.models.*;

@Controller
public class WarrantyController {
    @Autowired
    private PartService partService;

    @Autowired
    private CustomerLaptopService customerLaptopService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/history")
    public String viewHistory(Model model,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long customerId = customUserDetails.getUser().getId();
        List<Request> history = requestService.getHistoryByCustomerId(customerId);
        model.addAttribute("historyList", history);
        return "user/pages/history";
    }

    @GetMapping("/check-warranty")
    public String checkWarranty(
            @RequestParam(value = "serialNumber", required = false) String serialNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model) {

        Long customerId = customUserDetails.getUser().getId();
        List<CustomerLaptop> laptops = customerLaptopService.getLaptopsByCustomerIdAndSerial(customerId, serialNumber);
        model.addAttribute("laptops", laptops);
        return "user/pages/check_warranty";
    }

    @GetMapping("/schedule-warranty")
    public String showScheduleForm(Model model) {
        model.addAttribute("warrantyRequestDTO", new WarrantyRequestDTO());
        return "user/pages/schedule_warranty";
    }

    @PostMapping("/schedule-warranty")
    public String scheduleWarranty(
            @Valid @ModelAttribute("warrantyRequestDTO") WarrantyRequestDTO dto,
            BindingResult result,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            return "user/pages/schedule_warranty";
        }

        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    if (!ImgUploadUtil.isValidImageFormat(file)) {
                        model.addAttribute("imageError",
                                "Định dạng ảnh không hợp lệ. Chỉ cho phép: jpg, jpeg, png, gif, bmp");
                        model.addAttribute("warrantyRequestDTO", dto);
                        return "user/pages/schedule_warranty";
                    }
                }
            }
        }

        try {
            requestService.createScheduleRequest(dto, images);
            redirectAttributes.addFlashAttribute("success", "Đặt lịch thành công!");
            return "redirect:/schedule-warranty";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("warrantyRequestDTO", dto);
            return "user/pages/schedule_warranty";
        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi upload ảnh: " + e.getMessage());
            return "user/pages/schedule_warranty";
        }
    }

    @GetMapping("/search-parts")
    public String searchParts(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Part> parts = partService.searchByName(keyword);
        model.addAttribute("parts", parts);
        model.addAttribute("keyword", keyword);
        return "user/pages/search_parts";
    }

}
