package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateRequestDetailDTO {
    @NotNull(message = "ID không được để trống")
    private Long id;
    @NotNull(message = "Yêu cầu không được để trống")
    private Long requestId;
    @NotNull(message = "Linh kiện không được để trống")
    private Long partId;
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity;

    public UpdateRequestDetailDTO() {
    }

    public UpdateRequestDetailDTO(Long id, Long requestId, Long partId, Integer quantity) {
        this.id = id;
        this.requestId = requestId;
        this.partId = partId;
        this.quantity = quantity;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getPartId() {
        return partId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
