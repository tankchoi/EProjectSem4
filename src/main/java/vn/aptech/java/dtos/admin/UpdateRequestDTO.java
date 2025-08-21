package vn.aptech.java.dtos.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.java.models.Request;

import java.util.Date;
import java.util.List;

public class UpdateRequestDTO {
    @NotNull(message = "ID không được để trống")
    private Long id;

    @NotBlank(message = "Số serial không được để trống")
    private String serialNumber;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullname;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày đặt lịch không được để trống")
    private Date bookingDate;

    @NotNull(message = "Trạng thái không được để trống")
    private Request.Status status;

    private Long technicianId;

    private List<MultipartFile> newImages;

    private List<String> existingImageUrls;

    public UpdateRequestDTO() {
    }

    public UpdateRequestDTO(Long id, String serialNumber, String fullname, String email, String phone, String address, String description, Date bookingDate, Request.Status status, Long technicianId, List<MultipartFile> newImages, List<String> existingImageUrls) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.bookingDate = bookingDate;
        this.status = status;
        this.technicianId = technicianId;
        this.newImages = newImages;
        this.existingImageUrls = existingImageUrls;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStatus(Request.Status status) {
        this.status = status;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public void setNewImages(List<MultipartFile> newImages) {
        this.newImages = newImages;
    }

    public void setExistingImageUrls(List<String> existingImageUrls) {
        this.existingImageUrls = existingImageUrls;
    }

    public Long getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
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

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public Request.Status getStatus() {
        return status;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public List<MultipartFile> getNewImages() {
        return newImages;
    }

    public List<String> getExistingImageUrls() {
        return existingImageUrls;
    }
}