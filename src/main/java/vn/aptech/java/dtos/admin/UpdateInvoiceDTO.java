package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.NotNull;
import vn.aptech.java.models.Invoice;

public class UpdateInvoiceDTO {
    @NotNull(message = "ID không được để trống.")
    private Long id;
    @NotNull(message = "Trạng thái không được để trống.")
    private Invoice.Status status;

    public UpdateInvoiceDTO() {
    }

    public UpdateInvoiceDTO(Long id, Invoice.Status status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice.Status getStatus() {
        return status;
    }

    public void setStatus(Invoice.Status status) {
        this.status = status;
    }
}
