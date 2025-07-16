package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String index(Model model,
                        @RequestParam(value = "search", required = false) String search) {
        model.addAttribute("activePage", "model");
        model.addAttribute("models", modelService.filterModels(search));
        model.addAttribute("search", search);
        return "admin/pages/model/index";
    }
    @GetMapping("/model/create")
    public String create(Model model) {
        model.addAttribute("activePage", "model");
        model.addAttribute("model", new vn.aptech.java.dtos.CreateModelDTO());
        return "admin/pages/model/create";
    }
    @PostMapping("/model/create")
    public String store(@Valid @ModelAttribute("model") vn.aptech.java.dtos.CreateModelDTO createModelDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "model");
            return "admin/pages/model/create";
        }
        try {
            modelService.createModel(createModelDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm model thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "model");
            model.addAttribute("error", "Có lỗi xảy ra khi thêm model: " + e.getMessage());
            model.addAttribute("model", createModelDTO);
            return "admin/pages/model/create";
        }
        return "redirect:/admin/model";
    }
    @GetMapping("/model/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<vn.aptech.java.models.Model> modelEntityOpt = modelService.getModelById(id);
        if (modelEntityOpt.isPresent()) {
            model.addAttribute("activePage", "model");
            model.addAttribute("model", modelEntityOpt.get());
            return "admin/pages/model/update";
        } else {
            redirectAttributes.addFlashAttribute("error", "Model không tồn tại");
            return "redirect:/admin/model";
        }
    }
    @PostMapping("/model/update")
    public String update(@Valid @ModelAttribute("model") vn.aptech.java.dtos.UpdateModelDTO updateModelDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "model");
            return "admin/pages/model/update";
        }
        try {
            modelService.updateModel(updateModelDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật model thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "model");
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật model: " + e.getMessage());
            model.addAttribute("model", updateModelDTO);
            return "admin/pages/model/update";
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
}
