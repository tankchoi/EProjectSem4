package vn.aptech.java.controllers.admin;

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
import vn.aptech.java.dtos.CreateCustomerDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.services.CustomerService;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer")
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

        Page<User> customers;
        if (search != null && !search.trim().isEmpty()) {
            customers = customerService.searchCustomers(search, status, pageable);
        } else {
            customers = customerService.getAllCustomers(status, pageable);
        }

        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("totalElements", customers.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "admin/pages/customer/index";
    }

    @GetMapping("/customer/create")
    public String create(Model model) {
        model.addAttribute("customer", new CreateCustomerDTO());
        return "admin/pages/customer/create";
    }

    @PostMapping("/customer")
    public String store(@Valid @ModelAttribute("customer") CreateCustomerDTO createCustomerDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/pages/customer/create";
        }

        try {
            User customer = customerService.createCustomer(createCustomerDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Khách hàng '" + customer.getUsername() + "' đã được tạo thành công!");
            return "redirect:/admin/customer";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi tạo khách hàng: " + e.getMessage());
            return "admin/pages/customer/create";
        }
    }

    @GetMapping("/customer/{id}")
    public String show(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User customer = customerService.getCustomerById(id);
            model.addAttribute("customer", customer);
            return "admin/pages/customer/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khách hàng!");
            return "redirect:/admin/customer";
        }
    }

    @GetMapping("/customer/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User customer = customerService.getCustomerById(id);
            CreateCustomerDTO customerDTO = new CreateCustomerDTO();
            customerDTO.setUsername(customer.getUsername());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setPhone(customer.getPhone());
            customerDTO.setFullname(customer.getFullname());

            model.addAttribute("customer", customerDTO);
            model.addAttribute("customerId", id);
            return "admin/pages/customer/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khách hàng!");
            return "redirect:/admin/customer";
        }
    }

    @PostMapping("/customer/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("customer") CreateCustomerDTO createCustomerDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerId", id);
            return "admin/pages/customer/edit";
        }

        try {
            User customer = customerService.updateCustomer(id, createCustomerDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Khách hàng '" + customer.getUsername() + "' đã được cập nhật thành công!");
            return "redirect:/admin/customer";
        } catch (Exception e) {
            model.addAttribute("customerId", id);
            model.addAttribute("errorMessage", "Lỗi khi cập nhật khách hàng: " + e.getMessage());
            return "admin/pages/customer/edit";
        }
    }

    @PostMapping("/customer/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa khách hàng: " + e.getMessage());
        }
        return "redirect:/admin/customer";
    }

    @PostMapping("/customer/{id}/restore")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.restoreCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được khôi phục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi khôi phục khách hàng: " + e.getMessage());
        }
        return "redirect:/admin/customer";
    }
}
