package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.RequestDetail;

public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    boolean existsByRequestIdAndPartId(Long requestId, Long partId);

    java.util.List<RequestDetail> findByRequestId(Long requestId);

    @Query("SELECT COALESCE(SUM(rd.quantity), 0) FROM RequestDetail rd WHERE rd.request.id = :requestId")
    long countByRequestId(@Param("requestId") Long requestId);

    @Query("SELECT SUM(rd.quantity * rd.part.price) FROM RequestDetail rd WHERE rd.request.id = :requestId")
    Double getTotalValueByRequestId(@Param("requestId") Long requestId);
}
