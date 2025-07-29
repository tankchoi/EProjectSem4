package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Part;
import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    @Query(value = """
    SELECT p.* FROM parts p
    JOIN part_types pt ON p.part_type_id = pt.id
    JOIN laptops l ON p.laptop_id = l.id
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(pt.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR CAST(p.price AS CHAR) LIKE CONCAT('%', :keyword, '%')
       OR CAST(p.quantity AS CHAR) LIKE CONCAT('%', :keyword, '%')
       OR CAST(p.warranty_period AS CHAR) LIKE CONCAT('%', :keyword, '%')
    """, nativeQuery = true)
    List<Part> searchPartsByKeyword(@Param("keyword") String keyword);
}
