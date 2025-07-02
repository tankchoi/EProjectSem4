package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.CreatePartTypeDTO;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartRepository;
import vn.aptech.java.repositories.PartTypeRepository;
import vn.aptech.java.services.PartTypeService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartTypeServiceImpl implements PartTypeService {

    @Autowired
    private PartTypeRepository partTypeRepository;

    @Autowired
    private PartRepository partRepository;

    @Override
    public List<PartType> getAllPartTypes() {
        return partTypeRepository.findAll();
    }

    @Override
    public Page<PartType> getAllPartTypes(Pageable pageable) {
        return partTypeRepository.findAll(pageable);
    }

    @Override
    public Page<PartType> searchPartTypes(String search, Pageable pageable) {
        return partTypeRepository.findWithSearch(search, pageable);
    }

    @Override
    public Optional<PartType> getPartTypeById(Long id) {
        return partTypeRepository.findById(id);
    }

    @Override
    public PartType getPartTypeByIdOrThrow(Long id) {
        return partTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại linh kiện với ID: " + id));
    }

    @Override
    public PartType createPartType(PartType partType) {
        return partTypeRepository.save(partType);
    }

    @Override
    public PartType createPartType(CreatePartTypeDTO createPartTypeDTO) {
        // Validate name uniqueness
        List<PartType> existingPartTypes = partTypeRepository
                .findByNameContainingIgnoreCase(createPartTypeDTO.getName().trim());
        for (PartType existing : existingPartTypes) {
            if (existing.getName().trim().equalsIgnoreCase(createPartTypeDTO.getName().trim())) {
                throw new IllegalArgumentException("Tên loại linh kiện đã tồn tại");
            }
        }

        PartType partType = new PartType();
        partType.setName(createPartTypeDTO.getName().trim());

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
    public PartType updatePartType(Long id, CreatePartTypeDTO createPartTypeDTO) {
        PartType existingPartType = getPartTypeByIdOrThrow(id);

        // Validate name uniqueness (exclude current partType)
        List<PartType> existingPartTypes = partTypeRepository
                .findByNameContainingIgnoreCase(createPartTypeDTO.getName().trim());
        for (PartType existing : existingPartTypes) {
            if (!existing.getId().equals(id)
                    && existing.getName().trim().equalsIgnoreCase(createPartTypeDTO.getName().trim())) {
                throw new IllegalArgumentException("Tên loại linh kiện đã tồn tại");
            }
        }

        existingPartType.setName(createPartTypeDTO.getName().trim());

        return partTypeRepository.save(existingPartType);
    }

    @Override
    public void deletePartType(Long id) {
        if (!partTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy loại linh kiện với ID: " + id);
        }
        partTypeRepository.deleteById(id);
    }

    @Override
    public List<PartType> searchPartTypes(String name) {
        return partTypeRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Long countPartsByPartTypeId(Long partTypeId) {
        return (long) partRepository.findByPartTypeId(partTypeId).size();
    }
}
