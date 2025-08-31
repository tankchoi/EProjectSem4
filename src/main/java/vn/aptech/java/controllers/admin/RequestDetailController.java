package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.admin.CreateRequestDetailDTO;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.PartType;
import vn.aptech.java.models.Request;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

import java.util.List;

@Controller
@RequestMapping("/admin/request-detail")
public class RequestDetailController {
    @Autowired
    private RequestDetailService requestDetailService;
    @Autowired
    private PartService partService;
    @Autowired
    private PartTypeService partTypeService;
    @Autowired
    private RequestService requestService;

    @GetMapping("/create")
    public String create(Model model, @RequestParam(required = false) Long requestId) {
        model.addAttribute("activePage", "requestDetail");
        CreateRequestDetailDTO createRequestDetailDTO = new CreateRequestDetailDTO();
        if (requestId != null) {
            createRequestDetailDTO.setRequestId(requestId);
        }
        List<Request> requests = requestService.getRequests();
        List<Part> parts = partService.getParts(null, null, null);
        List<PartType> partTypes = partTypeService.getPartTypes(null);
        model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
        model.addAttribute("requests", requests);
        model.addAttribute("parts", parts);
        model.addAttribute("partTypes", partTypes);
        return "admin/pages/request_detail/create";
    }
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("createRequestDetailDTO") CreateRequestDetailDTO createRequestDetailDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            if(bindingResult.hasErrors()) {
                System.out.println("Validation errors: " + bindingResult.getAllErrors());
                model.addAttribute("activePage", "requestDetail");
                List<Request> requests = requestService.getRequests();
                List<Part> parts = partService.getParts(null, null, null);
                List<PartType> partTypes = partTypeService.getPartTypes(null);
                model.addAttribute("requests", requests);
                model.addAttribute("parts", parts);
                model.addAttribute("partTypes", partTypes);
                model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
                return "admin/pages/request_detail/create";
            }
            requestDetailService.createRequestDetail(createRequestDetailDTO);
            System.out.println("Request id: " + createRequestDetailDTO.getRequestId());
            System.out.println("Part id: " + createRequestDetailDTO.getPartId());
            redirectAttributes.addFlashAttribute("success", "Thêm linh kiện vào yêu cầu thành công!");
            return "redirect:/admin/request/" + createRequestDetailDTO.getRequestId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "requestDetail");
            List<Request> requests = requestService.getRequests();
            List<Part> parts = partService.getParts(null, null, null);
            List<PartType> partTypes = partTypeService.getPartTypes(null);
            model.addAttribute("requests", requests);
            model.addAttribute("parts", parts);
            model.addAttribute("partTypes", partTypes);
            model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
            return "admin/pages/request_detail/create";
        }
    }
}
