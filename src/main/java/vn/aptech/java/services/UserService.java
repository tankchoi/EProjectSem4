package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
