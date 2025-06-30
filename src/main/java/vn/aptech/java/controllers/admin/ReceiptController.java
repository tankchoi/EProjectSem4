package vn.aptech.java.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.java.models.Invoice;
import vn.aptech.java.services.InvoiceService;
import vn.aptech.java.services.RequestService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ReceiptController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/receipt")
    public String index(Model model) {
        model.addAttribute("activePage", "receipt");
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "admin/pages/receipt/index";
    }

    @GetMapping("/receipt/create")
    public String create(Model model) {
        model.addAttribute("activePage", "receipt");
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("requests", requestService.getAllRequests());
        return "admin/pages/receipt/create";
    }

    @PostMapping("/receipt/create")
    public String store(@ModelAttribute Invoice invoice, RedirectAttributes redirectAttributes) {
        try {
            if (invoice.getTotalPrice() == null || invoice.getTotalPrice() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Tổng tiền không được để trống và phải lớn hơn 0!");
                return "redirect:/admin/receipt/create";
            }

            invoiceService.createInvoice(invoice);
            redirectAttributes.addFlashAttribute("success", "Thêm hóa đơn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm hóa đơn: " + e.getMessage());
        }
        return "redirect:/admin/receipt";
    }

    @GetMapping("/receipt/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            model.addAttribute("activePage", "receipt");
            model.addAttribute("invoice", invoice.get());
            model.addAttribute("requests", requestService.getAllRequests());
            return "admin/pages/receipt/edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn!");
            return "redirect:/admin/receipt";
        }
    }

    @PostMapping("/receipt/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Invoice invoice,
            RedirectAttributes redirectAttributes) {
        try {
            if (invoice.getTotalPrice() == null || invoice.getTotalPrice() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Tổng tiền không được để trống và phải lớn hơn 0!");
                return "redirect:/admin/receipt/edit/" + id;
            }

            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoice);
            if (updatedInvoice != null) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật hóa đơn thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn để cập nhật!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật hóa đơn: " + e.getMessage());
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
}
