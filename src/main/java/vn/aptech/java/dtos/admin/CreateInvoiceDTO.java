package vn.aptech.java.dtos.admin;

import vn.aptech.java.models.Invoice;

public class CreateInvoiceDTO {
    private Long requestId;
    private Invoice.Status status;

    public CreateInvoiceDTO() {
    }

    public CreateInvoiceDTO(Long requestId, Invoice.Status status) {
        this.requestId = requestId;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Invoice.Status getStatus() {
        return status;
    }

    public void setStatus(Invoice.Status status) {
        this.status = status;
    }
}
