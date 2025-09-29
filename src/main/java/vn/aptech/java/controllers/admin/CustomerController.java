package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.User;
import vn.aptech.java.services.CustomerLaptopService;
import vn.aptech.java.services.CustomerService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerLaptopService customerLaptopService;

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
        List<CustomerLaptop> laptops = customerLaptopService.getLaptopsByCustomer(id);

        model.addAttribute("activePage", "customer");
        model.addAttribute("customer", customer);
        model.addAttribute("customerLaptops", laptops);

        return "admin/pages/customer/view";
    }

}
