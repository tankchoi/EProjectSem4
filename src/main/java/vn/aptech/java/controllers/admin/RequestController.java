package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.admin.CreatePartDTO;
import vn.aptech.java.dtos.admin.CreateRequestDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDTO;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestImg;
import vn.aptech.java.services.RequestImgService;
import vn.aptech.java.services.RequestService;
import vn.aptech.java.services.UserService;
import vn.aptech.java.utils.ImgUploadUtil;

import java.util.Optional;

@Controller
@RequestMapping("/admin/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestImgService requestImgService;
    @Autowired
    private UserService userService;
    @GetMapping
    public String index() {
        return "admin/pages/request/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("activePage", "request");
        model.addAttribute("request", new CreateRequestDTO());
        model.addAttribute("technicians", userService.getAllTechnicians());
        return "admin/pages/request/create";
    }
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("request") CreateRequestDTO createRequestDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            if (bindingResult.hasErrors()) {
                System.out.println("Binding errors: " + bindingResult.getAllErrors());
                model.addAttribute("activePage", "request");
                model.addAttribute("technicians", userService.getAllTechnicians());
                model.addAttribute("request", createRequestDTO);
                return "admin/pages/request/create";
            }
            requestService.createRequest(createRequestDTO);
            redirectAttributes.addFlashAttribute("success", "Yêu cầu đã được tạo thành công.");
            return "redirect:/admin/request";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "request");
            model.addAttribute("request", createRequestDTO);
            model.addAttribute("technicians", userService.getAllTechnicians());
            return "admin/pages/request/create";
        }
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "request");
        try{
            Optional<Request> RequestOpt = requestService.getRequestById(id);
            if (RequestOpt.isPresent()) {
                Request request = RequestOpt.get();
                UpdateRequestDTO updateRequestDTO = new UpdateRequestDTO();
                updateRequestDTO.setId(request.getId());
                updateRequestDTO.setSerialNumber(request.getCustomerLaptop().getSerialNumber());
                updateRequestDTO.setFullname(request.getFullname());
                updateRequestDTO.setEmail(request.getEmail());
                updateRequestDTO.setPhone(request.getPhone());
                updateRequestDTO.setAddress(request.getAddress());
                updateRequestDTO.setDescription(request.getDescription());
                updateRequestDTO.setBookingDate(request.getBookingDate());
                updateRequestDTO.setStatus(request.getStatus());
                updateRequestDTO.setTechnicianId(request.getTechnician() != null ? request.getTechnician().getId() : null);
                updateRequestDTO.setExistingImageUrls(
                        requestImgService.getRequestImgByRequestId(id).stream()
                                .map(RequestImg::getImgUrl)
                                .toList()
                );
                model.addAttribute("request", updateRequestDTO);
                model.addAttribute("technicians", userService.getAllTechnicians());
                return "admin/pages/request/edit";
            } else {
                redirectAttributes.addFlashAttribute("error", "Yêu cầu không tồn tại: " + id);
                return "redirect:/admin/request";
            }
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi truy xuất yêu cầu: " + id);
            return "redirect:/admin/request";
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("request") UpdateRequestDTO updateRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "request");
            model.addAttribute("technicians", userService.getAllTechnicians());
            model.addAttribute("request", updateRequestDTO);
            System.out.println("newImageUrls: " + updateRequestDTO.getNewImages());
            System.out.println("Error: " + bindingResult.getFieldErrors());
            System.out.println("Binding errors: " + bindingResult.getAllErrors());
            return "admin/pages/request/edit";
        }
        try {
            requestService.updateRequest(updateRequestDTO);
            redirectAttributes.addFlashAttribute("success", "Yêu cầu đã được cập nhật thành công.");
            return "redirect:/admin/request";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "request");
            model.addAttribute("technicians", userService.getAllTechnicians());
            model.addAttribute("request", updateRequestDTO);
            return "admin/pages/request/edit";
        }
    }

}
