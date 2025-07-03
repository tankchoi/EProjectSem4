package vn.aptech.java.controllers.admin;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreateRequestDTO;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.RequestService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/request")
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Model model) {

        Sort sort = Sort.by(sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Request> requests;
        if (search != null && !search.trim().isEmpty()) {
            requests = requestService.searchRequests(search, status, pageable);
        } else {
            requests = requestService.getAllRequests(status, pageable);
        }

        model.addAttribute("requests", requests);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requests.getTotalPages());
        model.addAttribute("totalElements", requests.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("activePage", "request");

        return "admin/pages/request/index";
    }

    @GetMapping("/request/create")
    public String create(Model model) {
        model.addAttribute("request", new CreateRequestDTO());
        List<CustomerLaptop> customerLaptops = customerLaptopRepository.findAll();
        List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
        model.addAttribute("customerLaptops", customerLaptops);
        model.addAttribute("technicians", technicians);
        return "admin/pages/request/create";
    }

    @PostMapping("/request")
    public String store(@Valid @ModelAttribute("request") CreateRequestDTO createRequestDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            List<CustomerLaptop> customerLaptops = customerLaptopRepository.findAll();
            List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
            model.addAttribute("customerLaptops", customerLaptops);
            model.addAttribute("technicians", technicians);
            return "admin/pages/request/create";
        }

        try {
            Request request = requestService.createRequest(createRequestDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Yêu cầu bảo hành '" + request.getFullname() + "' đã được tạo thành công!");
            return "redirect:/admin/request";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi tạo yêu cầu: " + e.getMessage());
            List<CustomerLaptop> customerLaptops = customerLaptopRepository.findAll();
            List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
            model.addAttribute("customerLaptops", customerLaptops);
            model.addAttribute("technicians", technicians);
            return "admin/pages/request/create";
        }
    }

    @GetMapping("/request/{id}")
    public String show(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Request request = requestService.getRequestByIdOrThrow(id);
            model.addAttribute("request", request);

            // Load technicians for assignment if needed
            List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
            model.addAttribute("technicians", technicians);

            return "admin/pages/request/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy yêu cầu!");
            return "redirect:/admin/request";
        }
    }

    @GetMapping("/request/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            HttpServletResponse response) {
        // Add cache control headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        try {
            Request request = requestService.getRequestByIdOrThrow(id);
            CreateRequestDTO requestDTO = new CreateRequestDTO();
            requestDTO.setCustomerLaptopId(request.getCustomerLaptop().getId());
            requestDTO.setFullname(request.getFullname());
            requestDTO.setEmail(request.getEmail());
            requestDTO.setPhone(request.getPhone());
            requestDTO.setAddress(request.getAddress());
            requestDTO.setDescription(request.getDescription());
            requestDTO.setBookingDate(request.getBookingDate());
            requestDTO.setStatus(request.getStatus());
            requestDTO.setTechnicianId(request.getTechnician() != null ? request.getTechnician().getId() : null);

            model.addAttribute("request", requestDTO);
            model.addAttribute("requestId", id);
            List<CustomerLaptop> customerLaptops = customerLaptopRepository.findAll();
            List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
            model.addAttribute("customerLaptops", customerLaptops);
            model.addAttribute("technicians", technicians);
            return "admin/pages/request/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy yêu cầu!");
            return "redirect:/admin/request";
        }
    }

    @PostMapping("/request/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("request") CreateRequestDTO createRequestDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Xử lý technicianId nếu là chuỗi rỗng
        if (createRequestDTO.getTechnicianId() != null && createRequestDTO.getTechnicianId() == 0) {
            createRequestDTO.setTechnicianId(null);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("requestId", id);
            List<CustomerLaptop> customerLaptops = customerLaptopRepository.findAll();
            List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
            model.addAttribute("customerLaptops", customerLaptops);
            model.addAttribute("technicians", technicians);
            return "admin/pages/request/edit";
        }
        try {
            Request request = requestService.updateRequest(id, createRequestDTO);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Yêu cầu bảo hành '" + request.getFullname() + "' đã được cập nhật thành công!");

            return "redirect:/admin/request";
        } catch (Exception e) {
            model.addAttribute("requestId", id);
            model.addAttribute("errorMessage", "Lỗi khi cập nhật yêu cầu: " + e.getMessage());
            List<CustomerLaptop> customerLaptops = customerLaptopRepository.findAll();
            List<User> technicians = userRepository.findByRoleAndStatus(User.Role.STAFF, User.Status.ACTIVE);
            model.addAttribute("customerLaptops", customerLaptops);
            model.addAttribute("technicians", technicians);
            return "admin/pages/request/edit";
        }
    }

    @PostMapping("/request/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            requestService.deleteRequest(id);
            redirectAttributes.addFlashAttribute("successMessage", "Yêu cầu đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa yêu cầu: " + e.getMessage());
        }
        return "redirect:/admin/request";
    }

    @PostMapping("/request/{id}/assign-technician")
    public String assignTechnician(@PathVariable Long id,
            @RequestParam Long technicianId,
            RedirectAttributes redirectAttributes) {
        try {
            requestService.assignTechnician(id, technicianId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã gán kỹ thuật viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi gán kỹ thuật viên: " + e.getMessage());
        }
        return "redirect:/admin/request/" + id;
    }

    @PostMapping("/request/{id}/update-status")
    public String updateStatus(@PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        try {
            requestService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/request/" + id;
    }
}
