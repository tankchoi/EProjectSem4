package vn.aptech.java.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateRequestDetailDTO {

    @NotNull(message = "Request ID không được để trống")
    private Long requestId;

    @NotNull(message = "Part ID không được để trống")
    private Long partId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    public UpdateRequestDetailDTO() {
    }

    public UpdateRequestDetailDTO(Long requestId, Long partId, Integer quantity) {
        this.requestId = requestId;
        this.partId = partId;
        this.quantity = quantity;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "UpdateRequestDetailDTO{" +
                "requestId=" + requestId +
                ", partId=" + partId +
                ", quantity=" + quantity +
                '}';
    }
}
