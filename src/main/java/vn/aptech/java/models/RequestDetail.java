package vn.aptech.java.models;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
@Entity
@Table(name = "RequestDetails")
public class RequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Part part;

    private Integer quantity;
    private Double price;
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public RequestDetail() {
    }

    public RequestDetail(Long id, Request request, Part part, Integer quantity, Double price, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.request = request;
        this.part = part;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public Part getPart() {
        return part;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}
