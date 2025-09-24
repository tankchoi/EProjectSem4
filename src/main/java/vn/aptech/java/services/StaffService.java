package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.dtos.admin.CreateStaffDTO;
import vn.aptech.java.dtos.admin.UpdateStaffDTO;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<User> getAllStaff();

    Optional<User> getStaffById(Long id);

    User createStaff(CreateStaffDTO dto);

    void updateStaff(UpdateStaffDTO dto);

    void banStaff(Long id);

    List<User> searchByPhone(String phone);

    Page<User> getAllStaffPaginated(Pageable pageable);
    // lỗi chưa fix tạm thời dùng datatable

    Page<User> searchStaffPaginated(String search, Pageable pageable);

    List<User> searchByNameEmailPhone(String search);

    void restoreStaff(Long id);

    UpdateStaffDTO getUpdateStaffDTO(Long id);

    User resetPassword(Long staffId);
}