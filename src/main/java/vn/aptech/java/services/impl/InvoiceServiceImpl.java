package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.CreateInvoiceDTO;
import vn.aptech.java.models.Invoice;
import vn.aptech.java.models.Request;
import vn.aptech.java.repositories.InvoiceRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.services.InvoiceService;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice createInvoice(CreateInvoiceDTO createInvoiceDTO) {
        Invoice invoice = new Invoice();

        // Fetch the request entity
        Request request = requestRepository.findById(createInvoiceDTO.getRequestId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        invoice.setRequest(request);
        invoice.setTotalPrice(createInvoiceDTO.getTotalPrice());
        invoice.setStatus(createInvoiceDTO.getStatus());

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(Long id, Invoice invoice) {
        if (invoiceRepository.existsById(id)) {
            invoice.setId(id);
            return invoiceRepository.save(invoice);
        }
        return null;
    }

    @Override
    public Invoice updateInvoice(Long id, CreateInvoiceDTO createInvoiceDTO) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // Fetch the request entity
        Request request = requestRepository.findById(createInvoiceDTO.getRequestId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        existingInvoice.setRequest(request);
        existingInvoice.setTotalPrice(createInvoiceDTO.getTotalPrice());
        existingInvoice.setStatus(createInvoiceDTO.getStatus());

        return invoiceRepository.save(existingInvoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public List<Invoice> searchInvoices(String keyword) {
        return invoiceRepository.findByKeyword(keyword);
    }

    @Override
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    public List<Invoice> getInvoicesByTechnician(Long technicianId) {
        return invoiceRepository.findByTechnicianId(technicianId);
    }
}
