package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Request;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r " +
            "JOIN FETCH r.customerLaptop cl " +
            "JOIN FETCH cl.laptop l " +
            "LEFT JOIN FETCH r.technician t " +
            "WHERE cl.customer.id = :customerId " +
            "ORDER BY r.bookingDate DESC")
    List<Request> getHistoryByCustomerId(@Param("customerId") Long customerId);
}