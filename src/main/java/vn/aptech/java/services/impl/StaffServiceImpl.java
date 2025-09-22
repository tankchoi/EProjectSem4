package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.CreateStaffDTO;
import vn.aptech.java.dtos.admin.UpdateStaffDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.StaffService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public User createStaff(CreateStaffDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // TODO: encode password
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
    public List<User> searchByPhone(String phone) {
        return userRepository.findByPhoneContaining(phone)
                .stream()
                .filter(u -> u.getRole() == User.Role.STAFF)
                .collect(Collectors.toList());
    }

    @Override
    public Page<User> getAllStaffPaginated(Pageable pageable) {
        List<User> allStaffs = getAllStaff();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allStaffs.size());

        if (start > allStaffs.size()) {
            return new PageImpl<>(List.of(), pageable, allStaffs.size());
        }

        List<User> staffsPage = allStaffs.subList(start, end);
        return new PageImpl<>(staffsPage, pageable, allStaffs.size());
    }

    @Override
    public Page<User> searchStaffPaginated(String search, Pageable pageable) {
        List<User> foundStaffs = searchByNameEmailPhone(search);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), foundStaffs.size());

        if (start > foundStaffs.size()) {
            return new PageImpl<>(List.of(), pageable, foundStaffs.size());
        }

        List<User> staffsPage = foundStaffs.subList(start, end);
        return new PageImpl<>(staffsPage, pageable, foundStaffs.size());
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
}