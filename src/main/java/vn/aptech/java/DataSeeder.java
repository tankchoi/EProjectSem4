package vn.aptech.java;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.aptech.java.models.User;
import vn.aptech.java.models.User.Role;
import vn.aptech.java.models.User.Status;
import vn.aptech.java.services.UserService;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class DataSeeder {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void seedData() {
        User admin = userService.findByUsername("admin");
        if (admin == null) {
            admin = userService.createUser(new User(null, "admin", "admin123", "Quản trị viên", "admin@example.com", "0909999999", Role.ADMIN, Status.ACTIVE, Timestamp.from(Instant.now()), Timestamp.from(Instant.now())));
        }


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("admin", null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        User newUser = new User(null, "john_doe", "password123", "John Doe", "john.doe@example.com", "0908123456", Role.CUSTOMER, Status.ACTIVE, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        userService.createUser(newUser);

        newUser.setPhone("0911222333");
        userService.updateUser(newUser);

        userService.deleteUser(newUser);

        System.out.println("✅ DataSeeder hoàn tất. Kiểm tra bảng AuditLogs.");
    }
}
