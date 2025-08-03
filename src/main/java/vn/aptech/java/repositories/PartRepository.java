package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Part;
import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    @Query(value = """
    SELECT p.* FROM parts p
    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:partTypeId IS NULL OR p.part_type_id = :partTypeId)
      AND (:laptopId IS NULL OR p.laptop_id = :laptopId)
    """, nativeQuery = true)
    List<Part> filterParts(@Param("name") String name,
                           @Param("partTypeId") Long partTypeId,
                           @Param("laptopId") Long laptopId);
    @Query("SELECT p FROM Part p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Part> findByNameParts(@Param("keyword") String keyword);
}
