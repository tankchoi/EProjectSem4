package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.LaptopRepository;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.CUSTOMER)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getCustomerById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == User.Role.CUSTOMER);
    }

    @Override
    public Page<User> getAllCustomersPaginated(Pageable pageable) {
        List<User> allCustomers = getAllCustomers();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allCustomers.size());

        if (start > allCustomers.size()) {
            return new PageImpl<>(List.of(), pageable, allCustomers.size());
        }

        List<User> customersPage = allCustomers.subList(start, end);
        return new PageImpl<>(customersPage, pageable, allCustomers.size());
    }

    @Override
    public Page<User> searchCustomersPaginated(String search, Pageable pageable) {
        List<User> foundCustomers = searchByNameEmailPhone(search);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), foundCustomers.size());

        if (start > foundCustomers.size()) {
            return new PageImpl<>(List.of(), pageable, foundCustomers.size());
        }

        List<User> customersPage = foundCustomers.subList(start, end);
        return new PageImpl<>(customersPage, pageable, foundCustomers.size());
    }

    @Override
    public List<User> searchByNameEmailPhone(String search) {
        String searchLower = search.toLowerCase();
        return getAllCustomers().stream()
                .filter(user -> user.getFullname().toLowerCase().contains(searchLower) ||
                        user.getEmail().toLowerCase().contains(searchLower) ||
                        (user.getPhone() != null && user.getPhone().contains(search)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Laptop> getCustomerLaptops(Long customerId) {
        // TODO: Cần thêm field customer/owner trong Laptop model để track ownership
        // Hiện tại return empty list
        return List.of();
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
}
