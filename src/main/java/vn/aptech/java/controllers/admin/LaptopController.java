package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.services.LaptopService;
import vn.aptech.java.services.ModelService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class LaptopController {

    @Autowired
    private LaptopService laptopService;

    @Autowired
    private ModelService modelService;

    @GetMapping("/laptop")
    public String index(Model model) {
        model.addAttribute("activePage", "laptop");
        model.addAttribute("laptops", laptopService.getAllLaptops());
        return "admin/pages/laptop/index";
    }

    @GetMapping("/laptop/create")
    public String create(Model model) {
        model.addAttribute("activePage", "laptop");
        model.addAttribute("laptop", new Laptop());
        model.addAttribute("models", modelService.getAllModels());
        return "admin/pages/laptop/create";
    }

    @PostMapping("/laptop/create")
    public String store(@ModelAttribute Laptop laptop, RedirectAttributes redirectAttributes) {
        try {
            laptopService.createLaptop(laptop);
            redirectAttributes.addFlashAttribute("success", "Thêm laptop thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm laptop!");
        }
        return "redirect:/admin/laptop";
    }

    @GetMapping("/laptop/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Laptop> laptop = laptopService.getLaptopById(id);
        if (laptop.isPresent()) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("laptop", laptop.get());
            model.addAttribute("models", modelService.getAllModels());
            return "admin/pages/laptop/update";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy laptop!");
            return "redirect:/admin/laptop";
        }
    }

    @PostMapping("/laptop/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Laptop laptop, RedirectAttributes redirectAttributes) {
        try {
            Laptop updatedLaptop = laptopService.updateLaptop(id, laptop);
            if (updatedLaptop != null) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật laptop thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy laptop để cập nhật!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật laptop!");
        }
        return "redirect:/admin/laptop";
    }

    @PostMapping("/laptop/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            laptopService.deleteLaptop(id);
            redirectAttributes.addFlashAttribute("success", "Xóa laptop thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa laptop!");
        }
        return "redirect:/admin/laptop";
    }

    @GetMapping("/laptop/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("activePage", "laptop");
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("laptops", laptopService.searchLaptops(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("laptops", laptopService.getAllLaptops());
        }
        return "admin/pages/laptop/index";
    }
}
