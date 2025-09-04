package vn.aptech.java.controllers.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.aptech.java.dtos.admin.CreateInvoiceDTO;
import vn.aptech.java.dtos.admin.UpdateInvoiceDTO;
import vn.aptech.java.models.Invoice;
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
        model.addAttribute("requests", requestService.getRequests());
    }

    @GetMapping("/create")
    public String create(Model model) {
        prepareForm(model);
        model.addAttribute("invoice", new CreateInvoiceDTO());
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

}
