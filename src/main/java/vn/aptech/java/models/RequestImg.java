package vn.aptech.java.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.sql.Timestamp;

@Entity
@Table(name = "RequestImgs")
public class RequestImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Request request;

    @Column(nullable = false)
    private String imgUrl;

    @CreationTimestamp
    private Timestamp createdAt;

    public RequestImg() {
    }

    public RequestImg(Long id, Request request, String imgUrl, Timestamp createdAt) {
        this.id = id;
        this.request = request;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
