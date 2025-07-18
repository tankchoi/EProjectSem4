package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreatePartTypeDTO;
import vn.aptech.java.dtos.UpdatePartTypeDTO;
import vn.aptech.java.models.PartType;
import vn.aptech.java.services.PartTypeService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/part-type")
public class PartTypeController {
    @Autowired
    private PartTypeService partTypeService;
    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "search", required = false) String search) {
        model.addAttribute("activePage", "partTypes");
        model.addAttribute("search", search);
        model.addAttribute("partTypes", partTypeService.filterPartType(search));
        return "admin/pages/part_type/index";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("activePage", "partTypes");
        model.addAttribute("partType", new CreatePartTypeDTO());
        return "admin/pages/part_type/create";
    }
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("partType") vn.aptech.java.dtos.CreatePartTypeDTO createPartTypeDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "partTypes");
            return "admin/pages/part_type/create";
        }
        try {
            partTypeService.createPartType(createPartTypeDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm loại linh kiện thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "partTypes");
            model.addAttribute("error", "Có lỗi xảy ra khi thêm loại linh kiện: " + e.getMessage());
            model.addAttribute("partType", createPartTypeDTO);
            return "admin/pages/part_type/create";
        }
        return "redirect:/admin/part-type";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<PartType> partTypeOpt = partTypeService.getPartTypeById(id);
            if (partTypeOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy loại linh kiện!");
                return "redirect:/admin/part-type";
            }else{
                model.addAttribute("partType", partTypeOpt.get());
                model.addAttribute("activePage", "partTypes");
            }
            return "admin/pages/part_type/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy loại linh kiện!");
            return "redirect:/admin/part-type";
        }
    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("partType")UpdatePartTypeDTO updatePartTypeDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "partTypes");
            return "admin/pages/part_type/edit";
        }
        try {
            partTypeService.updatePartType(updatePartTypeDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật loại linh kiện thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "partTypes");
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật loại linh kiện: " + e.getMessage());
            model.addAttribute("partType", updatePartTypeDTO);
            return "admin/pages/part_type/edit";
        }
        return "redirect:/admin/part-type";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partTypeService.deletePartType(id);
            redirectAttributes.addFlashAttribute("success", "Xóa loại linh kiện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa loại linh kiện: " + e.getMessage());
        }
        return "redirect:/admin/part-type";
    }

}
