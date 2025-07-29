package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Laptop;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    @Query(value = """
    SELECT l.* FROM laptops l
    JOIN models m ON l.model_id = m.id
    WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR CAST(l.warranty_period AS CHAR) LIKE CONCAT('%', :keyword, '%')
    """, nativeQuery = true)
    List<Laptop> searchLaptopsByKeyword(@Param("keyword") String keyword);

}
