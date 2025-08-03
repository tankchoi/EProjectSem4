package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.repositories.CustomerLaptopRepository;
import vn.aptech.java.services.CustomerLaptopService;

import java.util.List;

@Service
public class CustomerLaptopServiceImpl implements CustomerLaptopService {

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Override
    public List<CustomerLaptop> getLaptopsByCustomerIdAndSerial(Long customerId, String serialNumber) {
        if (serialNumber == null || serialNumber.trim().isEmpty()) {
            return customerLaptopRepository.findAllByCustomerId(customerId);
        }

        CustomerLaptop cl = customerLaptopRepository.findBySerialAndCustomerId(serialNumber.trim(), customerId);
        return (cl != null) ? List.of(cl) : List.of();
    }
}
