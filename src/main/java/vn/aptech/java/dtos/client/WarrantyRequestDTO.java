package vn.aptech.java.dtos.client;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WarrantyRequestDTO {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private String serialNumber;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Ngày đặt lịch không được để trống")
    @Future(message = "Ngày đặt lịch phải sau ngày hiện tại")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    private List<MultipartFile> images;

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
