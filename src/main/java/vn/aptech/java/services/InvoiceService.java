package vn.aptech.java.services;

import vn.aptech.java.dtos.CreateInvoiceDTO;
import vn.aptech.java.models.Invoice;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    List<Invoice> getAllInvoices();

    Optional<Invoice> getInvoiceById(Long id);

    Invoice createInvoice(Invoice invoice);

    Invoice createInvoice(CreateInvoiceDTO createInvoiceDTO);

    Invoice updateInvoice(Long id, Invoice invoice);

    Invoice updateInvoice(Long id, CreateInvoiceDTO createInvoiceDTO);

    void deleteInvoice(Long id);

    List<Invoice> searchInvoices(String keyword);

    List<Invoice> getInvoicesByStatus(String status);

    List<Invoice> getInvoicesByTechnician(Long technicianId);

    /**
     * Tính tổng tiền từ các RequestDetail của một request
     */
    Double calculateTotalPriceFromRequestDetails(Long requestId);
}
