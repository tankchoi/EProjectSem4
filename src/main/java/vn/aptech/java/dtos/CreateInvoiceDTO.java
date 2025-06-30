package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateInvoiceDTO {
    @NotNull(message = "Yêu cầu không được để trống")
    private Long requestId;

    @NotNull(message = "Tổng tiền không được để trống")
    @Positive(message = "Tổng tiền phải lớn hơn 0")
    private Double totalPrice;

    private String status = "UNPAID"; // Default status

    public CreateInvoiceDTO() {
    }

    public CreateInvoiceDTO(Long requestId, Double totalPrice, String status) {
        this.requestId = requestId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
