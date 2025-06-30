package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.java.models.Part;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByNameContainingIgnoreCase(String name);

    List<Part> findByPartTypeId(Long partTypeId);

    List<Part> findByLaptopId(Long laptopId);
}
