package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.UpdatePasswordDTO;
import vn.aptech.java.dtos.admin.UpdateProfileDTO;
import vn.aptech.java.models.User;

public interface UserService {
    User findByUsername(String username);
    User getCurrentUser();
    void updateProfile(UpdateProfileDTO updateProfileDTO);
    void updatePassword(String newPassword);
}
