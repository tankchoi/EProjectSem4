package vn.aptech.java.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.aptech.java.dtos.CreatePartTypeDTO;
import vn.aptech.java.models.PartType;

import java.util.List;
import java.util.Optional;

public interface PartTypeService {
    List<PartType> getAllPartTypes();

    Page<PartType> getAllPartTypes(Pageable pageable);

    Page<PartType> searchPartTypes(String search, Pageable pageable);

    Optional<PartType> getPartTypeById(Long id);

    PartType getPartTypeByIdOrThrow(Long id);

    PartType createPartType(PartType partType);

    PartType createPartType(CreatePartTypeDTO createPartTypeDTO);

    PartType updatePartType(Long id, PartType partType);

    PartType updatePartType(Long id, CreatePartTypeDTO createPartTypeDTO);

    void deletePartType(Long id);

    List<PartType> searchPartTypes(String name);
}
