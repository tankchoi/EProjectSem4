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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(
            Model model) {
        List<User> customers = customerService.getAllCustomers();
        model.addAttribute("activePage", "customer");
        model.addAttribute("customerList", customers);
        return "admin/pages/customer/index";

    }

    @GetMapping("/search")
    public String searchCustomers(@RequestParam("phone") String phone, Model model) {
        List<User> customers = customerService.searchCustomersByPhone(phone);
        model.addAttribute("activePage", "customer");
        model.addAttribute("customerList", customers);
        model.addAttribute("search", phone);
        return "admin/pages/customer/index";
    }


    @GetMapping("/{id}")
    public String customerDetail(@PathVariable("id") Long id, Model model) {
        User customer = customerService.getCustomerById(id);
        model.addAttribute("activePage", "customer");
        model.addAttribute("customer", customer);
        model.addAttribute("customerLaptops", customerService.getCustomerLaptops(id));
        return "admin/pages/customer/view";
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
