package vn.aptech.java.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CreateRequestDTO {

    @NotNull(message = "Laptop khách hàng không được để trống")
    private Long customerLaptopId;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullname;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Mô tả vấn đề không được để trống")
    private String description;

    @NotNull(message = "Ngày đặt lịch không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    private String status = "PENDING"; // Default status

    private Long technicianId; // Optional, can be assigned later

    public CreateRequestDTO() {
    }

    public CreateRequestDTO(Long customerLaptopId, String fullname, String email, String phone,
            String address, String description, Date bookingDate, String status,
            Long technicianId) {
        this.customerLaptopId = customerLaptopId;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.bookingDate = bookingDate;
        this.status = status;
        this.technicianId = technicianId;
    }

    // Getters and Setters
    public Long getCustomerLaptopId() {
        return customerLaptopId;
    }

    public void setCustomerLaptopId(Long customerLaptopId) {
        this.customerLaptopId = customerLaptopId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
}
