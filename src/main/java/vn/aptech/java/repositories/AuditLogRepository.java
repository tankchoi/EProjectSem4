package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
