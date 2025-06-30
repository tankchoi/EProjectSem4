package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.CreateCustomerDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.CustomerService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findAllCustomers();
    }

    @Override
    public Page<User> getAllCustomers(String status, Pageable pageable) {
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            try {
                User.Status statusEnum = User.Status.valueOf(status.toUpperCase());
                return userRepository.findAllCustomersByStatus(statusEnum, pageable);
            } catch (IllegalArgumentException e) {
                // If invalid status, return all customers
                return userRepository.findAllCustomersPage(pageable);
            }
        }
        return userRepository.findAllCustomersPage(pageable);
    }

    @Override
    public Page<User> getCustomerPage(Pageable pageable) {
        return userRepository.findAllCustomersPage(pageable);
    }

    @Override
    public User getCustomerById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && userOpt.get().getRole() == User.Role.CUSTOMER) {
            return userOpt.get();
        }
        throw new IllegalArgumentException("Khách hàng không tồn tại");
    }

    @Override
    public Optional<User> findCustomerById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && userOpt.get().getRole() == User.Role.CUSTOMER) {
            return userOpt;
        }
        return Optional.empty();
    }

    @Override
    public User createCustomer(CreateCustomerDTO createCustomerDTO) {
        // Validate unique constraints
        if (userRepository.existsByUsername(createCustomerDTO.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(createCustomerDTO.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhone(createCustomerDTO.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        User newCustomer = new User();
        newCustomer.setUsername(createCustomerDTO.getUsername());
        newCustomer.setPassword(passwordEncoder.encode(createCustomerDTO.getPassword()));
        newCustomer.setFullname(createCustomerDTO.getFullname());
        newCustomer.setEmail(createCustomerDTO.getEmail());
        newCustomer.setPhone(createCustomerDTO.getPhone());
        newCustomer.setRole(User.Role.CUSTOMER); // Force role to CUSTOMER
        newCustomer
                .setStatus(createCustomerDTO.getStatus() != null ? createCustomerDTO.getStatus() : User.Status.ACTIVE);
        newCustomer.setImg_link("/assets/images/default-avatar.png"); // Set default avatar

        return userRepository.save(newCustomer);
    }

    @Override
    public User updateCustomer(Long id, CreateCustomerDTO updateCustomerDTO) {
        Optional<User> customerOpt = findCustomerById(id);
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Khách hàng không tồn tại");
        }

        User customer = customerOpt.get();

        // Validate unique constraints (excluding current user)
        if (userRepository.existsByUsernameAndIdNot(updateCustomerDTO.getUsername(), id)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmailAndIdNot(updateCustomerDTO.getEmail(), id)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhoneAndIdNot(updateCustomerDTO.getPhone(), id)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Update fields
        customer.setUsername(updateCustomerDTO.getUsername());
        if (updateCustomerDTO.getPassword() != null && !updateCustomerDTO.getPassword().trim().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(updateCustomerDTO.getPassword()));
        }
        customer.setFullname(updateCustomerDTO.getFullname());
        customer.setEmail(updateCustomerDTO.getEmail());
        customer.setPhone(updateCustomerDTO.getPhone());
        customer.setStatus(
                updateCustomerDTO.getStatus() != null ? updateCustomerDTO.getStatus() : customer.getStatus());

        return userRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Optional<User> customerOpt = findCustomerById(id);
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Khách hàng không tồn tại");
        }

        User customer = customerOpt.get();
        customer.setStatus(User.Status.BANNED);
        userRepository.save(customer);
    }

    @Override
    public void restoreCustomer(Long id) {
        Optional<User> customerOpt = findCustomerById(id);
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Khách hàng không tồn tại");
        }

        User customer = customerOpt.get();
        customer.setStatus(User.Status.ACTIVE);
        userRepository.save(customer);
    }

    @Override
    public List<User> searchCustomer(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCustomers();
        }
        return userRepository.searchCustomers(keyword.trim());
    }

    @Override
    public Page<User> searchCustomers(String keyword, String status, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCustomers(status, pageable);
        }

        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            try {
                User.Status statusEnum = User.Status.valueOf(status.toUpperCase());
                return userRepository.searchCustomersByStatus(keyword.trim(), statusEnum, pageable);
            } catch (IllegalArgumentException e) {
                // If invalid status, search without status filter
                return userRepository.searchCustomersPage(keyword.trim(), pageable);
            }
        }
        return userRepository.searchCustomersPage(keyword.trim(), pageable);
    }

    @Override
    public Page<User> searchCustomerPage(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getCustomerPage(pageable);
        }
        return userRepository.searchCustomersPage(keyword.trim(), pageable);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existsByPhoneAndIdNot(String phone, Long id) {
        return userRepository.existsByPhoneAndIdNot(phone, id);
    }
}
