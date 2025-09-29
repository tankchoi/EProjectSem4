package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.CreateStaffDTO;
import vn.aptech.java.dtos.admin.UpdateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.StaffService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public List<User> getAllStaff() {
        return userRepository.findByRole(User.Role.STAFF);
    }

    @Override
    public Optional<User> getStaffById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == User.Role.STAFF);
    }

    @Override
    public User createStaff(CreateStaffDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);

        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(User.Role.STAFF);
        user.setStatus(User.Status.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public void updateStaff(UpdateStaffDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        if (dto.getFullname() != null)
            user.setFullname(dto.getFullname());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
        if (dto.getPhone() != null)
            user.setPhone(dto.getPhone());
        if (dto.getStatus() != null)
            user.setStatus(dto.getStatus());

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
    public List<User> searchByNameEmailPhone(String search) {
        String searchLower = search.toLowerCase();
        return getAllStaff().stream()
                .filter(user -> user.getFullname().toLowerCase().contains(searchLower) ||
                        user.getEmail().toLowerCase().contains(searchLower) ||
                        (user.getPhone() != null && user.getPhone().contains(search)))
                .collect(Collectors.toList());
    }

    @Override
    public void restoreStaff(Long id) {
        Optional<User> staffOpt = getStaffById(id);
        if (staffOpt.isPresent()) {
            User staff = staffOpt.get();
            UpdateStaffDTO updateStaffDTO = new UpdateStaffDTO();
            updateStaffDTO.setId(staff.getId());
            updateStaffDTO.setFullname(staff.getFullname());
            updateStaffDTO.setEmail(staff.getEmail());
            updateStaffDTO.setPhone(staff.getPhone());
            updateStaffDTO.setStatus(User.Status.ACTIVE);
            updateStaff(updateStaffDTO);
        } else {
            throw new RuntimeException("Không tìm thấy nhân viên với ID: " + id);
        }
    }

    @Override
    public UpdateStaffDTO getUpdateStaffDTO(Long id) {
        Optional<User> staffOpt = getStaffById(id);
        if (staffOpt.isPresent()) {
            User staff = staffOpt.get();
            UpdateStaffDTO updateStaffDTO = new UpdateStaffDTO();
            updateStaffDTO.setId(staff.getId());
            updateStaffDTO.setFullname(staff.getFullname());
            updateStaffDTO.setEmail(staff.getEmail());
            updateStaffDTO.setPhone(staff.getPhone());
            updateStaffDTO.setStatus(staff.getStatus());
            return updateStaffDTO;
        }
        throw new RuntimeException("Không tìm thấy nhân viên với ID: " + id);
    }

    @Override
    public User resetPassword(Long staffId){
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên"));

        String rawPassword = generateRandomPassword(10);

        String encodedPassword = passwordEncoder.encode(rawPassword);
        staff.setPassword(encodedPassword);
        userRepository.save(staff);

        sendPasswordResetEmail(staff.getEmail(), rawPassword);
        System.out.println("Reset mật khẩu cho nhân viên: " + staff.getEmail());
        System.out.println("Mật khẩu mới (raw): " + rawPassword);


        return staff; // trả về để controller lấy email
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void sendPasswordResetEmail(String to, String newPassword){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Mật khẩu mới của bạn");
            message.setText("Xin chào,\\n\\nHệ thống đã reset mật khẩu của bạn. Mật khẩu mới là: " + newPassword + "\n\nVui lòng đăng nhập và đổi mật khẩu ngay sau khi sử dụng.") ;
            mailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}