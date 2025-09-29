package vn.aptech.java.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    // Dùng tạm sau lấy hàm phần get nhân viên của Trứng sau
    List<User> findAllByRoleAndStatus(User.Role role, User.Status status);

    long countByRole(User.Role role);

    long count();

    @Query("SELECT u FROM User u WHERE u.role = :role AND " +
            "(LOWER(u.fullname) LIKE %:search% OR LOWER(u.email) LIKE %:search% OR u.phone LIKE %:search%)")
    List<User> findByRoleAndSearch(String search);

    List<User> findByRole(User.Role role);

    User findByPhone(String phone);

    List<User> findByPhoneContaining(String phone);

}
