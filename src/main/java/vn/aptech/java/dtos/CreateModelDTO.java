package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotBlank;

public class CreateModelDTO {
    @NotBlank(message = "Tên model không được để trống")
    private String name;

    public CreateModelDTO() {
    }

    public CreateModelDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
