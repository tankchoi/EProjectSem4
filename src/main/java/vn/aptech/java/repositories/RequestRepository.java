package vn.aptech.java.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.java.models.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE r.status = :status")
    List<Request> findByStatus(@Param("status") String status);

    @Query("SELECT r FROM Request r WHERE r.status = :status")
    Page<Request> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.fullname LIKE %:keyword% OR r.email LIKE %:keyword% OR r.phone LIKE %:keyword%")
    List<Request> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT r FROM Request r WHERE " +
            "(r.fullname LIKE %:search% OR r.email LIKE %:search% OR r.phone LIKE %:search% OR r.address LIKE %:search% OR r.description LIKE %:search%) "
            +
            "AND (:status IS NULL OR r.status = :status)")
    Page<Request> findBySearchAndStatus(@Param("search") String search, @Param("status") String status,
            Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.technician.id = :technicianId")
    List<Request> findByTechnicianId(@Param("technicianId") Long technicianId);

    @Query("SELECT r FROM Request r WHERE r.status = 'APPROVED' AND r.id NOT IN (SELECT i.request.id FROM Invoice i)")
    List<Request> findApprovedRequestsWithoutInvoice();
}
