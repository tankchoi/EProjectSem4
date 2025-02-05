package vn.aptech.java.models;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Entity
@Table(name = "RequestDetails")
public class RequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    private Integer quantity;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public RequestDetail() {
    }

    public RequestDetail(Long id, Request request, Part part, Integer quantity, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.request = request;
        this.part = part;
        this.quantity = quantity;
        this.createdAt = createdAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
