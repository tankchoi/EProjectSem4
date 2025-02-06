package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.Model;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByNameLike(String name);
}
