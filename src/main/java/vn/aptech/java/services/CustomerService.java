package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.dtos.CreateCustomerDTO;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    /**
     * Lấy tất cả khách hàng (role = CUSTOMER)
     */
    List<User> getAllCustomers();

    /**
     * Lấy khách hàng phân trang với status filter
     */
    Page<User> getAllCustomers(String status, Pageable pageable);

    /**
     * Lấy khách hàng phân trang
     */
    Page<User> getCustomerPage(Pageable pageable);

    /**
     * Tìm khách hàng theo ID (return User directly)
     */
    User getCustomerById(Long id);

    /**
     * Tìm khách hàng theo ID (return Optional)
     */
    Optional<User> findCustomerById(Long id);

    /**
     * Tạo khách hàng mới
     */
    User createCustomer(CreateCustomerDTO createCustomerDTO);

    /**
     * Cập nhật thông tin khách hàng
     */
    User updateCustomer(Long id, CreateCustomerDTO updateCustomerDTO);

    /**
     * Xóa khách hàng (soft delete - chuyển status thành BANNED)
     */
    void deleteCustomer(Long id);

    /**
     * Khôi phục khách hàng (chuyển status về ACTIVE)
     */
    void restoreCustomer(Long id);

    /**
     * Tìm kiếm khách hàng theo keyword
     */
    List<User> searchCustomer(String keyword);

    /**
     * Tìm kiếm khách hàng theo keyword với phân trang và status filter
     */
    Page<User> searchCustomers(String keyword, String status, Pageable pageable);

    /**
     * Tìm kiếm khách hàng theo keyword với phân trang
     */
    Page<User> searchCustomerPage(String keyword, Pageable pageable);

    /**
     * Kiểm tra username đã tồn tại
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại
     */
    boolean existsByEmail(String email);

    /**
     * Kiểm tra phone đã tồn tại
     */
    boolean existsByPhone(String phone);

    /**
     * Kiểm tra username đã tồn tại (ngoại trừ user hiện tại)
     */
    boolean existsByUsernameAndIdNot(String username, Long id);

    /**
     * Kiểm tra email đã tồn tại (ngoại trừ user hiện tại)
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Kiểm tra phone đã tồn tại (ngoại trừ user hiện tại)
     */
    boolean existsByPhoneAndIdNot(String phone, Long id);
}
