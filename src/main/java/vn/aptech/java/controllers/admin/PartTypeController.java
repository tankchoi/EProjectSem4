package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreatePartTypeDTO;
import vn.aptech.java.dtos.PartTypeWithCountDTO;
import vn.aptech.java.models.PartType;
import vn.aptech.java.services.PartTypeService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class PartTypeController {

    @Autowired
    private PartTypeService partTypeService;

    @GetMapping("/part-type")
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

        // Create Pageable object
        Sort sort = Sort.by(sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PartType> partTypePage;

        if (search != null && !search.trim().isEmpty()) {
            partTypePage = partTypeService.searchPartTypes(search, pageable);
        } else {
            partTypePage = partTypeService.getAllPartTypes(pageable);
        }

        // Tạo danh sách PartTypeWithCountDTO với số lượng parts
        List<PartTypeWithCountDTO> partTypesWithCount = partTypePage.getContent().stream()
                .map(partType -> {
                    Long partCount = partTypeService.countPartsByPartTypeId(partType.getId());
                    return new PartTypeWithCountDTO(partType, partCount);
                })
                .collect(Collectors.toList());

        model.addAttribute("activePage", "partTypes");
        System.out.println("DEBUG: PartType activePage set to: partTypes");
        model.addAttribute("partTypes", partTypesWithCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", partTypePage.getTotalPages());
        model.addAttribute("totalElements", partTypePage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);

        return "admin/pages/part_type/index";
    }

    @GetMapping("/part-type/create")
    public String create(Model model) {
        model.addAttribute("activePage", "partTypes");
        model.addAttribute("partType", new CreatePartTypeDTO());
        return "admin/pages/part_type/create";
    }

    @PostMapping("/part-type")
    public String store(@Valid @ModelAttribute("partType") CreatePartTypeDTO createPartTypeDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "partTypes");
            return "admin/pages/part_type/create";
        }

        try {
            partTypeService.createPartType(createPartTypeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm loại linh kiện thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "partTypes");
            model.addAttribute("errorMessage", "Lỗi khi tạo loại linh kiện: " + e.getMessage());
            return "admin/pages/part_type/create";
        }

        return "redirect:/admin/part-type";
    }

    @GetMapping("/part-type/{id}")
    public String show(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PartType partType = partTypeService.getPartTypeByIdOrThrow(id);
            model.addAttribute("activePage", "partTypes");
            model.addAttribute("partType", partType);
            return "admin/pages/part_type/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy loại linh kiện!");
            return "redirect:/admin/part-type";
        }
    }

    @GetMapping("/part-type/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PartType partType = partTypeService.getPartTypeByIdOrThrow(id);
            CreatePartTypeDTO partTypeDTO = new CreatePartTypeDTO();
            partTypeDTO.setName(partType.getName());

            model.addAttribute("activePage", "partTypes");
            model.addAttribute("partType", partTypeDTO);
            model.addAttribute("partTypeId", id);

            return "admin/pages/part_type/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy loại linh kiện!");
            return "redirect:/admin/part-type";
        }
    }

    @PostMapping("/part-type/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("partType") CreatePartTypeDTO createPartTypeDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "partTypes");
            model.addAttribute("partTypeId", id);
            return "admin/pages/part_type/edit";
        }

        try {
            partTypeService.updatePartType(id, createPartTypeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật loại linh kiện thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "partTypes");
            model.addAttribute("partTypeId", id);
            model.addAttribute("errorMessage", "Lỗi khi cập nhật loại linh kiện: " + e.getMessage());
            return "admin/pages/part_type/edit";
        }

        return "redirect:/admin/part-type";
    }

    @PostMapping("/part-type/{id}/delete")
    public String deletePartType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partTypeService.deletePartType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa loại linh kiện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/part-type";
    }

    @GetMapping("/part-type/init-data")
    public String initData(RedirectAttributes redirectAttributes) {
        try {
            String[] partTypeNames = {
                    "RAM", "SSD", "HDD", "CPU", "Mainboard",
                    "VGA", "PSU", "Quạt tản nhiệt", "Ổ CD/DVD", "Card mạng",
                    "Bàn phím", "Chuột", "Màn hình", "Webcam", "Loa"
            };

            for (String name : partTypeNames) {
                try {
                    CreatePartTypeDTO dto = new CreatePartTypeDTO(name);
                    partTypeService.createPartType(dto);
                } catch (Exception e) {
                    // Ignore if already exists
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Khởi tạo dữ liệu mẫu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/part-type";
    }
}
