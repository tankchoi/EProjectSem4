package vn.aptech.java.models;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "Requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerLaptopId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerLaptop customerLaptop;

    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String description;

    private Date bookingDate;

    public enum Status {
        PENDING, APPROVED, COMPLETED, REJECTED
    }
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "technicianId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User technician;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public Request() {
    }

    public Request(Long id, CustomerLaptop customerLaptop, String fullname, String email, String phone, String address, String description, Date bookingDate, Status status, User technician, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.customerLaptop = customerLaptop;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.bookingDate = bookingDate;
        this.status = status;
        this.technician = technician;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public CustomerLaptop getCustomerLaptop() {
        return customerLaptop;
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

    public Status getStatus() {
        return status;
    }

    public User getTechnician() {
        return technician;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerLaptop(CustomerLaptop customerLaptop) {
        this.customerLaptop = customerLaptop;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
