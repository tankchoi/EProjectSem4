package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

        List<Request> findByCustomerLaptop(CustomerLaptop customerLaptop);

        @Query("SELECT r FROM Request r " +
                        "JOIN FETCH r.customerLaptop cl " +
                        "JOIN FETCH cl.laptop l " +
                        "LEFT JOIN FETCH r.technician t " +
                        "WHERE cl.customer.id = :customerId " +
                        "AND r.status = 'COMPLETED' " +
                        "ORDER BY r.bookingDate DESC")
        List<Request> getHistoryByCustomerId(@Param("customerId") Long customerId);

        long countByStatus(Request.Status status);

        @Query("SELECT COUNT(r) FROM Request r")
        long countAllRequests();

        @Query(value = """
                        SELECT DISTINCT r.*
                        FROM Requests r
                        LEFT JOIN Customer_Laptops cl ON r.customer_Laptop_Id = cl.id
                        WHERE (:fullname IS NULL OR :fullname = '' OR LOWER(r.fullname) LIKE LOWER(CONCAT('%', :fullname, '%')))
                          AND (:phone IS NULL OR :phone = '' OR LOWER(r.phone) LIKE LOWER(CONCAT('%', :phone, '%')))
                          AND (:email IS NULL OR :email = '' OR LOWER(r.email) LIKE LOWER(CONCAT('%', :email, '%')))
                          AND (:serialNumber IS NULL OR :serialNumber = '' OR LOWER(cl.serial_Number) LIKE LOWER(CONCAT('%', :serialNumber, '%')))
                          AND (:technicianId IS NULL OR r.technician_Id = :technicianId)
                          AND (:status IS NULL OR r.status = :status)
                        ORDER BY r.created_At DESC
                        """, nativeQuery = true)
        List<Request> filterRequests(
                        @Param("fullname") String fullname,
                        @Param("phone") String phone,
                        @Param("email") String email,
                        @Param("serialNumber") String serialNumber,
                        @Param("technicianId") Long technicianId,
                        @Param("status") String status);
}