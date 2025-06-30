package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.CreateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null
                || authentication.getName().equals("anonymousUser")) {
            return null; // Trả về null khi chưa có user đăng nhập
        }
        String userName = authentication.getName();
        return userRepository.findByUsername(userName);
    }

    public void createStaff(CreateStaffDTO createStaffDTO) {
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
        newStaff.setRole(createStaffDTO.getRole());
        newStaff.setStatus(createStaffDTO.getStatus());

        // Lưu vào cơ sở dữ liệu
        userRepository.save(newStaff);
    }
}
