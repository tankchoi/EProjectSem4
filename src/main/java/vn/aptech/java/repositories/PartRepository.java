package vn.aptech.java.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Part;
import java.util.List;
import java.util.Optional;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Part p WHERE p.id = :id")
    Optional<Part> findByIdWithLock(@Param("id") Long id);
}
