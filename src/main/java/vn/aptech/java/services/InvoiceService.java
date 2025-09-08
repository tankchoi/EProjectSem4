package vn.aptech.java.services;

import java.util.List;
import java.util.Optional;

import vn.aptech.java.dtos.admin.CreateInvoiceDTO;
import vn.aptech.java.dtos.admin.UpdateInvoiceDTO;
import vn.aptech.java.models.Invoice;

public interface InvoiceService {
    void createInvoice(CreateInvoiceDTO dto);

    void updateInvoice(UpdateInvoiceDTO dto);

    Double calculateTotalPrice(Long requestId);

    Optional<Invoice> getInvoiceById(Long id);

    List<Invoice> getInvoices();

    void deleteInvoice(Long id);
}