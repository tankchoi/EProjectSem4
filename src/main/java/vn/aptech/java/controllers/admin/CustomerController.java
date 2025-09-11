package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.User;
import vn.aptech.java.services.CustomerService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> customerPage;

            if (search != null && !search.trim().isEmpty()) {
                customerPage = customerService.searchCustomersPaginated(search.trim(), pageable);
            } else {
                customerPage = customerService.getAllCustomersPaginated(pageable);
            }

            model.addAttribute("activePage", "customer");
            model.addAttribute("customerList", customerPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", customerPage.getTotalPages());
            model.addAttribute("totalElements", customerPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("search", search);

            return "admin/pages/customer/index";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách khách hàng: " + e.getMessage());
            model.addAttribute("activePage", "customer");
            return "admin/pages/customer/index";
        }
    }

    @GetMapping("/{id}")
    public String customerDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> customerOpt = customerService.getCustomerById(id);
            if (customerOpt.isPresent()) {
                User customer = customerOpt.get();

                model.addAttribute("activePage", "customer");
                model.addAttribute("customer", customer);
                model.addAttribute("customerLaptops", customerService.getCustomerLaptops(id));
                // model.addAttribute("warrantyHistory",
                // customerService.getWarrantyHistory(id)); // TODO: Thêm khi có Warranty model

                return "admin/pages/customer/view";
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng!");
                return "redirect:/admin/customer";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi truy xuất thông tin khách hàng: " + e.getMessage());
            return "redirect:/admin/customer";
        }
    }

    @PostMapping("/{id}/ban")
    public String banCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.banCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Khóa tài khoản khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi khóa tài khoản: " + e.getMessage());
        }
        return "redirect:/admin/customer";
    }

    @PostMapping("/{id}/restore")
    public String restoreCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.restoreCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Khôi phục tài khoản khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi khôi phục tài khoản: " + e.getMessage());
        }
        return "redirect:/admin/customer";
    }
}
