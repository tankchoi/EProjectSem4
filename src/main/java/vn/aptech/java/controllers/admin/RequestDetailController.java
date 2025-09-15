package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.admin.CreateRequestDetailDTO;
import vn.aptech.java.dtos.admin.UpdateRequestDetailDTO;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void prepareForm(Model model) {
        model.addAttribute("activePage", "requestDetail");
        model.addAttribute("requests",
                requestService.getRequests(null, null, null, null, null, Request.Status.APPROVED));
        model.addAttribute("parts", partService.getParts(null, null, null));
        model.addAttribute("partTypes", partTypeService.getPartTypes(null));
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("activePage", "requestDetail");
        model.addAttribute("requestDetails", requestDetailService.getRequestDetails());
        return "admin/pages/request_detail/index";
    }

    @GetMapping("/create")
    public String create(Model model, @RequestParam(required = false) Long requestId) {
        CreateRequestDetailDTO createRequestDetailDTO = new CreateRequestDetailDTO();
        if (requestId != null) {
            createRequestDetailDTO.setRequestId(requestId);
        }
        prepareForm(model);
        model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
        return "admin/pages/request_detail/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("createRequestDetailDTO") CreateRequestDetailDTO createRequestDetailDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if (bindingResult.hasErrors()) {
                prepareForm(model);
                model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
                return "admin/pages/request_detail/create";
            }
            requestDetailService.createRequestDetail(createRequestDetailDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm linh kiện vào yêu cầu thành công!");
            return "redirect:/admin/request/" + createRequestDetailDTO.getRequestId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            prepareForm(model);
            model.addAttribute("createRequestDetailDTO", createRequestDetailDTO);
            return "admin/pages/request_detail/create";
        }
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Optional<RequestDetail> requestDetailOpt = requestDetailService.getRequestDetailById(id);
        if (requestDetailOpt.isPresent()) {
            model.addAttribute("requestDetail", requestDetailOpt.get());
            return "admin/pages/request_detail/view";
        } else {
            model.addAttribute("error", "Chi tiết yêu cầu không tồn tại");
            return "admin/pages/request_detail/index";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        prepareForm(model);
        try {
            Optional<RequestDetail> requestDetailOpt = requestDetailService.getRequestDetailById(id);
            if (requestDetailOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Chi tiết yêu cầu không tồn tại");
                return "redirect:/admin/request";
            }
            RequestDetail requestDetail = requestDetailOpt.get();
            UpdateRequestDetailDTO updateRequestDetailDTO = new UpdateRequestDetailDTO(
                    requestDetail.getId(),
                    requestDetail.getRequest().getId(),
                    requestDetail.getPart().getId(),
                    requestDetail.getQuantity());
            model.addAttribute("updateRequestDetailDTO", updateRequestDetailDTO);
            return "admin/pages/request_detail/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi lấy thông tin chi tiết yêu cầu: " + e.getMessage());
            return "redirect:/admin/request";
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("updateRequestDetailDTO") UpdateRequestDetailDTO updateRequestDetailDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if (bindingResult.hasErrors()) {
                prepareForm(model);
                model.addAttribute("updateRequestDetailDTO", updateRequestDetailDTO);
                return "admin/pages/request_detail/edit";
            }
            requestDetailService.updateRequestDetail(updateRequestDetailDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật chi tiết yêu cầu thành công!");
            return "redirect:/admin/request/" + updateRequestDetailDTO.getRequestId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            prepareForm(model);
            model.addAttribute("updateRequestDetailDTO", updateRequestDetailDTO);
            return "admin/pages/request_detail/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            requestDetailService.deleteRequestDetail(id);
            redirectAttributes.addFlashAttribute("success", "Xóa chi tiết yêu cầu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa chi tiết yêu cầu: " + e.getMessage());
        }
        return "redirect:/admin/request-detail";
    }

    @GetMapping("/count-by-request/{requestId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCountByRequest(@PathVariable Long requestId) {
        try {
            Map<String, Object> response = new HashMap<>();

            // Get count and total value from service
            long count = requestDetailService.countByRequestId(requestId);
            double totalValue = requestDetailService.getTotalValueByRequestId(requestId);

            response.put("count", count);
            response.put("totalValue", totalValue);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/by-request/{requestId}")
    public String getByRequest(@PathVariable Long requestId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<Request> requestOpt = requestService.getRequestById(requestId);
            if (requestOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Yêu cầu không tồn tại!");
                return "redirect:/admin/request";
            }

            Request request = requestOpt.get();
            model.addAttribute("request", request);

            List<RequestDetail> requestDetails = requestDetailService.getRequestDetailsByRequestId(requestId);
            if (requestDetails == null) {
                requestDetails = new ArrayList<>();
            }
            model.addAttribute("requestDetails", requestDetails);

            double totalValue = requestDetails.stream()
                    .mapToDouble(d -> d.getQuantity() * d.getPart().getPrice())
                    .sum();
            model.addAttribute("totalValue", totalValue);

            return "admin/pages/request_detail/by_request";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/request";
        }
    }

}
