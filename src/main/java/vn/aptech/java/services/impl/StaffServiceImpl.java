package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.CreateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.StaffService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllStaff() {
        return userRepository.findAllStaff();
    }

    @Override
    public Page<User> getStaffPage(Pageable pageable) {
        return userRepository.findAllStaffPage(pageable);
    }

    @Override
    public Optional<User> getStaffById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && userOpt.get().getRole() == User.Role.STAFF) {
            return userOpt;
        }
        return Optional.empty();
    }

    @Override
    public User createStaff(CreateStaffDTO createStaffDTO) {
        // Validate unique constraints
        if (userRepository.existsByUsername(createStaffDTO.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(createStaffDTO.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhone(createStaffDTO.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        User newStaff = new User();
        newStaff.setUsername(createStaffDTO.getUsername());
        newStaff.setPassword(passwordEncoder.encode(createStaffDTO.getPassword()));
        newStaff.setFullname(createStaffDTO.getFullname());
        newStaff.setEmail(createStaffDTO.getEmail());
        newStaff.setPhone(createStaffDTO.getPhone());
        newStaff.setRole(User.Role.STAFF); // Force role to STAFF
        newStaff.setStatus(createStaffDTO.getStatus() != null ? createStaffDTO.getStatus() : User.Status.ACTIVE);

        return userRepository.save(newStaff);
    }

    @Override
    public User updateStaff(Long id, CreateStaffDTO updateStaffDTO) {
        Optional<User> staffOpt = getStaffById(id);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Nhân viên không tồn tại");
        }

        User staff = staffOpt.get();

        // Validate unique constraints (excluding current user)
        if (userRepository.existsByUsernameAndIdNot(updateStaffDTO.getUsername(), id)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmailAndIdNot(updateStaffDTO.getEmail(), id)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhoneAndIdNot(updateStaffDTO.getPhone(), id)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Update fields
        staff.setUsername(updateStaffDTO.getUsername());
        if (updateStaffDTO.getPassword() != null && !updateStaffDTO.getPassword().trim().isEmpty()) {
            staff.setPassword(passwordEncoder.encode(updateStaffDTO.getPassword()));
        }
        staff.setFullname(updateStaffDTO.getFullname());
        staff.setEmail(updateStaffDTO.getEmail());
        staff.setPhone(updateStaffDTO.getPhone());
        staff.setStatus(updateStaffDTO.getStatus() != null ? updateStaffDTO.getStatus() : staff.getStatus());

        return userRepository.save(staff);
    }

    @Override
    public void deleteStaff(Long id) {
        Optional<User> staffOpt = getStaffById(id);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Nhân viên không tồn tại");
        }

        User staff = staffOpt.get();
        staff.setStatus(User.Status.BANNED);
        userRepository.save(staff);
    }

    @Override
    public void restoreStaff(Long id) {
        Optional<User> staffOpt = getStaffById(id);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Nhân viên không tồn tại");
        }

        User staff = staffOpt.get();
        staff.setStatus(User.Status.ACTIVE);
        userRepository.save(staff);
    }

    @Override
    public List<User> searchStaff(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStaff();
        }
        return userRepository.searchStaff(keyword.trim());
    }

    @Override
    public Page<User> searchStaffPage(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getStaffPage(pageable);
        }
        return userRepository.searchStaffPage(keyword.trim(), pageable);
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
