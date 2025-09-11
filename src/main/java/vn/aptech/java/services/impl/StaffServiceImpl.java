package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.CreateStaffDTO;
import vn.aptech.java.dtos.admin.UpdateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.StaffService;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllStaff() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.STAFF)
                .toList();
    }

    @Override
    public Optional<User> getStaffById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == User.Role.STAFF);
    }

    @Override
    public void createStaff(CreateStaffDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // TODO: encode password
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(User.Role.STAFF);
        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void updateStaff(UpdateStaffDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        if (dto.getFullname() != null) user.setFullname(dto.getFullname());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());

        userRepository.save(user);
    }

    @Override
    public void banStaff(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
        user.setStatus(User.Status.BANNED);
        userRepository.save(user);
    }

    @Override
    public List<User> searchByPhone(String phone) {
        return userRepository.findByPhoneContaining(phone);
    }
}