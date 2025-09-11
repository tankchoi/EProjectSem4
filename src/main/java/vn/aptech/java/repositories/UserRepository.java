package vn.aptech.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> findByPhone(String phone);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    // Dùng tạm sau lấy hàm phần get nhân viên của Trứng sau
    List<User> findAllByRoleAndStatus(User.Role role, User.Status status);

    List<User> findByPhoneContaining(String phone); // tìm kiếm theo số điện thoại

    long countByRole(User.Role role);

    long count();
}
