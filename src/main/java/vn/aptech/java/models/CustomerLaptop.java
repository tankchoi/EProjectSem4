package vn.aptech.java.models;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Entity
@Table(name = "CustomerLaptops")
public class CustomerLaptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "laptopId", nullable = false)
    private Laptop laptop;

    private String serialNumber;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public CustomerLaptop() {
    }

    public CustomerLaptop(Long id, User customer, String serialNumber, Laptop laptop, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.customer = customer;
        this.serialNumber = serialNumber;
        this.laptop = laptop;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public String getSerialNumber() {
        return serialNumber;
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

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
