package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.Part;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;
import vn.aptech.java.services.LaptopService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class PartController {

    @Autowired
    private PartService partService;

    @Autowired
    private PartTypeService partTypeService;

    @Autowired
    private LaptopService laptopService;

    @GetMapping("/part")
    public String index(Model model) {
        model.addAttribute("activePage", "part");
        model.addAttribute("parts", partService.getAllParts());
        return "admin/pages/part/index";
    }

    @GetMapping("/part/create")
    public String create(Model model) {
        model.addAttribute("activePage", "part");
        model.addAttribute("part", new Part());
        model.addAttribute("partTypes", partTypeService.getAllPartTypes());
        model.addAttribute("laptops", laptopService.getAllLaptops());
        return "admin/pages/part/create";
    }

    @PostMapping("/part/create")
    public String store(@ModelAttribute Part part, RedirectAttributes redirectAttributes) {
        try {
            if (part.getName() == null || part.getName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên linh kiện không được để trống!");
                return "redirect:/admin/part/create";
            }

            partService.createPart(part);
            redirectAttributes.addFlashAttribute("success", "Thêm linh kiện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm linh kiện: " + e.getMessage());
        }
        return "redirect:/admin/part";
    }

    @GetMapping("/part/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Part> part = partService.getPartById(id);
        if (part.isPresent()) {
            model.addAttribute("activePage", "part");
            model.addAttribute("part", part.get());
            model.addAttribute("partTypes", partTypeService.getAllPartTypes());
            model.addAttribute("laptops", laptopService.getAllLaptops());
            return "admin/pages/part/update";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy linh kiện!");
            return "redirect:/admin/part";
        }
    }

    @PostMapping("/part/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Part part, RedirectAttributes redirectAttributes) {
        try {
            if (part.getName() == null || part.getName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên linh kiện không được để trống!");
                return "redirect:/admin/part/edit/" + id;
            }

            Part updatedPart = partService.updatePart(id, part);
            if (updatedPart != null) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật linh kiện thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy linh kiện để cập nhật!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật linh kiện: " + e.getMessage());
        }
        return "redirect:/admin/part";
    }

    @PostMapping("/part/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partService.deletePart(id);
            redirectAttributes.addFlashAttribute("success", "Xóa linh kiện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa linh kiện: " + e.getMessage());
        }
        return "redirect:/admin/part";
    }

    @GetMapping("/part/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("activePage", "part");
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("parts", partService.searchParts(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("parts", partService.getAllParts());
        }
        return "admin/pages/part/index";
    }
}
