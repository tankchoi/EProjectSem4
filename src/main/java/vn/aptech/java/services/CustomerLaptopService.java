package vn.aptech.java.services;

import vn.aptech.java.models.CustomerLaptop;

import java.util.List;

public interface CustomerLaptopService {
        List<CustomerLaptop> getLaptopsBySerial(String serialNumber);
        boolean isLaptopExists(String serialNumber);


}
