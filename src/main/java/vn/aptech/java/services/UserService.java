package vn.aptech.java.services;

import vn.aptech.java.dtos.CreateStaffDTO;
import vn.aptech.java.models.User;

public interface UserService {
    User findByUsername(String username);
    User getCurrentUser();
}
