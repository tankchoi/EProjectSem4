package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UpdateProfileDTO {
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullname;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không đúng định dạng")
    private String phone;

    public UpdateProfileDTO() {
    }

    public UpdateProfileDTO(String fullname, String email, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}