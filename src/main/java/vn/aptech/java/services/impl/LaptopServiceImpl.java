package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.CreateLaptopDTO;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.Model;
import vn.aptech.java.repositories.LaptopRepository;
import vn.aptech.java.repositories.ModelRepository;
import vn.aptech.java.services.LaptopService;

import java.util.List;
import java.util.Optional;

@Service
public class LaptopServiceImpl implements LaptopService {

    @Autowired
    private LaptopRepository laptopRepository;

    @Autowired
    private ModelRepository modelRepository;

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
    public Laptop createLaptop(CreateLaptopDTO createLaptopDTO) {
        Laptop laptop = new Laptop();

        // Fetch the model entity
        Model model = modelRepository.findById(createLaptopDTO.getModelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy model"));

        laptop.setName(createLaptopDTO.getName());
        laptop.setModel(model);
        laptop.setWarrantyPeriod(createLaptopDTO.getWarrantyPeriod());
        laptop.setImgUrl(createLaptopDTO.getImgUrl());

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
    public Laptop updateLaptop(Long id, CreateLaptopDTO createLaptopDTO) {
        Laptop existingLaptop = laptopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy laptop"));

        // Fetch the model entity
        Model model = modelRepository.findById(createLaptopDTO.getModelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy model"));

        existingLaptop.setName(createLaptopDTO.getName());
        existingLaptop.setModel(model);
        existingLaptop.setWarrantyPeriod(createLaptopDTO.getWarrantyPeriod());
        existingLaptop.setImgUrl(createLaptopDTO.getImgUrl());

        return laptopRepository.save(existingLaptop);
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
