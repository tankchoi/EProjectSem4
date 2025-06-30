package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Invoice;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByStatus(String status);

    @Query("SELECT i FROM Invoice i WHERE " +
            "LOWER(i.request.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.request.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.request.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Invoice> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT i FROM Invoice i JOIN i.request r WHERE r.technician.id = :technicianId")
    List<Invoice> findByTechnicianId(@Param("technicianId") Long technicianId);
}
