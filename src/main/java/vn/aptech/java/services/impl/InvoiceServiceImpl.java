package vn.aptech.java.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.aptech.java.dtos.admin.CreateInvoiceDTO;
import vn.aptech.java.models.Invoice;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.RequestDetail;
import vn.aptech.java.repositories.InvoiceRepository;
import vn.aptech.java.services.InvoiceService;
import vn.aptech.java.services.RequestDetailService;
import vn.aptech.java.services.RequestService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestDetailService requestDetailService;

    @Override
    public void createInvoice(CreateInvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setRequest(requestService.getRequestById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Yêu cầu không tồn tại.")));
        invoice.setTotalPrice(calculateTotalPrice(dto.getRequestId()));
        invoice.setStatus(dto.getStatus());
        invoiceRepository.save(invoice);
    }

    @Override
    public Double calculateTotalPrice(Long requestId) {
        Request request = requestService.getRequestById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Yêu cầu không tồn tại."));

        LocalDateTime warrantyEnd = request.getCustomerLaptop()
                .getCreatedAt()
                .toLocalDateTime()
                .plusMonths(request.getCustomerLaptop().getLaptop().getWarrantyPeriod());

        if (warrantyEnd.isAfter(LocalDateTime.now())) {
            return 0d; // Còn bảo hành
        }

        return requestDetailService.getRequestDetailsByRequestId(requestId).stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getPart().getPrice())
                .sum();
    }

}
