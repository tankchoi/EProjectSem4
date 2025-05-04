package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.AuditLog;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.AuditLogRepository;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(User user, String tableName, Long recordId, String actionType, String oldValue, String newValue) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setTableName(tableName);
        log.setRecordId(recordId);
        log.setActionType(actionType);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        auditLogRepository.save(log);
    }
}
