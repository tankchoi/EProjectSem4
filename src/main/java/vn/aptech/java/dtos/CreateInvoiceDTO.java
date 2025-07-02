package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotNull;

public class CreateInvoiceDTO {
    @NotNull(message = "Yêu cầu không được để trống")
    private Long requestId;

    // Tổng tiền sẽ được tính tự động từ RequestDetail, không cần validation
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
