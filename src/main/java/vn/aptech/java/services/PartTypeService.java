package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreatePartTypeDTO;
import vn.aptech.java.dtos.admin.UpdatePartTypeDTO;
import vn.aptech.java.models.PartType;

import java.util.List;
import java.util.Optional;

public interface PartTypeService {
    List<PartType> getPartTypes(String keyword);
    Optional<PartType> getPartTypeById(Long id);
    void createPartType(CreatePartTypeDTO createPartTypeDTO);
    void updatePartType(UpdatePartTypeDTO updatePartTypeDTO);
    void deletePartType(Long id);
}
