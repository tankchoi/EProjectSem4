package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreateLaptopDTO;
import vn.aptech.java.dtos.admin.UpdateLaptopDTO;
import vn.aptech.java.models.Laptop;

import java.util.List;
import java.util.Optional;

public interface LaptopService {
    List<Laptop> getLaptops(String name,Long modelId);
    Optional<Laptop> getLaptopById(Long id);
    void createLaptop(CreateLaptopDTO createLaptopDTO);
    void updateLaptop(UpdateLaptopDTO updateLaptopDTO);
    void deleteLaptop(Long id);
}
