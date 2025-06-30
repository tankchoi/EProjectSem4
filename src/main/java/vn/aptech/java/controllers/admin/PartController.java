package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.Part;
import vn.aptech.java.services.FileUploadService;
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

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/part")
    public String index(Model model) {
        model.addAttribute("activePage", "parts");
        System.out.println("DEBUG: Part activePage set to: parts");
        model.addAttribute("parts", partService.getAllParts());
        return "admin/pages/part/index";
    }

    @GetMapping("/part/create")
    public String create(Model model) {
        model.addAttribute("activePage", "parts");
        model.addAttribute("part", new Part());
        model.addAttribute("partTypes", partTypeService.getAllPartTypes());
        model.addAttribute("laptops", laptopService.getAllLaptops());
        return "admin/pages/part/create";
    }

    @PostMapping("/part/create")
    public String store(@RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("partType") Long partTypeId,
            @RequestParam("warrantyPeriod") Integer warrantyPeriod,
            @RequestParam(value = "laptop", required = false) Long laptopId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            RedirectAttributes redirectAttributes,
            Model model) {

        String finalImgUrl = null;

        // Handle image upload or URL
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                if (fileUploadService.isValidImageFile(imageFile)) {
                    finalImgUrl = fileUploadService.saveFile(imageFile, "parts");
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "File không hợp lệ. Chỉ chấp nhận các file ảnh (jpg, png, gif, bmp, webp).");
                    return "redirect:/admin/part/create";
                }
            } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                if (fileUploadService.isValidImageUrl(imageUrl)) {
                    // Validate and truncate URL if necessary for database compatibility
                    finalImgUrl = fileUploadService.validateAndTruncateUrl(imageUrl);
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "URL ảnh không hợp lệ hoặc quá dài (tối đa 255 ký tự).");
                    return "redirect:/admin/part/create";
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xử lý ảnh: " + e.getMessage());
            return "redirect:/admin/part/create";
        }

        try {
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên linh kiện không được để trống!");
                return "redirect:/admin/part/create";
            }

            partService.createPart(name, price, quantity, partTypeId, warrantyPeriod, laptopId, finalImgUrl);
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
            model.addAttribute("activePage", "parts");
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
    public String update(@PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("partType") Long partTypeId,
            @RequestParam("warrantyPeriod") Integer warrantyPeriod,
            @RequestParam(value = "laptop", required = false) Long laptopId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            RedirectAttributes redirectAttributes) {

        String finalImgUrl = null;

        // Handle image upload or URL
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                if (fileUploadService.isValidImageFile(imageFile)) {
                    // Delete old image if it was uploaded before
                    Optional<Part> existingPart = partService.getPartById(id);
                    if (existingPart.isPresent() && existingPart.get().getImgUrl() != null &&
                            existingPart.get().getImgUrl().startsWith("/images/")) {
                        fileUploadService.deleteFile(existingPart.get().getImgUrl());
                    }

                    finalImgUrl = fileUploadService.saveFile(imageFile, "parts");
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "File không hợp lệ. Chỉ chấp nhận các file ảnh (jpg, png, gif, bmp, webp).");
                    return "redirect:/admin/part/edit/" + id;
                }
            } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                if (fileUploadService.isValidImageUrl(imageUrl)) {
                    // Validate and truncate URL if necessary for database compatibility
                    finalImgUrl = fileUploadService.validateAndTruncateUrl(imageUrl);
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "URL ảnh không hợp lệ hoặc quá dài (tối đa 255 ký tự).");
                    return "redirect:/admin/part/edit/" + id;
                }
            } else {
                // Keep existing image if no new image provided
                Optional<Part> existingPart = partService.getPartById(id);
                if (existingPart.isPresent()) {
                    finalImgUrl = existingPart.get().getImgUrl();
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xử lý ảnh: " + e.getMessage());
            return "redirect:/admin/part/edit/" + id;
        }

        try {
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên linh kiện không được để trống!");
                return "redirect:/admin/part/edit/" + id;
            }

            Part updatedPart = partService.updatePart(id, name, price, quantity, partTypeId, warrantyPeriod, laptopId,
                    finalImgUrl);
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
        model.addAttribute("activePage", "parts");
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("parts", partService.searchParts(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("parts", partService.getAllParts());
        }
        return "admin/pages/part/index";
    }
}
