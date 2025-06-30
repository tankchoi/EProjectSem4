package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.dtos.CreateStaffDTO;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface StaffService {

    /**
     * Lấy tất cả nhân viên (role = STAFF)
     */
    List<User> getAllStaff();

    /**
     * Lấy nhân viên phân trang
     */
    Page<User> getStaffPage(Pageable pageable);

    /**
     * Tìm nhân viên theo ID
     */
    Optional<User> getStaffById(Long id);

    /**
     * Tạo nhân viên mới
     */
    User createStaff(CreateStaffDTO createStaffDTO);

    /**
     * Cập nhật thông tin nhân viên
     */
    User updateStaff(Long id, CreateStaffDTO updateStaffDTO);

    /**
     * Xóa nhân viên (soft delete - chuyển status thành BANNED)
     */
    void deleteStaff(Long id);

    /**
     * Khôi phục nhân viên (chuyển status về ACTIVE)
     */
    void restoreStaff(Long id);

    /**
     * Tìm kiếm nhân viên theo keyword
     */
    List<User> searchStaff(String keyword);

    /**
     * Tìm kiếm nhân viên theo keyword với phân trang
     */
    Page<User> searchStaffPage(String keyword, Pageable pageable);

    /**
     * Kiểm tra username đã tồn tại
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại
     */
    boolean existsByEmail(String email);

    /**
     * Kiểm tra phone đã tồn tại
     */
    boolean existsByPhone(String phone);

    /**
     * Kiểm tra username đã tồn tại (ngoại trừ user hiện tại)
     */
    boolean existsByUsernameAndIdNot(String username, Long id);

    /**
     * Kiểm tra email đã tồn tại (ngoại trừ user hiện tại)
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Kiểm tra phone đã tồn tại (ngoại trừ user hiện tại)
     */
    boolean existsByPhoneAndIdNot(String phone, Long id);
}
