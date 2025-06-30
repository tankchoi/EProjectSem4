package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartTypeRepository;
import vn.aptech.java.services.PartTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class PartTypeServiceImpl implements PartTypeService {

    @Autowired
    private PartTypeRepository partTypeRepository;

    @Override
    public List<PartType> getAllPartTypes() {
        return partTypeRepository.findAll();
    }

    @Override
    public Optional<PartType> getPartTypeById(Long id) {
        return partTypeRepository.findById(id);
    }

    @Override
    public PartType createPartType(PartType partType) {
        return partTypeRepository.save(partType);
    }

    @Override
    public PartType updatePartType(Long id, PartType partType) {
        if (partTypeRepository.existsById(id)) {
            partType.setId(id);
            return partTypeRepository.save(partType);
        }
        return null;
    }

    @Override
    public void deletePartType(Long id) {
        partTypeRepository.deleteById(id);
    }

    @Override
    public List<PartType> searchPartTypes(String name) {
        return partTypeRepository.findByNameContainingIgnoreCase(name);
    }
}
