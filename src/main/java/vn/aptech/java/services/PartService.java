package vn.aptech.java.services;

import vn.aptech.java.dtos.CreatePartDTO;
import vn.aptech.java.dtos.UpdatePartDTO;
import vn.aptech.java.models.Part;

import java.util.List;
import java.util.Optional;

public interface PartService {
    List<Part> getParts(String keyword);
    Optional<Part> getPartById(Long id);
    Part createPart(CreatePartDTO createPartDTO);
    Part updatePart(UpdatePartDTO updatePartDTO);
    void deletePart(Long id);
}
