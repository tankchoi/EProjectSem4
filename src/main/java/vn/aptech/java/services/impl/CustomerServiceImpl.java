package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.CustomerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findByRole(User.Role.CUSTOMER);
    }

    @Override
    public User getCustomerById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID = " + id));
    }


    @Override
    public void banCustomer(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
        user.setStatus(User.Status.BANNED);
        userRepository.save(user);
    }

    @Override
    public void restoreCustomer(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public User getCustomerByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public List<User> searchCustomersByPhone(String phone) {
        return userRepository.findByPhoneContaining(phone);
    }

    @Override
    public List<Request> getWarrantyHistoryByCustomer(Long customerId) {
        return requestRepository.getHistoryByCustomerIdWithoutStatus(customerId);
    }


}
