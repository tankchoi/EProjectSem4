package vn.aptech.java.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.java.models.RequestDetail;

import java.util.List;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {

    /**
     * Tìm tất cả request details theo request ID
     */
    List<RequestDetail> findByRequestId(Long requestId);

    /**
     * Tìm tất cả request details theo part ID
     */
    List<RequestDetail> findByPartId(Long partId);

    /**
     * Tìm request detail theo request ID và part ID
     */
    @Query("SELECT rd FROM RequestDetail rd WHERE rd.request.id = :requestId AND rd.part.id = :partId")
    RequestDetail findByRequestIdAndPartId(@Param("requestId") Long requestId, @Param("partId") Long partId);

    /**
     * Tìm kiếm request details với phân trang
     */
    @Query("SELECT rd FROM RequestDetail rd " +
            "WHERE (:search IS NULL OR :search = '' OR " +
            "LOWER(rd.request.fullname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(rd.part.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(rd.part.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<RequestDetail> findBySearch(@Param("search") String search, Pageable pageable);

    /**
     * Tìm request details theo status của request
     */
    @Query("SELECT rd FROM RequestDetail rd WHERE rd.request.status = :status")
    List<RequestDetail> findByRequestStatus(@Param("status") String status);

    /**
     * Tìm request details theo technician ID
     */
    @Query("SELECT rd FROM RequestDetail rd WHERE rd.request.technician.id = :technicianId")
    List<RequestDetail> findByTechnicianId(@Param("technicianId") Long technicianId);

    /**
     * Tính tổng số lượng parts được sử dụng
     */
    @Query("SELECT COALESCE(SUM(rd.quantity), 0) FROM RequestDetail rd WHERE rd.part.id = :partId")
    Long getTotalQuantityByPartId(@Param("partId") Long partId);

    /**
     * Tính tổng giá trị của request
     */
    @Query("SELECT COALESCE(SUM(rd.quantity * rd.part.price), 0) FROM RequestDetail rd WHERE rd.request.id = :requestId")
    Double getTotalValueByRequestId(@Param("requestId") Long requestId);

    /**
     * Kiểm tra xem part có đang được sử dụng trong request nào không
     */
    @Query("SELECT COUNT(rd) > 0 FROM RequestDetail rd WHERE rd.part.id = :partId")
    boolean existsByPartId(@Param("partId") Long partId);

    /**
     * Lấy top parts được sử dụng nhiều nhất
     */
    @Query("SELECT rd.part.id, rd.part.name, SUM(rd.quantity) as totalUsed " +
            "FROM RequestDetail rd " +
            "GROUP BY rd.part.id, rd.part.name " +
            "ORDER BY totalUsed DESC")
    List<Object[]> getTopUsedParts(Pageable pageable);

    /**
     * Xóa tất cả request details của một request
     */
    void deleteByRequestId(Long requestId);

    /**
     * Đếm số lượng request details theo request ID
     */
    long countByRequestId(Long requestId);
}
