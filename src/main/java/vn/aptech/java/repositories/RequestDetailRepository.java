package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.RequestDetail;

public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    boolean existsByRequestIdAndPartId(Long requestId, Long partId);
}
