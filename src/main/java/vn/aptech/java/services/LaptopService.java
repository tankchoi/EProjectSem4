package vn.aptech.java.services;

import vn.aptech.java.models.Laptop;

import java.util.List;
import java.util.Optional;

public interface LaptopService {
    List<Laptop> getAllLaptops();

    Optional<Laptop> getLaptopById(Long id);

    Laptop createLaptop(Laptop laptop);

    Laptop updateLaptop(Long id, Laptop laptop);

    void deleteLaptop(Long id);

    List<Laptop> searchLaptops(String name);

    List<Laptop> getLaptopsByModelId(Long modelId);
}
