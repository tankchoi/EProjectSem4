package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.DashboardStatsDTO;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.*;
import vn.aptech.java.services.HomepageService;

@Service
public class HomepageServiceImpl implements HomepageService {

    @Autowired
    private LaptopRepository laptopRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartTypeRepository partTypeRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalLaptops = laptopRepository.count();
        long totalParts = partRepository.count();
        long totalRequests = requestRepository.count();
        long totalPartTypes = partTypeRepository.count();

        // Count users by role using enum values
        long totalCustomers = userRepository.countByRole(User.Role.CUSTOMER);
        long totalStaff = userRepository.countByRole(User.Role.STAFF);

        // Count requests by status
        long pendingRequests = requestRepository.countByStatus("PENDING");
        long completedRequests = requestRepository.countByStatus("COMPLETED");

        return new DashboardStatsDTO(
                totalLaptops,
                totalParts,
                totalRequests,
                totalCustomers,
                totalStaff,
                pendingRequests,
                completedRequests,
                totalPartTypes);
    }
}
