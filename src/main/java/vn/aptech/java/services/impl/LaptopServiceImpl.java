package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.CreateLaptopDTO;
import vn.aptech.java.dtos.UpdateLaptopDTO;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.Model;
import vn.aptech.java.repositories.LaptopRepository;
import vn.aptech.java.services.LaptopService;
import vn.aptech.java.services.ModelService;

import java.util.List;
import java.util.Optional;

@Service
public class LaptopServiceImpl implements LaptopService {

    @Autowired
    private LaptopRepository laptopRepository;
    @Autowired
    private ModelService modelService;

    @Override
    public List<Laptop> getLaptops(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return laptopRepository.findAll();
        } else {
            return laptopRepository.searchLaptopsByKeyword(keyword);
        }
    }

    @Override
    public Optional<Laptop> getLaptopById(Long id) {
        return laptopRepository.findById(id);
    }

    @Override
    public Laptop createLaptop(CreateLaptopDTO createLaptopDTO) {
        Laptop laptop = new Laptop();
        Optional<Model> model = modelService.getModelById(createLaptopDTO.getModelId());
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy model có ID: " + createLaptopDTO.getModelId());
        }
        laptop.setName(createLaptopDTO.getName());
        laptop.setModel(model.get());
        laptop.setWarrantyPeriod(createLaptopDTO.getWarrantyPeriod());
        laptop.setImgUrl(createLaptopDTO.getImgUrl());
        return laptopRepository.save(laptop);
    }

    @Override
    public Laptop updateLaptop(UpdateLaptopDTO updateLaptopDTO) {
        Optional<Laptop> laptopOpt = laptopRepository.findById(updateLaptopDTO.getId());
        if (laptopOpt.isPresent()) {
            Laptop laptop = laptopOpt.get();
            Model model = modelService.getModelById(updateLaptopDTO.getModelId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy model có ID: " + updateLaptopDTO.getModelId()));
            laptop.setName(updateLaptopDTO.getName());
            laptop.setModel(model);
            laptop.setWarrantyPeriod(updateLaptopDTO.getWarrantyPeriod());
            laptop.setImgUrl(updateLaptopDTO.getImgUrl());
            return laptopRepository.save(laptop);
        } else {
            throw new IllegalArgumentException("Laptop with ID " + updateLaptopDTO.getId() + " does not exist.");
        }
    }

    @Override
    public void deleteLaptop(Long id) {
        laptopRepository.deleteById(id);
    }

}
