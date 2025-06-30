package vn.aptech.java.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.java.models.PartType;

import java.util.List;

@Repository
public interface PartTypeRepository extends JpaRepository<PartType, Long> {
    List<PartType> findByNameContainingIgnoreCase(String name);

    @Query("SELECT pt FROM PartType pt WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(pt.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<PartType> findWithSearch(@Param("search") String search, Pageable pageable);
}
