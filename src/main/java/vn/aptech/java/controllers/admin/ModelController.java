package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreateModelDTO;
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
        model.addAttribute("model", new CreateModelDTO());
        return "admin/pages/model/create";
    }

    @PostMapping("/model/create")
    public String store(@Valid @ModelAttribute("model") CreateModelDTO createModelDTO,
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
            return "admin/pages/model/create";
        }
        return "redirect:/admin/model";
    }

    @GetMapping("/model/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<vn.aptech.java.models.Model> modelEntityOpt = modelService.getModelById(id);
        if (modelEntityOpt.isPresent()) {
            vn.aptech.java.models.Model modelEntity = modelEntityOpt.get();
            CreateModelDTO modelDTO = new CreateModelDTO();
            modelDTO.setName(modelEntity.getName());

            model.addAttribute("activePage", "model");
            model.addAttribute("model", modelDTO);
            model.addAttribute("modelId", id);
            return "admin/pages/model/update";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy model!");
            return "redirect:/admin/model";
        }
    }

    @PostMapping("/model/edit/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("model") CreateModelDTO createModelDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("=== UPDATE MODEL DEBUG ===");
        System.out.println("ID: " + id);
        System.out.println("DTO Name: " + createModelDTO.getName());
        System.out.println("Has errors: " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "model");
            model.addAttribute("modelId", id);
            return "admin/pages/model/update";
        }

        try {
            vn.aptech.java.models.Model updatedModel = modelService.updateModel(id, createModelDTO);
            if (updatedModel != null) {
                System.out.println("Update successful!");
                redirectAttributes.addFlashAttribute("success", "Cập nhật model thành công!");
            } else {
                System.out.println("Update failed - model not found!");
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy model để cập nhật!");
            }
        } catch (Exception e) {
            System.out.println("Update exception: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("activePage", "model");
            model.addAttribute("modelId", id);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật model: " + e.getMessage());
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
