package vn.aptech.java.services;

import vn.aptech.java.dtos.CreateLaptopDTO;
import vn.aptech.java.dtos.UpdateLaptopDTO;
import vn.aptech.java.models.Laptop;

import java.util.List;
import java.util.Optional;

public interface LaptopService {
    List<Laptop> getLaptops(String keyword);
    Optional<Laptop> getLaptopById(Long id);
    Laptop createLaptop(CreateLaptopDTO createLaptopDTO);
    Laptop updateLaptop(UpdateLaptopDTO updateLaptopDTO);
    void deleteLaptop(Long id);
}
