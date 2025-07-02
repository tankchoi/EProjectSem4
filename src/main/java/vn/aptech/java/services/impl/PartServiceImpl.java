package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.PartType;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.repositories.PartRepository;
import vn.aptech.java.repositories.PartTypeRepository;
import vn.aptech.java.repositories.LaptopRepository;
import vn.aptech.java.services.PartService;

import java.util.List;
import java.util.Optional;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartTypeRepository partTypeRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    @Override
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id);
    }

    @Override
    public Part getPartByIdOrThrow(Long id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy linh kiện với ID: " + id));
    }

    @Override
    public Part createPart(Part part) {
        return partRepository.save(part);
    }

    @Override
    public Part createPart(String name, String description, Double price, Integer quantity, Long partTypeId,
            Integer warrantyPeriod,
            Long laptopId, String imgUrl) {
        Part part = new Part();
        part.setName(name);
        part.setDescription(description);
        part.setPrice(price);
        part.setQuantity(quantity);
        part.setWarrantyPeriod(warrantyPeriod);
        part.setImgUrl(imgUrl);

        // Set PartType
        if (partTypeId != null) {
            Optional<PartType> partTypeOpt = partTypeRepository.findById(partTypeId);
            if (partTypeOpt.isPresent()) {
                part.setPartType(partTypeOpt.get());
            }
        }

        // Set Laptop (optional)
        if (laptopId != null) {
            Optional<Laptop> laptopOpt = laptopRepository.findById(laptopId);
            if (laptopOpt.isPresent()) {
                part.setLaptop(laptopOpt.get());
            }
        }

        return partRepository.save(part);
    }

    @Override
    public Part updatePart(Long id, Part part) {
        if (partRepository.existsById(id)) {
            part.setId(id);
            return partRepository.save(part);
        }
        return null;
    }

    @Override
    public Part updatePart(Long id, String name, String description, Double price, Integer quantity, Long partTypeId,
            Integer warrantyPeriod, Long laptopId, String imgUrl) {
        Optional<Part> existingPartOpt = partRepository.findById(id);
        if (existingPartOpt.isPresent()) {
            Part existingPart = existingPartOpt.get();

            // Update basic fields
            existingPart.setName(name);
            existingPart.setDescription(description);
            existingPart.setPrice(price);
            existingPart.setQuantity(quantity);
            existingPart.setWarrantyPeriod(warrantyPeriod);
            existingPart.setImgUrl(imgUrl);

            // Update PartType
            if (partTypeId != null) {
                Optional<PartType> partTypeOpt = partTypeRepository.findById(partTypeId);
                if (partTypeOpt.isPresent()) {
                    existingPart.setPartType(partTypeOpt.get());
                }
            }

            // Update Laptop (optional)
            if (laptopId != null) {
                Optional<Laptop> laptopOpt = laptopRepository.findById(laptopId);
                if (laptopOpt.isPresent()) {
                    existingPart.setLaptop(laptopOpt.get());
                }
            } else {
                existingPart.setLaptop(null);
            }

            return partRepository.save(existingPart);
        }
        return null;
    }

    @Override
    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }

    @Override
    public List<Part> searchParts(String name) {
        return partRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Part> getPartsByPartType(Long partTypeId) {
        return partRepository.findByPartTypeId(partTypeId);
    }

    @Override
    public List<Part> getPartsByLaptop(Long laptopId) {
        return partRepository.findByLaptopId(laptopId);
    }
}
