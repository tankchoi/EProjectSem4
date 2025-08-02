package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.admin.UpdateModelDTO;
import vn.aptech.java.dtos.admin.CreateModelDTO;
import vn.aptech.java.services.ModelService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/model")
public class ModelController {
    @Autowired
    private ModelService modelService;
    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("activePage", "model");
        model.addAttribute("models", modelService.getModels(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/pages/model/index";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("activePage", "model");
        model.addAttribute("model", new CreateModelDTO());
        return "admin/pages/model/create";
    }
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("model") CreateModelDTO createModelDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "model");
            model.addAttribute("model", createModelDTO);
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
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            Optional<vn.aptech.java.models.Model> modelEntityOpt = modelService.getModelById(id);
            if (modelEntityOpt.isPresent()) {
                UpdateModelDTO updateModelDTO = new UpdateModelDTO();
                updateModelDTO.setId(modelEntityOpt.get().getId());
                updateModelDTO.setName(modelEntityOpt.get().getName());
                model.addAttribute("activePage", "model");
                model.addAttribute("model", updateModelDTO);
                return "admin/pages/model/edit";
            } else {
                redirectAttributes.addFlashAttribute("error", "Model không tồn tại");
                return "redirect:/admin/model";
            }
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lấy thông tin model: " + e.getMessage());
            return "redirect:/admin/model";
        }

    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("model") UpdateModelDTO updateModelDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "model");
            model.addAttribute("model", updateModelDTO);
            return "admin/pages/model/edit";
        }
        try {
            modelService.updateModel(updateModelDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật model thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "model");
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật model: " + e.getMessage());
            model.addAttribute("model", updateModelDTO);
            return "admin/pages/model/edit";
        }
        return "redirect:/admin/model";
    }
    @PostMapping("/delete/{id}")
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
