package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Laptop;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    @Query("""
    SELECT l FROM Laptop l
    JOIN l.model m
    WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR STR(l.warrantyPeriod) LIKE %:keyword%
""")
    List<Laptop> searchLaptopByKeyword(@Param("keyword") String keyword);
    List<Laptop> findByModelId(Long id);
}
