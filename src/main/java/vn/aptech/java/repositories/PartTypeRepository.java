package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.PartType;

import java.util.List;

public interface PartTypeRepository extends JpaRepository<PartType, Long> {
    List<PartType> findByNameContainingIgnoreCase(String name);
}
