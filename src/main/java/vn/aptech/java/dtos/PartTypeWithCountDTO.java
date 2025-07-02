package vn.aptech.java.dtos;

import vn.aptech.java.models.PartType;
import java.sql.Timestamp;

public class PartTypeWithCountDTO {
    private Long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long partCount;

    public PartTypeWithCountDTO() {
    }

    public PartTypeWithCountDTO(PartType partType, Long partCount) {
        this.id = partType.getId();
        this.name = partType.getName();
        this.createdAt = partType.getCreatedAt();
        this.updatedAt = partType.getUpdatedAt();
        this.partCount = partCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPartCount() {
        return partCount;
    }

    public void setPartCount(Long partCount) {
        this.partCount = partCount;
    }
}
