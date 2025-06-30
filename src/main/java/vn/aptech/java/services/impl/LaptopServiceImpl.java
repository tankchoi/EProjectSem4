package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.repositories.LaptopRepository;
import vn.aptech.java.services.LaptopService;

import java.util.List;
import java.util.Optional;

@Service
public class LaptopServiceImpl implements LaptopService {

    @Autowired
    private LaptopRepository laptopRepository;

    @Override
    public List<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }

    @Override
    public Optional<Laptop> getLaptopById(Long id) {
        return laptopRepository.findById(id);
    }

    @Override
    public Laptop createLaptop(Laptop laptop) {
        return laptopRepository.save(laptop);
    }

    @Override
    public Laptop updateLaptop(Long id, Laptop laptop) {
        if (laptopRepository.existsById(id)) {
            laptop.setId(id);
            return laptopRepository.save(laptop);
        }
        return null;
    }

    @Override
    public void deleteLaptop(Long id) {
        laptopRepository.deleteById(id);
    }

    @Override
    public List<Laptop> searchLaptops(String name) {
        return laptopRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Laptop> getLaptopsByModelId(Long modelId) {
        return laptopRepository.findByModelId(modelId);
    }
}
