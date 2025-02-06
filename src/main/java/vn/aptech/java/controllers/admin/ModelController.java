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
    public String getAllModel(Model model) {
        model.addAttribute("models", modelService.getAllModels());
        return "admin/model/index";
    }
    @GetMapping("/add-model")
    public String addModel(Model model) {
        model.addAttribute("model", new vn.aptech.java.models.Model());
        return "admin/model/add";
    }
    @PostMapping("/add-model")
    public String addModel(@Valid @ModelAttribute("model") vn.aptech.java.models.Model model,
                              BindingResult result, Model viewModel, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/model/add";
        }

        modelService.addModel(model);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm thành công!");
        return "redirect:/admin/model";
    }

    @GetMapping("/update-model/{id}")
    public String updateModel(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<vn.aptech.java.models.Model> existingModel = modelService.findModelById(id);
        if (existingModel.isPresent()) {
            model.addAttribute("model", existingModel.get());
            return "admin/model/update";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy model!");
            return "redirect:/admin/model";
        }
    }
    @PostMapping("/update-model")
    public String updateModel(@Valid @ModelAttribute("model") vn.aptech.java.models.Model model,
                              BindingResult result, Model viewModel, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/model/update";
        }
        modelService.updateModel(model);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
        return "redirect:/admin/model";
    }
}
