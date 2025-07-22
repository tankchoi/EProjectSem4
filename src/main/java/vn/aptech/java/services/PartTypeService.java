package vn.aptech.java.services;

import vn.aptech.java.dtos.CreatePartTypeDTO;
import vn.aptech.java.dtos.UpdatePartTypeDTO;
import vn.aptech.java.models.PartType;

import java.util.List;
import java.util.Optional;

public interface PartTypeService {
    List<PartType> getPartType(String keyword);
    Optional<PartType> getPartTypeById(Long id);
    PartType createPartType(CreatePartTypeDTO createPartTypeDTO);
    PartType updatePartType(UpdatePartTypeDTO updatePartTypeDTO);
    void deletePartType(Long id);
}
