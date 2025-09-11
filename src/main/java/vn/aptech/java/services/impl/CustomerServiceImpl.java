package vn.aptech.java.services.impl;

import org.springframework.stereotype.Service;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Request;
import vn.aptech.java.models.User;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.repositories.RequestRepository;
import vn.aptech.java.repositories.UserRepository;
import vn.aptech.java.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepo;
    private final CustomerLaptopRepository customerLaptopRepo;
    private final RequestRepository requestRepo;

    public CustomerServiceImpl(UserRepository userRepo,
                               CustomerLaptopRepository customerLaptopRepo,
                               RequestRepository requestRepo) {
        this.userRepo = userRepo;
        this.customerLaptopRepo = customerLaptopRepo;
        this.requestRepo = requestRepo;
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepo.findByPhone(phone);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public List<CustomerLaptop> getCustomerLaptops(User customer) {
        return customerLaptopRepo.findByCustomer(customer);
    }

    @Override
    public List<Request> getRequestHistory(User customer) {
        List<CustomerLaptop> laptops = getCustomerLaptops(customer);
        List<Request> requests = new ArrayList<>();
        for (CustomerLaptop laptop : laptops) {
            requests.addAll(requestRepo.findByCustomerLaptop(laptop));
        }
        return requests;
    }
}
