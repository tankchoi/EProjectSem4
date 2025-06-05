package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.repositories.LaptopRepository;

import java.util.List;
import java.util.Optional;

@Service
public interface LaptopService {
    List<Laptop> getAllLaptops();

    Optional<Laptop> getLaptopById(Long id);

    Laptop createLaptop(Laptop laptop);

    Laptop updateLaptop(Long id, Laptop laptop);

    void deleteLaptop(Long id);

    List<Laptop> searchLaptops(String name);

    List<Laptop> getLaptopsByModelId(Long modelId);
//    @Autowired
//    private LaptopRepository laptopRepository;


}
