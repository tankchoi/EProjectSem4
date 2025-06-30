package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.java.models.CustomerLaptop;

import java.util.List;

@Repository
public interface CustomerLaptopRepository extends JpaRepository<CustomerLaptop, Long> {

    @Query("SELECT cl FROM CustomerLaptop cl WHERE cl.customer.id = :customerId")
    List<CustomerLaptop> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT cl FROM CustomerLaptop cl WHERE cl.laptop.id = :laptopId")
    List<CustomerLaptop> findByLaptopId(@Param("laptopId") Long laptopId);

    @Query("SELECT cl FROM CustomerLaptop cl WHERE cl.serialNumber = :serialNumber")
    CustomerLaptop findBySerialNumber(@Param("serialNumber") String serialNumber);
}
