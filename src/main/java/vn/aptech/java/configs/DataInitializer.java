package vn.aptech.java.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.aptech.java.models.Model;
import vn.aptech.java.models.PartType;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.ModelService;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ModelService modelService;

    @Autowired
    private PartTypeService partTypeService;

    @Autowired
    private PartService partService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo dữ liệu mẫu cho Model
        if (modelService.getAllModels().isEmpty()) {
            Model model1 = new Model();
            model1.setName("HP ProBook 450");
            modelService.createModel(model1);

            Model model2 = new Model();
            model2.setName("Dell Inspiron 15");
            modelService.createModel(model2);

            Model model3 = new Model();
            model3.setName("Lenovo ThinkPad E14");
            modelService.createModel(model3);

            Model model4 = new Model();
            model4.setName("ASUS VivoBook S15");
            modelService.createModel(model4);

            Model model5 = new Model();
            model5.setName("Acer Aspire 7");
            modelService.createModel(model5);

            System.out.println("Đã tạo dữ liệu mẫu cho Model");
        }

        // Tạo dữ liệu mẫu cho PartType
        if (partTypeService.getAllPartTypes().isEmpty()) {
            PartType partType1 = new PartType();
            partType1.setName("RAM");
            partTypeService.createPartType(partType1);

            PartType partType2 = new PartType();
            partType2.setName("SSD");
            partTypeService.createPartType(partType2);

            PartType partType3 = new PartType();
            partType3.setName("HDD");
            partTypeService.createPartType(partType3);

            PartType partType4 = new PartType();
            partType4.setName("VGA");
            partTypeService.createPartType(partType4);

            PartType partType5 = new PartType();
            partType5.setName("CPU");
            partTypeService.createPartType(partType5);

            System.out.println("Đã tạo dữ liệu mẫu cho PartType");
        }

        // Tạo dữ liệu mẫu cho Part
        // Tạm thời comment để tránh lỗi với laptop_id constraint
        /*
         * if (partService.getAllParts().isEmpty()) {
         * PartType ramType = partTypeService.getAllPartTypes().stream()
         * .filter(pt -> pt.getName().equals("RAM")).findFirst().orElse(null);
         * PartType ssdType = partTypeService.getAllPartTypes().stream()
         * .filter(pt -> pt.getName().equals("SSD")).findFirst().orElse(null);
         * PartType hddType = partTypeService.getAllPartTypes().stream()
         * .filter(pt -> pt.getName().equals("HDD")).findFirst().orElse(null);
         * 
         * // Tạm thời tạo một laptop dummy để gán cho Part
         * vn.aptech.java.models.Laptop dummyLaptop = new
         * vn.aptech.java.models.Laptop();
         * dummyLaptop.setName("Default Laptop");
         * // Cần có LaptopService để lưu laptop này
         * 
         * if (ramType != null) {
         * Part part1 = new Part();
         * part1.setName("Kingston DDR4 8GB 2400MHz");
         * part1.setPrice(1500000.0);
         * part1.setQuantity(50);
         * part1.setWarrantyPeriod(3);
         * part1.setPartType(ramType);
         * part1.setLaptop(null); // Tạm thời để null, sẽ fix sau
         * part1.setImgUrl("/assets/images/parts/ram-kingston.jpg");
         * partService.createPart(part1);
         * 
         * Part part2 = new Part();
         * part2.setName("Corsair DDR4 16GB 3200MHz");
         * part2.setPrice(3200000.0);
         * part2.setQuantity(30);
         * part2.setWarrantyPeriod(5);
         * part2.setPartType(ramType);
         * part2.setLaptop(null); // Tạm thời để null, sẽ fix sau
         * part2.setImgUrl("/assets/images/parts/ram-corsair.jpg");
         * partService.createPart(part2);
         * }
         * 
         * if (ssdType != null) {
         * Part part3 = new Part();
         * part3.setName("Samsung SSD 980 500GB NVMe");
         * part3.setPrice(2100000.0);
         * part3.setQuantity(25);
         * part3.setWarrantyPeriod(5);
         * part3.setPartType(ssdType);
         * part3.setLaptop(null); // Tạm thời để null, sẽ fix sau
         * part3.setImgUrl("/assets/images/parts/ssd-samsung.jpg");
         * partService.createPart(part3);
         * 
         * Part part4 = new Part();
         * part4.setName("WD Blue SN570 1TB NVMe");
         * part4.setPrice(2800000.0);
         * part4.setQuantity(20);
         * part4.setWarrantyPeriod(5);
         * part4.setPartType(ssdType);
         * part4.setLaptop(null); // Tạm thời để null, sẽ fix sau
         * part4.setImgUrl("/assets/images/parts/ssd-wd.jpg");
         * partService.createPart(part4);
         * }
         * 
         * if (hddType != null) {
         * Part part5 = new Part();
         * part5.setName("Seagate BarraCuda 1TB");
         * part5.setPrice(1200000.0);
         * part5.setQuantity(40);
         * part5.setWarrantyPeriod(2);
         * part5.setPartType(hddType);
         * part5.setLaptop(null); // Tạm thời để null, sẽ fix sau
         * part5.setImgUrl("/assets/images/parts/hdd-seagate.jpg");
         * partService.createPart(part5);
         * }
         * 
         * System.out.println("Đã tạo dữ liệu mẫu cho Part");
         * }
         */

        // Khởi tạo dữ liệu User
        initializeUserData();
    }

    private void initializeUserData() {
        // Kiểm tra xem đã có user admin chưa
        if (userRepository.findByUsername("admin") == null) {
            // Tạo user admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullname("Administrator");
            admin.setEmail("admin@company.com");
            admin.setPhone("0123456789");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.Status.ACTIVE);
            admin.setImg_link("/assets/images/default-avatar.png"); // Set default avatar
            userRepository.save(admin);
            System.out.println("✅ Created admin user: admin/admin123");
        }

        // Tạo một số nhân viên test
        createStaffIfNotExists("staff1", "Nguyễn Văn An", "staff1@company.com", "0987654321");
        createStaffIfNotExists("staff2", "Trần Thị Bình", "staff2@company.com", "0976543210");
        createStaffIfNotExists("staff3", "Lê Văn Cường", "staff3@company.com", "0965432109");
        createStaffIfNotExists("staff4", "Phạm Thị Dung", "staff4@company.com", "0954321098");
        createStaffIfNotExists("staff5", "Hoàng Văn Em", "staff5@company.com", "0943210987");

        // Tạo nhân viên bị khóa để test
        createStaffIfNotExists("staff6", "Nguyễn Thị Phương", "staff6@company.com", "0932109876", User.Status.BANNED);

        // Tạo một số khách hàng test
        createCustomerIfNotExists("customer1", "Nguyễn Văn Khách", "customer1@gmail.com", "0123456780");
        createCustomerIfNotExists("customer2", "Trần Thị Lan", "customer2@gmail.com", "0123456781");
        createCustomerIfNotExists("customer3", "Lê Văn Minh", "customer3@gmail.com", "0123456782");
        createCustomerIfNotExists("customer4", "Phạm Thị Nga", "customer4@gmail.com", "0123456783");
        createCustomerIfNotExists("customer5", "Hoàng Văn Phong", "customer5@gmail.com", "0123456784");
        createCustomerIfNotExists("customer6", "Đỗ Thị Quỳnh", "customer6@gmail.com", "0123456785");
        createCustomerIfNotExists("customer7", "Vũ Văn Sơn", "customer7@gmail.com", "0123456786");

        // Tạo khách hàng bị khóa để test
        createCustomerIfNotExists("customer8", "Nguyễn Thị Thu", "customer8@gmail.com", "0123456787",
                User.Status.BANNED);

        // Tạo thêm khách hàng để test
        createCustomerIfNotExists("customer9", "Lê Văn Tùng", "customer9@gmail.com", "0123456788");
    }

    private void createStaffIfNotExists(String username, String fullname, String email, String phone) {
        createStaffIfNotExists(username, fullname, email, phone, User.Status.ACTIVE);
    }

    private void createStaffIfNotExists(String username, String fullname, String email, String phone,
            User.Status status) {
        if (userRepository.findByUsername(username) == null) {
            User staff = new User();
            staff.setUsername(username);
            staff.setPassword(passwordEncoder.encode("123456"));
            staff.setFullname(fullname);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setRole(User.Role.STAFF);
            staff.setStatus(status);
            staff.setImg_link("/assets/images/default-avatar.png"); // Set default avatar
            userRepository.save(staff);
            System.out.println("✅ Created staff user: " + username + " (" + fullname + ")");
        }
    }

    private void createCustomerIfNotExists(String username, String fullname, String email, String phone) {
        createCustomerIfNotExists(username, fullname, email, phone, User.Status.ACTIVE);
    }

    private void createCustomerIfNotExists(String username, String fullname, String email, String phone,
            User.Status status) {
        if (userRepository.findByUsername(username) == null) {
            User customer = new User();
            customer.setUsername(username);
            customer.setPassword(passwordEncoder.encode("123456"));
            customer.setFullname(fullname);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setRole(User.Role.CUSTOMER);
            customer.setStatus(status);
            customer.setImg_link("/assets/images/default-avatar.png"); // Set default avatar
            userRepository.save(customer);
            System.out.println("✅ Created customer user: " + username + " (" + fullname + ")");
        }
    }
}
