package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateLaptopDTO {
    @NotBlank(message = "Tên laptop không được để trống")
    private String name;

    @NotNull(message = "Model không được để trống")
    private Long modelId;

    @NotNull(message = "Thời gian bảo hành không được để trống")
    @Positive(message = "Thời gian bảo hành phải lớn hơn 0")
    private Integer warrantyPeriod;

    private String imgUrl;

    public CreateLaptopDTO() {
    }

    public CreateLaptopDTO(String name, Long modelId, Integer warrantyPeriod, String imgUrl) {
        this.name = name;
        this.modelId = modelId;
        this.warrantyPeriod = warrantyPeriod;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
