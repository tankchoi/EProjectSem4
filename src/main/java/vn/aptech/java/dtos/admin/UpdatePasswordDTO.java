package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordDTO {

    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    private String currentPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu mới phải từ 6-100 ký tự")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
    @AssertTrue(message = "Mật khẩu mới và xác nhận mật khẩu không khớp")
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
    @AssertTrue(message = "Mật khẩu mới phải khác mật khẩu hiện tại")
    public boolean isNewPasswordDifferent() {
        return currentPassword != null && newPassword != null && !currentPassword.equals(newPassword);
    }
    public UpdatePasswordDTO() {}

    public UpdatePasswordDTO(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }




}