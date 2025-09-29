package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.CreateLaptopDTO;
import vn.aptech.java.dtos.admin.UpdateLaptopDTO;
import vn.aptech.java.models.CustomerLaptop;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.Model;
import vn.aptech.java.repositories.CustomerLaptopRepository;
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

    @Autowired
    private CustomerLaptopRepository customerLaptopRepository;

    @Override
    public List<Laptop> getLaptops(String name, Long modelId) {
        if (name == null || name.isEmpty()) {
            name = null;
        }
        if (modelId == null || modelId <= 0) {
            modelId = null;
        }
        return laptopRepository.filterLaptops(name, modelId);
    }

    @Override
    public Optional<Laptop> getLaptopById(Long id) {
        return laptopRepository.findById(id);
    }

    @Override
    public void createLaptop(CreateLaptopDTO createLaptopDTO) {
        Laptop laptop = new Laptop();
        Optional<Model> model = modelService.getModelById(createLaptopDTO.getModelId());
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy model có ID: " + createLaptopDTO.getModelId());
        }
        laptop.setName(createLaptopDTO.getName());
        laptop.setModel(model.get());
        laptop.setWarrantyPeriod(createLaptopDTO.getWarrantyPeriod());
        laptop.setImgUrl(createLaptopDTO.getImgUrl());
        laptopRepository.save(laptop);
    }

    @Override
    public void updateLaptop(UpdateLaptopDTO updateLaptopDTO) {
        Optional<Laptop> laptopOpt = laptopRepository.findById(updateLaptopDTO.getId());
        if (laptopOpt.isPresent()) {
            Laptop laptop = laptopOpt.get();
            Model model = modelService.getModelById(updateLaptopDTO.getModelId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy model có ID: " + updateLaptopDTO.getModelId()));
            laptop.setName(updateLaptopDTO.getName());
            laptop.setModel(model);
            laptop.setWarrantyPeriod(updateLaptopDTO.getWarrantyPeriod());
            laptop.setImgUrl(updateLaptopDTO.getImgUrl());
            laptopRepository.save(laptop);
        } else {
            throw new IllegalArgumentException("Laptop with ID " + updateLaptopDTO.getId() + " does not exist.");
        }
    }

    @Override
    public void deleteLaptop(Long id) {
        laptopRepository.deleteById(id);
    }

    @Override
    public long count() {
        return laptopRepository.count();
    }

    @Override
    public List<CustomerLaptop> getLaptopsByCustomer(Long customerId) {
        return customerLaptopRepository.findByCustomerId(customerId);
    }


}
