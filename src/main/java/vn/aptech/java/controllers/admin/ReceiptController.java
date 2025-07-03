package vn.aptech.java.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.dtos.CreateInvoiceDTO;
import vn.aptech.java.models.Invoice;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.repositories.RequestDetailRepository;
import vn.aptech.java.services.InvoiceService;
import vn.aptech.java.services.RequestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ReceiptController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestDetailRepository requestDetailRepository;

    @GetMapping("/receipt")
    public String index(Model model) {
        model.addAttribute("activePage", "receipt");
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "admin/pages/receipt/index";
    }

    @GetMapping("/receipt/create")
    public String create(Model model) {
        model.addAttribute("activePage", "receipt");
        model.addAttribute("invoice", new CreateInvoiceDTO());
        model.addAttribute("requests", requestService.getAllRequests());
        return "admin/pages/receipt/create";
    }

    @PostMapping("/receipt/create")
    public String store(@Valid @ModelAttribute("invoice") CreateInvoiceDTO createInvoiceDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "receipt");
            model.addAttribute("requests", requestService.getAllRequests());
            return "admin/pages/receipt/create";
        }

        try {
            invoiceService.createInvoice(createInvoiceDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm hóa đơn thành công!");
        } catch (Exception e) {
            model.addAttribute("activePage", "receipt");
            model.addAttribute("requests", requestService.getAllRequests());
            model.addAttribute("error", "Có lỗi xảy ra khi thêm hóa đơn: " + e.getMessage());
            return "admin/pages/receipt/create";
        }
        return "redirect:/admin/receipt";
    }

    @GetMapping("/receipt/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Invoice> invoiceOpt = invoiceService.getInvoiceById(id);
        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            CreateInvoiceDTO invoiceDTO = new CreateInvoiceDTO();
            invoiceDTO.setRequestId(invoice.getRequest().getId());
            invoiceDTO.setTotalPrice(invoice.getTotalPrice());
            invoiceDTO.setStatus(invoice.getStatus());

            model.addAttribute("activePage", "receipt");
            model.addAttribute("invoice", invoiceDTO);
            model.addAttribute("invoiceId", id);
            model.addAttribute("requests", requestService.getAllRequests());
            return "admin/pages/receipt/edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn!");
            return "redirect:/admin/receipt";
        }
    }

    @PostMapping("/receipt/update/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("invoice") CreateInvoiceDTO createInvoiceDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "receipt");
            model.addAttribute("invoiceId", id);
            model.addAttribute("requests", requestService.getAllRequests());
            return "admin/pages/receipt/edit";
        }

        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(id, createInvoiceDTO);
            if (updatedInvoice != null) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật hóa đơn thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn để cập nhật!");
            }
        } catch (Exception e) {
            model.addAttribute("activePage", "receipt");
            model.addAttribute("invoiceId", id);
            model.addAttribute("requests", requestService.getAllRequests());
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật hóa đơn: " + e.getMessage());
            return "admin/pages/receipt/edit";
        }
        return "redirect:/admin/receipt";
    }

    @GetMapping("/receipt/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            model.addAttribute("activePage", "receipt");
            model.addAttribute("invoice", invoice.get());
            return "admin/pages/receipt/view";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn!");
            return "redirect:/admin/receipt";
        }
    }

    @GetMapping("/receipt/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            invoiceService.deleteInvoice(id);
            redirectAttributes.addFlashAttribute("success", "Xóa hóa đơn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa hóa đơn: " + e.getMessage());
        }
        return "redirect:/admin/receipt";
    }

    @GetMapping("/receipt/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("activePage", "receipt");
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("invoices", invoiceService.searchInvoices(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("invoices", invoiceService.getAllInvoices());
        }
        return "admin/pages/receipt/index";
    }

    /**
     * API endpoint để lấy tổng tiền tính từ RequestDetail
     */
    @GetMapping("/receipt/calculate-total/{requestId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calculateTotalPrice(@PathVariable Long requestId) {
        try {
            List<RequestDetail> requestDetails = requestDetailRepository.findByRequestId(requestId);

            Double totalPrice = invoiceService.calculateTotalPriceFromRequestDetails(requestId);

            Map<String, Object> response = new HashMap<>();
            response.put("totalPrice", totalPrice);
            response.put("requestDetailsCount", requestDetails.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không thể tính tổng tiền: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * API endpoint để kiểm tra xem request đã có hóa đơn chưa
     */
    @GetMapping("/receipt/api/check-by-request/{requestId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkInvoiceByRequest(@PathVariable Long requestId) {
        try {
            // Tìm hóa đơn theo request ID
            Optional<Invoice> invoiceOpt = invoiceService.getInvoiceByRequestId(requestId);

            Map<String, Object> response = new HashMap<>();
            if (invoiceOpt.isPresent()) {
                Invoice invoice = invoiceOpt.get();
                response.put("exists", true);
                response.put("invoiceId", invoice.getId());
                response.put("totalPrice", invoice.getTotalPrice());
                response.put("status", invoice.getStatus());
            } else {
                response.put("exists", false);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không thể kiểm tra hóa đơn: " + e.getMessage());
            errorResponse.put("exists", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
