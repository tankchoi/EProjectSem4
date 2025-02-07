package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PartTypeService {
    @Autowired
    private PartTypeRepository partTypeRepository;
    public List<PartType> getAllPartTypes() {
        return partTypeRepository.findAll();
    }
    public void addPartType(PartType partType) {
        partTypeRepository.save(partType);
    }
    public void updatePartType(PartType partType) {
        Optional<PartType> oldPartType = partTypeRepository.findById(partType.getId());
        partType.setCreatedAt(oldPartType.get().getCreatedAt());
        partTypeRepository.save(partType);
    }
    public void deletePartType(Long id) {
        partTypeRepository.deleteById(id);
    }
    public Optional<PartType> getPartTypeById(Long id) {
        return partTypeRepository.findById(id);
    }
    public List<PartType> getPartTypeByName(String name) {
        return partTypeRepository.findByNameContainingIgnoreCase(name);
    }
}
