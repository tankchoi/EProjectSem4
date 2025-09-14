package vn.aptech.java.controllers.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.aptech.java.dtos.admin.CreateInvoiceDTO;
import vn.aptech.java.dtos.admin.UpdateInvoiceDTO;
import vn.aptech.java.models.Invoice;
import vn.aptech.java.models.Request;
import vn.aptech.java.services.InvoiceService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestDetailService requestDetailService;

    private void prepareForm(Model model) {
        model.addAttribute("activePage", "receipt");
        model.addAttribute("requests",
                requestService.getRequests(null, null, null, null, null, Request.Status.COMPLETED));
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("activePage", "receipt");
        model.addAttribute("invoices", invoiceService.getInvoices());
        return "admin/pages/invoice/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("activePage", "receipt");
        try {
            Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(id);
            if (optionalInvoice.isEmpty()) {
                model.addAttribute("error", "Hóa đơn không tồn tại.");
                return "admin/pages/invoice/index";
            }
            model.addAttribute("invoice", optionalInvoice.get());
            return "admin/pages/invoice/view";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải hóa đơn: " + e.getMessage());
            return "admin/pages/invoice/index";
        }
    }

    @GetMapping("/create")
    public String create(Model model, @RequestParam(required = false) Long requestId) {
        CreateInvoiceDTO createInvoiceDTO = new CreateInvoiceDTO();
        if (requestId != null) {
            createInvoiceDTO.setRequestId(requestId);
        }
        prepareForm(model);
        model.addAttribute("invoice", createInvoiceDTO);
        return "admin/pages/invoice/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("invoice") CreateInvoiceDTO dto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            prepareForm(model);
            model.addAttribute("invoice", dto);
            return "admin/pages/invoice/create";
        }
        try {
            invoiceService.createInvoice(dto);
            redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công.");
            return "redirect:/admin/invoice";
        } catch (Exception e) {
            prepareForm(model);
            model.addAttribute("invoice", dto);
            model.addAttribute("error", "Lỗi khi tạo hóa đơn: " + e.getMessage());
            return "admin/pages/invoice/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "receipt");
        try {
            Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(id);
            if (optionalInvoice.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại.");
                return "redirect:/admin/invoice";
            }
            Invoice invoice = optionalInvoice.get();
            UpdateInvoiceDTO dto = new UpdateInvoiceDTO(invoice.getId(), invoice.getStatus());
            model.addAttribute("invoice", invoice);
            model.addAttribute("invoiceDto", dto);
            return "admin/pages/invoice/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải hóa đơn: " + e.getMessage());
            return "redirect:/admin/invoice";
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("invoiceDto") UpdateInvoiceDTO dto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            Model model) {

        try {
            if (bindingResult.hasErrors()) {
                Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(dto.getId());
                if (optionalInvoice.isPresent()) {
                    model.addAttribute("invoice", optionalInvoice.get());
                    model.addAttribute("invoiceDto", dto);
                    model.addAttribute("activePage", "receipt");
                    return "admin/pages/invoice/edit";
                }
                redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại.");
                return "redirect:/admin/invoice";
            }
            invoiceService.updateInvoice(dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật hóa đơn thành công.");
            return "redirect:/admin/invoice";
        } catch (Exception e) {
            Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(dto.getId());
            if (optionalInvoice.isPresent()) {
                model.addAttribute("invoice", optionalInvoice.get());
                model.addAttribute("invoiceDto", dto);
                model.addAttribute("activePage", "receipt");
                return "admin/pages/invoice/edit";
            }
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật hóa đơn: " + e.getMessage());
            return "redirect:/admin/invoice";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            invoiceService.deleteInvoice(id);
            redirectAttributes.addFlashAttribute("success", "Xóa hóa đơn thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
        return "redirect:/admin/invoice";
    }

    /**
     * API endpoint để lấy tổng tiền tính từ RequestDetail
     */
    @GetMapping("/calculate-total/{requestId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calculateTotalPrice(@PathVariable Long requestId) {
        try {
            Double totalPrice = invoiceService.calculateTotalPrice(requestId);
            Map<String, Object> response = new HashMap<>();
            response.put("totalPrice", totalPrice);
            response.put("requestDetailsCount", requestDetailService.getRequestDetailsByRequestId(requestId).size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không thể tính tổng tiền: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * API endpoint để kiểm tra xem request đã có invoice chưa
     */
    @GetMapping("/check-by-request/{requestId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkInvoiceByRequest(@PathVariable Long requestId) {
        try {
            Map<String, Object> response = new HashMap<>();

            // Check if invoice exists for this request
            Optional<Invoice> invoiceOpt = invoiceService.getInvoiceByRequestId(requestId);

            if (invoiceOpt.isPresent()) {
                response.put("exists", true);
                response.put("invoiceId", invoiceOpt.get().getId());
                response.put("status", invoiceOpt.get().getStatus().name());
            } else {
                response.put("exists", false);
                response.put("invoiceId", null);
                response.put("status", null);
            }
            System.out.println("Invoice check response: " + response + " requestId: " + requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
