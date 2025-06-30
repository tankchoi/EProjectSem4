package vn.aptech.java.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Parts")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partTypeId", nullable = false)
    private PartType partType;

    @ManyToOne
    @JoinColumn(name = "laptopId", nullable = true)
    private Laptop laptop;

    private String name;
    private Double price;
    private Integer quantity;
    private Integer warrantyPeriod;
    private String imgUrl;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public Part() {
    }

    public Part(Long id, PartType partType, Laptop laptop, String name, Double price, Integer quantity,
            Integer warrantyPeriod, String imgUrl, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.partType = partType;
        this.laptop = laptop;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.warrantyPeriod = warrantyPeriod;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public PartType getPartType() {
        return partType;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getImgUrl() {
        return imgUrl;
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

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
