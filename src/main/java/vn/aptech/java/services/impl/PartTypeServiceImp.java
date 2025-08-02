package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.CreatePartTypeDTO;
import vn.aptech.java.dtos.admin.UpdatePartTypeDTO;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartTypeRepository;
import vn.aptech.java.services.PartTypeService;

import java.util.List;
import java.util.Optional;
@Service
public class PartTypeServiceImp implements PartTypeService {
    @Autowired
    private PartTypeRepository partTypeRepository;
    @Override
    public List<PartType> getPartTypes(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return partTypeRepository.findAll();
        } else {
            return partTypeRepository.findByNameContainingIgnoreCase(keyword);
        }
    }

    @Override
    public Optional<PartType> getPartTypeById(Long id) {
        return partTypeRepository.findById(id);
    }

    @Override
    public void createPartType(CreatePartTypeDTO createPartTypeDTO) {
        PartType partType = new PartType();
        partType.setName(createPartTypeDTO.getName());
        partTypeRepository.save(partType);
    }

    @Override
    public void updatePartType(UpdatePartTypeDTO updatePartTypeDTO) {
        Optional<PartType> optionalPartType = partTypeRepository.findById(updatePartTypeDTO.getId());
        if (optionalPartType.isPresent()) {
            PartType partType = optionalPartType.get();
            partType.setName(updatePartTypeDTO.getName());
            partTypeRepository.save(partType);
        } else {
            throw new IllegalArgumentException("PartType with ID " + updatePartTypeDTO.getId() + " does not exist.");
        }
    }

    @Override
    public void deletePartType(Long id) {
        partTypeRepository.deleteById(id);
    }
}
