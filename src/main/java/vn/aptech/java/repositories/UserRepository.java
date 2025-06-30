package vn.aptech.java.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.java.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    // Staff-specific queries
    @Query("SELECT u FROM User u WHERE u.role = 'STAFF' ORDER BY u.createdAt DESC")
    List<User> findAllStaff();

    @Query("SELECT u FROM User u WHERE u.role = 'STAFF' ORDER BY u.createdAt DESC")
    Page<User> findAllStaffPage(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = 'STAFF' AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phone LIKE CONCAT('%', :keyword, '%'))")
    List<User> searchStaff(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.role = 'STAFF' AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phone LIKE CONCAT('%', :keyword, '%'))")
    Page<User> searchStaffPage(@Param("keyword") String keyword, Pageable pageable);

    // Customer-specific queries
    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' ORDER BY u.createdAt DESC")
    List<User> findAllCustomers();

    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' ORDER BY u.createdAt DESC")
    Page<User> findAllCustomersPage(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND u.status = :status ORDER BY u.createdAt DESC")
    Page<User> findAllCustomersByStatus(@Param("status") User.Status status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phone LIKE CONCAT('%', :keyword, '%'))")
    List<User> searchCustomers(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phone LIKE CONCAT('%', :keyword, '%'))")
    Page<User> searchCustomersPage(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND u.status = :status AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phone LIKE CONCAT('%', :keyword, '%'))")
    Page<User> searchCustomersByStatus(@Param("keyword") String keyword, @Param("status") User.Status status,
            Pageable pageable);

    // General queries for any role and status
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = :status ORDER BY u.createdAt DESC")
    List<User> findByRoleAndStatus(@Param("role") User.Role role, @Param("status") User.Status status);

    // Validation queries for update operations
    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}
