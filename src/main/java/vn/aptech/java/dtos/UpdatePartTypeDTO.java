package vn.aptech.java.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdatePartTypeDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "Tên kiểu linh kiện không được để trống")
    private String name;
    public UpdatePartTypeDTO() {}
    public UpdatePartTypeDTO(Long id, String name) {
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
