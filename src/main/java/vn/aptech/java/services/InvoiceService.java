package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreateInvoiceDTO;

public interface InvoiceService {
    void createInvoice(CreateInvoiceDTO dto);

    Double calculateTotalPrice(Long requestId);
}