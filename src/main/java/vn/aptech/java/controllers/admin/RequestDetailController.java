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
import vn.aptech.java.dtos.CreateRequestDetailDTO;
import vn.aptech.java.dtos.UpdateRequestDetailDTO;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.PartType;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/request-detail")
public class RequestDetailController {

    @Autowired
    private RequestDetailService requestDetailService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PartService partService;

    @Autowired
    private PartTypeService partTypeService;

    /**
     * Hiển thị danh sách request details
     */
    @GetMapping
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<RequestDetail> requestDetails;

        if (search != null && !search.trim().isEmpty()) {
            requestDetails = requestDetailService.searchRequestDetails(search, pageable);
        } else {
            requestDetails = requestDetailService.getAllRequestDetails(pageable);
        }

        model.addAttribute("activePage", "requestDetail");
        model.addAttribute("requestDetails", requestDetails.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requestDetails.getTotalPages());
        model.addAttribute("search", search);

        return "admin/pages/request_detail/index";
    }

    /**
     * Hiển thị chi tiết của một request detail
     */
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            RequestDetail requestDetail = requestDetailService.getRequestDetailByIdOrThrow(id);
            model.addAttribute("activePage", "requestDetail");
            model.addAttribute("requestDetail", requestDetail);
            return "admin/pages/request_detail/view";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/request-detail";
        }
    }

    /**
     * Hiển thị form tạo mới request detail
     */
    @GetMapping("/create")
    public String create(
            @RequestParam(required = false) Long requestId,
            Model model) {

        model.addAttribute("activePage", "requestDetail");

        CreateRequestDetailDTO createRequestDetailDTO = new CreateRequestDetailDTO();
        if (requestId != null) {
            createRequestDetailDTO.setRequestId(requestId);
        }

        List<Request> requests = requestService.getAllRequests();
        List<Part> parts = partService.getAllParts();
        List<PartType> partTypes = partTypeService.getAllPartTypes();

        model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
        model.addAttribute("requests", requests);
        model.addAttribute("parts", parts);
        model.addAttribute("partTypes", partTypes);

        return "admin/pages/request_detail/create";
    }

    /**
     * Xử lý tạo mới request detail
     */
    @PostMapping("/create")
    public String store(
            @Valid @ModelAttribute CreateRequestDetailDTO createRequestDetailDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "requestDetail");
            List<Request> requests = requestService.getAllRequests();
            List<Part> parts = partService.getAllParts();
            List<PartType> partTypes = partTypeService.getAllPartTypes();
            model.addAttribute("requests", requests);
            model.addAttribute("parts", parts);
            model.addAttribute("partTypes", partTypes);
            return "admin/pages/request_detail/create";
        }

        try {
            RequestDetail requestDetail = requestDetailService.createRequestDetail(createRequestDetailDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm linh kiện vào yêu cầu thành công!");
            return "redirect:/admin/request-detail/" + requestDetail.getId();
        } catch (IllegalArgumentException e) {
            List<Request> requests = requestService.getAllRequests();
            List<Part> parts = partService.getAllParts();
            model.addAttribute("requests", requests);
            model.addAttribute("parts", parts);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/pages/request_detail/create";
        }
    }

    /**
     * Hiển thị form chỉnh sửa request detail
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        try {
            RequestDetail requestDetail = requestDetailService.getRequestDetailByIdOrThrow(id);

            model.addAttribute("activePage", "requestDetail");

            UpdateRequestDetailDTO updateRequestDetailDTO = new UpdateRequestDetailDTO();
            updateRequestDetailDTO.setRequestId(requestDetail.getRequest().getId());
            updateRequestDetailDTO.setPartId(requestDetail.getPart().getId());
            updateRequestDetailDTO.setQuantity(requestDetail.getQuantity());

            List<Request> requests = requestService.getAllRequests();
            List<Part> parts = partService.getAllParts();
            List<PartType> partTypes = partTypeService.getAllPartTypes();

            model.addAttribute("requestDetail", requestDetail);
            model.addAttribute("updateRequestDetailDTO", updateRequestDetailDTO);
            model.addAttribute("requests", requests);
            model.addAttribute("parts", parts);
            model.addAttribute("partTypes", partTypes);

            return "admin/pages/request_detail/edit";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/request-detail";
        }
    }

    /**
     * Xử lý cập nhật request detail
     */
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateRequestDetailDTO updateRequestDetailDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            try {
                RequestDetail requestDetail = requestDetailService.getRequestDetailByIdOrThrow(id);
                List<Request> requests = requestService.getAllRequests();
                List<Part> parts = partService.getAllParts();

                model.addAttribute("requestDetail", requestDetail);
                model.addAttribute("requests", requests);
                model.addAttribute("parts", parts);
                return "admin/pages/request_detail/edit";
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/admin/request-detail";
            }
        }

        try {
            RequestDetail updatedRequestDetail = requestDetailService.updateRequestDetail(id, updateRequestDetailDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật linh kiện thành công!");
            return "redirect:/admin/request-detail/" + updatedRequestDetail.getId();
        } catch (IllegalArgumentException e) {
            try {
                RequestDetail requestDetail = requestDetailService.getRequestDetailByIdOrThrow(id);
                List<Request> requests = requestService.getAllRequests();
                List<Part> parts = partService.getAllParts();

                model.addAttribute("requestDetail", requestDetail);
                model.addAttribute("requests", requests);
                model.addAttribute("parts", parts);
                model.addAttribute("errorMessage", e.getMessage());
                return "admin/pages/request_detail/edit";
            } catch (IllegalArgumentException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
                return "redirect:/admin/request-detail";
            }
        }
    }

    /**
     * Xử lý xóa request detail
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            requestDetailService.deleteRequestDetail(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa linh kiện khỏi yêu cầu thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/request-detail";
    }

    /**
     * Hiển thị danh sách request details theo request ID
     */
    @GetMapping("/by-request/{requestId}")
    public String byRequest(@PathVariable Long requestId, Model model) {
        try {
            Request request = requestService.getRequestByIdOrThrow(requestId);
            List<RequestDetail> requestDetails = requestDetailService.getRequestDetailsByRequestId(requestId);
            Double totalValue = requestDetailService.getTotalValueByRequestId(requestId);

            model.addAttribute("request", request);
            model.addAttribute("requestDetails", requestDetails);
            model.addAttribute("totalValue", totalValue);

            return "admin/pages/request_detail/by_request";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/request";
        }
    }

    /**
     * API endpoint để lấy thông tin part
     */
    @GetMapping("/api/part/{partId}")
    @ResponseBody
    public Part getPartInfo(@PathVariable Long partId) {
        return partService.getPartByIdOrThrow(partId);
    }

    /**
     * API endpoint để lấy thông tin request details theo request ID
     */
    @GetMapping("/api/by-request/{requestId}")
    @ResponseBody
    public Map<String, Object> getRequestDetailsInfo(@PathVariable Long requestId) {
        try {
            List<RequestDetail> requestDetails = requestDetailService.getRequestDetailsByRequestId(requestId);
            Double totalValue = requestDetailService.getTotalValueByRequestId(requestId);

            Map<String, Object> response = new HashMap<>();
            response.put("count", requestDetails.size());
            response.put("totalValue", totalValue != null ? totalValue : 0.0);
            response.put("requestDetails", requestDetails);

            return response;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không thể lấy thông tin: " + e.getMessage());
            errorResponse.put("count", 0);
            errorResponse.put("totalValue", 0.0);
            return errorResponse;
        }
    }

    /**
     * Thống kê top parts được sử dụng
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {
        List<Object[]> topUsedParts = requestDetailService.getTopUsedParts(10);
        model.addAttribute("topUsedParts", topUsedParts);
        return "admin/pages/request_detail/statistics";
    }
}
