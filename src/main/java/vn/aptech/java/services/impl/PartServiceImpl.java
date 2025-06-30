package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Part;
import vn.aptech.java.repositories.PartRepository;
import vn.aptech.java.services.PartService;

import java.util.List;
import java.util.Optional;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartRepository partRepository;

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    @Override
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id);
    }

    @Override
    public Part createPart(Part part) {
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
