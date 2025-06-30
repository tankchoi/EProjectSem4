package vn.aptech.java.services;

import vn.aptech.java.models.PartType;

import java.util.List;
import java.util.Optional;

public interface PartTypeService {
    List<PartType> getAllPartTypes();

    Optional<PartType> getPartTypeById(Long id);

    PartType createPartType(PartType partType);

    PartType updatePartType(Long id, PartType partType);

    void deletePartType(Long id);

    List<PartType> searchPartTypes(String name);
}
