package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePartTypeDTO {

    @NotBlank(message = "Tên loại linh kiện không được để trống")
    @Size(min = 2, max = 100, message = "Tên loại linh kiện phải có từ 2 đến 100 ký tự")
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
