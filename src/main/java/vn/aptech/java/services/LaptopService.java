package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.repositories.LaptopRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LaptopService {
    @Autowired
    private LaptopRepository laptopRepository;


}
