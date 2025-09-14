package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<User> getAllCustomers();

    Optional<User> getCustomerById(Long id);

    Page<User> getAllCustomersPaginated(Pageable pageable);
    // đang lỗi chưa fix, dùng tạm datatable

    Page<User> searchCustomersPaginated(String search, Pageable pageable);

    List<User> searchByNameEmailPhone(String search);

    List<Laptop> getCustomerLaptops(Long customerId);

    // List<Warranty> getWarrantyHistory(Long customerId); // Tạm comment đến khi có
    // Warranty model

    void banCustomer(Long id);

    void restoreCustomer(Long id);
}
