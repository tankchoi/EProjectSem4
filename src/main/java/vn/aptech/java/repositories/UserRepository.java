package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
