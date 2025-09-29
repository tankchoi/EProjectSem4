package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<User> getAllCustomers();

    User getCustomerById(Long id);

    void banCustomer(Long id);

    void restoreCustomer(Long id);

    User getCustomerByPhone(String phone);

    List<User> searchCustomersByPhone(String phone);

    List<Request> getWarrantyHistoryByCustomer(Long customerId);
}
