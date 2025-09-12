package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.Laptop;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
  @Query(value = """
      SELECT l.* FROM laptops l
      WHERE (:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:modelId IS NULL OR l.model_id = :modelId)
      """, nativeQuery = true)
  List<Laptop> filterLaptops(@Param("name") String name, @Param("modelId") Long modelId);

}
