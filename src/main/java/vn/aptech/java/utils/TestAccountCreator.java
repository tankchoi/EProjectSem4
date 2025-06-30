package vn.aptech.java.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;

@Component
public class TestAccountCreator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createTestAccounts();
    }

    private void createTestAccounts() {
        // Tạo tài khoản Admin test
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456")); // Mật khẩu: 123456
            admin.setFullname("Administrator");
            admin.setEmail("admin@laptopcare.com");
            admin.setPhone("0123456789");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.Status.ACTIVE);
            userRepository.save(admin);
            System.out.println("✅ Created ADMIN account: admin / 123456");
        }

        // Tạo tài khoản Staff test
        if (!userRepository.existsByUsername("staff")) {
            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("123456")); // Mật khẩu: 123456
            staff.setFullname("Staff Member");
            staff.setEmail("staff@laptopcare.com");
            staff.setPhone("0123456788");
            staff.setRole(User.Role.STAFF);
            staff.setStatus(User.Status.ACTIVE);
            userRepository.save(staff);
            System.out.println("✅ Created STAFF account: staff / 123456");
        }

        // Tạo tài khoản Customer test
        if (!userRepository.existsByUsername("customer")) {
            User customer = new User();
            customer.setUsername("customer");
            customer.setPassword(passwordEncoder.encode("123456")); // Mật khẩu: 123456
            customer.setFullname("Test Customer");
            customer.setEmail("customer@gmail.com");
            customer.setPhone("0123456787");
            customer.setRole(User.Role.CUSTOMER);
            customer.setStatus(User.Status.ACTIVE);
            userRepository.save(customer);
            System.out.println("✅ Created CUSTOMER account: customer / 123456");
        }

        // Tạo tài khoản Admin test thêm
        if (!userRepository.existsByUsername("testadmin")) {
            User testAdmin = new User();
            testAdmin.setUsername("testadmin");
            testAdmin.setPassword(passwordEncoder.encode("testadmin123")); // Mật khẩu: testadmin123
            testAdmin.setFullname("Test Administrator");
            testAdmin.setEmail("testadmin@laptopcare.com");
            testAdmin.setPhone("0123456786");
            testAdmin.setRole(User.Role.ADMIN);
            testAdmin.setStatus(User.Status.ACTIVE);
            userRepository.save(testAdmin);
            System.out.println("✅ Created TEST ADMIN account: testadmin / testadmin123");
        }

        System.out.println("🎉 Test accounts creation completed!");
        System.out.println("📝 Login credentials:");
        System.out.println("   Admin:      admin / 123456");
        System.out.println("   Test Admin: testadmin / testadmin123");
        System.out.println("   Staff:      staff / 123456");
        System.out.println("   Customer:   customer / 123456");
    }
}
