package vn.aptech.java.models;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Entity
@Table(name = "Laptops")
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "modelId", nullable = false)
    private Model model;

    private Integer warrantyPeriod;
    private String imgUrl;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public Laptop() {
    }

    public Laptop(Long id, String name, Model model, Integer warrantyPeriod, String imgUrl, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.warrantyPeriod = warrantyPeriod;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModel(Model model) {
        this.model = model;
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
