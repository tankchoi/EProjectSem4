package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.NotNull;
import vn.aptech.java.models.User;

public class UpdateStaffDTO {
    @NotNull
    private Long id;

    private String fullname;
    private String email;
    private String phone;
    private User.Status status; // cho phép update status (ban/active)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public User.Status getStatus() { return status; }
    public void setStatus(User.Status status) { this.status = status; }
}
