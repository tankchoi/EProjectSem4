package vn.aptech.java.services;

import vn.aptech.java.dtos.admin.CreatePartDTO;
import vn.aptech.java.dtos.admin.UpdatePartDTO;
import vn.aptech.java.models.Part;

import java.util.List;
import java.util.Optional;

public interface PartService {
    List<Part> getParts(String name, Long partTypeId, Long laptopId);
    Optional<Part> getPartById(Long id);
    void createPart(CreatePartDTO createPartDTO);
    void updatePart(UpdatePartDTO updatePartDTO);
    void deletePart(Long id);

    List<Part> searchByName(String keyword);
    Optional<Part> lockPartById(Long id);

    long count();
}
