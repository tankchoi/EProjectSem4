package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreateLaptopDTO;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.services.FileUploadService;
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

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/laptop")
    public String index(Model model) {
        model.addAttribute("activePage", "laptop");
        model.addAttribute("laptops", laptopService.getAllLaptops());
        return "admin/pages/laptop/index";
    }

    @GetMapping("/laptop/create")
    public String create(Model model) {
        model.addAttribute("activePage", "laptop");
        model.addAttribute("laptop", new CreateLaptopDTO());
        model.addAttribute("models", modelService.getAllModels());
        return "admin/pages/laptop/create";
    }

    @PostMapping("/laptop/create")
    public String store(@Valid @ModelAttribute("laptop") CreateLaptopDTO createLaptopDTO,
            BindingResult bindingResult,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Handle image upload or URL
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                if (fileUploadService.isValidImageFile(imageFile)) {
                    String imagePath = fileUploadService.saveFile(imageFile, "laptops");
                    createLaptopDTO.setImgUrl(imagePath);
                } else {
                    model.addAttribute("error",
                            "File không hợp lệ. Chỉ chấp nhận các file ảnh (jpg, png, gif, bmp, webp).");
                    model.addAttribute("activePage", "laptop");
                    model.addAttribute("models", modelService.getAllModels());
                    return "admin/pages/laptop/create";
                }
            } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                if (fileUploadService.isValidImageUrl(imageUrl)) {
                    // Validate and truncate URL if necessary for database compatibility
                    String validatedUrl = fileUploadService.validateAndTruncateUrl(imageUrl);
                    createLaptopDTO.setImgUrl(validatedUrl);
                } else {
                    model.addAttribute("error", "URL ảnh không hợp lệ hoặc quá dài (tối đa 255 ký tự).");
                    model.addAttribute("activePage", "laptop");
                    model.addAttribute("models", modelService.getAllModels());
                    return "admin/pages/laptop/create";
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi xử lý ảnh: " + e.getMessage());
            model.addAttribute("activePage", "laptop");
            model.addAttribute("models", modelService.getAllModels());
            return "admin/pages/laptop/create";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("models", modelService.getAllModels());
            return "admin/pages/laptop/create";
        }

        try {
            laptopService.createLaptop(createLaptopDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm laptop thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("models", modelService.getAllModels());
            model.addAttribute("error", "Có lỗi xảy ra khi thêm laptop: " + e.getMessage());
            return "admin/pages/laptop/create";
        }
        return "redirect:/admin/laptop";
    }

    @GetMapping("/laptop/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Laptop> laptopOpt = laptopService.getLaptopById(id);
        if (laptopOpt.isPresent()) {
            Laptop laptop = laptopOpt.get();
            CreateLaptopDTO laptopDTO = new CreateLaptopDTO();
            laptopDTO.setName(laptop.getName());
            laptopDTO.setModelId(laptop.getModel().getId());
            laptopDTO.setWarrantyPeriod(laptop.getWarrantyPeriod());
            laptopDTO.setImgUrl(laptop.getImgUrl());

            model.addAttribute("activePage", "laptop");
            model.addAttribute("laptop", laptopDTO);
            model.addAttribute("laptopId", id);
            model.addAttribute("models", modelService.getAllModels());
            return "admin/pages/laptop/update";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy laptop!");
            return "redirect:/admin/laptop";
        }
    }

    @PostMapping("/laptop/edit/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("laptop") CreateLaptopDTO createLaptopDTO,
            BindingResult bindingResult,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Handle image upload or URL
        try {
            // Get existing laptop to preserve old image if no new image provided
            Optional<Laptop> existingLaptopOpt = laptopService.getLaptopById(id);
            if (existingLaptopOpt.isEmpty()) {
                model.addAttribute("error", "Không tìm thấy laptop để cập nhật!");
                model.addAttribute("activePage", "laptop");
                model.addAttribute("laptopId", id);
                model.addAttribute("models", modelService.getAllModels());
                return "admin/pages/laptop/update";
            }

            Laptop existingLaptop = existingLaptopOpt.get();
            String finalImgUrl = existingLaptop.getImgUrl(); // Keep existing image by default

            if (imageFile != null && !imageFile.isEmpty()) {
                // New file uploaded
                if (fileUploadService.isValidImageFile(imageFile)) {
                    // Delete old image if it was uploaded before
                    if (existingLaptop.getImgUrl() != null && existingLaptop.getImgUrl().startsWith("/images/")) {
                        fileUploadService.deleteFile(existingLaptop.getImgUrl());
                    }

                    String imagePath = fileUploadService.saveFile(imageFile, "laptops");
                    finalImgUrl = imagePath;
                } else {
                    model.addAttribute("error",
                            "File không hợp lệ. Chỉ chấp nhận các file ảnh (jpg, png, gif, bmp, webp).");
                    model.addAttribute("activePage", "laptop");
                    model.addAttribute("laptopId", id);
                    model.addAttribute("models", modelService.getAllModels());
                    return "admin/pages/laptop/update";
                }
            } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                // New URL provided
                if (fileUploadService.isValidImageUrl(imageUrl)) {
                    // Validate and truncate URL if necessary for database compatibility
                    String validatedUrl = fileUploadService.validateAndTruncateUrl(imageUrl);
                    finalImgUrl = validatedUrl;
                } else {
                    model.addAttribute("error", "URL ảnh không hợp lệ hoặc quá dài (tối đa 255 ký tự).");
                    model.addAttribute("activePage", "laptop");
                    model.addAttribute("laptopId", id);
                    model.addAttribute("models", modelService.getAllModels());
                    return "admin/pages/laptop/update";
                }
            }
            // If no new image provided (both imageFile and imageUrl are empty), keep
            // existing image

            // Set the final image URL to DTO
            createLaptopDTO.setImgUrl(finalImgUrl);
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi xử lý ảnh: " + e.getMessage());
            model.addAttribute("activePage", "laptop");
            model.addAttribute("laptopId", id);
            model.addAttribute("models", modelService.getAllModels());
            return "admin/pages/laptop/update";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("laptopId", id);
            model.addAttribute("models", modelService.getAllModels());
            return "admin/pages/laptop/update";
        }

        try {
            Laptop updatedLaptop = laptopService.updateLaptop(id, createLaptopDTO);
            if (updatedLaptop != null) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật laptop thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy laptop để cập nhật!");
            }
        } catch (Exception e) {
            model.addAttribute("activePage", "laptop");
            model.addAttribute("laptopId", id);
            model.addAttribute("models", modelService.getAllModels());
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật laptop: " + e.getMessage());
            return "admin/pages/laptop/update";
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
