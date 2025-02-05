package vn.aptech.java.models;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Entity
@Table(name = "AuditLogs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String tableName;
    private Long recordId;
    private String actionType;
    private String oldValue;
    private String newValue;

    @CreationTimestamp
    private Timestamp createdAt;

    public AuditLog() {
    }

    public AuditLog(Long id, User user, String tableName, Long recordId, String actionType, String oldValue, String newValue, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.tableName = tableName;
        this.recordId = recordId;
        this.actionType = actionType;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTableName() {
        return tableName;
    }

    public Long getRecordId() {
        return recordId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
