package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.UpdateProfileDTO;
import vn.aptech.java.dtos.client.RegisterDTO;
import vn.aptech.java.dtos.client.UpdateInfoDTO;
import vn.aptech.java.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);

    User getCurrentUser();

    void updateProfile(UpdateProfileDTO updateProfileDTO);

    void updatePassword(String newPassword);

    List<User> getTechnicians();

    void createAccount(RegisterDTO registerDTO);

    void updateInformation(Long userId, UpdateInfoDTO dto);

    Optional<User> findById(Long id);


    long countByRole(User.Role role);

}
