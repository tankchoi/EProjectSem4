package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotBlank;

public class CreatePartTypeDTO {
    @NotBlank(message = "Tên kiểu linh kiện không được để trống")
    private String name;
    public CreatePartTypeDTO() {
    }
    public CreatePartTypeDTO(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
