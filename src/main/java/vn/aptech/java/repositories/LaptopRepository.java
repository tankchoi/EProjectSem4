package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.Laptop;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    List<Laptop> findByNameContainingIgnoreCase(String name);
    List<Laptop> findByModelId(Long id);

}
