package vn.aptech.java.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.aptech.java.models.User;
import vn.aptech.java.services.CustomerService;

import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/search")
    public String searchForm() {
        return "customers/search"; // => resources/templates/customers/search.html
    }

    @PostMapping("/search")
    public String searchCustomer(@RequestParam("phone") String phone, Model model) {
        Optional<User> customerOpt = customerService.findByPhone(phone);

        if (customerOpt.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy khách hàng với số điện thoại: " + phone);
            return "customers/search";
        }

        User customer = customerOpt.get();
        model.addAttribute("customer", customer);
        model.addAttribute("laptops", customerService.getCustomerLaptops(customer));
        model.addAttribute("requests", customerService.getRequestHistory(customer));

        return "customers/detail"; // => resources/templates/customers/detail.html
    }

    @GetMapping("/{id}")
    public String customerDetail(@PathVariable Long id, Model model) {
        Optional<User> customerOpt = customerService.findById(id);

        if (customerOpt.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy khách hàng với ID: " + id);
            return "customers/search";
        }

        User customer = customerOpt.get();
        model.addAttribute("customer", customer);
        model.addAttribute("laptops", customerService.getCustomerLaptops(customer));
        model.addAttribute("requests", customerService.getRequestHistory(customer));

        return "customers/detail"; // => resources/templates/customers/detail.html
    }
}
