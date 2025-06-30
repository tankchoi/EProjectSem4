package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.services.ModelService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @GetMapping("/model")
    public String index(Model model) {
        model.addAttribute("activePage", "model");
        model.addAttribute("models", modelService.getAllModels());
        return "admin/pages/model/index";
    }

    @GetMapping("/model/create")
    public String create(Model model) {
        model.addAttribute("activePage", "model");
        model.addAttribute("model", new vn.aptech.java.models.Model());
        return "admin/pages/model/create";
    }

    @PostMapping("/model/create")
    public String store(@ModelAttribute vn.aptech.java.models.Model modelEntity,
            RedirectAttributes redirectAttributes) {
        try {
            if (modelEntity.getName() == null || modelEntity.getName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên model không được để trống!");
                return "redirect:/admin/model/create";
            }

            modelService.createModel(modelEntity);
            redirectAttributes.addFlashAttribute("success", "Thêm model thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm model: " + e.getMessage());
        }
        return "redirect:/admin/model";
    }

    @GetMapping("/model/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<vn.aptech.java.models.Model> modelEntity = modelService.getModelById(id);
        if (modelEntity.isPresent()) {
            model.addAttribute("activePage", "model");
            model.addAttribute("model", modelEntity.get());
            return "admin/pages/model/update";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy model!");
            return "redirect:/admin/model";
        }
    }

    @PostMapping("/model/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute vn.aptech.java.models.Model modelEntity,
            RedirectAttributes redirectAttributes) {
        try {
            if (modelEntity.getName() == null || modelEntity.getName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên model không được để trống!");
                return "redirect:/admin/model/edit/" + id;
            }

            vn.aptech.java.models.Model updatedModel = modelService.updateModel(id, modelEntity);
            if (updatedModel != null) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật model thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy model để cập nhật!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật model: " + e.getMessage());
        }
        return "redirect:/admin/model";
    }

    @PostMapping("/model/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modelService.deleteModel(id);
            redirectAttributes.addFlashAttribute("success", "Xóa model thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa model: " + e.getMessage());
        }
        return "redirect:/admin/model";
    }

    @GetMapping("/model/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("activePage", "model");
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("models", modelService.searchModels(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("models", modelService.getAllModels());
        }
        return "admin/pages/model/index";
    }
}
