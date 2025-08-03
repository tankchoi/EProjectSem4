package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.aptech.java.models.CustomerLaptop;
import java.util.List;

public interface CustomerLaptopRepository extends JpaRepository<CustomerLaptop, Long> {

       @Query("SELECT cl FROM CustomerLaptop cl " +
                     "JOIN FETCH cl.laptop l " +
                     "JOIN FETCH l.model m " +
                     "WHERE cl.customer.id = :customerId")
       List<CustomerLaptop> findAllByCustomerId(@Param("customerId") Long customerId);

       @Query("SELECT cl FROM CustomerLaptop cl " +
                     "JOIN FETCH cl.laptop l " +
                     "JOIN FETCH l.model m " +
                     "WHERE cl.serialNumber = :serial AND cl.customer.id = :customerId")
       CustomerLaptop findBySerialAndCustomerId(@Param("serial") String serial,
                     @Param("customerId") Long customerId);

       @Query("SELECT cl FROM CustomerLaptop cl WHERE cl.serialNumber = :serial")
       CustomerLaptop findBySerial(@Param("serial") String serial);
}
