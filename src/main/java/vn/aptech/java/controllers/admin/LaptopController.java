package vn.aptech.java.controllers.admin;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreateLaptopDTO;
import vn.aptech.java.dtos.UpdateLaptopDTO;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.services.LaptopService;
import vn.aptech.java.services.ModelService;
import vn.aptech.java.utils.ImgUploadUtil;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping("/admin/laptop")
public class LaptopController {
    @Autowired
    LaptopService laptopService;
    @Autowired
    private ModelService modelService;
    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("activePage", "laptop");
        model.addAttribute("laptops", laptopService.getLaptops(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/pages/laptop/index";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("activePage", "laptop");
        model.addAttribute("laptop", new CreateLaptopDTO());
        model.addAttribute("models", modelService.getModels(null));
        return "admin/pages/laptop/create";
    }
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("laptop") CreateLaptopDTO createLaptopDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("models", modelService.getModels(null));
            model.addAttribute("laptop", createLaptopDTO);
            return "admin/pages/laptop/create";
        }
        try {
            if(createLaptopDTO.getImgFile() != null && !createLaptopDTO.getImgFile().isEmpty()) {
                if(!ImgUploadUtil.isValidImageFormat(createLaptopDTO.getImgFile())){
                    model.addAttribute("activePage", "laptop");
                    bindingResult.rejectValue("imgFile", "imgFile.invalidType",
                            "Định dạng ảnh không hợp lệ. Vui lòng tải lên ảnh có định dạng jpg, jpeg, png hoặc webp.");
                    model.addAttribute("models", modelService.getModels(null));
                    model.addAttribute("laptop", createLaptopDTO);
                    return "admin/pages/laptop/create";
                }
                createLaptopDTO.setImgUrl(ImgUploadUtil.saveFile(createLaptopDTO.getImgFile(), "laptops"));
            }
            laptopService.createLaptop(createLaptopDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm laptop thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("error", "Có lỗi xảy ra khi thêm laptop: " + e.getMessage());
            model.addAttribute("models", modelService.getModels(null));
            model.addAttribute("laptop", createLaptopDTO);
            return "admin/pages/laptop/create";
        }
        return "redirect:/admin/laptop";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Laptop> laptopOpt = laptopService.getLaptopById(id);
            if (laptopOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy laptop!");
                return "redirect:/admin/laptop";
            }
            UpdateLaptopDTO updateLaptopDTO = new UpdateLaptopDTO();
            updateLaptopDTO.setId(laptopOpt.get().getId());
            updateLaptopDTO.setName(laptopOpt.get().getName());
            updateLaptopDTO.setModelId(laptopOpt.get().getModel().getId());
            updateLaptopDTO.setImgUrl(laptopOpt.get().getImgUrl());
            updateLaptopDTO.setWarrantyPeriod(laptopOpt.get().getWarrantyPeriod());
            model.addAttribute("activePage", "laptop");
            model.addAttribute("laptop", updateLaptopDTO);
            model.addAttribute("models", modelService.getModels(null));
            return "admin/pages/laptop/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi truy cập laptop: " + e.getMessage());
            return "redirect:/admin/laptop";
        }
    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("laptop") UpdateLaptopDTO updateLaptopDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("models", modelService.getModels(null));
            model.addAttribute("laptop", updateLaptopDTO);
            return "admin/pages/laptop/edit";
        }

        try {
            Optional<Laptop> existingLaptopOpt = laptopService.getLaptopById(updateLaptopDTO.getId());
            String oldImgUrl = existingLaptopOpt.map(Laptop::getImgUrl).orElse(null);
            if ((updateLaptopDTO.getImgUrl() == null || updateLaptopDTO.getImgUrl().isEmpty())
                    && (updateLaptopDTO.getImgFile() == null || updateLaptopDTO.getImgFile().isEmpty())) {
                if (oldImgUrl != null) {
                    updateLaptopDTO.setImgUrl(oldImgUrl);
                }
            }
            if (updateLaptopDTO.getImgUrl() != null && !updateLaptopDTO.getImgUrl().isEmpty()
                    && (updateLaptopDTO.getImgFile() == null || updateLaptopDTO.getImgFile().isEmpty())) {
                if (oldImgUrl != null && !oldImgUrl.equals(updateLaptopDTO.getImgUrl())
                        && !oldImgUrl.startsWith("http")) {
                    ImgUploadUtil.deleteFile(oldImgUrl);
                }
            }
            if (updateLaptopDTO.getImgFile() != null && !updateLaptopDTO.getImgFile().isEmpty()) {
                if (!ImgUploadUtil.isValidImageFormat(updateLaptopDTO.getImgFile())) {
                    model.addAttribute("activePage", "laptop");
                    bindingResult.rejectValue("imgFile", "imgFile.invalidType",
                            "Định dạng ảnh không hợp lệ. Vui lòng tải lên ảnh có định dạng jpg, jpeg, png hoặc webp.");
                    model.addAttribute("models", modelService.getModels(null));
                    model.addAttribute("laptop", updateLaptopDTO);
                    return "admin/pages/laptop/edit";
                }
                if (oldImgUrl != null && !oldImgUrl.startsWith("http")) {
                    ImgUploadUtil.deleteFile(oldImgUrl);
                }
                updateLaptopDTO.setImgUrl(ImgUploadUtil.saveFile(updateLaptopDTO.getImgFile(), "laptops"));
            }

            laptopService.updateLaptop(updateLaptopDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật laptop thành công!");

        } catch (Exception e) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật laptop: " + e.getMessage());
            model.addAttribute("models", modelService.getModels(null));
            model.addAttribute("laptop", updateLaptopDTO);
            return "admin/pages/laptop/edit";
        }

        return "redirect:/admin/laptop";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Laptop> laptopOpt = laptopService.getLaptopById(id);
            if (laptopOpt.isPresent()) {
                Laptop laptop = laptopOpt.get();
                if (laptop.getImgUrl() != null && !laptop.getImgUrl().startsWith("http")) {
                    ImgUploadUtil.deleteFile(laptop.getImgUrl());
                }
                laptopService.deleteLaptop(id);
                redirectAttributes.addFlashAttribute("success", "Xóa laptop thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy laptop để xóa!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa laptop: " + e.getMessage());
        }
        return "redirect:/admin/laptop";
    }
}

