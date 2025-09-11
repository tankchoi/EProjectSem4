package vn.aptech.java.services;

import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Optional<User> findByPhone(String phone);

    Optional<User> findById(Long id);

    List<CustomerLaptop> getCustomerLaptops(User customer);

    List<Request> getRequestHistory(User customer);
}
