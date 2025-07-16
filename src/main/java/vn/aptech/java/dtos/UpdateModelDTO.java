package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateModelDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "Tên model không được để trống")
    private String name;
    public UpdateModelDTO() {
    }
    public UpdateModelDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
