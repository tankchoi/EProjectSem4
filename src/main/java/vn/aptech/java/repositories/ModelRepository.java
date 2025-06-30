package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.java.models.Model;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByNameContainingIgnoreCase(String name);
}
