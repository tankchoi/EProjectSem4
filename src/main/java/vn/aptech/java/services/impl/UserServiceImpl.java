package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.UpdateProfileDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private void checkExistence(String username , String email, String phone) {
        if(userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if(userRepository.existsByPhone(phone)) {
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


}
