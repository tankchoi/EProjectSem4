package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class CreatePartDTO {
    @NotNull(message = "Loại linh kiện không được để trống")
    private Long partTypeId;
    private Long laptopId;
    @NotBlank(message = "Tên linh kiện không được để trống")
    private String name;
    @NotNull(message = "Giá linh kiện không được để trống")
    @Positive(message = "Giá linh kiện phải là một số dương")
    private Double price;
    @NotNull(message = "Số lượng linh kiện không được để trống")
    @PositiveOrZero(message = "Số lượng linh kiện phải là một số nguyên không âm")
    private Integer quantity;
    @NotNull(message = "Thời gian bảo hành không được để trống")
    @Min(value = 0, message = "Thời gian bảo hành phải là một số nguyên không âm")
    private Integer warrantyPeriod;
    private String imgUrl;
    private MultipartFile imgFile;
    @AssertTrue(message = "Cần cung cấp ảnh URL hoặc file ảnh")
    public boolean isImageProvided() {
        return (imgUrl != null && !imgUrl.trim().isEmpty()) ||
               (imgFile != null && !imgFile.isEmpty());
    }
    public CreatePartDTO() {
    }

    public CreatePartDTO(Long partTypeId, Long laptopId, String name, Double price, Integer quantity, Integer warrantyPeriod, String imgUrl, MultipartFile imgFile) {
        this.partTypeId = partTypeId;
        this.laptopId = laptopId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.warrantyPeriod = warrantyPeriod;
        this.imgUrl = imgUrl;
        this.imgFile = imgFile;
    }

    public Long getPartTypeId() {
        return partTypeId;
    }

    public Long getLaptopId() {
        return laptopId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
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

    public void setPartTypeId(Long partTypeId) {
        this.partTypeId = partTypeId;
    }

    public void setLaptopId(Long laptopId) {
        this.laptopId = laptopId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
