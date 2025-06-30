package vn.aptech.java.services;

import vn.aptech.java.models.Invoice;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    List<Invoice> getAllInvoices();

    Optional<Invoice> getInvoiceById(Long id);

    Invoice createInvoice(Invoice invoice);

    Invoice updateInvoice(Long id, Invoice invoice);

    void deleteInvoice(Long id);

    List<Invoice> searchInvoices(String keyword);

    List<Invoice> getInvoicesByStatus(String status);

    List<Invoice> getInvoicesByTechnician(Long technicianId);
}
