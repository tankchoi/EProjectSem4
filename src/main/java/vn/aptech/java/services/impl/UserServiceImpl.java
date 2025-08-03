package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.aptech.java.dtos.admin.UpdateProfileDTO;
import vn.aptech.java.dtos.client.RegisterDTO;
import vn.aptech.java.dtos.client.UpdateInfoDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private void checkExistence(String username, String email, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null
                || authentication.getName().equals("anonymousUser")) {
            return null;
        }
        String userName = authentication.getName();
        return userRepository.findByUsername(userName).orElse(null);
    }

    @Override
    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        User user = getCurrentUser();
        if (userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhoneAndIdNot(user.getPhone(), user.getId())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }
        user.setFullname(updateProfileDTO.getFullname());
        user.setEmail(updateProfileDTO.getEmail());
        user.setPhone(updateProfileDTO.getPhone());
        userRepository.save(user);
    }

    @Override
    public void updatePassword(String newPassword) {
        User user = getCurrentUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void createAccount(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        user.setStatus(User.Status.ACTIVE);

        userRepository.save(user);
    }

    @Override
    public void updateInformation(Long userId, UpdateInfoDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), userId)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("Mật khẩu mới và xác nhận không khớp");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        userRepository.save(user);
    }

}
