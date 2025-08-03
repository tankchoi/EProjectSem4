package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.admin.CreatePartDTO;
import vn.aptech.java.dtos.admin.UpdatePartDTO;
import vn.aptech.java.models.Part;
import vn.aptech.java.services.LaptopService;
import vn.aptech.java.services.PartService;
import org.springframework.ui.Model;
import vn.aptech.java.services.PartTypeService;
import vn.aptech.java.utils.ImgUploadUtil;

import java.util.Optional;

@Controller
@RequestMapping("/admin/part")
public class PartController {
    @Autowired
    private PartService partService;
    @Autowired
    private PartTypeService partTypeService;
    @Autowired
    private LaptopService laptopService;
    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "partTypeId", required = false) Long partTypeId,
                        @RequestParam(value = "laptopId", required = false) Long laptopId){
        model.addAttribute("activePage", "part");
        model.addAttribute("name", name);
        model.addAttribute("partTypeId", partTypeId);
        model.addAttribute("laptopId", laptopId);
        model.addAttribute("partTypes", partTypeService.getPartTypes(null));
        model.addAttribute("laptops", laptopService.getLaptops(null, null));
        model.addAttribute("parts", partService.getParts(name, partTypeId, laptopId));
        return "admin/pages/part/index";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("activePage", "part");
        model.addAttribute("part", new CreatePartDTO());
        model.addAttribute("partTypes", partTypeService.getPartTypes(null));
        model.addAttribute("laptops", laptopService.getLaptops(null, null));
        return "admin/pages/part/create";
    }
    private void handleErrorAttributes(Model model){
        model.addAttribute("activePage", "part");
        model.addAttribute("partTypes", partTypeService.getPartTypes(null));
        model.addAttribute("laptops", laptopService.getLaptops(null, null));
    }
    @PostMapping("/create")
    public String store(@Valid@ModelAttribute("part") CreatePartDTO createPartDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            handleErrorAttributes(model);
            model.addAttribute("part", createPartDTO);
            return "admin/pages/part/create";
        }
        try {
            if(createPartDTO.getImgFile() != null && !createPartDTO.getImgFile().isEmpty()) {
                if(!ImgUploadUtil.isValidImageFormat(createPartDTO.getImgFile())){
                    bindingResult.rejectValue("imgFile", "imgFile.invalidType",
                            "Định dạng ảnh không hợp lệ. Vui lòng tải lên ảnh có định dạng jpg, jpeg, png hoặc webp.");
                    handleErrorAttributes(model);
                    model.addAttribute("part", createPartDTO);
                    return "admin/pages/part/create";
                }
                createPartDTO.setImgUrl(ImgUploadUtil.saveFile(createPartDTO.getImgFile(), "parts"));
            }
            System.out.println(createPartDTO);
            partService.createPart(createPartDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm linh kiện thành công!");
            return "redirect:/admin/part";
        } catch (Exception e) {
            handleErrorAttributes(model);
            model.addAttribute("error", "Có lỗi xảy ra khi thêm linh kiện: " + e.getMessage());
            model.addAttribute("part", createPartDTO);
            return "admin/pages/part/create";
        }
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            Optional<Part> partOpt = partService.getPartById(id);
            if (partOpt.isPresent()) {
                Part part = partOpt.get();
                UpdatePartDTO updatePartDTO = new UpdatePartDTO();
                updatePartDTO.setId(part.getId());
                updatePartDTO.setName(part.getName());
                updatePartDTO.setPartTypeId(part.getPartType().getId());
                updatePartDTO.setLaptopId(part.getLaptop() != null ? part.getLaptop().getId() : null);
                updatePartDTO.setImgUrl(part.getImgUrl());
                updatePartDTO.setPrice(part.getPrice());
                updatePartDTO.setQuantity(part.getQuantity());
                updatePartDTO.setWarrantyPeriod(part.getWarrantyPeriod());
                model.addAttribute("activePage", "part");
                model.addAttribute("part", updatePartDTO);
                model.addAttribute("partTypes", partTypeService.getPartTypes(null));
                model.addAttribute("laptops", laptopService.getLaptops(null, null));
                return "admin/pages/part/edit";
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy linh kiện!");
                return "redirect:/admin/part";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi truy xuất linh kiện: " + e.getMessage());
            return "redirect:/admin/part";
        }
    }
    @PostMapping("/update")
    public String update(@Valid@ModelAttribute("part") UpdatePartDTO updatePartDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            handleErrorAttributes(model);
            model.addAttribute("part", updatePartDTO);
            return "admin/pages/part/edit";
        }
        try {
            Optional<Part> existingPartOpt = partService.getPartById(updatePartDTO.getId());
            String oldImgUrl = existingPartOpt.map(Part::getImgUrl).orElse(null);
            if ((updatePartDTO.getImgUrl() == null || updatePartDTO.getImgUrl().isEmpty())
                    && (updatePartDTO.getImgFile() == null || updatePartDTO.getImgFile().isEmpty())) {
                if (oldImgUrl != null) {
                    updatePartDTO.setImgUrl(oldImgUrl);
                }
            }
            if (updatePartDTO.getImgUrl() != null && !updatePartDTO.getImgUrl().isEmpty()
                    && (updatePartDTO.getImgFile() == null || updatePartDTO.getImgFile().isEmpty())) {
                if (oldImgUrl != null && !oldImgUrl.equals(updatePartDTO.getImgUrl())
                        && !oldImgUrl.startsWith("http")) {
                    ImgUploadUtil.deleteFile(oldImgUrl);
                }
            }
            if (updatePartDTO.getImgFile() != null && !updatePartDTO.getImgFile().isEmpty()) {
                if (!ImgUploadUtil.isValidImageFormat(updatePartDTO.getImgFile())) {
                    bindingResult.rejectValue("imgFile", "imgFile.invalidType",
                            "Định dạng ảnh không hợp lệ. Vui lòng tải lên ảnh có định dạng jpg, jpeg, png hoặc webp.");
                    handleErrorAttributes(model);
                    model.addAttribute("part", updatePartDTO);
                    return "admin/pages/part/edit";
                }
                if (oldImgUrl != null && !oldImgUrl.startsWith("http")) {
                    ImgUploadUtil.deleteFile(oldImgUrl);
                }
                updatePartDTO.setImgUrl(ImgUploadUtil.saveFile(updatePartDTO.getImgFile(), "parts"));
            }

            partService.updatePart(updatePartDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật linh kiện thành công!");

        } catch (Exception e) {
            handleErrorAttributes(model);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật linh kiện: " + e.getMessage());
            model.addAttribute("part", updatePartDTO);
            return "admin/pages/part/edit";
        }
        return "redirect:/admin/part";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Part> partOpt = partService.getPartById(id);
            if (partOpt.isPresent()) {
                Part part = partOpt.get();
                if (part.getImgUrl() != null && !part.getImgUrl().startsWith("http")) {
                    ImgUploadUtil.deleteFile(part.getImgUrl());
                }
                partService.deletePart(id);
                redirectAttributes.addFlashAttribute("success", "Xóa linh kiện thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy linh kiện để xóa!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa linh kiện: " + e.getMessage());
        }
        return "redirect:/admin/part";
    }
}

