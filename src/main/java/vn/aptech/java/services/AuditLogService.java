package vn.aptech.java.services;

import vn.aptech.java.models.User;

public interface AuditLogService {
    void log(User user, String tableName, Long recordId, String actionType, String oldValue, String newValue);
}
