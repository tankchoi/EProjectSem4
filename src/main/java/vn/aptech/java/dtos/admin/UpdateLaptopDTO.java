package vn.aptech.java.dtos.admin;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public class UpdateLaptopDTO {
    @NotNull(message = "ID không được để trống")
    private Long id;
    @NotBlank(message = "Tên laptop không được để trống")
    private String name;
    @NotNull(message = "Model không được để trống")
    private Long modelId;
    @NotNull(message = "Thời gian bảo hành không được để trống")
    @Positive(message = "Thời gian bảo hành phải lớn hơn 0")
    private Integer warrantyPeriod;
    private String imgUrl;
    private MultipartFile imgFile;

    public UpdateLaptopDTO() {
    }
    public UpdateLaptopDTO(Long id, String name, Long modelId, Integer warrantyPeriod, String imgUrl, MultipartFile imgFile) {
        this.id = id;
        this.name = name;
        this.modelId = modelId;
        this.warrantyPeriod = warrantyPeriod;
        this.imgUrl = imgUrl;
        this.imgFile = imgFile;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getModelId() {
        return modelId;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public MultipartFile getImgFile() {
        return imgFile;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }
}
